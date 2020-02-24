#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

struct Tile {
    int rowPos;
    int colPos;
    char a;
};

/**
 * Start a new game, print out the board by height and width.
 * Set all value of this board to 0
 */
void game_start(int height, int width, int (*board)[width]) {
    for (int i = 0; i < height; i++) {
        for (int k = 0; k < width; k++) {
            board[i][k] = 0;
        }
    }
    for (int i = 0; i < height; i++) {
        for (int k = 0; k < width; k++) {
            printf(".");
        }
        printf("\n");
    }
}

/**
 * Show the tile when user need to input the position.
 * Input the tile, which user will use.
 */
void show_tile(struct Tile tile[]) {
    for (int n = 0; n < 25; n++) {
        printf("%c", tile[n].a);
        if ((n + 1) % 5 == 0) {
            printf("\n");
        }
    }
}

/**
 * Print out board with different symbol '.', '*', '#'.
 * copy each line of tile2 four times and store in a new struct.
 */
void draw_tile(struct Tile tile2[]) {
    int r0 = 0;
    int r1 = 20;
    int r2 = 40;
    int r3 = 60;
    int r4 = 80;
    struct Tile tile3[100];
    for (int k = 0; k < 100; k++) {
        if (tile2[k].rowPos == 0) {
            tile3[r0] = tile2[k];
            r0++;
        } else if (tile2[k].rowPos == 1) {
            tile3[r1] = tile2[k];
            r1++;
        } else if (tile2[k].rowPos == 2) {
            tile3[r2] = tile2[k];
            r2++;
        } else if (tile2[k].rowPos == 3) {
            tile3[r3] = tile2[k];
            r3++;
        } else if (tile2[k].rowPos == 4) {
            tile3[r4] = tile2[k];
            r4++;
        }
    }
    for (int k = 0; k < 100; k++) {
        printf("%c", tile3[k].a);
        if ((k + 1) % 20 == 0) {
            printf("\n");
        } else if ((k + 1) % 5 == 0) {
            printf(" ");
        }
    }
}

/**
 * Display all direction of one tile, 0, 90, 180, 270.
 * Store all different direction into one tile.
 */
void display_file(struct Tile tile[]) {
    struct Tile tile2[100];
    int k = 0;
    for (int i = 1; i < 5; i++) {
        for (int n = 0; n < 25; n++) {
            if (i == 1) {
                tile2[k] = tile[n];
            } else if (i == 2) {
                int colPos = tile[n].rowPos;
                int rowPos = 4 - tile[n].colPos;
                int m = n + (rowPos - tile[n].rowPos) * 5 +
                (colPos - tile[n].colPos);
                tile2[k] = tile[n];
                tile2[k].a = tile[m].a;
            } else if (i == 3) {
                tile2[k] = tile[n];
                tile2[k].a = tile[24 - n].a;
            } else {
                int rowPos = tile[n].colPos;
                int colPos = 4 - tile[n].rowPos;
                int m = n + (rowPos - tile[n].rowPos) * 5 +
                (colPos - tile[n].colPos);
                tile2[k] = tile[n];
                tile2[k].a = tile[m].a;
            }
            k++;
        }
    }
    draw_tile(tile2);
}

/**
 * Input the number of tile and the total Tile.
 * Call the display_file function to display the tile.
 */
void display_all(int numTile, struct Tile totalTile[]) {
    int k = 0;
    for (int i = 0; i < numTile; i++) {
        struct Tile display[25];
        for (int n = 0; n < 25; n++) {
            display[n] = totalTile[k];
            k++;
        }
        display_file(display);
        if (i + 1 != numTile) {
            printf("\n");
        }
    }
}

/**
 * Check the place is legal or ilegal.
 * Input the row, column position and rotation.
 * Return 0 if it is ilegal, otherwise return 1.
 */
int place_legal(int row, int col, int angle, int height, int width,
                struct Tile tile[], int (*board)[width]) {
    struct Tile tile1[25];
    int flag = 1;
    for (int i = 0; i < 25; i++) {
        tile1[i] = tile[i];
    }
    if (angle != 0 && angle != 90 && angle != 180 && angle != 270) {
        return 0;
    }
    int h = tile1[13].rowPos - row;
    int v = tile1[13].colPos - col;
    if (angle == 90) {
        for (int m = 0; m < 25; m++) {
            int temp = 4 - tile1[m].rowPos;
            tile1[m].rowPos = tile1[m].colPos;
            tile1[m].colPos = temp;
        }
    } else if (angle == 180) {
        for (int m = 0; m < 25; m++) {
            tile1[m].rowPos = 4 - tile1[m].rowPos;
            tile1[m].colPos = 4 - tile1[m].colPos;
        }
    } else if (angle == 270) {
        for (int m = 0; m < 25; m++) {
            int temp = tile1[m].colPos;
            tile1[m].colPos = tile1[m].rowPos;
            tile1[m].rowPos = 4 - temp;
        }
    }
    for (int m = 0; m < 25; m++) {
        if (tile1[m].a == '!') {
            if (tile1[m].rowPos - h < 0 || tile1[m].rowPos - h >= height
                || tile1[m].colPos - v + 1 < 0
                || tile1[m].colPos - v + 1 >= width) {
                flag = 0;
            }
            if (board[tile1[m].rowPos - h][tile1[m].colPos - v + 1] != 0) {
                flag = 0;
            }
        }
    }
    return flag;
}

