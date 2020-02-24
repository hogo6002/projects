#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <pthread.h>
#include <netdb.h>
#include <errno.h>
#include <err.h>
#include <ctype.h>
#include <limits.h>
#include <signal.h>
#include <unistd.h>

#include "deck.h"
#include "server.h"
#include "game.h"
#include "util.h"
#include "token.h"
#include "protocol.h"

/*
 * A global variable which stores all of the game state and sockets.
 * And it has to be used since it even stores some signal for this process.
 */
struct Games games;

/* The information passed to an austerity program from its command line
 * arguments.
 */
struct Args {
    // The key file name
    char *keyfile;
    // The deck file name
    char *deckfile;
    // The stat file name
    char *statfile;
    // The number of time out
    int timeout;
};

/*
 * The exit code for server
 */
enum ExitCode {

    NORMAL_EXIT = 0,
    ARGUMENT_COUNT = 1,
    BAD_KEYFILE = 2,
    BAD_DECKFILE = 3,
    BAD_STATFILE = 4,
    BAD_TIMEOUT = 5,
    FAILD_LISTEN = 6,
    SYSTEM_ERR = 10,
};

/*
 * A data struct, which stores some game states, and will be used for the
 * game void pointer(data)
 */
struct Data {
    int gameCounter;
    pthread_t threadId;
    int joinPlayer;
    int gameStart;
    int port;
};

/**
 * A struct stores all the games, including game number, the string of key
 * file, and all of the ports number.
 */
struct Games {
    struct Game **games;
    int numbers;
    char *keyString;
    int numPort[10];
};

/**
 * A struct can store all the ports including the each line of stat file and
 * the total number of ports
 */
struct Ports {
    struct Stat **line;
    int number;
};

/**
 * A struct which contains all the information of each line of stat file,
 * including port, tokens, point and play count. Also, including some other
 * help data, which will be used by thread.
 */
struct Stat {
    int port;
    int tokens;
    int point;
    int playerCount;
    int id;
    char *string;
    struct Args args;
    int end;
};

/**
 * A method to parse all the args and store it into Args struct.
 * Check the total number of arguments, and each argument, whether it is the
 * expected type of not. But this method will not check whether files are
 * vailed or not.
 * @param output -  A Args which stores all the information that we got from
 *                  arguments.
 * @param argc -    the total number of arguments.
 * @param argv -    All of the arguments.
 * @return :    NORMAL_EXIT - if there is nothing wrong when we parse those
 *                          arguments.
 *              ARGUMENT_COUNT - If the argc is not equals to 5.
 *              BAD_TIMEOUT - if the timeout is not a number or it is
 *                            smaller than 0.
 *
 */
enum ExitCode parse_args(struct Args *output, int argc, char **argv) {
    if (argc != 5) {
        return ARGUMENT_COUNT;
    }
    output->keyfile = argv[1];
    output->deckfile = argv[2];
    output->statfile = argv[3];
    char *end;
    int number = parse_int(argv[4], &end);
    if (argv[4] == end || number < 0) {
        return BAD_TIMEOUT;
    } else {
        output->timeout = number;
    }
    return NORMAL_EXIT;
}

/**
 * Setup game over status. print the error message if it has, and exit
 * process as it expected.
 * @param code -   the err code, which will be used to exit this program.
 */
void game_over(enum ExitCode code) {
    switch (code) {
        case NORMAL_EXIT:
            break;
        case ARGUMENT_COUNT:
            fprintf(stderr,
                    "Usage: rafiki keyfile deckfile statfile timeout\n");
            break;
        case BAD_KEYFILE:
            fprintf(stderr, "Bad keyfile\n");
            break;
        case BAD_DECKFILE:
            fprintf(stderr, "Bad deckfile\n");
            break;
        case BAD_STATFILE:
            fprintf(stderr, "Bad statfile\n");
            break;
        case FAILD_LISTEN:
            fprintf(stderr, "Failed listen\n");
            break;
        case SYSTEM_ERR:
            fprintf(stderr, "System error\n");
            break;
        case BAD_TIMEOUT:
            fprintf(stderr, "Bad timeout\n");
            break;
    }
    exit(code);
}

