#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <unistd.h>

#include "protocol.h"
#include "server.h"
#include "util.h"
#include "player.h"
#include "game.h"

/**
 * Receive the take tokens number from user, and check whether it is legal
 * or not. If it is illegal then ask user again. if it is legal return the
 * number that was received from user.
 * @param color -   the color of tokens that player wants to ask user.
 * @param game -    The current game.
 * @return :    get_take(color, game) - prompt user again if it is illegal.
 *              number - the number of this token that user wants to get.
 */
int get_take(int color, struct GameState *game) {
    switch (color) {
        case 0:
            printf("Token-P> ");
            break;
        case 1:
            printf("Token-B> ");
            break;
        case 2:
            printf("Token-Y> ");
            break;
        case 3:
            printf("Token-R> ");
            break;
    }
    fflush(stdout);
    char input[10];
    fgets(input, 10, stdin);
    char *end;
    int number = parse_int(input, &end);
    if (input == end || number > game->tokenCount[color] || number < 0) {
        return get_take(color, game);
    } else {
        return number;
    }
}

/**
 * Handle when user received take message, take each tokens as user's decision.
 * @param game -   The game that this player wants to reconnect with.
 * @param writeFile -   A file that can write messages to server
 */
void handle_takes(struct GameState *game, FILE *writeFile) {
    struct TakeMessage takeMessage;
    for (unsigned int i = 0; i < (TOKEN_MAX - 1); i++) {
        takeMessage.tokens[i] = get_take(i, game);
    }
    char *message = print_take_message(takeMessage);
    fputs(message, writeFile);
    fflush(writeFile);
}

/**
 * Pay one type of tokens, ask user how many he/she wants to pay for this
 * card in this color.
 * @param game -   The game that this player wants to reconnect with.
 * @param color -   the color of tokens that player wants to ask user.
 * @return
 */
int pay_token(struct GameState *game, int color) {
    switch (color) {
        case 0:
            printf("Token-P> ");
            break;
        case 1:
            printf("Token-B> ");
            break;
        case 2:
            printf("Token-Y> ");
            break;
        case 3:
            printf("Token-R> ");
            break;
        case 4:
            printf("Token-W> ");
            break;
    }
    fflush(stdout);
    char input[40];
    fgets(input, 40, stdin);
    char *end;
    int number = parse_int(input, &end);
    if (input == end || number > game->players[game->selfId].tokens[color]
            || number < 0) {
        return pay_token(game, color);
    } else {
        return number;
    }
}

/**
 * Handle pay tokens, when user decides to purchase one card
 * @param game -   The game that this player wants to reconnect with.
 * @param tokens -  To stores how many tokens user wants to pay for each one.
 */
void handle_pay_tokens(struct GameState *game, int tokens[TOKEN_MAX]) {
    for (unsigned int i = 0; i < TOKEN_MAX; i++) {
        if (game->players[game->selfId].tokens[i] != 0) {
            tokens[i] = pay_token(game, i);
        } else {
            tokens[i] = 0;
        }
    }
}

/**
 * handle purchase action, when user wants to purchase any card.
 * @param game -   The game that this player wants to reconnect with.
 * @param writeFile -   A file that can write messages to server
 */
void handle_purchase(struct GameState *game, FILE *writeFile) {
    struct PurchaseMessage purchaseMessage;
    printf("Card> ");
    fflush(stdout);
    char input[10];
    fgets(input, 10, stdin);
    char *end;
    int number = parse_int(input, &end);
    purchaseMessage.cardNumber = number;
    if (input == end || number > game->boardSize || number < 0) {
        handle_purchase(game, writeFile);
    } else {
        handle_pay_tokens(game, purchaseMessage
                .costSpent);
        char *message = print_purchase_message(purchaseMessage);
        fprintf(writeFile, "%s", message);
        fflush(writeFile);
    }
}

/**
 * Handle dowhat message, when player receive this message, it will respond
 * it and ask user to choose any action.
 * @param game -   The game that this player wants to reconnect with.
 * @param writeFile -   A file that can write messages to server
 */
