#include <stdlib.h>
#include <string.h>
#include "player.h"

void report_err(int argc, char **argv) {
    if (argc != 3) {
        fprintf(stderr, "Usage: banzai pcount myid\n");
        exit(1);
    }
    if (!atoi(argv[1])) {
        fprintf(stderr, "Invalid player count\n");
        exit(2);
    }
    int number = atoi(argv[1]);
    if (number <= 0 || number > 26) {
        fprintf(stderr, "Invalid player count\n");
        exit(2);
    }
    //    if (!atoi(argv[2]) && (id = atoi(argv[2]) != 0)) {
    //        fprintf(stderr, "here \n");
    //        fprintf(stderr, "Invalid player ID\n");
    //        exit(3);
    //    }
    char *string;
    long id = strtol(argv[2], &string, 10);
    if (id < 0 || id > number - 1) {
        fprintf(stderr, "Invalid player ID\n");
        exit(3);
    }
}

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

void take_tokens(Player *player, Hub *hub) {
//    fprintf(stderr, "before %d\n", player->tokens->tokensP);
    int count = 0;
    if (hub->tokens->tokensY == 0) {
        count++;
    }
    if (hub->tokens->tokensB == 0) {
        count++;
    }
    if (hub->tokens->tokensP == 0) {
        count++;
    }
    if (hub->tokens->tokensR == 0) {
        count++;
    }
    int tokenCount = 0;
    int tokenY = 0;
    int tokenB = 0;
    int tokenP = 0;
    int tokenR = 0;
    if (hub->tokens->tokensY > 0) {
//        player->tokens->tokensP += 1;
//        hub->tokens->tokensP -= 1;
        tokenCount++;
        tokenP++;
    }
    if (hub->tokens->tokensB > 0) {
//        player->tokens->tokensB += 1;
//        hub->tokens->tokensB -= 1;
        tokenCount++;
        tokenB++;
    }
    if (hub->tokens->tokensP > 0) {
//        player->tokens->tokensY += 1;
//        hub->tokens->tokensY -= 1;
        tokenCount++;
        tokenY++;
    }
    if (tokenCount < 3 && hub->tokens->tokensR > 0) {
//        player->tokens->tokensR += 1;
//        hub->tokens->tokensR -= 1;
        tokenR++;
    }
//    fprintf(stderr, "after %d\n", player->tokens->tokensP);
    printf("take%d,%d,%d,%d\n", tokenP, tokenB, tokenY, tokenR);
    fflush(stdout);
}

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

int max_wild(Player *player, Cards *faceUpCards) {
    int max = 0;
    for (int i = 0; i < faceUpCards->faceUpNumber; i++) {
        int total = wild_tokens(player, faceUpCards->cards[i]);
        if (total > max) {
            max = total;
        }
    }
    return max;
}

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
//        } else {
//            for (int i = 0; i < faceUpCards->faceUpNumber; ++i) {
//                if (total_tokens(faceUpCards->cards[i]) == maxTokens) {
//                    if (wild_tokens(player, faceUpCards->cards[i]) ==
//                        maxWild) {
//                        return faceUpCards->cards[i];
//                    }
//                }
//            }
//        }
    }

//    int maxTokens = max_tokens(faceUpCards);
//    int count = 0;
//    for (int i = 0; i < faceUpCards->faceUpNumber; i++) {
//        if (total_tokens(faceUpCards->cards[i]) == maxTokens) {
//            count++;
//        }
//    }
//    if (count < 2) {
//        for (int i = 0; i < faceUpCards->faceUpNumber; ++i) {
//            if (total_tokens(faceUpCards->cards[i]) == maxTokens) {
//                return faceUpCards->cards[i];
//            }
//        }
//    }
//    count = 0;
//    int maxWild = max_wild(player, faceUpCards);
//    for (int i = 0; i < faceUpCards->faceUpNumber; i++) {
//        if (total_tokens(faceUpCards->cards[i]) == maxTokens) {
//            if (wild_tokens(player, faceUpCards->cards[i]) == maxWild) {
//                count++;
//            }
//        }
//    }
//    if (count < 2) {
//        for (int i = 0; i < faceUpCards->faceUpNumber; ++i) {
//            if (total_tokens(faceUpCards->cards[i]) == maxTokens) {
//                if (wild_tokens(player, faceUpCards->cards[i]) == maxWild) {
//                    return faceUpCards->cards[i];
//                }
//            }
//        }
//    }
//    for (int i = 0; i < faceUpCards->faceUpNumber; i++) {
//        if (total_tokens(faceUpCards->cards[i]) == maxTokens) {
//            if (wild_tokens(player, faceUpCards->cards[i]) == maxWild) {
//                return faceUpCards->cards[i];
//            }
//        }
//    }
    return NULL;
}

