#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "player.h"

/**
 * Find the current player by mod round by player size.
 * @param round :   the round of the game
 * @param players   :   the total players of the game
 * @return  :   the current player in this round
 */
Player *current_player(int round, Players *players) {
    Player *currentPlayer = players->player[round % players->size];
    return currentPlayer;
}

/**
 * Check wheter player can afford this card or not. By compare the player
 * tokens and discount with card tokens.
 * @param player    :   The player who is used to compare
 * @param card  :   The card which is used to compare
 * @return  :   1 - if player can afford this card.
 *              0 - if player cannot afford this card.
 */
int can_afford(Player *player, Card *card) {
    if (player->tokens->tokensP + player->discount->tokensP >=
            card->tokens->tokensP &&
            player->tokens->tokensB + player->discount->tokensB >=
            card->tokens->tokensB &&
            player->tokens->tokensY + player->discount->tokensY >=
            card->tokens->tokensY &&
            player->tokens->tokensR + player->discount->tokensR >=
            card->tokens->tokensR) {
        return 1;
    }
    int wild = player->tokens->tokensW;
    int total = 0;
    int wildP = card->tokens->tokensP - (player->tokens->tokensP +
            player->discount->tokensP);
    if (wildP < 0) {
        wildP = 0;
    }
    int wildB = card->tokens->tokensB - (player->tokens->tokensB +
            player->discount->tokensB);
    if (wildB < 0) {
        wildB = 0;
    }
    int wildY = card->tokens->tokensY - (player->tokens->tokensY +
            player->discount->tokensY);
    if (wildY < 0) {
        wildY = 0;
    }
    int wildR = card->tokens->tokensR - (player->tokens->tokensR +
            player->discount->tokensR);
    if (wildR < 0) {
        wildR = 0;
    }
    total = wildP + wildB + wildY + wildR;
    if (total <= wild) {
//        fprintf(stderr, "card %d %d %d %d\n", card->tokens->tokensP,
//                card->tokens->tokensB, card->tokens->tokensY,
//                card->tokens->tokensR);
//        fprintf(stderr, "wildP %d %d %d %d\n", wildP, wildB, wildY, wildR);
        return 1;
    }
    return 0;
}

/**
 * Check is there any card can be purchased by player
 * @param player    :   The player who is used to compare
 * @param faceUpCards   :   The deck of cards
 * @return  :   1 - if there is a card that player can afford it.
 *              0 - if player cannot afford any cards.
 */
int can_purchase(Player *player, Cards *faceUpCards) {
    for (int i = 0; i < faceUpCards->faceUpNumber; i++) {
        if (can_afford(player, faceUpCards->cards[i])) {
            return 1;
        }
    }
    return 0;
}

/**
 * Player purchased a card, calculate the tokens that player need to pay.
 * And send the message about purchase card from player to hub.
 * @param player    :   The player who purchase the card.
 * @param card  :   The card which is purchased by player.
 * @return  Card that purchased by player
 */
Card *purchase_card(Player *player, Card *card) {
    card->status = 0;
    int tokenP = player->tokens->tokensP;
    int tokenB = player->tokens->tokensB;
    int tokenY = player->tokens->tokensY;
    int tokenR = player->tokens->tokensR;
    int tokenW = player->tokens->tokensW;
    pay_tokens(player, card);
    tokenP -= player->tokens->tokensP;
    tokenB -= player->tokens->tokensB;
    tokenY -= player->tokens->tokensY;
    tokenR -= player->tokens->tokensR;
    tokenW -= player->tokens->tokensW;
    player->tokens->tokensP += tokenP;      // add tokens back to player.
    // tokens will be removed when player receives purchased message from hub.
    player->tokens->tokensB += tokenB;
    player->tokens->tokensY += tokenY;
    player->tokens->tokensR += tokenR;
    player->tokens->tokensW += tokenW;
    printf("purchase%d:%d,%d,%d,%d,%d\n", card->faceUp, tokenP,
            tokenB, tokenY, tokenR, tokenW);
    fflush(stdout);
    return card;
}

/**
 * Pay the tokens from player.
 * @param player    :   The player who purchase the card.
 * @param card  :   The card which is purchased by player.
 */
