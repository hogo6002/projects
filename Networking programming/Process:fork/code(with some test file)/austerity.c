#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <sys/wait.h>
#include "game.h"
#include "player.h"

/**
 * Set up the hub before game start.
 * Initial the hub with tokens and point.
 * @param tokenNumber   :   the token number of each colour of hub
 * @param point :   the point which will trigger the game over
 * @return  :   hub which contains tokens and point.
 */
Hub *set_up_hub(int tokenNumber, int point) {
    Hub *hub = malloc(sizeof(Hub));
    hub->tokens = malloc(sizeof(Tokens));
    hub->tokens->tokensP = tokenNumber;
    hub->tokens->tokensB = tokenNumber;
    hub->tokens->tokensY = tokenNumber;
    hub->tokens->tokensR = tokenNumber;
    hub->tokens->tokensW = 999;         // as wild tokens is infinite
    hub->point = point;
    hub->round = 0;
    return hub;
}

/**
 * Check whether the message from player is equal to purchase.
 * @param message   :    the message that received from pipe.
 * @return  :    1 - is equal to purchase
 *               0 - is not equal to purchase
 */
int is_purchase(char *message) {
    char *string = "purchase";
    return is_equal(message, string);
}

/**
 * Check whether the message from player is equal to take.
 * @param message   :    the message that received from pipe.
 * @return  :    1 - is equal to take
 *               0 - is not equal to take
 */
int is_take(char *message) {
    char *string = "take";
    return is_equal(message, string);
}

/**
 * print the purchased information to stdout
 * @param player    :   the current player
 * @param card  :   the purchased card
 * @param hub   :   the hub
 */
void print_purchased(Player *player, Card *card, Hub *hub) {
    printf("Player %c purchased %d using %d,%d,%d,%d,%d\n",
            player->pLetter, card->faceUp,
            card->tokens->tokensP, card->tokens->tokensB,
            card->tokens->tokensY, card->tokens->tokensR,
            card->tokens->tokensW);
    hub->tokens->tokensP += card->tokens->tokensP;
    hub->tokens->tokensB += card->tokens->tokensB;
    hub->tokens->tokensY += card->tokens->tokensY;
    hub->tokens->tokensR += card->tokens->tokensR;
}

/**
 * Decode the message, find the specific action of this message
 * @param message   :   received from pipe sent by player
 * @param hub   :    contains tokens and point
 * @param faceUpCards   :   all the available cards
 * @param player    :   the current player
 * @param status    :   the status, whether it is notice first time or not.
 */
void hub_decode(char *message, Hub *hub, Cards *faceUpCards, Player *player,
        int status) {
    if (is_purchase(message)) {
        Card *card = split_purchase_card(message);
        fprintf(stderr, "purchased%c:%d:%d,%d,%d,%d,%d\n", player->pLetter,
                card->faceUp,
                card->tokens->tokensP, card->tokens->tokensB,
                card->tokens->tokensY, card->tokens->tokensR,
                card->tokens->tokensW);
        if (status == 1) {
            print_purchased(player, card, hub);
            for (int i = 0; i < faceUpCards->faceUpNumber; i++) {
                if (faceUpCards->cards[i]->faceUp == card->faceUp) {
                    player->point += faceUpCards->cards[i]->point;
                    break;
                }
            }
            update_cards(card->faceUp, faceUpCards);
        }
    } else if (is_take(message)) {
        int length = strlen(message) - 4;
        char string[length];
        int index = 0;
        for (int i = 4; i < strlen(message); i++) {
            string[index] = message[i];
            index++;
        }
        Tokens *tokens = split_tokens(string);
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

/**
 * Report the errors, and exit process.
 * @param argc  :    number of arguments
 * @param argv  :    all the arguments
 */
void hub_report_err(int argc, char **argv) {
    if (argc < 6 || argc > 30) {
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
    if (atoi(argv[1]) < 0 || atoi(argv[2]) <= 0) {
        fprintf(stderr, "Bad argument\n");
        exit(2);
    }
}

/**
 * Run the child process
 * @param index :   the index of child, which child
 * @param pipeEnds1 :    the pipe write
 * @param pipeEnds2 :    the pipe in
 * @param totalPlayers  :   all the players who joined this game
 * @param pathName  :    the path name of the player process
 */
void child_process(int index, int (*pipeEnds1)[2], int (*pipeEnds2)[2],
        Players *totalPlayers, char *pathName) {
    dup2(pipeEnds1[index][1], STDOUT_FILENO);
    close(pipeEnds1[index][1]);
    dup2(pipeEnds2[index][0], STDIN_FILENO);
    close(pipeEnds2[index][0]);
    char argv1[2];
    sprintf(argv1, "%d", totalPlayers->size);
    char argv2[2];
    sprintf(argv2, "%d", index);
    execl(pathName, pathName, argv1, argv2, NULL);
}

/**
 * Print the initial message to player
 * @param deckCards :    all the cards
 * @param tokenNumber   :   how many tokens.
 */
void initial_message(Cards *deckCards, int tokenNumber) {
    for_player(deckCards);
    fprintf(stderr, "tokens%d\n", tokenNumber);
}

/**
 * the main method of austrity
 * @param argc  :   number of arguments
 * @param argv  :   arguments
 * @return  :   0 - after game over
 */
int main(int argc, char **argv) {
    hub_report_err(argc, argv);
    int tokenNumber = atoi(argv[1]);
    int point = atoi(argv[2]);
    int playerSize = argc - 4;
    Players *totalPlayers = create_players(playerSize);
    FILE *deckFile = fopen(argv[3], "r");
    Cards *deckCards = read_deck(deckFile);
    Hub *hub = set_up_hub(tokenNumber, point);
    char *pathName = argv[4];
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
            close(2);
            child_process(i, pipeEnds1, pipeEnds2, totalPlayers, pathName);
        }
    }
    char message[25];
    char temp[playerSize][playerSize][25];
    hub->round = 0;
    Player *currentPlayer = current_player(hub->round, totalPlayers);
    game_start(deckCards);
    int newcards[playerSize][playerSize];
    for (int k = 0; k < playerSize; k++) {
        for (int j = 0; j < playerSize; j++) {
            newcards[k][j] = 0;
        }
    }
    int loop = 0;
    while (!is_game_over(totalPlayers, hub, deckCards)) {
        for (int i = 0; i < playerSize; i++) {
            currentPlayer = current_player(hub->round, totalPlayers);
            dup2(pipeEnds1[i][0], STDIN_FILENO);
            dup2(pipeEnds2[i][1], STDERR_FILENO);
            if (loop == 0) {
                initial_message(deckCards, tokenNumber);
            }
            int count = 0;
            for (int j = 0; j < playerSize; j++) {
                if (newcards[i][j] != 0) {
                    count++;
                }
            }
            report_update(deckCards, count);
            for (int j = 0; j < playerSize; j++) {
                newcards[i][j] = 0;
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
            int flag = 0;
            if (is_purchase(message)) {
                flag = 1;
            }
            hub_decode(message, hub, deckCards, currentPlayer, 1);
            if (flag) {
                if (deckCards->faceUpNumber == 8) {
                    for (int j = 0; j < playerSize; j++) {
                        newcards[j][i] = 1;
                    }
                }
            }
            hub->round++;
        }
        loop++;
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
    wait(NULL);
    gmae_over(currentPlayer);
    return 0;
}