/**
 * Parse the key file, check whether it is valid or not. And store the key
 * string into the output string.
 * @param keyString -   the output string, which will stores all the string
 *                      of key file.
 * @param filename -    a file name that stores key file
 * @return :    NORMAL_EXIT - if this key file parse successfully and it is
 *                            valid.
 *              BAD_KEYFILE - if this key file is not valid or does not
 *                            exist.
 */
enum ExitCode parse_key_file(char **keyString, const char *filename) {
    FILE *keyfile = fopen(filename, "r");
    if (keyfile == NULL) {
        return BAD_KEYFILE;
    }
    int flag = 1;
    if (read_line(keyfile, keyString, 0) <= 0) {
        return BAD_KEYFILE;
    }
    while (!feof(keyfile)) {
        flag = 0;
        break;
    }
    if (flag == 0) {
        return BAD_KEYFILE;
    }
    fclose(keyfile);
    return NORMAL_EXIT;
}

/**
 * Give the basic value to a game by the Stat, which is tokens, point, and
 * player count.
 * Initialize all the players.
 * @param stat -    a struct that contains tokens, point, and player count.
 * @param output -  Game that created by the stat info.
 */
void parse_game(struct Stat *stat, struct Game *output) {
    output->tokenCount[0] = stat->tokens;
    output->tokenCount[1] = stat->tokens;
    output->tokenCount[2] = stat->tokens;
    output->tokenCount[3] = stat->tokens;
    output->winScore = stat->point;
    output->playerCount = stat->playerCount;
    output->players = malloc(sizeof(struct GamePlayer) *
            output->playerCount);
    for (int j = 0; j < output->playerCount; j++) {
        initialize_player(&(output->players)[j].state, j);
    }
}

/**
 * Create a new game, give the status value to this game by call parse_game().
 * And parse the deck file by using parse_deck_file() from library.
 * @param output -  The game that be setup by other input
 * @param stat -    The stat from statfile, which including tokens, point
 *                  and player count info.
 * @param arguments - The Args struct, which offer the deck file to this game.
 * @return :    NORMAL_EXIT - If there is nothing wrong.
 *              BAD_DECKFILE - if the deck file is not valid.
 */
enum ExitCode setup_game(struct Game *output, struct Stat *stat, struct
        Args arguments) {
    enum ExitCode err;
    parse_game(stat, output);
    int count = 0;
    int flag = parse_deck_file(&count, &output->deck, arguments.deckfile);
    if (flag) {
        err = BAD_DECKFILE;
        return err;
    }
    output->deckSize = count;
    return NORMAL_EXIT;
}

/**
 * Setup the sockets, connect the socket to the particular port as it is
 * expected. And bind, listen this sock
 * @param sock -    the sock number which will be used to other function
 * @param line -    A single line of statfile, which stores the port info
 * @return  :   NORMAL_EXIT -   if there is nothing wrong
 *              FAILD_LISTEM - if the connection is failed
 */
enum ExitCode setup_sockets(int *sock, struct Stat line) {
    *sock = socket(AF_INET, SOCK_STREAM, 0);
    if (*sock == -1) {
        return FAILD_LISTEN;
    }
    struct sockaddr_in servAddr;
    servAddr.sin_family = AF_INET;
    servAddr.sin_port = htons(line.port);
    servAddr.sin_addr.s_addr = INADDR_ANY;
    int optVal = 1;
    if (setsockopt(*sock, SOL_SOCKET, SO_REUSEADDR, &optVal, sizeof(int)) <
            0) {
        return FAILD_LISTEN;
    }
    bind(*sock, (struct sockaddr *) &servAddr, sizeof(servAddr));
    listen(*sock, 3);
    socklen_t len = sizeof(servAddr);
    getsockname(*sock, (struct sockaddr *) &servAddr, &len);
    line.port = ntohs(servAddr.sin_port);
    games.numPort[line.id] = ntohs(servAddr.sin_port);
    return NORMAL_EXIT;
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
    return output;
}

