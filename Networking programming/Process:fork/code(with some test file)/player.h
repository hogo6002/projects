#ifndef PLAYER_H
#define PLAYER_H

#include <unistd.h>
#include <stdbool.h>
#include "deck.h"

/* Contains a single player information. */
typedef struct Player Player;
struct Player {
    char pLetter;      // Player letter starts with A, same order with playerId
    int point;         // how many point does player has
    Tokens *tokens;     // The tokens that this player has
    Tokens *discount;   // Discount for each colour that player has
};

/* Contains all the players in this game and how many players are there. */
typedef struct {
    int size;              // how many players joined this game.
    Player **player;        // An array of all the players.
} Players;

/**
 * Find the current player by mod round by player size.
 * @param round :   the round of the game
 * @param players   :   the total players of the game
 * @return  :   the current player in this round
 */
Player *current_player(int round, Players *players);

/**
 * Player purchased a card, calculate the tokens that player need to pay.
 * And send the message about purchase card from player to hub.
 * @param player    :   The player who purchase the card.
 * @param card  :   The card which is purchased by player.
 * @return  Card that purchased by player
 */
Card *purchase_card(Player *player, Card *card);

/**
 * To figure out wether player has enough tokens to buy this card or not
 * @param player The current player who wants to buy this card
 * @param card The card that player wants to buy
 * @return 0 if player cannot afford it
 *         1 if player can afford it
 */
int can_afford(Player *player, Card *card);

/**
 * Pay the tokens from player.
 * @param player    :   The player who purchase the card.
 * @param card  :   The card which is purchased by player.
 */
void pay_tokens(Player *player, Card *card);

/**
 *
 * @param card
 * @return
 */
int total_tokens(Card *card, Player *player);

/**
 * Calculate the max tokens that player need to pay to by a card from face
 * up cards.
*  @param cards :    All the face up cards.
 * @param player    :   player who is selected to find the max tokens.
 * @return  :   The max tokens as integer.
 */
int max_tokens(Cards *cards, Player *player);

/**
 * Check is there any card can be purchased by player
 * @param player    :   The player who is used to compare
 * @param faceUpCards   :   The deck of cards
 * @return  :   1 - if there is a card that player can afford it.
 *              0 - if player cannot afford any cards.
 */
int can_purchase(Player *player, Cards *faceUpCards);

/**
 * Create all the players by the size of player.
 * Initial all players with no tokens no discount.
 * And named those players by give the player letter and player id.
 * @param playerSize    :   Read from argv, that total players.
 * @return  :   the total players who joined this game.
 */
Players *create_players(int playerSize);

/**
 * Splite the took tokens meesage, which is sent from hub to player.
 * @param string    :   the string which stored those information
 * @return  :   Player who is a temp player, stores the player letter and
 *              tokens that read from message.
 */
Player *split_took_tokens(char *string);

/**
 * Find the player who took tokens and add those tokens to this player.
 * Find to player who has the same player letter with the temp player.
 * Add the tokens to this player.
 * @param player    :   The temp player who stored the player letter and
 *                      tokens.
 * @param totalPlayer   :   All of the players who joined this game.
 */
void took_tokens(Player *player, Players *totalPlayer);

/**
 * Check whether there is a space in the message or not.
 * If there is a space in this message, then it is the invalid message, exit
 * process with 6.
 * @param message   :   the message that received by player
 */
void check_space(char *message);

/**
 * Display the information about players and cards, which will print out to
 * stderr.
 * @param faceUpCards   :   the deck of cards
 * @param totalPlayer   :   all the players who joined this game
 */
void display_info(Cards *faceUpCards, Players *totalPlayer);

/**
 * Check whether the message is "wild" or not
 * @param message   :   received from pipe
 *  @return  :    1 - if it is wild
 *                0 - if it is not wild
 */
int is_wild(char *message);

/**
 * Check whether the message is "took" or not
 * @param message   :   received from pipe
 *  @return  :    1 - if it is took
 *                0 - if it is not took
 */
int is_took(char *message);

/**
 * Check whether the message is "purchased" or not
 * @param message   :   received from pipe
 *  @return  :    1 - if it is purchased
 *                0 - if it is not purchased
 */
int is_purchased(char *message);

/**
 * Check whether the message is "newcard" or not
 * @param message   :   received from pipe
 *  @return  :    1 - if it is newcard
 *                0 - if it is not newcard
 */