/* Place tile in the right position. */
void place_tiles(int row, int col, int angle, int height, int width,
                 int player, struct Tile tile[], int (*board)[width]) {
    struct Tile tile1[25];
    for (int i = 0; i < 25; i++) {
        tile1[i] = tile[i];
    }
    int h = tile1[13].rowPos - row;
    int v = tile1[13].colPos - col;
    if (angle == 90) {
        for (int m = 0; m < 25; m++) {
            int temp = 4 - tile1[m].rowPos;
            tile1[m].rowPos = tile1[m].colPos;
            tile1[m].colPos = temp;
        }
    } else if (angle == 180) {
        for (int m = 0; m < 25; m++) {
            tile1[m].rowPos = 4 - tile1[m].rowPos;
            tile1[m].colPos = 4 - tile1[m].colPos;
        }
    } else if (angle == 270) {
        for (int m = 0; m < 25; m++) {
            int temp = tile1[m].colPos;
            tile1[m].colPos = tile1[m].rowPos;
            tile1[m].rowPos = 4 - temp;
        }
    }
    for (int m = 0; m < 25; m++) {
        if (tile1[m].a == '!') {
            if (player == 1) {
                board[tile1[m].rowPos - h][tile1[m].colPos - v + 1] = 101;
            } else {
                board[tile1[m].rowPos - h][tile1[m].colPos - v + 1] = 102;
            }
        }
    }
    for (int i = 0; i < height; i++) {
        for (int k = 0; k < width; k++) {
            if (board[i][k] == 101 || board[i][k] == 102) {
                if (board[i][k] == 101) {
                    printf("*");
                } else {
                    printf("#");
                }
            } else {
                printf(".");
            }
        }
        printf("\n");
    }
}

/**
 * Check whether the game is over or not.
 * Return 0 if the game is not over, return 1 if it is.
 */
int is_game_over(int height, int width, struct Tile tile[],
                 int (*board)[width]) {
    int maxRow = height + 2;
    int maxCol = width + 2;
    for (int i = -2; i <= maxRow; i++) {
        for (int n = -2; n <= maxCol; n++) {
            if (place_legal(i, n, 0, height, width, tile, board)) {
                return 0;
            } else if (place_legal(i, n, 90, height, width, tile, board)) {
                return 0;
            } else if (place_legal(i, n, 180, height, width, tile, board)) {
                return 0;
            } else if (place_legal(i, n, 270, height, width, tile, board)) {
                return 0;
            }
        }
    }
    return 1;
}

/**
 * Set the move of automatic player type 1.
 * Start with the left corner then go to right bottom.
 * Change the angle firts then column.
 * Everytime changed the value, call place_legal function to check it.
 * If the place is legal change the input to that value and return it.
 */
void auto_player1(int height, int width, int* input, struct Tile tile[],
                  int (*board)[width]) {
    int maxRow = height + 2;
    int maxCol = width + 2;
    int rStart = input[0];
    int cStart = input[1];
    int angle = 0;
    int r = rStart;
    int c = cStart;
    
    while (angle <= 270) {
        do {
            if (place_legal(r, c, angle, height, width, tile, board)) {
                input[0] = r;
                input[1] = c;
                input[2] = angle;
                return;
            }
            c++;
            if (c > maxCol) {
                c = -2;
                r++;
            }
            if (r > maxRow) {
                r = -2;
            }
        } while (c != cStart || r != rStart);
        angle += 90;
    }
}

/**
 * Set the automatic player type 2.
 * If it is player 1, then start with the left corner.
 * Player 2 start with the right bottom.
 * If it is lagal change the input to those value.
 */
