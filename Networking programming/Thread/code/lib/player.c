#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <limits.h>

#include "game.h"
#include "protocol.h"
#include "token.h"
#include "util.h"
#include "player.h"

void display_eog_info(const struct GameState* game) {
    int numWinners = 0;
    int winningScore = INT_MIN;

    for (int i = 0; i < game->playerCount; ++i) {
        int score = game->players[i].score;
        if (score > winningScore) {
            numWinners = 0;
        }

        if (score >= winningScore) {
            game->players[numWinners++] = game->players[i];
            winningScore = score;
        }
    }

    fprintf(stdout, "Game over. Winners are ");
    for (int i = 0; i < numWinners; ++i) {
        fprintf(stdout, "%c%c", 'A' + game->players[i].playerId,
                i + 1 == numWinners ? '\n' : ',');
    }
}

void display_player_state(struct Player player) {
    fprintf(stdout,
            "Player %c:%d:Discounts=%d,%d,%d,%d:Tokens=%d,%d,%d,%d,%d\n",
            'A' + player.playerId, player.score,
            player.discounts[TOKEN_PURPLE], player.discounts[TOKEN_BROWN],
            player.discounts[TOKEN_YELLOW], player.discounts[TOKEN_RED],
            player.tokens[TOKEN_PURPLE], player.tokens[TOKEN_BROWN],
            player.tokens[TOKEN_YELLOW], player.tokens[TOKEN_RED],
            player.tokens[TOKEN_WILD]);
}

void display_turn_info(const struct GameState* game) {
    for (int i = 0; i < game->boardSize; ++i) {
        struct Card card = game->board[i];
        fprintf(stdout, "Card %d:%c/%d/%d,%d,%d,%d\n", i,
                print_token(card.discount), card.points,
                card.cost[TOKEN_PURPLE], card.cost[TOKEN_BROWN],
                card.cost[TOKEN_YELLOW], card.cost[TOKEN_RED]);
    }
    for (int i = 0; i < game->playerCount; ++i) {
        struct Player player = game->players[i];
        display_player_state(player);
   }
}

enum ErrorCode handle_purchased_message(struct GameState* game,
        const char* line) {
    struct PurchaseMessage body;
    int playerId;
    if (parse_purchased_message(&body, &playerId, line) != 0) {
        return COMMUNICATION_ERROR;
    }

    // find the player involved
    if (playerId < 0 || playerId >= game->playerCount) {
        return COMMUNICATION_ERROR;
    }
    struct Player* affected = &game->players[playerId];

    // find the card involved
    if (body.cardNumber < 0 || body.cardNumber >= game->boardSize) {
        return COMMUNICATION_ERROR;
    }
    struct Card purchased = game->board[body.cardNumber];

    // check that the costs line up
    if (validate_costs(*affected, purchased, body.costSpent) != 0) {
        return COMMUNICATION_ERROR;
    }

    // buy the card, and remove it from the board
    buy_card(game->tokenCount, affected, purchased);
    game->boardSize -= 1;
    memmove(&game->board[body.cardNumber], &game->board[body.cardNumber + 1],
            sizeof(struct Card) * (game->boardSize - body.cardNumber));

    return 0;
}

enum ErrorCode handle_took_message(struct GameState* game, const char* line) {
    struct TakeMessage body;
    int playerId;
    if (parse_took_message(&body, &playerId, line) != 0) {
        return COMMUNICATION_ERROR;
    }

    if (playerId < 0 || playerId >= game->playerCount) {
        return COMMUNICATION_ERROR;
    }

    struct Player* affected = &game->players[playerId];
    if (process_take_tokens(game->tokenCount, affected, body) < 0) {
        return COMMUNICATION_ERROR;
    }

    return 0;
}

enum ErrorCode handle_took_wild_message(struct GameState* game,
        const char* line) {
    int playerId;
    if (parse_took_wild_message(&playerId, line) != 0) {
        return COMMUNICATION_ERROR;
    }

    if (playerId < 0 || playerId >= game->playerCount) {
        return COMMUNICATION_ERROR;
    }
    struct Player* affected = &game->players[playerId];
    affected->tokens[TOKEN_WILD] += 1;

    return 0;
}

enum ErrorCode handle_new_card_message(struct GameState* game,
        const char* line) {
    struct Card card;
    if (parse_new_card_message(&card, line) != 0) {
        return COMMUNICATION_ERROR;
    }

    if (game->boardSize >= BOARD_SIZE) {
        // the card doesn't fit
        return COMMUNICATION_ERROR;
    }

    game->board[game->boardSize++] = card;

    return 0;
}
