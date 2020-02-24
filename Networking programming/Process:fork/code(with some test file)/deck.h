#ifndef DECK_H
#define DECK_H

#include <stdio.h>

/* Contains the attribute of a whole tokens of the game .*/
typedef struct {
    int tokensP;        // The number of token purple
    int tokensB;        // The number of token Brown
    int tokensY;        // The number of token Yellow
    int tokensR;        // The number of token Red
    int tokensW;        // The number of token Wild
} Tokens;

/* Contains the attribute of a single card. */
typedef struct {
    char discount;
    int point;
    int faceUp;     // id of all faceUp cards
    int status;   // whether it was bought by player. 0 if bought, 1 if not
    Tokens *tokens;
} Card;

/* Contains the cards of the game. */
typedef struct {
    Card **cards;
    int capacity;       // size of this deck.
    int faceUpNumber;      // how many cards are face up
} Cards;

/* Contains all the attribute for a hub. */
typedef struct {
    Tokens *tokens;     // The tokens that hub has
    int point;          // The points to trigger end of the game
    int round;          // Record the number of round
} Hub;

/**
 * Read the data from a given DeckFile and return the decks.
 * @param file  :   the DeckFile that we need to read data from
 * @return  :   A cards struct that contains all the card inside of this file
 */
Cards *read_deck(FILE *file);

/**
 * Split the new card message, read from a string and return a card
*  @param string    :   the string that hub send to player
 * @return  card that store information from this string
 */
Card *split_newcard(char *string);

/**
 * Split the string (without wild) which contains tokens information
 * @param string    :   tokens string format that (int,int,int,int)
 * @return  Tokens which store all value from string
 */
Tokens *split_tokens(char *string);

/**
 * Split string that contains purchase card information. send from player to
 * hub.
 * @param string    :   purchase card string which format as "purchaseC:P,B,
 *                      Y,R,W"
 * @return  :   card which store the value from string
 */
Card *split_purchase_card(char *string);

/**
 * Updata cards, show the face up cards.
 * Move the purcahsed card to the end of the face up cards.
 * The replace it by the new card.
 * @param faceUpId  :   which is faceUp. it is the number of this card among
 *                      all the face up cards.
 * @param faceUpCards   :   It is decks, which read from deck file. and
 *                          store all cards.
 */
void update_cards(int faceUpId, Cards *faceUpCards);

/**
 * Split string that contains purchase card information. send from hub to
 * player.
 * @param string    :   purchase card string which format as "purchasedP:C:P,B,
 *                      Y,R,W"
 * @return  :   card which store the value from string
 */
Card *split_purchased_card(char *string);

/**
 * Split string that store new card information. send from hub to player
 * @param string    :   new card information, format as "newcardD:V:P,B,Y,R"
 * @return  :   card which store the value from string
 */
Card *read_new_card(char *string);

/**
 * Split the string from the deck file, which may contain some invalid string
 * Check whether the stirng is valid or not
 * If it is, return Tokens, if not, exit with Invaild deck file.
 * @param string    :   read from deck file and comtains value of tokens.
 * @return  :   the tokens that read from string
 */
Tokens *split_tokens_start(char *string);

/**
 * Check whther the card's discount is valid or not.
 * If the discount is invalid, exit with communication Error.
 * This is used by player
 * @param colour    :   the colour that read from the file
 */
void check_discount(char colour);

/**
 * Send new card message from hub to all the player.
 * Find how many message need to be sent for a single player
 * @param faceUpCards   :   the whole decks
 * @param count :   the number that how many people bought cards before this
 *                  player in the turn.
 */
void report_update(Cards *faceUpCards, int count);

#endif // DECK_H