void pay_tokens(Player *player, Card *card) {
    if (player->discount->tokensP < card->tokens->tokensP) {
        if ((card->tokens->tokensP - player->discount->tokensP) <=
                player->tokens->tokensP) {
            player->tokens->tokensP -= (card->tokens->tokensP -
                    player->discount->tokensP);
        } else {
            int wildP = card->tokens->tokensP - (player->tokens->tokensP +
                    player->discount->tokensP);
            player->tokens->tokensP = 0;
            player->tokens->tokensW -= wildP;
        }
    }
    if (player->discount->tokensB < card->tokens->tokensB) {
        if ((card->tokens->tokensB - player->discount->tokensB) <=
                player->tokens->tokensB) {
            player->tokens->tokensB -= (card->tokens->tokensB -
                    player->discount->tokensB);
        } else {
            int wildB = card->tokens->tokensB - (player->tokens->tokensB +
                    player->discount->tokensB);
            player->tokens->tokensB = 0;
            player->tokens->tokensW -= wildB;
        }
    }
    if (player->discount->tokensY < card->tokens->tokensY) {
        if ((card->tokens->tokensY - player->discount->tokensY) <=
                player->tokens->tokensY) {
            player->tokens->tokensY -= (card->tokens->tokensY -
                    player->discount->tokensY);
        } else {
            int wildY = card->tokens->tokensY - (player->tokens->tokensY +
                    player->discount->tokensY);
            player->tokens->tokensY = 0;
            player->tokens->tokensW -= wildY;
        }
    }
    if (player->discount->tokensR < card->tokens->tokensR) {
        if ((card->tokens->tokensR - player->discount->tokensR) <=
                player->tokens->tokensR) {
            player->tokens->tokensR -= (card->tokens->tokensR -
                    player->discount->tokensR);
        } else {
            int wildR = card->tokens->tokensR - (player->tokens->tokensR +
                    player->discount->tokensR);
            player->tokens->tokensR = 0;
            player->tokens->tokensW -= wildR;
        }
    }
}

/**
 * Calculate the total tokens that player need to pay to purchase a card
*  @param card  :    The card that player want to buy.
*  @param player    :    The player who purchase the card.
*  @return  :    The number that how many tokens player needs to pay to
 *               purchase this card.
 */
int total_tokens(Card *card, Player *player) {
    int total = 0;
    if (player->discount->tokensP < card->tokens->tokensP) {
        total += (card->tokens->tokensP - player->discount->tokensP);
    }
    if (player->discount->tokensB < card->tokens->tokensB) {
        total += (card->tokens->tokensB - player->discount->tokensB);
    }
    if (player->discount->tokensY < card->tokens->tokensY) {
        total += (card->tokens->tokensY - player->discount->tokensY);
    }
    if (player->discount->tokensR < card->tokens->tokensR) {
        total += (card->tokens->tokensR - player->discount->tokensR);
    }
    return total;
}

/**
 * Calculate the max tokens that player need to pay to by a card from face
 * up cards.
*  @param cards :    All the face up cards.
 * @param player    :   player who is selected to find the max tokens.
 * @return  :   The max tokens as integer.
 */
int max_tokens(Cards *cards, Player *player) {
    int maxTokens = 0;
    for (int i = 0; i < cards->faceUpNumber; i++) {
        int tokens;
        if ((tokens = total_tokens(cards->cards[i], player)) > maxTokens) {
            maxTokens = tokens;
        }
    }
    return maxTokens;
}

/**
 * Create all the players by the size of player.
 * Initial all players with no tokens no discount.
 * And named those players by give the player letter and player id.
 * @param playerSize    :   Read from argv, that total players.
 * @return  :   the total players who joined this game.
 */
