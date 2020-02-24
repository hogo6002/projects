#ifndef GAME1_DECK_H
#define GAME1_DECK_H

#include <stdio.h>

typedef struct {
    int tokensP;        // The number of token purple
    int tokensB;        // The number of token Brown
    int tokensY;        // The number of token Yellow
    int tokensR;        // The number of token Red
    int tokensW;        // The number of token Wild
} Tokens;

typedef struct {
    char discount;
    int point;
    int faceUp;     // id of all faceUp cards
    int status;   // whether it was bought by player. 0 if bought, 1 if not
    int id;
    Tokens* tokens;
} Card;

typedef struct {
    Card** cards;
    int capacity;
    int faceUpNumber;
} Cards;

typedef struct {
    Tokens* tokens;     // The tokens that hub has
    int point;          // The points to trigger end of the game
    int round;          // Record the number of round
    int pid;
    FILE *out;
} Hub;

Cards* read_deck(FILE* file);

Card* split_newCard(char *string);

Tokens* split_tokens(char *string);

Card* split_purchase_card(char* string);

void update_cards(int faceUpId, Cards* faceUpCards);

Card* split_purchased_card(char* string);

Card* read_new_card(char* string);

Tokens* split_tokens_start(char *string);

void check_discount(char colour);

void report_update(Cards* faceUpCards, int count);

#endif //GAME1_DECK_H
