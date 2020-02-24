#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "deck.h"
#include "player.h"

/**
 * Read the data from a given DeckFile and return the decks.
 * @param file  :   the DeckFile that we need to read data from
 * @return  :   A cards struct that contains all the card inside of this file
 */
Cards *read_deck(FILE *file) {
    Cards *deckCards = malloc(sizeof(Cards));
    deckCards->capacity = 0;
    deckCards->cards = malloc(sizeof(Card *) * deckCards->capacity);
    for (int i = 0; i < deckCards->capacity; i++) {
        deckCards->cards[i] = malloc(sizeof(Card));
        deckCards->cards[i]->tokens = malloc(sizeof(Tokens));
    }
    char line[100];
    int index = 0;
    while (fgets(line, sizeof(line), file) != NULL) {
        if (strlen(line) <= 11) {
            fprintf(stderr, "Invalid deck file contents\n");
            exit(4);
        }
        deckCards->capacity++;
        deckCards->cards = realloc(deckCards->cards, sizeof(Card *) *
                deckCards->capacity);
        deckCards->cards[deckCards->capacity - 1] = malloc(sizeof(Card));
        deckCards->cards[deckCards->capacity - 1]->tokens = malloc(
                sizeof(Tokens));
        deckCards->cards[index] = read_new_card(line);
        deckCards->cards[index]->status = 1;
        index++;
    }
    if (deckCards->capacity == 0) {
        fprintf(stderr, "Invalid deck file contents\n");
        exit(4);
    }
    fclose(file);
    for (int i = 0; i < deckCards->capacity; i++) {
        if (deckCards->cards[i]->discount != 'P' &&
                deckCards->cards[i]->discount != 'B' &&
                deckCards->cards[i]->discount != 'Y' &&
                deckCards->cards[i]->discount != 'R') {
            fprintf(stderr, "Invalid deck file contents\n");
            exit(4);
        }
    }
    return deckCards;
}

/**
 * Check whther the card's discount is valid or not.
 * If the discount is invalid, exit with communication Error.
 * This is used by player
 * @param colour    :   the colour that read from the file
 */
void check_discount(char colour) {
    if (colour != 'P' && colour != 'B' && colour != 'Y' && colour != 'R') {
        fprintf(stderr, "Communication Error\n");
        exit(6);
    }
}

/**
 * Split the new card message, read from a string and return a card
*  @param string    :   the string that hub send to player
 * @return  card that store information from this string
 */
Card *split_newcard(char *string) {
    Card *card = malloc(sizeof(Card));
    card->tokens = malloc(sizeof(Tokens));
    char *p;
    const char *colon = ":";
    p = strtok(string, colon);
    card->discount = p[7];      // "newcard" is length 7
    check_discount(card->discount);
    char *tokens;
    while (p != NULL) {
        if (strlen(p) <= 2) {
            card->point = atoi(p);
        } else {
            tokens = p;
        }
        p = strtok(NULL, colon);
    }
    card->tokens = split_tokens(tokens);
    return card;
}

/**
 * Split the string (without wild) which contains tokens information
 * @param string    :   tokens string format that (int,int,int,int)
 * @return  Tokens which store all value from string
 */
Tokens *split_tokens(char *string) {
    Tokens *tokens = malloc(sizeof(Tokens));
    const char *comma = ",";
    char *p = strtok(string, comma);
    tokens->tokensP = atoi(p);
    int i = 0;
    for (i = 0; p != NULL; i++) {
        if (i == 1) {
            tokens->tokensB = atoi(p);
        } else if (i == 2) {
            tokens->tokensY = atoi(p);
        } else if (i == 3) {
            tokens->tokensR = atoi(p);
        }
        p = strtok(NULL, comma);
    }
    if (i != 4) {
        fprintf(stderr, "Communication Error\n");
        exit(6);
    }
    return tokens;
}

/**
 * Split the string from the deck file, which may contain some invalid string
 * Check whether the stirng is valid or not
 * If it is, return Tokens, if not, exit with Invaild deck file.
 * @param string    :   read from deck file and comtains value of tokens.
 * @return  :   the tokens that read from string
 */