void handle_dowhat(struct GameState *game, FILE *writeFile) {
    printf("Action> ");
    fflush(stdout);
    char answer[20];
    fgets(answer, 20, stdin);
    char *take = "take\n";
    int flag = 1;
    for (unsigned int i = 0; i < strlen(take); i++) {
        if (answer[i] != take[i] || strlen(answer) != strlen(take)) {
            flag = 0;
            break;
        }
    }
    if (flag) {
        handle_takes(game, writeFile);
    } else {
        char *purchase = "purchase\n";
        flag = 1;
        for (unsigned int i = 0; i < strlen(purchase); i++) {
            if (answer[i] != purchase[i] || strlen(answer) != strlen
                    (purchase)) {
                flag = 0;
                break;
            }
        }
        if (flag) {
            handle_purchase(game, writeFile);
        } else {
            char *wild = "wild\n";
            flag = 1;
            for (unsigned int i = 0; i < strlen(wild); i++) {
                if (answer[i] != wild[i] || strlen(answer) != strlen(wild)) {
                    flag = 0;
                    break;
                }
            }
            if (flag) {
                fprintf(writeFile, "wild\n");
                fflush(writeFile);
            } else {
                handle_dowhat(game, writeFile);
            }
        }
    }
}

/**
 * Exit game with normal status, print the game info.
 * @param line -    the message that received from server.
 * @param game -    the current game that player plays with.
 * @return NOTHING_WRONG -  There is nothing wrong.
 */
enum ErrorCode exit_game_normal(char *line, struct GameState *game) {
    free(line);
    display_eog_info(game);
    return NOTHING_WRONG;
}

/**
 * Exit game with player disconnected, print the info and exit with status 9.
 * @param line -    the message that received from server.
 * @return INTERRUPTED -    player disconnected.
 */
enum ErrorCode exit_game_disco(char *line) {
    free(line);
    int player;
    if (parse_disco_message(&player, line) != -1) {
        char playerLetter = 'A' + player;
        fprintf(stderr, "Player %c disconnected\n", playerLetter);
        exit(9);
    }
    return INTERRUPTED;
}

/**
 * Exit game with staus 10, when player sent invalid message.
 * @param line -    the message that received from server.
 * @return ILLEAGAL_MOVE -  player is invalid.
 */
enum ErrorCode exit_game_invalid(char *line) {
    free(line);
    int player;
    if (parse_invalid_message(&player, line) != -1) {
        char playerLetter = 'A' + player;
        fprintf(stderr, "Player %c sent invalid message\n", playerLetter);
        exit(10);
    }
    return ILLEGAL_MOVE;
}

/**
 * The main logic of play a game for player.
 * Classify the message from hub first then determinate how to respond it.
 * it will stop when player is received a "eog" message or there is any
 * error shows.
 * @param game -   The game that this player wants to reconnect with.
 * @param readFile -    All the messages that comes from server.
 * @param writeFile -   A file that can write messages to server
 * @return  :   NOTHING_WRONG - if this player receive "eog"
 *              COMMUNICATION_ERROR - if hub sent an invalid message
 */
enum ErrorCode play_game(struct GameState *game, FILE *readFile,
        FILE *writeFile) {
    enum ErrorCode err = 0;
    int tokens;
    while (1) {
        char *line;
        if ((read_line(readFile, &line, 0)) <= 0) {
            return COMMUNICATION_ERROR;
        }
        enum MessageFromHub type = classify_from_hub(line);
        switch (type) {
            case END_OF_GAME:
                return exit_game_normal(line, game);
            case DO_WHAT:
                printf("Received dowhat\n");
                handle_dowhat(game, writeFile);
                break;
            case PURCHASED:
                err = handle_purchased_message(game, line);
                break;
            case TOOK:
                err = handle_took_message(game, line);
                break;
            case TOOK_WILD:
                err = handle_took_wild_message(game, line);
                break;
            case NEW_CARD:
                err = handle_new_card_message(game, line);
                break;
            case TOKENS:
                parse_tokens_message(&tokens, line);
                for (int i = 0; i < 4; i++) {
                    game->tokenCount[i] = tokens;
                }
                break;
            case DISCO:
                return exit_game_disco(line);
            case INVALID:
                return exit_game_invalid(line);
            default:
                free(line);
                return COMMUNICATION_ERROR;
        }
        free(line);
        if (err) {
            return err;
        } else if (type != DO_WHAT) {
            display_turn_info(game);
        }
    }
}

