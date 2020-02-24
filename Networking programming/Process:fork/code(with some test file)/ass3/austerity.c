#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <memory.h>
#include <sys/types.h>
#include <sys/wait.h>
#include "game.h"
#include "player.h"

void create_pipe(Hub* hub, Players* players, char* pathName) {


//    int returnValue = fork();
//    hub->pid = getpid();
//    if (returnValue == -1) {
//        return;
//    } else if (returnValue != 0) {
//        dup2(pipeEnds1[1], STDOUT_FILENO);
//        close(pipeEnds1[1]);
//        dup2(pipeEnds2[0], STDIN_FILENO);
//        close(pipeEnds2[0]);
//        char argv1[2];
//        sprintf(argv1, "%d", players->size);
//        char argv2[2];
//        sprintf(argv2, "%d", players->player[hub->pid - getpid()]->playerId);
//        execl(pathName, pathName, argv1, argv2, NULL);
//    } else {
//        dup2(pipeEnds1[0], STDIN_FILENO);
//        close(pipeEnds1[0]);
//        dup2(pipeEnds2[1], STDERR_FILENO);
//        close(pipeEnds2[1]);
//        wait(NULL);
//    }
}

Hub* set_up_hub(int tokenNumber, int point) {
    Hub* hub = malloc(sizeof(Hub));
    hub->tokens = malloc(sizeof(Tokens));
    hub->tokens->tokensP = tokenNumber;
    hub->tokens->tokensB = tokenNumber;
    hub->tokens->tokensY = tokenNumber;
    hub->tokens->tokensR = tokenNumber;
    hub->tokens->tokensW = 999;
    hub->point = point;
    hub->round = 0;
    return hub;
}

int is_purchase(char* message) {
    char* string = "purchase";
    return is_equal(message, string);
}

int is_take(char* message) {
    char* string = "take";
    return is_equal(message, string);
}

void hub_decode(char* message, Hub* hub, Cards* faceUpCards,Player* player,
        int status) {
    if (is_purchase(message)) {
        Card* card = split_purchase_card(message);
//        printf("card %d \n", card->faceUp);
        fprintf(stderr, "purchased%c:%d:%d,%d,%d,%d,%d\n", player->pLetter,
                card->faceUp,
                card->tokens->tokensP, card->tokens->tokensB,
                card->tokens->tokensY, card->tokens->tokensR,
                card->tokens->tokensW);
        if (status == 1) {
            printf("Player %c purchased %d using %d,%d,%d,%d,%d\n",
                   player->pLetter,
                   card->faceUp,
                   card->tokens->tokensP, card->tokens->tokensB,
                   card->tokens->tokensY, card->tokens->tokensR,
                   card->tokens->tokensW);
            hub->tokens->tokensP += card->tokens->tokensP;
            hub->tokens->tokensB += card->tokens->tokensB;
            hub->tokens->tokensY += card->tokens->tokensY;
            hub->tokens->tokensR += card->tokens->tokensR;
            for (int i = 0; i < faceUpCards->faceUpNumber; i++) {
                if (faceUpCards->cards[i]->faceUp == card->faceUp) {
                    player->point += faceUpCards->cards[i]->point;
                    break;
                }
            }
            update_cards(card->faceUp, faceUpCards);
        }
        report_update(faceUpCards);
    } else if (is_take(message)) {
        int length = strlen(message) - 4;
        char string[length];
        int index = 0;
        for (int i = 4; i < strlen(message); i++) {
            string[index] = message[i];
            index++;
        }
        Tokens* tokens = split_tokens(string);
        if (status == 1) {
            printf("Player %c drew %d,%d,%d,%d\n", player->pLetter,
                   tokens->tokensP,
                   tokens->tokensB, tokens->tokensY, tokens->tokensR);
            hub->tokens->tokensP -= tokens->tokensP;
            hub->tokens->tokensB -= tokens->tokensB;
            hub->tokens->tokensY -= tokens->tokensY;
            hub->tokens->tokensR -= tokens->tokensR;
        }
        fprintf(stderr, "took%c:%d,%d,%d,%d\n", player->pLetter,
                tokens->tokensP,
                tokens->tokensB, tokens->tokensY, tokens->tokensR);
    } else if (is_wild(message)) {
        fprintf(stderr, "wild%c\n", player->pLetter);
        if (status == 1) {
            printf("Player %c took a wild\n", player->pLetter);
        }
    }
}

void hub_report_err(int argc, char** argv) {
    if (argc < 5) {
        fprintf(stderr, "Usage: austerity tokens points deck player player "
                "[player ...]\n");
        exit(1);
    }
    if (!atoi(argv[1]) || !atoi(argv[2])) {
        fprintf(stderr, "Bad argument\n");
        exit(2);
    }
    if (!(fopen(argv[3], "r"))) {
        fprintf(stderr, "Cannot access deck file\n");
        exit(3);
    }
    if(atoi(argv[1]) == 0 || atoi(argv[2]) == 0) {
        fprintf(stderr, "Bad start\n");
        exit(5);
    }
}

