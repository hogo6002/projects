#include <stdlib.h>
#include <string.h>
#include "player.h"

/**
 * Check whether player need to take tokens or not
 * @param player    :    the current player
 */
int need_take(Player *player) {
    int totalTokens = 0;
    totalTokens += player->tokens->tokensP;
    totalTokens += player->tokens->tokensB;
    totalTokens += player->tokens->tokensY;
    totalTokens += player->tokens->tokensR;
    totalTokens += player->tokens->tokensW;
    if (totalTokens >= 3) {
        return 0;
    }
    return 1;
}

/**
 * Take tokens from hub to player
 * @param hub   :    contains all the tokens
 * @param player    :    the current player
 * @param card  :   the selected card
 */
void take_tokens(Hub *hub, Player *player, Card *card) {
    int tokenY = 0;
    int tokenR = 0;
    int tokenB = 0;
    int tokenP = 0;
    if (((player->tokens->tokensY + player->discount->tokensY) <
            card->tokens->tokensY) && hub->tokens->tokensY > 0) {
        tokenY++;
    }
    if (((player->tokens->tokensR + player->discount->tokensR) <
            card->tokens->tokensR) && hub->tokens->tokensR > 0) {
        tokenR++;
    }
    if (((player->tokens->tokensB + player->discount->tokensB) <
            card->tokens->tokensB) && hub->tokens->tokensB > 0) {
        tokenB++;
    }
    if (((player->tokens->tokensP + player->discount->tokensP) <
            card->tokens->tokensP) && hub->tokens->tokensP > 0) {
        tokenP++;
    }
    int tokenCount = tokenY + tokenR + tokenB + tokenP;
    if (tokenCount < 3) {
    } else {
        printf("take%d,%d,%d,%d\n", tokenP, tokenB, tokenY, tokenR);
        fflush(stdout);
    }
}

/**
 * Check whether it can take tokens or not
 * @param hub   :   contains all tokens
 */
int can_take_tokens(Hub *hub) {
    int count = 0;
    if (hub->tokens->tokensP > 0) {
        count++;
    }
    if (hub->tokens->tokensB > 0) {
        count++;
    }
    if (hub->tokens->tokensY > 0) {
        count++;
    }
    if (hub->tokens->tokensR > 0 && count < 3) {
        count++;
    }
    if (count == 3) {
        return 1;
    }
    return 0;
}

/**
 * take all the tokens that player needs from hub
 * @param hub   :   contains all tokens
 */
void take_all_tokens(Hub *hub) {
    int tokenCount = 0;
    int tokenY = 0;
    int tokenR = 0;
    int tokenB = 0;
    int tokenP = 0;
    if (can_take_tokens(hub)) {
        if (hub->tokens->tokensY) {
            tokenY++;
            tokenCount++;
        }
        if (hub->tokens->tokensR) {
            tokenR++;
            tokenCount++;
        }
        if (hub->tokens->tokensB) {
            tokenB++;
            tokenCount++;
        }
        if (hub->tokens->tokensP && tokenCount < 3) {
            tokenP++;
        }
        printf("take%d,%d,%d,%d\n", tokenP, tokenB, tokenY, tokenR);
        fflush(stdout);
    } else {
        printf("wild\n");
        fflush(stdout);
    }
}

/**
 * Find the next id, by mod the player size.
 * @param totalPlayer   :   All the players who joined this game
 * @param id    :   the id of the current player
 * @param next  :    which round of player after this player
 * @return  :   index - after "next" round, the player id.
 */
int next_id(Players *totalPlayer, int id, int next) {
    int index = (next + id) % totalPlayer->size;
    return index;
}

/**
 * Using ed logic to choose a card and purchase it.
 * @param player    :   the player who wants to purchase a card
 * @param faceUpCards   :   all the available card
 * @return
 */