void auto_player2(int height, int width, int* thisInput, int player,
                  struct Tile tile[], int (*board)[width]) {
    int maxRow = height + 2;
    int maxCol = width + 2;
    int rStart = thisInput[0];
    int cStart = thisInput[1];
    int r = rStart;
    int c = cStart;
    int angle;
    if (player == 1) {
        do {
            angle = 0;
            while (angle <= 270) {
                if (place_legal(r, c, angle, height, width, tile, board)) {
                    thisInput[0] = r;
                    thisInput[1] = c;
                    thisInput[2] = angle;
                    return;
                }
                angle += 90;
            }
            c++;
            if (c > maxCol) {
                c = -2;
                r++;
            }
            if (r > maxRow) {
                r = -2;
            }
        } while (c != cStart || r != rStart);
    } else {
        do {
            angle = 0;
            while (angle <= 270) {
                if (place_legal(r, c, angle, height, width, tile, board)) {
                    thisInput[0] = r;
                    thisInput[1] = c;
                    thisInput[2] = angle;
                    return;
                }
                angle += 90;
            }
            c--;
            if (c < -2) {
                c = maxCol;
                r--;
            }
        } while (c != -2 || r != -2);
    }
}

/**
 * Read the tilefile to memory, store it in totalTile.
 * Return the number of tile.
 */
int read_line(struct Tile totalTile[], FILE* file) {
    int position = 0;
    int next = 0;
    int n = 0;
    int count = 0;
    int newline = 0;
    int numTile = 0;
    for (int i = 0; i < 200; i++) {
        next = fgetc(file);
        if (next == EOF) {
            break;
        }
        if (next == '\n') {
            n++;
            position = 0;
            i--;
            newline++;
            if ((i + 1) % 25 == 0) {
                n = 0;
                newline = 0;
            }
        } else {
            totalTile[count].a = (char) next;
            totalTile[count].rowPos = n;
            totalTile[count].colPos = position;
            position++;
            count++;
        }
    }
    numTile = count / 25;
    return numTile;
}

/**
 * Change the tile every round by find the value of round mod numer of tile.
 */
void choose_file(int numTile, struct Tile totalTile[], struct Tile tile[],
                 struct Tile tile4[], int round) {
    round = round % numTile;
    int begin = round * 25;
    for (int n = 0; n < 25; n++) {
        if (numTile == 1) {
            tile[n] = totalTile[begin];
            tile4[n] = totalTile[begin];
        } else if (round % 2 == 0) {
            tile[n] = totalTile[begin];
        } else {
            tile4[n] = totalTile[begin];
        }
        begin++;
    }
}

/**
 * Save the game by using inputs which is got from user.
 */
void save_game(char* inputs, int height, int width,
               int player, int numTile, int round, int (*board)[width]) {
    char path[999];
    int position = 0;
    for (int i = 4; i < strlen(inputs) - 1; i++) {
        path[position] = inputs[i];
        position++;
    }
    FILE* fp = fopen(path, "w");
    char buff[10000];
    int num = round % numTile;
    player -= 1;
    sprintf(buff, "%d %d %d %d\n", num, player, height, width);
    fputs(buff, fp);
    for (int n = 0; n < height; n++) {
        for (int j = 0; j < width; j++) {
            if (board[n][j] == 0) {
                fputs(".", fp);
            } else if (board[n][j] == 101) {
                fputs("*", fp);
            } else if (board[n][j] == 102) {
                fputs("#", fp);
            }
        }
        fputs("\n", fp);
    }
    exit(0);
}

/**
 * Print error to the stand error, and exit with certain number.
 */
void report_err(int argc, char** argv, struct Tile totalTile[], int numTile) {
    if (argc == 5 && fopen(argv[4], "r") == NULL) {
        fprintf(stderr, "Can't access save file\n");
        exit(6);
    }
    if (argc == 5 && argv[4] == argv[4]) {
        fprintf(stderr, "Invalid save file contents\n");
        exit(7);
    }
    if (argc == 2) {
        if (numTile == 0) {
            fprintf(stderr, "Invalid tile file contents\n");
            exit(3);
        }
        display_all(numTile, totalTile);
        exit(0);
    }
    if (numTile == 0) {
        fprintf(stderr, "Invalid tile file contents\n");
        exit(3);
    } else if (strcmp(argv[2], "h") != 0 && atoi(argv[2]) != 1
               && atoi(argv[2]) != 2) {
        fprintf(stderr, "Invalid player type\n");
        exit(4);
    } else if (strcmp(argv[3], "h") != 0 && atoi(argv[3]) != 1
               && atoi(argv[3]) != 2) {
        fprintf(stderr, "Invalid player type\n");
        exit(4);
    } else if (argc == 6 && (atoi(argv[4]) < 1 || atoi(argv[5]) < 1)) {
        fprintf(stderr, "Invalid dimensions\n");
        exit(5);
    }
}

