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

void take_tokens(Hub *hub, Player* player, Card* card) {
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
        if (hub->tokens->tokensY) {
            tokenY++;
            tokenCount++;
        }
        if (tokenCount == 3) {
            printf("take%d,%d,%d,%d\n", tokenP, tokenB, tokenY, tokenR);
            fflush(stdout);
        }
        if (hub->tokens->tokensR) {
            tokenR++;
            tokenCount++;
        }
        if (tokenCount == 3) {
            printf("take%d,%d,%d,%d\n", tokenP, tokenB, tokenY, tokenR);
            fflush(stdout);
        }
        if (hub->tokens->tokensB) {
            tokenB++;
            tokenCount++;
        }
        if (tokenCount == 3) {
            printf("take%d,%d,%d,%d\n", tokenP, tokenB, tokenY, tokenR);
            fflush(stdout);
        }
        if (hub->tokens->tokensP) {
            tokenP++;
            tokenCount++;
        }
        if (tokenCount == 3) {
            printf("take%d,%d,%d,%d\n", tokenP, tokenB, tokenY, tokenR);
            fflush(stdout);
        }
    }
}

int can_take_tokens(Hub* hub) {
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
    if (hub->tokens->tokensR > 0) {
        count++;
    }
    if (count >= 3) {
        return 1;
    }
    return 0;
}

void take_all_tokens(Hub* hub) {
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

int next_id (Players* totalPlayer, int id, int next) {
    int index = (next + id) % totalPlayer->size;
    return index;
}

Card *ed_choose(Players *totalPlayer, int id, Cards *faceUpCards) {
    fprintf(stderr, "1\n");
    int heighest = 0;
    for (int i = 0; i < totalPlayer->size; i++) {
        for (int j = 0; j < faceUpCards->faceUpNumber; j++) {
            if (can_afford(totalPlayer->player[i], faceUpCards->cards[j])) {
                heighest = faceUpCards->cards[j]->point;
            }
        }
    }
    fprintf(stderr, "2\n");
    int count = 0;
    for (int i = 0; i < totalPlayer->size; i++) {
        fprintf(stderr, "3\n");
        for (int j = 0; j < faceUpCards->faceUpNumber; j++) {
            fprintf(stderr, "4\n");
            if (can_afford(totalPlayer->player[i], faceUpCards->cards[j])) {
                fprintf(stderr, "5\n");
                if (faceUpCards->cards[i]->point == heighest) {
                    count++;
                }
            }
        }
    }
    if (count == 1) {
        for (int i = 0; i < totalPlayer->size; i++) {
            for (int j = 0; j < faceUpCards->faceUpNumber; j++) {
                if (can_afford(totalPlayer->player[i], faceUpCards->cards[j])) {
                    if (faceUpCards->cards[i]->point == heighest) {
                        return faceUpCards->cards[i];
                    }
                }
            }
        }
    } else {
        for (int i = 0; i < totalPlayer->size; i++) {
            for (int j = 0; j < faceUpCards->faceUpNumber; j++) {
                if (can_afford(totalPlayer->player[i], faceUpCards->cards[j])) {
                    if (faceUpCards->cards[i]->point == heighest) {
                        for (int next = 0; next < totalPlayer->size; next++) {
                            if (can_afford(totalPlayer->player[next_id
                            (totalPlayer, id, next)], faceUpCards->cards[i])) {
                                return faceUpCards->cards[i];
                            }
                        }
                    }
                }
            }
        }
    }
    return NULL;
}

void choose_actions(Players *totalPlayers, Cards *faceUpCards, Hub *hub, int
id) {
//    fprintf(stderr, "1111\n");
    int flag = 0;
    for (int i = 0; i < totalPlayers->size; i++) {
        if (can_purchase(totalPlayers->player[i], faceUpCards)) {
            fprintf(stderr, "can purchase\n");
            flag = 1;
            break;
        }
    }
    if (flag) {
        Card* card = ed_choose(totalPlayers, id, faceUpCards);
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
        if (faceUpCards->faceUpNumber == 0) {
            fprintf(stderr, "Communication Error\n");
            exit(6);
        }
        choose_actions(totalPlayer, faceUpCards, hub, id);
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