/**
 * A method to concat two string. put input2 at the end of input1, and
 * return a new string.
 * @param input1 -  the input string 1
 * @param input2 -  the input string 2
 * @return :    output - which combines those two string.
 */
char *concat(const char *input1, const char *input2) {
    char *output = malloc(strlen(input1) + strlen(input2) + 1);
    strcpy(output, input1);
    strcat(output, input2);
    strcat(output, "\n");
    return output;
}

/**
 * parse the rid message, the rid message is sent by server, and player
 * needs to store the info from this rid.
 * @param game -    The current game, which stores the game state.
 * @param message - the message that sends from server.
 */
void parse_rid_message(struct GameState *game, char *message) {
    char *ridStart = "rid";
    for (unsigned int i = 0; i < strlen(ridStart); i++) {
        if (message[i] != ridStart[i]) {
            fprintf(stderr, "Bad reconnect id\n");
            exit(8);
        }
    }
    char *rid = malloc(sizeof(char));
    for (unsigned int i = 3; i < strlen(message); i++) {
        rid[i - 3] = message[i];
    }
    printf("%s\n", rid);
    const char *comma = ",";
    char *p = strtok(message, comma);
    int i = 0;
    for (i = 0; p != NULL; i++) {
        if (i == 1) {
        } else if (i == 2) {
            game->selfId = atoi(p);
            break;
        }
        p = strtok(NULL, comma);
    }
}

/**
 * parse the playinfo, which is sent from server, and store the data.
 * @param game -    The current game, which stores the game state.
 * @param message - the message that sends from server.
 */
void parse_playinfo_message(struct GameState *game, char *message) {
    char *playinfo = "playinfo";
    for (unsigned int i = 0; i < strlen(playinfo); i++) {
        if (message[i] != playinfo[i]) {
            fprintf(stderr, "Communication Error\n");
            exit(8);
        }
    }
    const char *slash = "/";
    char *p = strtok(message, slash);
    int i = 0;
    for(i = 0; p != NULL; i++) {
        if (i == 1) {
            game->playerCount = atoi(p);
            game->players = malloc(sizeof(struct Player) *
                    game->playerCount);
            for (int j = 0; j < game->playerCount; j++) {
                initialize_player(&game->players[j], j);
            }
        }
        p = strtok(NULL, slash);
    }
    display_turn_info(game);
}

/**
 * Handle the reconnect player, send reconnect to server and parse the
 * playinfo when they received.
 * @param game -    The game that this player wants to reconnect with.
 * @param readFile -    All the messages that comes from server.
 * @param writeFile -   A file that can write messages to server
 * @param argv  -   The arguments that player uses to start this game,
 *                  including their game name and names.
 * @param key - a string which stores the "password" for this server for auth.
 */
void handle_reconnect(struct GameState *game, FILE *readFile, FILE *writeFile,
        char **argv, char *key) {
    char *message = concat("reconnect", key);
    fputs(message, writeFile);
    fflush(writeFile);
    free(message);
    char *servRes = malloc(sizeof(char *));
    read_line(readFile, &servRes, 0);
    char *yes = "yes";
    for (unsigned int i = 0; i < strlen(yes); i++) {
        if (yes[i] != servRes[i]) {
            fprintf(stderr, "Bad auth\n");
            exit(6);
        }
    }
    message = concat("rid", argv[4]);
    fputs(message, writeFile);
    fflush(writeFile);
    free(message);
    servRes = malloc(sizeof(char *));
    read_line(readFile, &servRes, 0);
    for (unsigned int i = 0; i < strlen(yes); i++) {
        if (yes[i] != servRes[i]) {
            fprintf(stderr, "Bad reconnect id\n");
            exit(7);
        }
    }
    read_line(readFile, &servRes, 0);
    parse_playinfo_message(game, servRes);
}