void choose_actions(Player *player, Cards *faceUpCards, Hub *hub) {
    if (need_take(player)) {
        take_tokens(player, hub);
    } else if (can_purchase(player, faceUpCards)) {
//        fprintf(stderr, "player %")
        purchase_card(player, banzai_choose(player, faceUpCards));
    } else {
//        player->tokens->tokensW++;
        printf("wild\n");
        fflush(stdout);
    }
}

void decode(Hub *hub, char *message, Cards *faceUpCards,
            Players *totalPlayer, int id, int flag) {
    check_space(message);
    if (is_eog(message) != 0) {
        if (!flag) {
            fprintf(stderr, "Game over. Winners are %c\n",
                    find_winner(totalPlayer)->pLetter);
        }
        exit(0);
    } else if (is_dowhat(message)) {
        if (!flag)
            fprintf(stderr, "Received dowhat\n");
        choose_actions(totalPlayer->player[id], faceUpCards, hub);
//        printf("%d\n", player->tokens->tokensP);
    } else if (is_tokens(message) != 0) {
        int length = strlen(message) - 6;
        char numbers[length];
        int index = 0;
        for (int i = 6; i < strlen(message); i++) {
            numbers[index] = message[i];
            index++;
        }
        if (atoi(numbers) <= 0) {
            fprintf(stderr, "Communication Error\n");
            exit(6);
        }
        hub->tokens->tokensP = atoi(numbers);
        hub->tokens->tokensB = atoi(numbers);
        hub->tokens->tokensY = atoi(numbers);
        hub->tokens->tokensR = atoi(numbers);
        hub->tokens->tokensW = 999;
        if (!flag) {
            display_info(faceUpCards, totalPlayer);
        }
    } else if (is_newcard(message) != 0) {
        Card* card = split_newCard(message);
        add_card(faceUpCards, card);
        if (!flag) {
            display_info(faceUpCards, totalPlayer);
        }
    } else if (is_purchased(message)) {
        Card* card = split_purchased_card(message);
        for (int i = 0; i < totalPlayer->size; i++) {
            if (totalPlayer->player[i]->pLetter == card->discount) {
                decode_purchased(hub, card, faceUpCards,
                                 totalPlayer->player[i]);
                break;
            }
        }
        if (!flag) {
            display_info(faceUpCards, totalPlayer);
        }
    } else if (is_took(message)) {
        Player* player = split_took_tokens(message);
        took_tokens(player, totalPlayer);
        hub->tokens->tokensP -= player->tokens->tokensP;
        hub->tokens->tokensB -= player->tokens->tokensB;
        hub->tokens->tokensY -= player->tokens->tokensY;
        hub->tokens->tokensR -= player->tokens->tokensR;
        if (!flag) {
            display_info(faceUpCards, totalPlayer);
        }
    } else if (is_wild(message)) {
        for (int i = 0; i < totalPlayer->size; i++) {
            if (totalPlayer->player[i]->pLetter == message[4]) {
//                if (totalPlayer->player[i]->pLetter == 'C')
//                fprintf(stderr, "before %d\n",
//                        totalPlayer->player[i]->tokens->tokensW);
//                if (totalPlayer->player[id]->pLetter != message[4])
                totalPlayer->player[i]->tokens->tokensW++;
            }
        }
        if (!flag) {
            display_info(faceUpCards, totalPlayer);
        }
        return;
    } else {
        fprintf(stderr, "Communication Error\n");
        exit(6);
    }

}

int main(int argc, char **argv) {
    report_err(argc, argv);
    char message[35];
    int flag = 0;
    Players* totalPlayer = create_players(atoi(argv[1]));
    int playerID = atoi(argv[2]);
    Hub* hub = malloc(sizeof(Hub));
    hub->tokens = malloc(sizeof(Tokens));
    Cards* faceUpCards = face_up_card();
    fgets(message, 35, stdin);
    if (message[0] == 'h') {
//        fclose(stderr);
        flag = 1;
    } else {
        decode(hub, message, faceUpCards, totalPlayer, playerID, flag);
    }
    while (1) {
        fgets(message, 35, stdin);
        decode(hub, message, faceUpCards, totalPlayer, playerID, flag);

    }
    return 0;
}