Players *create_players(int playerSize) {
    Players *players = malloc(sizeof(Players));
    players->size = playerSize;
    players->player = malloc(sizeof(Player *) * players->size);
    for (int i = 0; i < players->size; i++) {
        players->player[i] = malloc(sizeof(Player));
        players->player[i]->tokens = malloc(sizeof(Tokens));
        players->player[i]->discount = malloc(sizeof(Tokens));
    }
    for (int i = 0; i < players->size; i++) {
        players->player[i]->tokens->tokensP = 0;
        players->player[i]->tokens->tokensB = 0;
        players->player[i]->tokens->tokensY = 0;
        players->player[i]->tokens->tokensR = 0;
        players->player[i]->tokens->tokensW = 0;
        players->player[i]->point = 0;
        players->player[i]->pLetter = (char) (65 + i);      // 65 stands for
        // letter 'A', which means the player letter starts with 'A', and
        // increased by i.
        players->player[i]->discount->tokensP = 0;
        players->player[i]->discount->tokensB = 0;
        players->player[i]->discount->tokensY = 0;
        players->player[i]->discount->tokensR = 0;
    }
    return players;
}

/**
 * Splite the took tokens meesage, which is sent from hub to player.
 * @param string    :   the string which stored those information
 * @return  :   Player who is a temp player, stores the player letter and
 *              tokens that read from message.
 */
Player *split_took_tokens(char *string) {
    Player *player = malloc(sizeof(Player));
    player->tokens = malloc(sizeof(Player));
    const char *colon = ":";
    char *p;
    p = strtok(string, colon);
    player->pLetter = p[4];         // word "took" is length 4, so the letter
    // is at position 4.
    char *tokens;
    while (p != NULL) {
        tokens = p;
        p = strtok(NULL, colon);
    }
    player->tokens = split_tokens(tokens);
    return player;
}

/**
 * Display the information about players and cards, which will print out to
 * stderr.
 * @param faceUpCards   :   the deck of cards
 * @param totalPlayer   :   all the players who joined this game
 */
void display_info(Cards *faceUpCards, Players *totalPlayer) {
    for (int i = 0; i < faceUpCards->faceUpNumber; i++) {
        fprintf(stderr, "Card %d:%c/%d/%d,%d,%d,%d\n", i,
                faceUpCards->cards[i]->discount,
                faceUpCards->cards[i]->point,
                faceUpCards->cards[i]->tokens->tokensP,
                faceUpCards->cards[i]->tokens->tokensB,
                faceUpCards->cards[i]->tokens->tokensY,
                faceUpCards->cards[i]->tokens->tokensR);
    }
    for (int i = 0; i < totalPlayer->size; i++) {
        fprintf(stderr, "Player %c:%d:Discounts=%d,%d,%d,%d:Tokens=%d,%d,%d,"
                "%d,%d\n", totalPlayer->player[i]->pLetter,
                totalPlayer->player[i]->point,
                totalPlayer->player[i]->discount->tokensP,
                totalPlayer->player[i]->discount->tokensB,
                totalPlayer->player[i]->discount->tokensY,
                totalPlayer->player[i]->discount->tokensR,
                totalPlayer->player[i]->tokens->tokensP,
                totalPlayer->player[i]->tokens->tokensB,
                totalPlayer->player[i]->tokens->tokensY,
                totalPlayer->player[i]->tokens->tokensR,
                totalPlayer->player[i]->tokens->tokensW);
    }
}

/**
 * Check whether there is a space in the message or not.
 * If there is a space in this message, then it is the invalid message, exit
 * process with 6.
 * @param message   :   the message that received by player
 */
void check_space(char *message) {
    for (int i = 0; i < strlen(message); i++) {
        if (message[i] == ' ') {
            fprintf(stderr, "Communication Error\n");
            exit(6);
        }
    }
}

/**
 * Find the player who took tokens and add those tokens to this player.
 * Find to player who has the same player letter with the temp player.
 * Add the tokens to this player.
 * @param player    :   The temp player who stored the player letter and
 *                      tokens.
 * @param totalPlayer   :   All of the players who joined this game.
 */
void took_tokens(Player *player, Players *totalPlayer) {
    for (int i = 0; i < totalPlayer->size; i++) {
        if (totalPlayer->player[i]->pLetter == player->pLetter) {
            totalPlayer->player[i]->tokens->tokensP +=
                    player->tokens->tokensP;
            totalPlayer->player[i]->tokens->tokensB +=
                    player->tokens->tokensB;
            totalPlayer->player[i]->tokens->tokensY +=
                    player->tokens->tokensY;
            totalPlayer->player[i]->tokens->tokensR +=
                    player->tokens->tokensR;
            return;
        }
    }
}

