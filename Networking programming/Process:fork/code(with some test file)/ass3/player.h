#ifndef GAME1_PLAYER_H
#define GAME1_PLAYER_H

#include <unistd.h>
#include <stdbool.h>
#include "deck.h"

typedef struct Player Player;
struct Player {
    int playerId;       // The number of this particular player (starting at 0)
    char pLetter;      // Player letter starts with A, same order with playerId
    char playerType;    // Player type, A for shenzi, B for banzai, C for ed
    int point;
    Tokens* tokens;     // The tokens that this player has
    Tokens* discount;   // Discount for each colour that player has
};

typedef struct {
    int size;
    Player** player;
} Players;

Player *current_player(int round, Players* players);

Card *purchase_card(Player *player, Card* card);

/**
 * To figure out wether player has enough tokens to buy this card or not
 * @param player The current player who wants to buy this card
 * @param card The card that player wants to buy
 * @return 0 if player cannot afford it
 *         1 if player can afford it
 */
int can_afford(Player* player, Card* card);

void pay_tokens(Player* player, Card* card);

/**
 *
 * @param card
 * @return
 */
int total_tokens(Card* card, Player* player);

int max_tokens(Cards* cards, Player* player);

int can_purchase(Player* player, Cards* faceUpCards);
Player* create_player();

Players *create_players(int playerSize);

Player* split_took_tokens(char* string);

Player* current (Players* totalPlayer, Player* lastPlayer);

void took_wild(Player* player, Players* totalPlayer);

void took_tokens(Player* player, Players* totalPlayer);

void check_space(char* message);

void display_info (Cards* faceUpCards, Players* totalPlayer);

int is_wild(char* message);

int is_took(char* message);

int is_purchased(char* message);

int is_newcard(char* message);

int is_tokens(char* message);

int is_eog(char* message);

int is_dowhat(char* message);

int is_equal(char* message, char* string);

Cards* face_up_card();

void remove_card(Cards* faceUpCards, int cardID);

void add_card(Cards* faceUpCards, Card* card);

Player* find_winner(Players* players);

void decode_purchased (Hub *hub, Card* card, Cards* faceUpCards, Player*
player);

#endif //GAME1_PLAYER_H