void check_bad_arg(char** argv) {
    char* shenzi = "./shenzi";
    char* banzai = "./banzai";
    char* ed = "./ed";
    if (strlen(argv[4]) == 8) {
        for (int i = 0; i < strlen(argv[4]); i++) {
            if (shenzi[i] == argv[4][i]) {
                return;
            }
        }
        for (int i = 0; i < strlen(argv[4]); i++) {
            if (banzai[i] == argv[4][i]) {
                return;
            }
        }
        fprintf(stderr, "Bad argument\n");
        exit(2);
    } else {
        for (int i = 0; i < strlen(argv[4]); i++) {
            if (ed[i] != argv[4][i]) {
                fprintf(stderr, "Bad argument\n");
                exit(2);
            }
        }
    }
}

int main(int argc, char **argv) {
    hub_report_err(argc, argv);
    int tokenNumber = atoi(argv[1]);
    int point = atoi(argv[2]);
    int playerSize = argc - 4;
    Players* totalPlayers = create_players(playerSize);
    FILE* deckFile = fopen(argv[3], "r");
    Cards* deckCards = read_deck(deckFile);
    Hub* hub = set_up_hub(tokenNumber, point);
    check_bad_arg(argv);
    char* pathName = argv[4];
    int pipeEnds1[playerSize][2];
    int pipeEnds2[playerSize][2];
    pid_t pid1[playerSize];
    for (int i = 0; i < playerSize; i++) {
        pipe(pipeEnds1[i]);
        pipe(pipeEnds2[i]);
    }
    for (int i = 0; i < playerSize; i++) {
        pid1[i] = fork();
        if (pid1[i] == 0) {
            dup2(pipeEnds1[i][1], STDOUT_FILENO);
            close(pipeEnds1[i][1]);
            dup2(pipeEnds2[i][0], STDIN_FILENO);
            close(pipeEnds2[i][0]);
            char argv1[2];
            sprintf(argv1, "%d", totalPlayers->size);
            char argv2[2];
            sprintf(argv2, "%d", i);
            execl(pathName, pathName, argv1, argv2, NULL);
        }
    }
    char message[25];
    char temp[playerSize][playerSize][25];
    hub->round = 0;
    Player* currentPlayer = current_player(hub->round, totalPlayers);
    game_start(deckCards);
    int loop = 0;
    while(!is_game_over(totalPlayers, hub, deckCards)) {
        for (int i = 0; i < playerSize; i++) {
            currentPlayer = current_player(hub->round, totalPlayers);
            dup2(pipeEnds1[i][0], STDIN_FILENO);
            dup2(pipeEnds2[i][1], STDERR_FILENO);
            if (loop == 0) {
                fprintf(stderr, "h\n");
                for_player(deckCards);
                fprintf(stderr, "tokens%d\n", tokenNumber);
            }
            if (loop == 0) {
                for (int j = 0; j < i; j++) {
                    if (j != i) {
                        hub_decode(temp[j][i], hub, deckCards, current_player
                                (hub->round - (i - j), totalPlayers), 0);
                    }
                }
            } else {
                for (int j = 0; j < playerSize; j++) {
                    if (j != i) {
                        hub_decode(temp[j][i], hub, deckCards, current_player
                                    (hub->round - (i - j), totalPlayers), 0);
                    }
                }
            }
            fprintf(stderr, "dowhat\n");
            fgets(message, 25, stdin);
            for (int j = 0; j < playerSize; j++) {
                strcpy(temp[i][j], message);
            }
            hub_decode(message, hub, deckCards, currentPlayer, 1);

            hub->round++;
        }
        loop ++;
    }
    for (int i = 0; i < playerSize; i++) {
        dup2(pipeEnds1[i][0], STDIN_FILENO);
        dup2(pipeEnds2[i][1], STDERR_FILENO);
        fprintf(stderr, "eog\n");
    }
    for (int i = 0; i < playerSize; i++) {
        close(pipeEnds1[i][0]);
        close(pipeEnds2[i][1]);
    }

//    while(!is_game_over(totalPlayers, hub, deckCards)) {
//        currentPlayer = current_player(hub->round, totalPlayers);
//        fprintf(stderr, "dowhat\n");
//        fgets(message, 25, stdin);
//        hub->round++;
//        hub_decode(message, hub, deckCards, currentPlayer);
//        printf("player %c point %d\n", currentPlayer->pLetter,
//                currentPlayer->point);
        wait(NULL);
//    }
    gmae_over(currentPlayer);
    return 0;
}