/**
 * Handle if this player wants to enter a new game.
 * Send message to the server for auth.
 * Then send the game name and player name to server, also parse the rid
 * message and playinfo message.
 * @param game -    The game that player wants to join.
 * @param readFile -    All the messages that comes from server.
 * @param writeFile -   A file that can write messages to server
 * @param argv  -   The arguments that player uses to start this game,
 *                  including their game name and names.
 * @param key - a string which stores the "password" for this server for auth.
 */
void handle_new_game(struct GameState *game, FILE *readFile, FILE *writeFile,
        char **argv, char *key) {
    for (unsigned int i = 0; i < strlen(argv[3]); i++) {
        if (argv[3][i] < 'A' || argv[3][i] > 'z') {
            fprintf(stderr, "Bad name\n");
            exit(3);
        }
    }
    for (unsigned int i = 0; i < strlen(argv[4]); i++) {
        if (argv[4][i] < 'A' || argv[4][i] > 'z') {
            fprintf(stderr, "Bad name\n");
            exit(3);
        }
    }
    char *message = concat("play", key);
    fputs(message, writeFile);
    fflush(writeFile);
    free(message);
    char *servRes = malloc(sizeof(char *));
    read_line(readFile, &servRes, 0);
    char *yes = "yes";
    for (unsigned int i = 0; i < strlen(yes); i++) {
        if (yes[i] != servRes[i]) {
            fprintf(stderr, "Bad auth\n");
            exit(6);
        }
    }
    fputs(argv[3], writeFile);
    fputs("\n", writeFile);
    fputs(argv[4], writeFile);
    fputs("\n", writeFile);
    fflush(writeFile);
    read_line(readFile, &servRes, 0);
    parse_rid_message(game, servRes);
    read_line(readFile, &servRes, 0);
    parse_playinfo_message(game, servRes);
}

/**
 * setup connection, connect with server in the particular port.
 * if anything wrong, exit game with Failed to connect.
 * @param port -    the port that player needs to connect by.
 * @param sock -    the sock that player needs to connect with.
 */
void setup_connection(char *port, int *sock) {
    char *end;
    int portNumber = parse_int(port, &end);
    if (port == end || portNumber < 0 || portNumber > 65535) {
        fprintf(stderr, "Failed to connect\n");
        exit(5);
    }
    *sock = socket(AF_INET, SOCK_STREAM, 0);
    struct sockaddr_in servAddr;
    servAddr.sin_family = AF_INET;
    servAddr.sin_port = htons(portNumber);
    servAddr.sin_addr.s_addr = INADDR_ANY;
    int connectionStatus = connect(*sock, (struct sockaddr *)
            &servAddr, sizeof(servAddr));
    if (connectionStatus == -1) {
        fprintf(stderr, "Failed to connect\n");
        exit(5);
    }
}

/**
 * The main entrance of this player program.
 * @param argc  -   the total number of argument
 * @param argv  -   all of the argument
 */
int main(int argc, char **argv) {
    if (argc != 5) {
        fprintf(stderr, "Usage: zazu keyfile port game pname\n");
        exit(1);
    }
    FILE *keyfile = fopen(argv[1], "r");
    if (keyfile == NULL) {
        fprintf(stderr, "Bad key file\n");
        exit(2);
    }
    char *key = malloc(sizeof(char *));
    if (read_line(keyfile, &key, 0) <= 0) {
        fprintf(stderr, "Bad key file\n");
        exit(2);
    }
    if (!feof(keyfile)) {
        fprintf(stderr, "Bad key file\n");
        exit(2);
    }
    int sock;
    setup_connection(argv[2], &sock);
    int newGame = 0;
    char *reconnect = "reconnect";
    for (unsigned int i = 0; i < strlen(reconnect); i++) {
        if (reconnect[i] != argv[3][i]) {
            newGame = 1;
            break;
        }
    }
    FILE *readFile = fdopen(sock, "r");
    FILE *writeFile = fdopen(dup(sock), "w");
    struct GameState game;
    enum ErrorCode err;
    if (newGame) {
        handle_new_game(&game, readFile, writeFile, argv, key);
    } else {
        handle_reconnect(&game, readFile, writeFile, argv, key);
    }
    err = play_game(&game, readFile, writeFile);
    if (err == COMMUNICATION_ERROR) {
        fprintf(stderr, "Communication Error\n");
        exit(8);
    }
    return 0;
}