/**
 * Receive message from client at first time, to figure out what kind of
 * type client it is. Assuming all client is player at first time, so dup
 * the client sock to player.fileDescriptor.
 * @param gamePlayer -  contains the file descriptor, which user will use to
 *                      receive the message.
 * @param games -   Games struct, which contains key string, using to
 *                  determinate whether player is valid or not.
 * @return :    1 - if this client is a valid player.
 *              2 - if this client is a scores client.
 *              3 - if this client is a valid type.
 */
int receive_message(struct GamePlayer *gamePlayer, struct Games *games) {
    char *buff = malloc(sizeof(char));
    read_line(gamePlayer->fromPlayer, &buff, 0);
    char *player = concat("play", games->keyString);
    char *scores = "scores";
    int playerFlag = 1;
    for (unsigned int i = 0; i < strlen(player); i++) {
        if (player[i] != buff[i]) {
            playerFlag = 0;
            break;
        }
    }
    int scoreFlag = 1;
    for (unsigned int i = 0; i < strlen(scores); i++) {
        if (scores[i] != buff[i]) {
            scoreFlag = 0;
            break;
        }
    }
    if (playerFlag == 1) {
        return 1;
    } else if (scoreFlag == 1) {
        return 2;
    } else {
        return 3;
    }
}

/**
 * Setup connect between server and client.
 * Store the accept to player.fileDescriptor, then dup this file descriptor
 * to open it by fromPlayer and toPlayer.
 * @param sock -    the server socket number
 * @param player -  the currently player client
 * @param games -   the total games, which stores key string
 * @param port -    stores the port that we are accept now.
 * @return :    the flag which means the type of client
 *              1 - if this client is a valid player.
 *              2 - if this client is a scores client.
 *              3 - if this client is a valid type.
 */
int setup_connect(int sock, struct GamePlayer *player, struct Games *games,
        int *port) {
    struct sockaddr_in client;
    int c = sizeof(struct sockaddr_in);
    player->fileDescriptor = accept(sock, (struct sockaddr *) &client,
            (socklen_t *) &c);
    socklen_t addrSize = sizeof(client);
    getsockname(sock, (struct sockaddr *) &client, &addrSize);
    *port = ntohs(client.sin_port);
    player->fromPlayer = fdopen(player->fileDescriptor, "r");
    player->toPlayer = fdopen(player->fileDescriptor, "w");
    int flag = receive_message(player, games);
    return flag;
}

/**
 * Send "yes" to scores client.
 * @param clientSock -  the file descriptor that we can send message back
 */
void handle_score(int clientSock) {
    char *message = "yes\n";
    send(clientSock, message, strlen(message), 0);
}

/**
 * Send do what to player and handle the responds.
 * Return any ErrorCode that cause in this action, and return it.
 * @param game -    the current game
 * @param playerId -    The player id start with 0.
 * @return :    NOTHING_WRONG - if there is nothing wrong
 *              INTERRUPTED - if it is interrupted
 *              PLAYER_CLOSE - if this player disconnect
 *              PROTOCOL_ERR - if the respond is invalid.
 */
enum ErrorCode do_what(struct Game *game, int playerId) {
    enum ErrorCode err = NOTHING_WRONG;
    FILE *toPlayer = game->players[playerId].toPlayer;
    FILE *fromPlayer = game->players[playerId].fromPlayer;

