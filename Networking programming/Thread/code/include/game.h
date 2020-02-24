#ifndef _GAME_H_
#define _GAME_H_

#include "token.h"
#include "protocol.h"

#define MIN_PLAYERS 2
#define MAX_PLAYERS 26

#define BOARD_SIZE 8

/* The error codes for game play scenarios.
 */
enum ErrorCode {
    NOTHING_WRONG = 0,
    COMMUNICATION_ERROR = 1,
    PLAYER_CLOSED = 2,
    PROTOCOL_ERROR = 3,
    ILLEGAL_MOVE = 4,
    INTERRUPTED = 5,
};

/* The information and state that describes a single player.
*/
struct Player {
    // The ID of this player, starting at 0
    int playerId;
    // Possible discounts this player has on all non-wild tiles
    int discounts[TOKEN_MAX - 1];
    // The number of tokens this player has
    int tokens[TOKEN_MAX];
    // The score this player has
    int score;
    // The name of the player
    char* name;
    // Place for arbitrary data
    void* data;
};

/* Set the initial conditions of a player. Takes as inputs a pointer to the
 * location to store the initial player state, and the id of this player within
 * the game.
 */
void initialize_player(struct Player* output, int id);

/* Processes a "take" message, and moves tokens from the global token pool to
 * a player's token pool. Takes as arguments a pointer to the global token
 * pool (must be TOKEN_MAX - 1 in length), a pointer to the player taking the
 * tokens, and the body of the take (or took) message being processed. Returns
 * 0 on success, and -1 on failure (leaving the tokenPool and player
 * untouched).
 */
int process_take_tokens(int* tokenPool, struct Player* player,
        struct TakeMessage tokens);

/* Let a player purchase a card from the deck. Returns 0 if the purchase
 * succeeds, and nonzero if it fails. Takes as arguments the size of the pool
 * of non wild tokens available in the board (must have length TOKEN_MAX), the
 * player purchasing and the card being purchased. On failure, neither the
 * tokenPool nor the player are affected. On success, both of these are updated
 * to reflect the purchase taking place.
 */
int buy_card(int* tokenPool, struct Player* player, struct Card card);

/* Check whether an attempt to purchase a card by a player is legitimate. Takes
 * as arguments the player and card involved in the purchase, as well as a
 * pointer to an array of length TOKEN_MAX that represents the amount of each
 * token that the player claimed to use in the purchase. Returns -1 on failure,
 * and 0 if the costs are valid.
 */
int validate_costs(struct Player player, struct Card card, int* tokensUsed);

/* Figure out which cards a player is able to buy, from those on the board.
 * Takes as inputs an array (with length) of the cards currently on the board,
 * the player attempting the purchase, and a location to store outputs. On
 * completion of this function, output (an array, with space for at least
 * boardSize elements) will be filled with the cards the player is able to
 * purchase, in the order they were listed. This function will return the
 * number of cards which could be bought.
 */
int get_purchaseable(const struct Card* board, int boardSize,
        struct Card* output, struct Player player);

/* Take a list of cards which a player is able to purchase, and figure out
 * which of them would be the best purchase. Takes as arguments an array (with
 * length) of cards that are to be narrowed down, and an objective function.
 * The objective function should evaluate to an integer which represents that
 * cards value as a purchase. On completion, the best card(s) will be moved to
 * the start of the input array, and the function will return the number of
 * cards tying for top spot with that objective function. As a last parameter,
 * this function takes an argument which can be given to the objective
 * function.
 */
int find_best_purchases(struct Card* cards, int cardCount,
        int (*objective)(struct Card, const void* arg), const void* arg);

#endif
