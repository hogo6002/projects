#include <stdbool.h>
#include <errno.h>
#include <fcntl.h>
#include <signal.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <limits.h>

#include "server.h"
#include "game.h"
#include "token.h"
#include "util.h"
#include "protocol.h"

void draw_card(struct Game* game) {
    if (game->deckSize <= 0 || game->boardSize >= BOARD_SIZE) {
        return;
    }

    struct Card flip = game->deck[0];
    game->deckSize -= 1;
    memmove(game->deck, &game->deck[1], sizeof(struct Card) * game->deckSize);

    game->board[game->boardSize++] = flip;

    char* message = print_new_card_message(flip);
    for (int i = 0; i < game->playerCount; ++i) {
        fputs(message, game->players[i].toPlayer);
        fflush(game->players[i].toPlayer);
    }
    free(message);
}

bool cards_left(const struct Game* game) {
    if (game->boardSize == 0 && game->deckSize == 0) {
        return false;
    }

    return true;
}

bool is_game_over(const struct Game* game) {

    if (!cards_left(game)) {
        return true;
    }

    for (int i = 0; i < game->playerCount; ++i) {
        if (game->players[i].state.score >= game->winScore) {
            return true;
        }
    }

    return false;
}

void send_purchased_message(int playerId, struct Game* game,
        struct PurchaseMessage received) {
    char* printed = print_purchased_message(received, playerId);
    for (int i = 0; i < game->playerCount; ++i) {
        fputs(printed, game->players[i].toPlayer);
        fflush(game->players[i].toPlayer);
    }
    free(printed);
}

enum ErrorCode handle_purchase_message(int playerId, struct Game* game,
        const char* line) {
    struct PurchaseMessage body;
    if (parse_purchase_message(&body, line) != 0) {
        return PROTOCOL_ERROR;
    }

    // find the player involved
    struct Player* affected = &game->players[playerId].state;

    // find the card involved
    if (body.cardNumber < 0 || body.cardNumber >= game->boardSize) {
        return PROTOCOL_ERROR;
    }
    struct Card purchased = game->board[body.cardNumber];

    // test that the purchase is possible
    if (validate_costs(*affected, purchased, body.costSpent) != 0) {
        return PROTOCOL_ERROR;
    }

    // all is well, buy the card, remove the purchased card from the board and
    // start to tell everyone what happened
    buy_card(game->tokenCount, affected, purchased);
    send_purchased_message(playerId, game, body);

    game->boardSize -= 1;
    memmove(&game->board[body.cardNumber], &game->board[body.cardNumber + 1],
            sizeof(struct Card) * (game->boardSize - body.cardNumber));

    draw_card(game);

    return 0;
}

enum ErrorCode handle_take_message(int playerId, struct Game* game,
        const char* line) {
    struct TakeMessage body;
    if (parse_take_message(&body, line) != 0) {
        return PROTOCOL_ERROR;
    }

    struct Player* affected = &game->players[playerId].state;
    if (process_take_tokens(game->tokenCount, affected, body) < 0) {
        return PROTOCOL_ERROR;
    }

    char* printed = print_took_message(body, playerId);
    for (int i = 0; i < game->playerCount; ++i) {
        fputs(printed, game->players[i].toPlayer);
        fflush(game->players[i].toPlayer);
    }
    free(printed);

    return 0;
}

void handle_wild_message(int playerId, struct Game* game) {
    // update state
    game->players[playerId].state.tokens[TOKEN_WILD] += 1;

    // tell everyone
    char* printed = print_took_wild_message(playerId);
    for (int i = 0; i < game->playerCount; ++i) {
        fputs(printed, game->players[i].toPlayer);
        fflush(game->players[i].toPlayer);
    }
    free(printed);
}