    fputs("dowhat\n", toPlayer);
    fflush(toPlayer);
    char *line;
    int readBytes = read_line(fromPlayer, &line, 0);
    if (readBytes <= 0) {
        if (ferror(fromPlayer) && errno == EINTR) {
            free(line);
            return INTERRUPTED;
        } else if (feof(fromPlayer)) {
            free(line);
            return PLAYER_CLOSED;
        }
    }
    enum MessageFromPlayer type = classify_from_player(line);
    switch (type) {
        case PURCHASE:
            err = handle_purchase_message(playerId, game, line);
            break;
        case TAKE:
            err = handle_take_message(playerId, game, line);
            break;
        case WILD:
            handle_wild_message(playerId, game);
            break;
        default:
            free(line);
            return PROTOCOL_ERROR;
    }
    free(line);
    return err;
}

/**
 * Find out how many game with the same name exist, including its self.
 * @param games -   Total games, all the games
 * @param gameName -    The game name that we want to determinate
 * @return :    count - how many games already exist with the same name,
 *              including its self, so it will be a positive number start
 *              with 1.
 */
int find_game_counter(struct Games *games, char *gameName) {
    int count = 0;
    for (int i = 0; i < games->numbers; i++) {
        if (strcmp(games->games[i]->name, gameName) == 0) {
            count += 1;
        }
    }
    return count;
}

/**
 * Receive the player name, and sort it as lexicographic order, also, send
 * them the rid and playinfo message.
 * @param game -    the current game.
 */
void send_game_info(struct Game *game) {
    int count = find_game_counter(&games, game->name);
    (*(struct Data *) game->data).gameCounter = count;
    for (int i = 0; i < game->playerCount; i++) {
        read_line(game->players[i].fromPlayer,
                &game->players[i].state.name, 0);
    }
    struct GamePlayer temp;
    for (int i = 0; i < game->playerCount - 1; i++) {
        for (int j = i + 1; j < game->playerCount; j++) {
            if (strcmp(game->players[i].state.name,
                    game->players[j].state.name) > 0) {
                temp = game->players[i];
                game->players[i] = game->players[j];
                game->players[j] = temp;
            }
        }
    }
    for (int i = 0; i < game->playerCount; i++) {
        game->players[i].state.playerId = i;
        fprintf(game->players[i].toPlayer, "rid%s,%d,%d\n", game->name,
                (*(struct Data *) game->data).gameCounter, i);
        fprintf(game->players[i].toPlayer, "playinfo%c/%d\n", (i + 65),
                game->playerCount);
        fflush(game->players[i].toPlayer);
    }
}

/**
 * A function that play a game. it will start at send game info and receive
 * the player info, then send the tokens, new card message. After that will
 * send "dowhat" and handle it forever, until there is any error or game is
 * over. And then, send "eog" to all players.
 * @param game -    The current game, including all the game information.
 * @return :    NOTHING_WRONG : if it is normal
 *              NOTHING_WRONG - if there is nothing wrong
 *              INTERRUPTED - if it is interrupted
 *              PLAYER_CLOSE - if this player disconnect
 *              PROTOCOL_ERR - if the respond is invalid.
 */
enum ErrorCode play_game(struct Game *game) {
    (*(struct Data *) game->data).gameStart = 1;
    send_game_info(game);
    char *message = print_tokens_message(game->tokenCount[0]);
    for (int i = 0; i < game->playerCount; i++) {
        fputs(message, game->players[i].toPlayer);
        fflush(game->players[i].toPlayer);
    }
    int number;
    game->boardSize = 0;
    if (game->deckSize >= BOARD_SIZE) {
        number = BOARD_SIZE;
    } else {
        number = game->deckSize;
    }
    for (int i = 0; i < number; i++) {
        draw_card(game);
    }
    enum ErrorCode err;
    while (!is_game_over(game)) {
        for (int i = 0; i < game->playerCount && cards_left(game); ++i) {
            err = do_what(game, i);
            if (err == PROTOCOL_ERROR) {
                err = do_what(game, i);
            }
            if (err) {
                if (err == PLAYER_CLOSED) {
                    for (int j = 0; j < game->playerCount && i != j; j++) {
                        fputs(print_disco_message(i), game->players[j]
                                .toPlayer);
                        fflush(game->players[j].toPlayer);
                    }
                } else {
                    for (int j = 0; j < game->playerCount; j++) {
                        fputs(print_invalid_message(i), game->players[j]
                                .toPlayer);
                        fflush(game->players[j].toPlayer);
                    }
                }
                return err;
            }
        }
    }
    for (int i = 0; i < game->playerCount; i++) {
        fputs("eog\n", game->players[i].toPlayer);
        fflush(game->players[i].toPlayer);
    }
    return NOTHING_WRONG;
}

