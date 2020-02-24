#ifndef _PROTOCOL_H_
#define _PROTOCOL_H_

#include "token.h"

/* The information that describes an individual card.
 */
struct Card {
    // Amount of each token type (non-wild) required to purchase this card
    int cost[TOKEN_MAX - 1];
    // Amount of each token type (non-wild) this card grants as discount
    enum Token discount;
    // The amount of points awarded by this card
    int points;
};

/* Parse a card from a string. On success, returns 0 and fills output with
 * information about the card. On failure, returns -1 and the value of output
 * is unspecified.
 */
int parse_card(struct Card* output, const char* input);

/* Types of messages that the player can send to the hub.
 */
enum MessageFromPlayer {
    // The player intends to purchase a card
    PURCHASE,
    // The player intends to take tokens
    TAKE,
    // The player intends to take a wild token
    WILD,
};

/* Determines the type of a message received from a player. Takes as input the
 * message itself, and returns either the type of the message, or -1 if the
 * message is invalid. For messages that have arguments, this function does
 * not check the validity of the arguments.
 */
enum MessageFromPlayer classify_from_player(const char* message);

/* Information relating to a purchase message from a player.
 */
struct PurchaseMessage {
    // The ID of the card within the drawn pile
    int cardNumber;
    // The amount of each token type that is spent purchasing that card
    int costSpent[TOKEN_MAX];
};

/* Parses the arguments of a purchase message from a player. If the message is
 * valid, the number of tokens in each non-wild pile is stored in output, and 0
 * is returned. If the message is not valid, then -1 is returned.
 */
int parse_purchase_message(struct PurchaseMessage* output,
        const char* message);

/* Serializes a purchase message into a string, ready to be sent. This string
 * will be in dynamically allocated memory, so should be freed after use.
 * Takes as an argument the message body, and returns the string. The output
 * of this function is newline terminated.
 */
char* print_purchase_message(struct PurchaseMessage input);

/* Information relating to a take message from a player.
 */
struct TakeMessage {
    int tokens[TOKEN_MAX - 1];
};

/* Parses the arguments of a take message from a player. If the message is
 * valid, the arguments are stored in output, and 0 is returned. If the message
 * is not valid, then -1 is returned.
 */
int parse_take_message(struct TakeMessage* output, const char* message);

/* Serializes a take message into a string, ready to be sent. This string will
 * be in dynamically allocated memory, so should be freed after use. Takes as
 * an argument the message body, and returns the string. The output of this
 * function is newline terminated.
 */
char* print_take_message(struct TakeMessage input);

/* Types of messages that the hub can send to the player.
 */
enum MessageFromHub {
    // The game has ended
    END_OF_GAME,
    // What do you want to do, its your turn
    DO_WHAT,
    // A players has purchased a card
    PURCHASED,
    // A player has taken some tokens
    TOOK,
    // A player has taken a wild token
    TOOK_WILD,
    // A new card is available in the deck
    NEW_CARD,
    // Informs players of the number of each type of token initially available
    TOKENS,
    // Informs players that another player has disconnected, and that the game
    // is over
    DISCO,
    // Informs players that an invalid move has been made by a player, and that
    // the game is over
    INVALID,
};

/* Determines the type of a message received from the hub. Takes as input the
 * message itself, and returns either the type of message, or -1 if the message
 * is invalid. For messages that have arguments, this function does not check
 * the validity of the arguments.
 */
enum MessageFromHub classify_from_hub(const char* message);

/* Parses the arguments of a purchased message from the hub. If the message is
 * valid, the arguments are left in output, and playerId, and 0 is returned. If
 * the message is not valid, then -1 is returned.
 */
int parse_purchased_message(struct PurchaseMessage* output, int* playerId,
        const char* message);

/* Serializes a purchased message into a string, ready to be sent. This string
 * will be in dynamically allocated memory, so should be freed after use. Takes
 * as arguments the arguments of the message, and returns the serialized
 * string. The output of this function is newline terminated.
 */
char* print_purchased_message(struct PurchaseMessage input, int playerId);

/* Parses the arguments of a took message from the hub. If the message is
 * valid, the arguments are left in output and playerId, and 0 is returned. If
 * the message is not valid, then -1 is returned.
 */
int parse_took_message(struct TakeMessage* output, int* playerId,
        const char* message);

/* Serializes a took message into a string, ready to be sent. This string will
 * be in dynamically allocated memory, so should be freed after use. Takes as
 * an argument the message body, and returns the string. The output of this
 * function is newline terminated.
 */
char* print_took_message(struct TakeMessage input, int playerId);

/* Parses the arguments of a took wild message from the hub. If the message is
 * valid, the ID of the player who took the wild token is stored in output, and
 * 0 is returned. If the message is not valid, then -1 is returned.
 */
int parse_took_wild_message(int* output, const char* message);

/* Serializes a took wild message into a string, ready to be sent. This string
 * will be in dynamically allocated memory, so should be freed after use. Takes
 * as an argument the ID of the player who took the wild token, and returns the
 * string. The output of this function is newline terminated.
 */
char* print_took_wild_message(int input);

/* Parses the arguments of a new card message from the hub. If the message is
 * valid, the information relating to the new card is stored in output, and 0
 * is returned. If the message is not valid, then -1 is returned.
 */
int parse_new_card_message(struct Card* output, const char* message);

/* Serializes a new card message into a string, ready to be sent. This string
 * will be in dynamically allocated memory, so should be freed after use.
 * Takes as an argument the message body, and returns the string. The output of
 * this function is newline terminated.
 */
char* print_new_card_message(struct Card input);

/* Parses the arguments of a tokens message from the hub. If the message is
 * valid, the number of tokens in each non-wild pile is stored in output, and 0
 * is returned. If the message is not valid, then -1 is returned.
 */
int parse_tokens_message(int* output, const char* message);

/* Serializes a tokens message into a string, ready to be sent. This string
 * will be in dynamically allocated memory, so should be freed after use. Takes
 * as arguments the number of tokens (the message argument) and returns the
 * serialized string. The output of this function is newline terminated.
 */
char* print_tokens_message(int input);

/* Parses the arguments of a disco message from the hub. If the message is
 * valid, the ID of the player who disconnected is stored in output, and 0 is
 * returned. If the message is not valid, then -1 is returned.
 */
int parse_disco_message(int* output, const char* message);

/* Serializes a disco message into a string, ready to be snt. This string will
 * be in dynamically allocated memory, so should be freed after use. Takes as
 * arguments the ID of the player who disconnected, and returns the serialized
 * string. The output of this function is newline terminated.
 */
char* print_disco_message(int input);

/* Parses the arguments of an invalid message from the hub. If the message is
 * valid, the ID of the player who sent the invalid message is stored in
 * output, and 0 is returned. If the message is not valid, then -1 is
 * returned.
 */
int parse_invalid_message(int* output, const char* message);

/* Serializes an invalid message into a string, ready to be snt. This string
 * will * be in dynamically allocated memory, so should be freed after use.
 * Takes as * arguments the ID of the player who sent the invalid message, and
 * returns the serialized * string. The output of this function is newline
 * terminated.
 */
char* print_invalid_message(int input);

#endif