/**
 * Main part.
 * Get six argv from stdin.
 * If incorrect exit and print message to stderr.
 * If the game is not over continue run the game.
 * If user enter invaild input, enter angin without show tile.
 * User can save file by typing save+path.
 * When game is over, return 0.
 */
int main(int argc, char** argv) {
    if (argc != 6 && argc != 2 && argc != 5) {
        fprintf(stderr, "Usage: fitz tilefile [p1type ");
        fprintf(stderr, "p2type [height width | filename]]\n");
        exit(1);
    } else if (argv[1] == NULL || fopen(argv[1], "r") == NULL) {
        fprintf(stderr, "Can't access tile file\n");
        exit(2);
    }
    struct Tile tile[25];
    struct Tile tile4[25];
    struct Tile totalTile[2500];
    FILE* tilefile = fopen(argv[1], "r");
    int numTile = read_line(totalTile, tilefile);
    fclose(tilefile);
    report_err(argc, argv, totalTile, numTile);
    int height = atoi(argv[4]);
    int width = atoi(argv[5]);
    int board[height][width];
    game_start(height, width, board);
    int p1type = 0;
    int p2type = 0;
    if (atoi(argv[2]) == 1) {
        p1type = 1;
    } else if (atoi(argv[2]) == 2) {
        p1type = 2;
    }
    if (atoi(argv[3]) == 1) {
        p2type = 1;
    } else if (atoi(argv[3]) == 2) {
        p2type = 2;
    }
    int input[3] = {-2, -2, 0};
    int round = 0;
    int player = 1;
    int player1Input[3] = {-2, -2, 0};
    int player2Input[3] = {height + 2, width + 2, 0};
    int show = 1;
    while (1) {
        choose_file(numTile, totalTile, tile, tile4, round);
        if (player == 1) {
            if (is_game_over(height, width, tile, board)) {
                printf("Player # wins\n");
                return 0;
            }
            if (p1type) {
                if (p1type == 1) {
                    auto_player1(height, width, input, tile, board);
                } else {
                    auto_player2(height, width, player1Input, player,
                                 tile, board);
                    input[0] = player1Input[0];
                    input[1] = player1Input[1];
                    input[2] = player1Input[2];
                }
                printf("Player * => %d %d rotated %d\n", input[0],
                       input[1], input[2]);
            } else {
                if (show) {
                    show = 1;
                    show_tile(tile);
                }
                printf("Player *] ");
                char inputs[80];
                if (fgets(inputs, 80, stdin) == NULL) {
                    fprintf(stderr, "End of input\n");
                    show = 0;
                    exit(10);
                }
                if(inputs[0] == 's' && inputs[1] == 'a' && inputs[2] == 'v'
                   && inputs[3] == 'e') {
                    save_game(inputs, height, width, player, numTile,
                              round, board);
                }
                input[2] = 1;
                sscanf(inputs, "%d %d %d", &input[0], &input[1], &input[2]);
            }
            if (place_legal(input[0], input[1], input[2], height, width,
                            tile, board)) {
                place_tiles(input[0], input[1], input[2], height, width,
                            player, tile, board);
            } else {
                show = 0;
                continue;
            }
            round++;
            player = 2;
            player1Input[0] = input[0];
            player1Input[1] = input[1];
            show = 1;
        } else {
            if (is_game_over(height, width, tile4, board)) {
                printf("Player * wins\n");
                return 0;
            }
            if (p2type) {
                if (p2type == 1) {
                    auto_player1(height, width, input, tile4, board);
                } else {
                    auto_player2(height, width, player2Input, 2, tile4, board);
                    input[0] = player2Input[0];
                    input[1] = player2Input[1];
                    input[2] = player2Input[2];
                }
                printf("Player # => %d %d rotated %d\n", input[0],
                       input[1], input[2]);
                
            } else {
                if (show) {
                    show_tile(tile4);
                    show = 1;
                }
                printf("Player #] ");
                char inputs[80];
                if (fgets(inputs, 80, stdin) == NULL) {
                    fprintf(stderr, "End of input\n");
                    exit(10);
                }
                if(inputs[0] == 's' && inputs[1] == 'a' && inputs[2] == 'v'
                   && inputs[3] == 'e') {
                    save_game(inputs, height, width, player, numTile,
                              round, board);
                }
                input[2] = 1;
                sscanf(inputs, "%d %d %d", &input[0], &input[1], &input[2]);
            }
            if (place_legal(input[0], input[1], input[2], height, width,
                            tile4, board)) {
                place_tiles(input[0], input[1], input[2], height, width,
                            player, tile4, board);
            } else {
                show = 0;
                continue;
            }
            round++;
            player = 1;
            player2Input[0] = input[0];
            player2Input[1] = input[1];
            show = 1;
        }
        
    }
    return 0;
}
