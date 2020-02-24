#ifndef _TOKEN_H_
#define _TOKEN_H_

/* All possible tokens that can be used in the game.
 */
enum Token {
    TOKEN_PURPLE = 0,
    TOKEN_BROWN = 1,
    TOKEN_YELLOW = 2,
    TOKEN_RED = 3,
    TOKEN_WILD = 4,
};

/* Gets the single character representation of this token.
 */
char print_token(enum Token token);

/* Counts how many different types of token are available in a token set. Takes
 * as arguments an array of token numbers (with length). Returns the number of
 * types of available tokens.
 */
int distinct_tokens_available(const int* tokens, int tokenCount);

/* The number of different types of token in the game (including wildcards).
 */
#define TOKEN_MAX (TOKEN_WILD + 1)

/* The number of piles that a player must take tokens from, when taking tokens.
 */
#define TAKE_NUMBER 3

/* If possible, adds a token to the list of tokens being taken in a move. Takes
 * as arguments a pointer to the tokens being taken, a pointer to the tokens
 * currently available in the pool, and a selection of which token to take. If
 * successful, will increment the relevant token in takePool.
 */
void take_if_possible(int* takePool, const int* tokenPool, enum Token choice);

#endif