/**
 * Game thread, that allows program to run multiple game at the same time
 * @param game -    the current game
 * @return :    void - exit this thread when all are done.
 */
void *game_thread(void *game) {
    struct Game *temp = (struct Game *) game;
    (*(struct Data *) temp->data).gameStart = 1;
    play_game(temp);
    pthread_exit(0);
}

/**
 * initialze struct Ports, to malloc the memory.
 * @param ports - the ports which is needed to be initialized.
 */
void initialize_ports(struct Ports *ports) {
    if (ports->number == 0) {
        ports->line = malloc(sizeof(struct Stat *) * (ports->number + 1));
    } else {
        ports->line = realloc(ports->line, sizeof(struct Stat *) *
                (ports->number + 1));
    }
    ports->line[ports->number] = malloc(sizeof(struct Stat));
}

/**
 * Parse the stat of each line, and store it into ports.
 * @param input - the input string, which is one line of statfile.
 * @param ports - a struct Ports, which contains all of the ports info.
 * @param lineNumber - the index of line of this statfile.
 * @return  :   1 - if it is invalid.
 *              0 - if it is valid.
 */
int parse_stat(char *input, struct Ports *ports, int lineNumber) {
    initialize_ports(ports);
    const char *comma = ",";
    char *p = strtok(input, comma);
    char *end;
    int number = parse_int(p, &end);
    if (p == end || number < 0 || number > 65535) {
        return 1;
    }
    ports->line[lineNumber]->port = number;
    ports->number++;
    for (int i = 0; p != NULL; i++) {
        if (i == 1) {
            number = parse_int(p, &end);
            if (p == end || number < 0) {
                return 1;
            }
            ports->line[lineNumber]->tokens = number;
        } else if (i == 2) {
            number = parse_int(p, &end);
            if (p == end || number <= 0) {
                return 1;
            }
            ports->line[lineNumber]->point = number;
        } else if (i == 3) {
            number = parse_int(p, &end);
            if (p == end || number < 2) {
                return 1;
            }
            ports->line[lineNumber]->playerCount = number;
        }
        p = strtok(NULL, comma);
    }
    ports->line[lineNumber]->id = lineNumber;
    return 0;
}

/**
 * Parse the whole stat file, and stores all the info into ports.
 * @param filename - the file name of this statfile
 * @param ports - the struct Ports, which contains all of the info
 * @return :    NORMAL_EXIT - if there is nothing wrong.
 *              BAD_STATFILE - if this stat file is invalid.
 */
enum ExitCode parse_stat_file(const char *filename,
        struct Ports *ports) {
    FILE *statfile = fopen(filename, "r");
    if (!statfile) {
        return BAD_STATFILE;
    }
    int lineNumber = 0;
    while (1) {
        char *line = malloc(sizeof(char *));
        if (read_line(statfile, &line, 0) <= 0) {
            if (feof(statfile)) {
                break;
            }
        }
        if (parse_stat(line, ports, lineNumber) != 0) {
            fclose(statfile);
            return BAD_STATFILE;
        }
        free(line);
        lineNumber++;
    }
    fclose(statfile);

    return NORMAL_EXIT;
}

