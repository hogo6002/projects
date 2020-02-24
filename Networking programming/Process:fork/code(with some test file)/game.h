#ifndef GAME_H
#define GAME_H

#include "deck.h"
#include "player.h"
#include <stdbool.h>

/**
 * Set up cards for game start.
 * Store the face up number for deck cards.
 * Choose the face up cards for deck cards.
 * @param deckCards :   store all cards, read from deck file
 */
void game_start(Cards *deckCards);

/**
 * Check whether the game is over or not
 * @return 1 if game is over
 *         0 if game is not over
 */
int is_game_over(Players *players, Hub *hub, Cards *faceUpCards);

/**
 * Send the game over message to stdout and all the players.
 * @param currentPlayer
 */
void gmae_over(Player *currentPlayer);

/**
 * Send the new cards message at begin the game to player from hub.
 * @param faceUpCards   :   the deck of cards that we use in the whole game
 */
void for_player(Cards *faceUpCards);

#endif