/**
 * Check whether the message is equal to specific string.
 * Identify which action is this message.
 * @param message   -   received from pipe
 * @param string    -   the string, which represent a specific action.
 * @return  1 - if they are equal to each other
 *          0 - if they are not.
 */
int is_equal(char *message, char *string) {
    for (int i = 0; i < strlen(string); i++) {
        if (message[i] != string[i]) {
            return 0;
        }
    }
    return 1;
}

/**
 * Check whether the message is "dowhat" or not
 * @param message   :   received from pipe
 *  @return  :    1 - if it is dowhat
 *                0 - if it is not dowhat
 */
int is_dowhat(char *message) {
    char *string = "dowhat";
    return is_equal(message, string);
}

/**
 * Check whether the message is "eog" or not
 * @param message   :   received from pipe
 *  @return  :    1 - if it is eog
 *                0 - if it is not eog
 */
int is_eog(char *message) {
    char *string = "eog";
    return is_equal(message, string);
}

/**
 * Check whether the message is "tokens" or not
 * @param message   :   received from pipe
 *  @return  :    1 - if it is tokens
 *                0 - if it is not tokens
 */
int is_tokens(char *message) {
    char *string = "tokens";
    return is_equal(message, string);
}

/**
 * Check whether the message is "newcard" or not
 * @param message   :   received from pipe
 *  @return  :    1 - if it is newcard
 *                0 - if it is not newcard
 */
int is_newcard(char *message) {
    char *string = "newcard";
    return is_equal(message, string);
}

/**
 * Check whether the message is "purchased" or not
 * @param message   :   received from pipe
 *  @return  :    1 - if it is purchased
 *                0 - if it is not purchased
 */
int is_purchased(char *message) {
    char *string = "purchased";
    return is_equal(message, string);
}

/**
 * Check whether the message is "took" or not
 * @param message   :   received from pipe
 *  @return  :    1 - if it is took
 *                0 - if it is not took
 */
int is_took(char *message) {
    char *string = "took";
    return is_equal(message, string);
}

/**
 * Check whether the message is "wild" or not
 * @param message   :   received from pipe
 *  @return  :    1 - if it is wild
 *                0 - if it is not wild
 */
int is_wild(char *message) {
    char *string = "wild";
    return is_equal(message, string);
}

/**
 * Create the face up cards for player.
 * @return  :   face up cards
 */
Cards *face_up_card() {
    Cards *deckCards = malloc(sizeof(Cards));
    deckCards->capacity = 0;
    deckCards->faceUpNumber = 0;
    deckCards->cards = malloc(sizeof(Card *) * deckCards->capacity);
    return deckCards;
}

/**
 * Add card to face up cards. Add to the end.
 * @param faceUpCards   :   The face up cards, which are create by player
 * @param card  :   card which needs to add to face up cards.
 */
void add_card(Cards *faceUpCards, Card *card) {
    faceUpCards->capacity++;
    if (faceUpCards->capacity <= 8) {
        faceUpCards->faceUpNumber++;
        card->faceUp = faceUpCards->faceUpNumber - 1;
        faceUpCards->cards = realloc(faceUpCards->cards,
                sizeof(Card *) * faceUpCards->capacity);
        faceUpCards->cards[faceUpCards->capacity - 1] = malloc(sizeof(Card));
        faceUpCards->cards[faceUpCards->capacity - 1]->tokens = malloc(
                sizeof(Tokens));
        faceUpCards->cards[faceUpCards->capacity - 1] = card;
    } else {
        faceUpCards->faceUpNumber++;
        card->faceUp = faceUpCards->faceUpNumber - 1;
        faceUpCards->cards[faceUpCards->faceUpNumber - 1] = card;
    }
}

/**
 * Remove specific card from the face up cards.
 * Move this card to the end of face up cards, then replace by new card.
 * @param faceUpCards   :   the face up cards that create by player.
 * @param cardID    :   the card id, which is the faceUp.
 */