/**
 * setup all the ports, which will get the statfile name from args, and
 * parse it into struct Stat into ports.
 * @param args -    the args which contains statfile name
 * @param ports -   the struct Ports that contains all info of statfile
 * @return :    NORMAL_EXIT - if there is nothing wrong.
 *              BAD_STATFILE - if this stat file is invalid.
 */
enum ExitCode setup_ports(struct Args args, struct Ports *ports) {
    enum ExitCode err;
    ports->number = 0;
    err = parse_stat_file(args.statfile, ports);
    return err;
}

/**
 * Check whether there is a game with the same name with the new one, and
 * check whether the game is start(full) or not.
 * @param games -   total games
 * @param gameName -    the game that this player wants to enter
 * @return :    i - the index of this exist game, if the game is exist and
 *                  has not started yet.
 *              -1 - if there is not game exist or the game is already started.
 */
int check_game_exist(struct Games *games, char *gameName) {
    for (int i = 0; i < games->numbers; i++) {
        if (strcmp(games->games[i]->name, gameName) == 0 && (*(struct Data *)
                games->games[i]->data).gameStart == 0) {
            return i;
        }
    }
    if (games->numbers == 0) {
        games->games = malloc(sizeof(struct Game *) * games->numbers);
    }
    return -1;
}

/**
 * Find which game does this player want to join with, and determinate
 * whether it is exist or not, and whether it is full or not.
 * If there is not such game, that player can join with, then create the new
 * game, initialize the new game, and add into the total games. and add
 * player into this game.
 * If there is a game that player can join, then add player into this game.
 * @param temp -    the temp player, which contains the file descriptor of this
 *              client.
 * @param games -   total games.
 * @param line -    The struct Stat of this game, setup some basic values
 * @param args -    the args that contains the deck info.
 * @param data -    The data struct, which will be pointed to game.data(the
 *                  void pointer). It means, make game.data point into this
 *                  struct, so that it can be stored more content in game
 *                  struct.
 * @return :    id - the exist game index, if there is a game that player
 *                   can join.
 *              (games->number - 1) - the new game index, which is the last
 *                                    one of the games, if there is no such
 *                                    game matchs.
 */
int find_game(struct GamePlayer *temp, struct Games *games,
        struct Stat *line, struct Args args, struct Data *data) {
    enum ExitCode err;
    char *buff = malloc(sizeof(char));
    fputs("yes\n", temp->toPlayer);
    fflush(temp->toPlayer);
    read_line(temp->fromPlayer, &buff, 0);
    int id;
    if (((id = check_game_exist(games, buff)) > -1)) {
        games->games[id]->players[(*(struct Data *) games->games[id]->data)
                .joinPlayer].fileDescriptor = temp->fileDescriptor;
        games->games[id]->players[(*(struct Data *) games->games[id]->data)
                .joinPlayer].fromPlayer = temp->fromPlayer;
        games->games[id]->players[(*(struct Data *) games->games[id]->data)
                .joinPlayer].toPlayer = temp->toPlayer;
        (*(struct Data *) games->games[id]->data).joinPlayer += 1;
        *data = *(struct Data *) games->games[id]->data;
        return id;
    } else {
        games->games[games->numbers] = malloc(sizeof(struct Game));
        err = setup_game(games->games[games->numbers], line, args);
        if (err) {
            game_over(err);
        }
        games->games[games->numbers]->name = buff;
        data->gameStart = 0;
        data->joinPlayer = 0;
        data->gameCounter = 0;
        games->games[games->numbers]->players[data->joinPlayer]
                .fileDescriptor = temp->fileDescriptor;
        games->games[games->numbers]->players[data->joinPlayer].fromPlayer =
                temp->fromPlayer;
        games->games[games->numbers]->players[data->joinPlayer].toPlayer =
                temp->toPlayer;
        if (err) {
            game_over(err);
        }
        games->numbers += 1;
        games->games = realloc(games->games, sizeof(struct Game *) *
                games->numbers + 1);
        data->joinPlayer += 1;
        return (games->numbers - 1);
    }
}

