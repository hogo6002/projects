//
// Created by Holly Gong on 21/9/18.
//
#include <string.h>
#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>
#include "game.h"

#define START_CARDS 8

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

void for_player(Cards *faceUpCards) {
    for (int i = 0; i < faceUpCards->faceUpNumber; i++) {
        fprintf(stderr, "newcard%c:%d:%d,%d,%d,%d\n",faceUpCards->cards[i]->discount,
                faceUpCards->cards[i]->point,
                faceUpCards->cards[i]->tokens->tokensP,
                faceUpCards->cards[i]->tokens->tokensB,
                faceUpCards->cards[i]->tokens->tokensY,
                faceUpCards->cards[i]->tokens->tokensR);
    }
}

int is_game_over(Players *players, Hub *hub, Cards* faceUpCards) {
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

void gmae_over(Player* currentPlayer) {
    printf("Winner(s) %c\n", currentPlayer->pLetter);
    fprintf(stderr, "eog\n");
}
