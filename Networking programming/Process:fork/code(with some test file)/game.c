#include <string.h>
#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>
#include "game.h"

#define START_CARDS 8

/**
 * Set up cards for game start.
 * Store the face up number for deck cards.
 * Choose the face up cards for deck cards.
 * @param deckCards :   store all cards, read from deck file
 */
void game_start(Cards *deckCards) {
    if (deckCards->capacity >= START_CARDS) {
        for (int i = 0; i < START_CARDS; i++) {
            deckCards->cards[i]->faceUp = i;
            printf("New card = Bonus %c, worth %d, costs %d,%d,%d,%d\n",
                    deckCards->cards[i]->discount,
                    deckCards->cards[i]->point,
                    deckCards->cards[i]->tokens->tokensP,
                    deckCards->cards[i]->tokens->tokensB,
                    deckCards->cards[i]->tokens->tokensY,
                    deckCards->cards[i]->tokens->tokensR);
        }
        deckCards->faceUpNumber = START_CARDS;
    } else {
        if (deckCards->capacity >= 1) {
            for (int i = 0; i < deckCards->capacity; i++) {
                deckCards->cards[i]->faceUp = i;
                printf("New card = Bonus %c, worth %d, costs %d,%d,%d,%d\n",
                        deckCards->cards[i]->discount,
                        deckCards->cards[i]->point,
                        deckCards->cards[i]->tokens->tokensP,
                        deckCards->cards[i]->tokens->tokensB,
                        deckCards->cards[i]->tokens->tokensY,
                        deckCards->cards[i]->tokens->tokensR);
            }
            deckCards->faceUpNumber = deckCards->capacity;
        }
    }

}

/**
 * Send the new cards message at begin the game to player from hub.
 * @param faceUpCards   :   the deck of cards that we use in the whole game
 */
void for_player(Cards *faceUpCards) {
    for (int i = 0; i < faceUpCards->faceUpNumber; i++) {
        fprintf(stderr, "newcard%c:%d:%d,%d,%d,%d\n",
                faceUpCards->cards[i]->discount,
                faceUpCards->cards[i]->point,
                faceUpCards->cards[i]->tokens->tokensP,
                faceUpCards->cards[i]->tokens->tokensB,
                faceUpCards->cards[i]->tokens->tokensY,
                faceUpCards->cards[i]->tokens->tokensR);
    }
}

/**
 * Check whether the game is over of not.
 * Check from whether a player reaches the specific point or whether cards
 * are all bought by players.
 * @param players   :   the total players who joined this game.
 * @param hub   :   the hub who store the point of winning.
 * @param faceUpCards   :   The deck of cards, which store the face up cards.
 * @return  :   1 - if the game is over.
 *              0 - if the game is not over.
 */
int is_game_over(Players *players, Hub *hub, Cards *faceUpCards) {
    for (int i = 0; i < players->size; i++) {
        if (players->player[i]->point >= hub->point) {
            return 1;
        }
    }
    if (faceUpCards->faceUpNumber == 0) {
        return 1;
    }
    return 0;
}

/**
 * Send the game over message to stdout and all the players.
 * @param currentPlayer
 */
void gmae_over(Player *currentPlayer) {
    printf("Winner(s) %c\n", currentPlayer->pLetter);
    fprintf(stderr, "eog\n");
}
