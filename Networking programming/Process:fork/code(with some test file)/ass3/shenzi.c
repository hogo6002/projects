#include <stdlib.h>
#include <string.h>
#include "player.h"

int shenzi_take_tokens(Hub* hub, Player* player) {
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
    return 0;
}

Card* shenzi_choose(Player* player, Cards* faceUpCards) {
    int canAfford = 0;
    int maxPoint = 0;
    Card* card = malloc(sizeof(Card));
    for (int i = 0; i < faceUpCards->faceUpNumber; i++) {
        if (can_afford(player, faceUpCards->cards[i])) {
            if (faceUpCards->cards[i]->point > maxPoint) {
                maxPoint = faceUpCards->cards[i]->point;
            }
        }
    }
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
                        totalTokens = total_tokens(faceUpCards->cards[i], player);
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

int choose_action(Player* player, Hub* hub, Cards* faceUpCards) {
    if (can_purchase(player, faceUpCards)) {
        return 1;
    } else {
//        fprintf(stderr, "player %c %d %d %d %d\n", player->pLetter,
//                player->tokens->tokensP,player->tokens->tokensB,
//                player->tokens->tokensY, player->tokens->tokensR);
        shenzi_take_tokens(hub, player);
        return 0;
    }
}

void report_err(int argc, char** argv) {
    if (argc != 3) {
        fprintf(stderr, "Usage: shenzi pcount myid\n");
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
    char* string;
    long id = strtol(argv[2], &string, 10);
    if (id < 0 || id > number - 1) {
        fprintf(stderr, "Invalid player ID\n");
        exit(3);
    }
}


void decode(Hub *hub, char* message, Cards* faceUpCards, Players*
totalPlayer, int id, int flag) {
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
        if (choose_action(totalPlayer->player[id], hub, faceUpCards)) {
            shenzi_choose(totalPlayer->player[id],
                    faceUpCards);
        }
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

int main(int argc, char** argv) {
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
//        fprintf(stderr, "message %s\n", message);
        decode(hub, message, faceUpCards, totalPlayer, playerID, flag);

    }
    return 0;
}

