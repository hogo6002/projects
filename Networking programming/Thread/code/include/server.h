#ifndef _SERVER_H_
#define _SERVER_H_

#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>

#include "game.h"
#include "token.h"
#include "protocol.h"

/* The server's view of a player, including both the players state and the
 * means of contacting that player.
 */
struct GamePlayer {
    // The player's state within the game
    struct Player state;

    // The means of contacting the connected player
    int fileDescriptor;
    // The file that can be written to, to talk to the player.
    FILE* toPlayer;
    // The file that can be read from, to listen to the player.
    FILE* fromPlayer;
};

/* Server view of game state.
 */
struct Game {
    // Name of the game
    char* name;

    // The number of points required to win
    int winScore;

    // The players in the game
    int playerCount;
    struct GamePlayer* players;

    // The cards currently on the board
    int boardSize;
    struct Card board[BOARD_SIZE];

    // The cards still in the deck, and not on the board
    int deckSize;
    struct Card* deck;

    // The number of available tokens of each type (make space for wild tokens,
    // but never use them)
    int tokenCount[TOKEN_MAX];

    // Arbitrary data storage
    void* data;
};


/* Draws a card from the deck and moves it onto the board. Also will update all
 * players of this information. Takes as an argument a pointer to the game
 * state for updating.
 */
void draw_card(struct Game* game);

/* Determines whether there are any cards left in the deck or on the board.
 * Used as part of calculating game overs. Takes as an argument a pointer to
 * the game struct, and returns true if there are cards left.
 */
bool cards_left(const struct Game* game);

/* Determines whether or not the game is currently over. Takes an an argument
 * the game state, and returns true if the game has completed, else false. Can
 * only be run at the end of a round.
 */
bool is_game_over(const struct Game* game);

/* Alerts all players of a purchase that has been made by one player. Takes as
 * arguments the ID of the player who sent the message, the game (to access
 * children) and the body of the message sent by the purchasing player
 * requesting the purchase. Does not update game state in any way, or attempt
 * to validate the message.
 */
void send_purchased_message(int playerId, struct Game* game,
        struct PurchaseMessage received);

/* Updates internal game state in response to a "purchase" message from a
 * player. Will return 0 if the message is valid (syntactically, semantically
 * and contextually), and the relevant exit code if it is not. Takes as
 * arguments the game state (to be updated), the message (to be parsed) and the
 * ID of the player the messgae was received from.
 */
enum ErrorCode handle_purchase_message(int playerId, struct Game* game,
        const char* line);

/* Updates internal game state in response to a "take" message from a player.
 * Will return 0 if the message is valid (syntactically, semantically and
 * contextually), and the relevant exit code if it is not. Takes as arguments
 * the game state (to be updated), the message (to be parsed) and the Id of the
 * player the message was received from.
 */
enum ErrorCode handle_take_message(int playerId, struct Game* game,
        const char* line);

/* Updates internal game state in response to a "wild" message from a player.
 * Takes as arguments the game state (to be updated), and the ID of the player
 * who sent the message.
 */
void handle_wild_message(int playerId, struct Game* game);

#endif
