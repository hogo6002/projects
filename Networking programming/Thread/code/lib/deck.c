#include <stdlib.h>
#include <stdio.h>
#include "deck.h"
#include "util.h"

/* Open, close, and parse a deck file. Takes as arguments points to an array
 * (with length) of cards, and the file to open. On success, returns 0 and
 * leaves cardCount and cards pointing to an allocated array. On failure,
 * returns the relevant ErrorCode, and leaves cardCount and cards pointing to
 * unspecified memory (cards will not need to be freed).
 */
enum DeckStatus parse_deck_file(int* cardCount, struct Card** cards,
        const char* filename) {
    FILE* deck = fopen(filename, "r");
    if (deck == NULL) {
        return DECK_ACCESS;
    }

    *cardCount = 0;
    int capacity = INITIAL_CARD_BUFFER_SIZE;
    *cards = malloc(sizeof(struct Card) * capacity);

    while (1) {
        char* line;
        if (read_line(deck, &line, 0) <= 0) {
            free(line);
            if (feof(deck)) {
                // EOF, no more cards
                if (*cardCount == 0) {
                    free(*cards);
                    fclose(deck);
                    return DECK_INVALID;
                }
                break;
            } else {
                // empty line, or other IO error
                free(*cards);
                fclose(deck);
                return DECK_INVALID;
            }
        }

        if (parse_card(&(*cards)[*cardCount], line) != 0) {
            // could not parse the card
            free(line);
            free(*cards);
            fclose(deck);
            return DECK_INVALID;
        }

        *cardCount += 1;
        if (*cardCount + 1 >= capacity) {
            capacity *= 2;
            *cards = realloc(*cards, sizeof(struct Card) * capacity);
        }
        free(line);
    }

    fclose(deck);
    return 0;
}