int is_newcard(char *message);

/**
 * Check whether the message is "tokens" or not
 * @param message   :   received from pipe
 *  @return  :    1 - if it is tokens
 *                0 - if it is not tokens
 */
int is_tokens(char *message);

/**
 * Check whether the message is "eog" or not
 * @param message   :   received from pipe
 *  @return  :    1 - if it is eog
 *                0 - if it is not eog
 */
int is_eog(char *message);

/**
 * Check whether the message is "dowhat" or not
 * @param message   :   received from pipe
 *  @return  :    1 - if it is dowhat
 *                0 - if it is not dowhat
 */
int is_dowhat(char *message);

/**
 * Check whether the message is equal to specific string.
 * Identify which action is this message.
 * @param message   -   received from pipe
 * @param string    -   the string, which represent a specific action.
 * @return  1 - if they are equal to each other
 *          0 - if they are not.
 */
int is_equal(char *message, char *string);

/**
 * Create the face up cards for player.
 * @return  :   face up cards
 */
Cards *face_up_card();

/**
 * Remove specific card from the face up cards.
 * Move this card to the end of face up cards, then replace by new card.
 * @param faceUpCards   :   the face up cards that create by player.
 * @param cardID    :   the card id, which is the faceUp.
 */
void remove_card(Cards *faceUpCards, int cardID);

/**
 * Add card to face up cards. Add to the end.
 * @param faceUpCards   :   The face up cards, which are create by player
 * @param card  :   card which needs to add to face up cards.
 */
void add_card(Cards *faceUpCards, Card *card);

/**
 * Find the winner by check who has the heighest point or has the enough point.
 * @param players   :   The total players who joined this game.
 * @return  :   this player
 */
Player *find_winner(Players *players);

/**
 * Decode the purchased, and remove those tokens from player then add them to
 * hub. Add the discount to specific player.
 * @param hub
 * @param card
 * @param faceUpCards
 * @param player
 */
void decode_purchased(Hub *hub, Card *card, Cards *faceUpCards, Player
        *player);

/**
 * Report all errors and exit with specific state.
 * Parameters:
 *          argc    :   how many arguments that is used to call this function.
 *          argv    :   all the arguments
 */
void report_err(int argc, char **argv, char *name);

/**
 * Respond to eog message, exit the player with state 0.
 * @param totalPlayer   :   all the players who joined this game.
 */
void action_eog(Players *totalPlayer);

/**
 * Respond to tokens message, add those tokens to hub.
 * @param message   :   received from pipe
 * @param hub   :    contains all the tokens and point
 * @param totalPlayer   :   all the players who joined this game.
 * @param faceUpCards   :   all the available cards
 */
void action_tokens(char *message, Hub *hub, Players *totalPlayer, Cards
        *faceUpCards);

/**
 * Respond to newcard message. Add the cards to face up cards.
 * @param message   :    received from pipe
 * @param faceUpCards   :   all the available cards
 * @param totalPlayer   :   all the players who joined this game.
 */
void action_newcard(char *message, Cards *faceUpCards, Players
        *totalPlayer);

/**
 * Respond to purchased message. Remove the card from face up cards.
 * @param message   :    received from pipe
 * @param totalPlayer   :   all the players who joined this game.
 * @param hub   :    contains all the tokens and point
 * @param faceUpCards   :   all the available cards
 */
void action_purchased(char *message, Players *totalPlayer, Hub *hub, Cards
        *faceUpCards);

/**
 * Respond to took message. Take tokens from hub, add it to player
 * @param message   :    received from pipe
 * @param totalPlayer   :   all the players who joined this game.
 * @param hub   :    contains all the tokens and point
 * @param faceUpCards   :   all the available cards
 */
void action_took(char *message, Players *totalPlayer, Hub *hub,
        Cards *faceUpCards);

/**
 * Respond to wild message, take a wild from hub to player.
 * @param message   :   received from pipe
 * @param totalPlayer   :   all the players who joined this game.
 * @param faceUpCards   :   all the available cards
 */
void action_wild(char *message, Players *totalPlayer, Cards
        *faceUpCards);

/**
 * Find whether there is other winners or not
 * @param players   :   total players of this game
 * @param winner    :    the first winner that we have found.
 */
void other_winners(Players *players, Player *winner);


#endif // PLAYER_H