/**
 * A sock thread, it is for multiple ports to listen at same time.
 * in each thread, it will start games in this port, and it will create
 * game_thread during this thread, which will allows program to have
 * multiple games in one ports at the same time.
 * @param portStat -    the portStat, which is actually a Stat struct, and
 *                      contains the info of this port.
 * @return :    void - exit this thread with status 0.
 */
void *sock_thread(void *portStat) {
    enum ExitCode err;
    struct Stat *temp = (struct Stat *) portStat;
    struct Stat line = *temp;
    int sock, clientSock = 0;
    err = setup_sockets(&sock, line);
    if (err) {
        game_over(err);
    }
    games.numbers = 0;
    games.keyString = line.string;
    int port;
    int flag;
    pthread_t threadId[10];
    struct GamePlayer tempPlayer;
    while ((flag = setup_connect(sock, &tempPlayer, &games, &port))) {
        if (flag == 1) {
            struct Data data;
            int gameNumber = find_game(&tempPlayer, &games, &line, line.args,
                    &data);
            if (data.joinPlayer == 1) {
                games.games[gameNumber]->data = (void *) &data;
            }
            if (data.joinPlayer == (games.games[gameNumber]->playerCount)) {
                (*(struct Data *) games.games[gameNumber]->data).gameStart = 1;
                pthread_create(&(*(struct Data *) games
                        .games[gameNumber]->data)
                        .threadId, NULL, game_thread,
                        (void *) games.games[gameNumber]);
                threadId[gameNumber] = (*(struct Data *) games
                        .games[gameNumber]->data).threadId;
            }
        } else if (flag == 2) {
            handle_score(clientSock);
        } else {
            send(clientSock, "no\n", 3, 0);
        }
    }
    for (int i = 0; i < games.numbers; i++) {
        pthread_join(threadId[i], NULL);
    }
    pthread_exit(0);
}

/**
 * A handle of signal, exit this game when receive those signal.
 */
void handle() {
    exit(0);
}

/**
 * print all the ports that used by this server.
 * @param ports -   A struct Ports that contains all the ports
 */
void print_ports(struct Ports ports) {
    for (int i = 0; i < ports.number; i++) {
        if (ports.line[i]->end == 1) {
            fprintf(stderr, "%d\n", games.numPort[i]);
        } else {
            fprintf(stderr, "%d ", games.numPort[i]);
        }

    }
}

/**
 * The main entrance of this server program.
 * @param argc  -   the total number of argument
 * @param argv  -   all of the argument
 */
int main(int argc, char **argv) {
    signal(SIGTERM, handle);
    signal(SIGINT, handle);
    enum ExitCode err;
    struct Args args;
    struct Ports ports;
    err = parse_args(&args, argc, argv);
    if (err) {
        game_over(err);
    }
    err = setup_ports(args, &ports);
    if (err) {
        game_over(err);
    }
    pthread_t sockThread[ports.number];
    err = parse_key_file(&ports.line[0]->string, args.keyfile);
    if (err) {
        game_over(err);
    }
    struct Game checkGame;
    for (int i = 0; i < ports.number; i++) {
        err = setup_game(&checkGame, ports.line[0], args);
        if (err) {
            game_over(err);
        }
    }
    for (int i = 0; i < ports.number; i++) {
        ports.line[i]->string = ports.line[0]->string;
        ports.line[i]->args = args;
        if (i == ports.number - 1) {
            ports.line[i]->end = 1;
        } else {
            ports.line[i]->end = 0;
        }
        pthread_create(&sockThread[i], NULL, sock_thread, (void *) ports
                .line[i]);
    }
    sleep(1);
    print_ports(ports);
    for (int i = 0; i < ports.number; ++i) {
        pthread_join(sockThread[i], NULL);
    }
    game_over(err);
    return 0;
}