Tokens *split_tokens_start(char *string) {
    Tokens *tokens = malloc(sizeof(Tokens));
    const char *comma = ",";
    char *p = strtok(string, comma);
    for (int j = 0; j <= strlen(p) - 1; j++) {
        if (p[j] < '0' || p[j] > '9') {
            fprintf(stderr, "Invalid deck file contents\n");
            exit(4);
        }
    }
    tokens->tokensP = atoi(p);
    for (int i = 0; p != NULL; i++) {
        if (i == 1) {
            for (int j = 0; j <= strlen(p) - 1; j++) {
                if (p[j] < '0' || p[j] > '9') {
                    fprintf(stderr, "Invalid deck file contents\n");
                    exit(4);
                }
            }
            tokens->tokensB = atoi(p);
        } else if (i == 2) {
            for (int j = 0; j <= strlen(p) - 1; j++) {
                if (p[j] < '0' || p[j] > '9') {
                    fprintf(stderr, "Invalid deck file contents\n");
                    exit(4);
                }
            }
            tokens->tokensY = atoi(p);
        } else if (i == 3) {
            for (int j = 0; j <= strlen(p) - 2; j++) {
                if (p[j] < '0' || p[j] > '9') {
                    fprintf(stderr, "Invalid deck file contents\n");
                    exit(4);
                }
            }
            tokens->tokensR = atoi(p);
        }
        p = strtok(NULL, comma);
    }
    return tokens;
}

/**
 * Split the string (with wild) which contains tokens information
 * @param string    :   tokens string format that (int,int,int,int)
 * @return  Tokens which store all value from string
 */
Tokens *split_tokens_wild(char *string) {
    Tokens *tokens = malloc(sizeof(Tokens));
    const char *comma = ",";
    char *p = strtok(string, comma);
    tokens->tokensP = atoi(p);
    int i = 0;
    for (i = 0; p != NULL; i++) {
        if (i == 1) {
            tokens->tokensB = atoi(p);
        } else if (i == 2) {
            tokens->tokensY = atoi(p);
        } else if (i == 3) {
            tokens->tokensR = atoi(p);
        } else if (i == 4) {
            tokens->tokensW = atoi(p);
        }
        p = strtok(NULL, comma);
    }
    if (i != 5) {
        fprintf(stderr, "Communication Error\n");
        exit(6);
    }
    return tokens;
}

/**
 * Split string that contains purchase card information. send from player to
 * hub.
 * @param string    :   purchase card string which format as "purchaseC:P,B,
 *                      Y,R,W"
 * @return  :   card which store the value from string
 */
Card *split_purchase_card(char *string) {
    Card *card = malloc(sizeof(Card));
    card->tokens = malloc(sizeof(Tokens));
    const char *colon = ":";
    char *p;
    p = strtok(string, colon);
    char *p1 = &p[8];               // "purchase" is length 8
    card->faceUp = atoi(p1);
    char *tokens;
    while (p != NULL) {
        if (strlen(p) <= 2) {
            card->point = atoi(p);
        } else {
            tokens = p;
        }
        p = strtok(NULL, colon);
    }
    card->tokens = split_tokens_wild(tokens);
    return card;
}

/**
 * Split string that contains purchase card information. send from hub to
 * player.
 * @param string    :   purchase card string which format as "purchasedP:C:P,B,
 *                      Y,R,W"
 * @return  :   card which store the value from string
 */
Card *split_purchased_card(char *string) {
    Card *card = malloc(sizeof(Card));
    card->tokens = malloc(sizeof(Tokens));
    const char *colon = ":";
    char *p;
    p = strtok(string, colon);
    char p1 = p[9];
    card->discount = p1;            //use card discount to store player ID
    char *string1;
    for (int i = 0; p != NULL; i++) {
        if (i == 1) {
            card->faceUp = atoi(p);
        } else if (i == 2) {
            string1 = p;
        }
        p = strtok(NULL, colon);
    }
    const char *comma = ",";
    Tokens *tokens = malloc(sizeof(Tokens));
    p = strtok(string1, comma);
    tokens->tokensP = atoi(p);
    for (int j = 0; p != NULL; j++) {
        if (j == 1) {
            tokens->tokensB = atoi(p);
        } else if (j == 2) {
            tokens->tokensY = atoi(p);
        } else if (j == 3) {
            tokens->tokensR = atoi(p);
        } else {
            tokens->tokensW = atoi(p);
        }
        p = strtok(NULL, comma);
    }
    card->tokens->tokensP = tokens->tokensP;
    card->tokens->tokensB = tokens->tokensB;
    card->tokens->tokensY = tokens->tokensY;
    card->tokens->tokensR = tokens->tokensR;
    card->tokens->tokensW = tokens->tokensW;
    return card;

}

