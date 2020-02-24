#include <stdlib.h>
#include <string.h>
#include "player.h"

/**
 * Check whether player need to take tokens or not, by checking the number
 * of total tokens of player.
*  @param player    :    the selected player
 * @return  :   0 - if dose not need to take
 *              1 - if need to take
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
 * Banzai take tokens, send the message to hub about take tokens.
 * @param hub   :   contains the tokens.
 */
void take_tokens(Hub *hub) {
    int tokenCount = 0;
    int tokenY = 0;
    int tokenB = 0;
    int tokenP = 0;
    int tokenR = 0;
    if (hub->tokens->tokensY > 0) {
        tokenCount++;
        tokenP++;
    }
    if (hub->tokens->tokensB > 0) {
        tokenCount++;
        tokenB++;
    }
    if (hub->tokens->tokensP > 0) {
        tokenCount++;
        tokenY++;
    }
    if (tokenCount < 3 && hub->tokens->tokensR > 0) {
        tokenR++;
    }
    printf("take%d,%d,%d,%d\n", tokenP, tokenB, tokenY, tokenR);
    fflush(stdout);
}

/**
 * Check how many wild tokens does player need to afford a card.
 * By calculate the player tokens and discount minus card tokens.
 * @param player    :   the current player.
*  @param card  :    the card that player wants to buy
 * @return  :   total tokens - the number of how many tokens that player
 *              need to purchase this card.
 */
int wild_tokens(Player *player, Card *card) {
    int total = 0;
    total += (player->tokens->tokensP + player->discount->tokensP -
            card->tokens->tokensP);
    total += (player->tokens->tokensB + player->discount->tokensB -
            card->tokens->tokensB);
    total += (player->tokens->tokensY + player->discount->tokensY -
            card->tokens->tokensY);
    total += (player->tokens->tokensR + player->discount->tokensR -
            card->tokens->tokensR);
    return total;
}

/**
 * Using banzai logic to choose a card and purchase it.
 * @param player    :   the player who wants to purchase a card
 * @param faceUpCards   :   all the available card
 * @return
 */
Card *banzai_choose(Player *player, Cards *faceUpCards) {
    int tokensCount = 0;
    int maxTokens = 0;
    for (int i = 0; i < faceUpCards->faceUpNumber; i++) {
        if (can_afford(player, faceUpCards->cards[i])) {
            if (total_tokens(faceUpCards->cards[i], player) > maxTokens) {
                maxTokens = total_tokens(faceUpCards->cards[i], player);
                faceUpCards->cards[i]->status = 2;
                tokensCount++;
            }
        }
    }
    int wildCount = 0;
    int maxWild = 0;
    if (tokensCount < 2) {
        for (int i = 0; i < faceUpCards->faceUpNumber; ++i) {
            if (can_afford(player, faceUpCards->cards[i])) {
                if (total_tokens(faceUpCards->cards[i], player) == maxTokens) {
                    return faceUpCards->cards[i];
                }
            }
        }
    } else {
        for (int i = 0; i < faceUpCards->faceUpNumber; i++) {
            if (can_afford(player, faceUpCards->cards[i])) {
                if (total_tokens(faceUpCards->cards[i], player) == maxTokens) {
                    if (wild_tokens(player, faceUpCards->cards[i]) > maxWild) {
                        maxWild = wild_tokens(player, faceUpCards->cards[i]);
                        wildCount++;
                    }
                }
            }
        }
        for (int i = 0; i < faceUpCards->faceUpNumber; ++i) {
            if (can_afford(player, faceUpCards->cards[i])) {
                if (total_tokens(faceUpCards->cards[i], player) == maxTokens) {
                    if (wild_tokens(player, faceUpCards->cards[i]) ==
                            maxWild) {
                        return faceUpCards->cards[i];
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
void choose_actions(Player *player, Cards *faceUpCards, Hub *hub) {
    if (need_take(player)) {
        take_tokens(hub);
    } else if (can_purchase(player, faceUpCards)) {
        purchase_card(player, banzai_choose(player, faceUpCards));
    } else {
        printf("wild\n");
        fflush(stdout);
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
        choose_actions(totalPlayer->player[id], faceUpCards, hub);
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
    char* name = "banzai";
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