void remove_card(Cards *faceUpCards, int cardID) {
    Card *card = malloc(sizeof(Card));
    int index = 0;
    for (int i = 0; i < faceUpCards->faceUpNumber; i++) {
        if (faceUpCards->cards[i]->faceUp == cardID) {
            card = faceUpCards->cards[i];
            break;
        }
        index++;
    }
    card->status = 0;
    Card *temp = card;
    for (int i = index; i < faceUpCards->faceUpNumber - 1; i++) {
        faceUpCards->cards[i] = faceUpCards->cards[i + 1];
    }
    faceUpCards->cards[faceUpCards->faceUpNumber - 1] = temp;
    for (int i = 0; i < faceUpCards->faceUpNumber; i++) {
        faceUpCards->cards[i]->faceUp = i;
    }
    faceUpCards->faceUpNumber--;
}

/**
 * Find the winner by check who has the heighest point or has the enough point.
 * @param players   :   The total players who joined this game.
 * @return  :   this player
 */
Player *find_winner(Players *players) {
    int maxPoint = 0;
    for (int i = 0; i < players->size; i++) {
        if (players->player[i]->point > maxPoint) {
            maxPoint = players->player[i]->point;
        }
    }
    for (int i = 0; i < players->size; i++) {
        if (players->player[i]->point == maxPoint) {
            return players->player[i];
        }
    }
    return NULL;
}

/**
 * Find whether there is other winners or not
 * @param players   :   total players of this game
 * @param winner    :    the first winner that we have found.
 */
void other_winners(Players *players, Player *winner) {
    for (int i = 0; i < players->size; i++) {
        if (players->player[i]->pLetter != winner->pLetter) {
            if (players->player[i]->point == winner->point) {
                fprintf(stderr, ",%c", players->player[i]->pLetter);
            }
        }
    }
    fprintf(stderr, "\n");
}

/**
 * Decode the purchased, and remove those tokens from player then add them to
 * hub. Add the discount to specific player.
 * @param hub
 * @param card
 * @param faceUpCards
 * @param player
 */
void decode_purchased(Hub *hub, Card *card, Cards *faceUpCards, Player
        *player) {
    for (int i = 0; i < faceUpCards->faceUpNumber; i++) {
        if (faceUpCards->cards[i]->faceUp == card->faceUp) {
            hub->tokens->tokensP += card->tokens->tokensP;
            hub->tokens->tokensB += card->tokens->tokensB;
            hub->tokens->tokensY += card->tokens->tokensY;
            hub->tokens->tokensR += card->tokens->tokensR;
            player->point += faceUpCards->cards[i]->point;
            player->tokens->tokensP -= card->tokens->tokensP;
            player->tokens->tokensB -= card->tokens->tokensB;
            player->tokens->tokensY -= card->tokens->tokensY;
            player->tokens->tokensR -= card->tokens->tokensR;
            player->tokens->tokensW -= card->tokens->tokensW;
            switch (faceUpCards->cards[i]->discount) {
                case 'P':
                    player->discount->tokensP += 1;
                    break;
                case 'B':
                    player->discount->tokensB += 1;
                    break;
                case 'Y':
                    player->discount->tokensY += 1;
                    break;
                case 'R':
                    player->discount->tokensR += 1;
                    break;
                default:
                    break;
            }
            remove_card(faceUpCards, card->faceUp);
            break;
        }
    }
}

/**
 * Report all errors and exit with specific state.
 * Parameters:
 *          argc    :   how many arguments that is used to call this function.
 *          argv    :   all the arguments
 *          name    :   the process name
 */
void report_err(int argc, char **argv, char *name) {
    if (argc != 3) {
        fprintf(stderr, "Usage: %s pcount myid\n", name);
        exit(1);
    }
    for (int i = 0; i < strlen(argv[1]); i++) {
        if (argv[1][i] > '9' || argv[1][i] < '0') {
            fprintf(stderr, "Invalid player count\n");
            exit(2);
        }
    }
    for (int i = 0; i < strlen(argv[2]); i++) {
        if (argv[1][i] > '9' || argv[2][i] < '0') {
            fprintf(stderr, "Invalid player ID\n");
            exit(3);
        }
    }
    int number = atoi(argv[1]);
    if (number <= 1 || number > 26) {
        fprintf(stderr, "Invalid player count\n");
        exit(2);
    }
    char *string;
    long id = strtol(argv[2], &string, 10);
    if (id > 25) {
        fprintf(stderr, "Invalid player ID\n");
        exit(3);
    }
    if (id < 0) {
        fprintf(stderr, "Invalid player ID\n");
        exit(3);
    }
    if (id > number - 1) {
        fprintf(stderr, "Invalid player ID\n");
        exit(3);
    }
}

