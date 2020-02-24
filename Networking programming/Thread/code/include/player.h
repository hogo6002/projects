#ifndef _PLAYER_H_
#define _PLAYER_H_

#include "game.h"
#include "protocol.h"

/* The information that describes a game, as seen by the player.
 */
struct GameState {
    // The total number of players
    int playerCount;

    // The state of each player within the game
    struct Player* players;

    // The ID of this individual player
    int selfId;

    // The number of available tokens of each type (make space for wild tokens,
    // but never use them)
    int tokenCount[TOKEN_MAX];

    // The number of cards currently on the board - guaranteed to be in the
    // range [0. BOARD_SIZE)
    int boardSize;

    // The cards currently on the board
    struct Card board[BOARD_SIZE];
};


/* Prints to stdout the information relating to winners at the end of the game.
 * Takes as an argument the game struct full of information to be printed.
 */
void display_eog_info(const struct GameState* game);

/* Prints to stdout the state of the given player, as per the game state 
 * that is shown after receiving certain messages.
 */
void display_player_state(struct Player player);

/* Prints to stdout the information relating to game state that needs to be
 * printed after each message that isn't eog or dowhat.
 */
void display_turn_info(const struct GameState* game);

/* Updates internal game state in response to a "PURCHASED" message from the
 * hub. Will return 0 if the message is valid (syntactically, semantically and
 * contextually), and the relevant error code if it is not. Takes as arguments
 * the game state (to be updated) and the message (to be parsed).
 */
enum ErrorCode handle_purchased_message(struct GameState* game,
        const char* line);

/* Updates internal game state in response to a "TOOK" message from the hub.
 * Will return 0 if the message is valid (syntactically, semantically and
 * contextually), and the relevant error code if it is not. Takes as arguments
 * the game state (to be updated) and the message (to be parsed).
 */
enum ErrorCode handle_took_message(struct GameState* game, const char* line);

/* Updates internal game state in response to a "WILD" message from the hub.
 * Will return 0 if the message is valid (syntactically, semantically and
 * contextually), and the relevant eror code if it is not. Takes as arguments
 * the game state (to be updated) and the message (to be parsed).
 */
enum ErrorCode handle_took_wild_message(struct GameState* game,
        const char* line);

/* Updates internal game state in response to a "NEW CARD" message from the
 * hub. Will return 0 if the message is valid (syntactically, semantically and
 * contextually), and the relevant error code if it is not. Takes as arguments
 * the game state (to be updated) and the message (to be parsed).
 */
enum ErrorCode handle_new_card_message(struct GameState* game,
        const char* line);

#endif
