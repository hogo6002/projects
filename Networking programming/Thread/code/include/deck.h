#ifndef _DECK_H_
#define _DECK_H_

#include "protocol.h"

#define INITIAL_CARD_BUFFER_SIZE 10

/* Status of the deck parsing
 */
enum DeckStatus {
    VALID = 0,
    DECK_ACCESS = 1,
    DECK_INVALID = 2
};

/* Open, close, and parse a deck file. Takes as arguments points to an array
 * (with length) of cards, and the file to open. On success, returns 0 and
 * leaves cardCount and cards pointing to an allocated array. On failure,
 * returns the relevant DeckStatus, and leaves cardCount and cards pointing to
 * unspecified memory (cards will not need to be freed).
 */
enum DeckStatus parse_deck_file(int* cardCount, struct Card** cards,
        const char* filename);

#endif