/**
 * Split string that store new card information. send from hub to player
 * @param string    :   new card information, format as "newcardD:V:P,B,Y,R"
 * @return  :   card which store the value from string
 */
Card *read_new_card(char *string) {
    Card *card = malloc(sizeof(Card));
    card->tokens = malloc(sizeof(Tokens));
    const char *colon = ":";
    char *p;
    p = strtok(string, colon);
    card->discount = *p;
    char *tokens;
    for (int i = 0; p != NULL; i++) {
        if (i == 1) {
            card->point = atoi(p);
        } else if (i == 2) {
            tokens = p;
        }
        p = strtok(NULL, colon);
    }
    card->tokens = split_tokens_start(tokens);
    return card;
}

/**
 * Updata cards, show the face up cards.
 * Move the purcahsed card to the end of the face up cards.
 * The replace it by the new card.
 * @param faceUpId  :   which is faceUp. it is the number of this card among
 *                      all the face up cards.
 * @param faceUpCards   :   It is decks, which read from deck file. and
 *                          store all cards.
 */
void update_cards(int faceUpId, Cards *faceUpCards) {
    Card *card = malloc(sizeof(Card));
    for (int i = 0; i < faceUpCards->faceUpNumber; i++) {
        if (faceUpCards->cards[i]->faceUp == faceUpId) {
            card = faceUpCards->cards[i];
        }
    }
    card->faceUp = 9;
    card->status = 0;
    Card *temp = malloc(sizeof(Card));
    temp->tokens = malloc(sizeof(Tokens));
    for (int i = 0; i < faceUpCards->capacity; i++) {
        if (faceUpCards->cards[i] == card) {
            temp = card;
            for (int index = i; index < faceUpCards->capacity - 1; index++) {
                faceUpCards->cards[index] = faceUpCards->cards[index + 1];
            }
            faceUpCards->cards[faceUpCards->capacity - 1] = temp;
            break;
        }
    }
    if (faceUpCards->cards[0]->status == 0) {
        faceUpCards->faceUpNumber = 0;
        return;
    }
    for (int i = faceUpCards->faceUpNumber - 1; i >= 0; i--) {
        if (faceUpCards->cards[i]->status != 0) {
            faceUpCards->cards[i]->status = 1;
            faceUpCards->faceUpNumber = i + 1;
            break;
        }
    }
    for (int i = 0; i < faceUpCards->faceUpNumber; i++) {
        faceUpCards->cards[i]->faceUp = i;
    }

    if (faceUpCards->faceUpNumber == 8) {
        printf("New card = Bonus %c, worth %d, costs %d,%d,%d,%d\n",
                faceUpCards->cards[faceUpCards->faceUpNumber -
                1]->discount,
                faceUpCards->cards[faceUpCards->faceUpNumber - 1]->point,
                faceUpCards->cards[faceUpCards->faceUpNumber
                - 1]->tokens->tokensP,
                faceUpCards->cards[faceUpCards->faceUpNumber
                - 1]->tokens->tokensB,
                faceUpCards->cards[faceUpCards->faceUpNumber
                - 1]->tokens->tokensY,
                faceUpCards->cards[faceUpCards->faceUpNumber
                - 1]->tokens->tokensR);
    }
}

/**
 * Send new card message from hub to all the player.
 * Find how many message need to be sent for a single player
 * @param faceUpCards   :   the whole decks
 * @param count :   the number that how many people bought cards before this
 *                  player in the turn.
 */
void report_update(Cards *faceUpCards, int count) {
    if (faceUpCards->faceUpNumber == 8) {
        int index = faceUpCards->faceUpNumber - count;
        for (int i = index; i < faceUpCards->faceUpNumber; i++) {
            fprintf(stderr, "newcard%c:%d:%d,%d,%d,%d\n",
                    faceUpCards->cards[i]->discount,
                    faceUpCards->cards[i]->point,
                    faceUpCards->cards[i]->tokens->tokensP,
                    faceUpCards->cards[i]->tokens->tokensB,
                    faceUpCards->cards[i]->tokens->tokensY,
                    faceUpCards->cards[i]->tokens->tokensR);
        }
    }

}