Card *ed_choose(Players *totalPlayer, int id, Cards *faceUpCards) {
    int heighest = 0;
    for (int i = 0; i < totalPlayer->size; i++) {
        for (int j = 0; j < faceUpCards->faceUpNumber; j++) {
            if (can_afford(totalPlayer->player[i], faceUpCards->cards[j])) {
                if (faceUpCards->cards[j]->point > heighest) {
                    heighest = faceUpCards->cards[j]->point;
                }
            }
        }
    }
    int count = 0;
    for (int i = 0; i < totalPlayer->size; i++) {
        for (int j = 0; j < faceUpCards->faceUpNumber; j++) {
            if (can_afford(totalPlayer->player[i], faceUpCards->cards[j])) {
                if (faceUpCards->cards[j]->point == heighest) {
                    count++;
                }
            }
        }
    }
    if (count == 1) {
        for (int i = 0; i < totalPlayer->size; i++) {
            for (int j = 0; j < faceUpCards->faceUpNumber; j++) {
                if (can_afford(totalPlayer->player[i],
                        faceUpCards->cards[j])) {
                    if (faceUpCards->cards[j]->point == heighest) {
                        return faceUpCards->cards[j];
                    }
                }
            }
        }
    }
    return NULL;
}

/**
 * Respond dowhat by choose an action.
 * If player need to take tokens then take it. if player can purchases a
 * card then purchases it. Otherwise, take a wild.
*  @param player    :    the current player.
 * @param faceUpCards   :   all the available cards
 * @param hub   :   the hub who contains tokens
 */
void choose_actions(Players *totalPlayers, Cards *faceUpCards, Hub *hub, int
        id) {
    int flag = 0;
    for (int i = 0; i < totalPlayers->size; i++) {
        if (can_purchase(totalPlayers->player[i], faceUpCards)) {
            flag = 1;
            break;
        }
    }
    if (flag) {
        Card *card = ed_choose(totalPlayers, id, faceUpCards);
        fprintf(stderr, "card %d %c %d\n", card->faceUp, card->discount,
                card->point);
        if (can_afford(totalPlayers->player[id], card)) {
            purchase_card(totalPlayers->player[id], card);
        } else {
            if (can_take_tokens(hub)) {
                take_tokens(hub, totalPlayers->player[id], card);
            } else {
                printf("wild\n");
                fflush(stdout);
            }
        }
    } else {
        take_all_tokens(hub);
    }
}

/**
 * Docode those message, find the specific action of this message.
 * @param hub   :   the hub who contains tokens and point.
 * @param message   :   the message that received from pipe
 * @param faceUpCards   :   all availbale cards
 * @param totalPlayer   :   all the players who joined this game
 * @param id    :   the player id for this process
 */
void decode(Hub *hub, char *message, Cards *faceUpCards,
        Players *totalPlayer, int id) {
    check_space(message);
    if (is_eog(message) != 0) {
        action_eog(totalPlayer);
    } else if (is_dowhat(message)) {
        fprintf(stderr, "Received dowhat\n");
        if (faceUpCards->faceUpNumber == 0) {
            fprintf(stderr, "Communication Error\n");
            exit(6);
        }
        choose_actions(totalPlayer, faceUpCards, hub, id);
    } else if (is_tokens(message) != 0) {
        action_tokens(message, hub, totalPlayer, faceUpCards);
    } else if (is_newcard(message) != 0) {
        action_newcard(message, faceUpCards, totalPlayer);
    } else if (is_purchased(message)) {
        action_purchased(message, totalPlayer, hub, faceUpCards);
    } else if (is_took(message)) {
        action_took(message, totalPlayer, hub, faceUpCards);
    } else if (is_wild(message)) {
        action_wild(message, totalPlayer, faceUpCards);
    } else {
        fprintf(stderr, "Communication Error\n");
        exit(6);
    }
}

/**
 * The main method of banzai
 * @param argc  :   number of arguments
 * @param argv  :   arguments
 * @return  :   0 - after game over
 */
int main(int argc, char **argv) {
    char *name = "ed";
    report_err(argc, argv, name);
    char message[35];
    Players *totalPlayer = create_players(atoi(argv[1]));
    int playerID = atoi(argv[2]);
    Hub *hub = malloc(sizeof(Hub));
    hub->tokens = malloc(sizeof(Tokens));
    Cards *faceUpCards = face_up_card();
    while (1) {
        if (fgets(message, 35, stdin) != NULL) {
            decode(hub, message, faceUpCards, totalPlayer, playerID);
        } else {
            fprintf(stderr, "Communication Error\n");
            exit(6);
        }

    }
    return 0;
}

