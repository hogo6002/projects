#ifndef GAME_H
#define GAME_H

#include "deck.h"
#include "player.h"
#include <stdbool.h>

void game_start(Cards* deckCards);

/**
 * Check whether the game is over or not
 * @return 1 if game is over
 *         0 if game is not over
 */
int is_game_over(Players* players, Hub* hub, Cards* faceUpCards);

void gmae_over(Player* currentPlayer);

void for_player(Cards *faceUpCards);

#endif