/**
 * Respond to eog message, exit the player with state 0.
 * @param totalPlayer   :   all the players who joined this game.
 */
void action_eog(Players *totalPlayer) {
    fprintf(stderr, "Game over. Winners are %c",
            find_winner(totalPlayer)->pLetter);
    other_winners(totalPlayer, find_winner(totalPlayer));
    exit(0);
}

/**
 * Respond to tokens message, add those tokens to hub.
 * @param message   :   received from pipe
 * @param hub   :    contains all the tokens and point
 * @param totalPlayer   :   all the players who joined this game.
 * @param faceUpCards   :   all the available cards
 */
void action_tokens(char *message, Hub *hub, Players *totalPlayer, Cards
        *faceUpCards) {
    int length = strlen(message) - 6;
    char numbers[length];
    int index = 0;
    for (int i = 6; i < strlen(message); i++) {
        numbers[index] = message[i];
        index++;
    }
    if (atoi(numbers) <= 0) {
        fprintf(stderr, "Communication Error\n");
        exit(6);
    }
    hub->tokens->tokensP = atoi(numbers);
    hub->tokens->tokensB = atoi(numbers);
    hub->tokens->tokensY = atoi(numbers);
    hub->tokens->tokensR = atoi(numbers);
    hub->tokens->tokensW = 999;
    display_info(faceUpCards, totalPlayer);
}

/**
 * Respond to newcard message. Add the cards to face up cards.
 * @param message   :    received from pipe
 * @param faceUpCards   :   all the available cards
 * @param totalPlayer   :   all the players who joined this game.
 */
void action_newcard(char *message, Cards *faceUpCards, Players
        *totalPlayer) {
    Card *card = split_newcard(message);
    add_card(faceUpCards, card);
    display_info(faceUpCards, totalPlayer);
}

/**
 * Respond to purchased message. Remove the card from face up cards.
 * @param message   :    received from pipe
 * @param totalPlayer   :   all the players who joined this game.
 * @param hub   :    contains all the tokens and point
 * @param faceUpCards   :   all the available cards
 */
void action_purchased(char *message, Players *totalPlayer, Hub *hub, Cards
        *faceUpCards) {
    Card *card = split_purchased_card(message);
    for (int i = 0; i < totalPlayer->size; i++) {
        if (totalPlayer->player[i]->pLetter == card->discount) {
            decode_purchased(hub, card, faceUpCards,
                    totalPlayer->player[i]);
            break;
        }
    }
    display_info(faceUpCards, totalPlayer);
}

/**
 * Respond to took message. Take tokens from hub, add it to player
 * @param message   :    received from pipe
 * @param totalPlayer   :   all the players who joined this game.
 * @param hub   :    contains all the tokens and point
 * @param faceUpCards   :   all the available cards
 */
void action_took(char *message, Players *totalPlayer, Hub *hub,
        Cards *faceUpCards) {
    Player *player = split_took_tokens(message);
    took_tokens(player, totalPlayer);
    hub->tokens->tokensP -= player->tokens->tokensP;
    hub->tokens->tokensB -= player->tokens->tokensB;
    hub->tokens->tokensY -= player->tokens->tokensY;
    hub->tokens->tokensR -= player->tokens->tokensR;
    display_info(faceUpCards, totalPlayer);
}

/**
 * Respond to wild message, take a wild from hub to player.
 * @param message   :   received from pipe
 * @param totalPlayer   :   all the players who joined this game.
 * @param flag  :   whether player need to show game information or not
 * @param faceUpCards   :   all the available cards
 */
void action_wild(char *message, Players *totalPlayer, Cards
        *faceUpCards) {
    for (int i = 0; i < totalPlayer->size; i++) {
        if (totalPlayer->player[i]->pLetter == message[4]) {
            totalPlayer->player[i]->tokens->tokensW++;
        }
    }
    display_info(faceUpCards, totalPlayer);
}


