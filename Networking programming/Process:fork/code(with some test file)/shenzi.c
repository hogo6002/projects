#include <stdlib.h>
#include <string.h>
#include "player.h"

/**
 * Shenzi take tokens. The order is purple, brown, yellow and red. If hub
 * dose not have enough tokens take wild. print all those message to hub.
 * Parameters:
 *      hub   :   The hub who contains tokens and point.
 *      player    :    The player who take tokens.
 */
void shenzi_take_tokens(Hub *hub, Player *player) {
    int count = 0;
    if (hub->tokens->tokensP <= 0) {
        count++;
    }
    if (hub->tokens->tokensB <= 0) {
        count++;
    }
    if (hub->tokens->tokensY <= 0) {
        count++;
    }
    if (hub->tokens->tokensR <= 0) {
        count++;
    }
    int tokenCount = 0;
    int tokenP = 0;
    int tokenB = 0;
    int tokenY = 0;
    int tokenR = 0;
    if (count < 2) {
        if (hub->tokens->tokensP > 0) {
            tokenCount++;
            tokenP++;
        }
        if (hub->tokens->tokensB > 0) {
            tokenCount++;
            tokenB++;
        }
        if (hub->tokens->tokensY > 0) {
            tokenCount++;
            tokenY++;
        }
        if (tokenCount < 3 && hub->tokens->tokensR > 0) {
            tokenR++;
        }
        printf("take%d,%d,%d,%d\n", tokenP, tokenB, tokenY, tokenR);
        fflush(stdout);

    } else {
        printf("wild\n");
        fflush(stdout);
    }
}

/**
 * Find the max point of all the cards which player can afford it.
 * @param player    :   player who want to purchase a card.
 * @param faceUpCards   :    Cards which can be purchased by player now (face
 *                           up)
 * @return  :   the max point.
 */
int max_afford_point(Player *player, Cards *faceUpCards) {
    int maxPoint = 0;
    for (int i = 0; i < faceUpCards->faceUpNumber; i++) {
        if (can_afford(player, faceUpCards->cards[i])) {
            if (faceUpCards->cards[i]->point > maxPoint) {
                maxPoint = faceUpCards->cards[i]->point;
            }
        }
    }
    return maxPoint;
}

/**
 * Find the min tokens for specific condition and player can afford it.
 * @param player    :   player who want to purchase a card.
 * @param faceUpCards   :    Cards which can be purchased by player now (face
 *                           up)
 * @param maxPoint  :   the max point of cards
 *
 * @return  :    the min tokens
 */
int min_afford_tokens(Player *player, Cards *faceUpCards, int maxPoint) {
    int totalTokens = 0;
    int minTokens = max_tokens(faceUpCards, player);
    for (int i = 0; i < faceUpCards->faceUpNumber; i++) {
        if (can_afford(player, faceUpCards->cards[i])) {
            if (faceUpCards->cards[i]->point == maxPoint) {
                totalTokens = total_tokens(faceUpCards->cards[i], player);
                if (totalTokens < minTokens) {
                    minTokens = totalTokens;
                }
            }
        }
    }
    return minTokens;
}

/**
 * Choose a card for shenzi type player.
 * @param player    :   player who want to purchase a card.
 * @param faceUpCards   :    Cards which can be purchased by player now (face
 *                           up)
 * @return  :   the card which is purchased by player
 */
Card *shenzi_choose(Player *player, Cards *faceUpCards) {
    int canAfford = 0;
    Card *card = malloc(sizeof(Card));
    int maxPoint = max_afford_point(player, faceUpCards);
    for (int i = 0; i < faceUpCards->faceUpNumber; i++) {
        if (faceUpCards->cards[i]->point == maxPoint) {
            canAfford++;
            card = faceUpCards->cards[i];
        }
    }
    if (canAfford == 1) {
        return purchase_card(player, card);
    } else {
        canAfford = 0;
        int totalTokens = 0;
        int minTokens = min_afford_tokens(player, faceUpCards, maxPoint);
        for (int i = 0; i < faceUpCards->faceUpNumber; i++) {
            if (can_afford(player, faceUpCards->cards[i])) {
                if (faceUpCards->cards[i]->point == maxPoint) {
                    if (total_tokens(faceUpCards->cards[i], player) ==
                            minTokens) {
                        canAfford++;
                        card = faceUpCards->cards[i];
                    }
                }
            }
        }
        if (canAfford == 1) {
            return purchase_card(player, card);
        } else {
            for (int i = 0; i < faceUpCards->faceUpNumber; i++) {
                if (can_afford(player, faceUpCards->cards[i])) {
                    if (faceUpCards->cards[i]->point == maxPoint) {
                        totalTokens = total_tokens(faceUpCards->cards[i],
                                player);
                        if (totalTokens == minTokens) {
                            card = faceUpCards->cards[i];
                        }
                    }
                }
            }
            return purchase_card(player, card);
        }
    }
}

/**
 * Choose a action when player receive "dowhat"
 * @param player    :   the current player
 * @param hub   :   the hub who contians tokens
 * @param faceUpCards   :   all the available cards.
 * @return  :   1 - if player can purchase a card.
 *              0 - if player cannot and take tokens.
 */
int choose_action(Player *player, Hub *hub, Cards *faceUpCards) {
    if (can_purchase(player, faceUpCards)) {
        return 1;
    } else {
        shenzi_take_tokens(hub, player);
        return 0;
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
        if (choose_action(totalPlayer->player[id], hub, faceUpCards)) {
            shenzi_choose(totalPlayer->player[id],
                    faceUpCards);
        }
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
 * The main method of shenzi
 * @param argc  :   number of arguments
 * @param argv  :   arguments
 * @return  :   0 - after game over
 */
int main(int argc, char **argv) {
    char* name = "shenzi";
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

