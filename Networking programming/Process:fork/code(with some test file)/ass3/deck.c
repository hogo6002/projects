#include <stdio.h>
#include <stdlib.h>
#include <memory.h>
#include "deck.h"
#include "player.h"

Cards* read_deck(FILE* file) {
    Cards* deckCards = malloc(sizeof(Cards));
    deckCards->capacity = 0;
    deckCards->cards = malloc(sizeof(Card*) * deckCards->capacity);
    for (int i = 0; i < deckCards->capacity; i++) {
        deckCards->cards[i] = malloc(sizeof(Card));
        deckCards->cards[i]->tokens = malloc(sizeof(Tokens));
    }
    char line[100];
    int index = 0;
    while(fgets(line, sizeof(line), file)!=NULL) {
        if (strlen(line) <= 11) {
            fprintf(stderr, "Invalid deck file contents\n");
            exit(4);
        }
        deckCards->capacity++;
        deckCards->cards = realloc(deckCards->cards, sizeof(Card*) *
        deckCards->capacity);
        deckCards->cards[deckCards->capacity - 1] = malloc(sizeof(Card));
        deckCards->cards[deckCards->capacity - 1]->tokens = malloc(sizeof(Tokens));
        deckCards->cards[index] = read_new_card(line);
        deckCards->cards[index]->status = 1;
        index++;
    }
    if (deckCards->capacity == 0) {
        fprintf(stderr, "Invalid deck file contents\n");
        exit(4);
    }
    fclose(file);
    for (int i = 0 ; i < deckCards->capacity; i++) {
        if (deckCards->cards[i]->discount != 'P' &&
        deckCards->cards[i]->discount != 'B' &&
        deckCards->cards[i]->discount != 'Y' &&
        deckCards->cards[i]->discount != 'R') {
            fprintf(stderr, "Invalid deck file contents\n");
            exit(4);
        }
    }
//    int next = 0;
//    next = fgetc(file);
//    int index = 0;
//    int position = 0;
//    while (next != EOF) {
//        if (next == '\n') {
//            deckCards->cards[index]->status = 1;
//            deckCards->cards[index]->tokens->tokensW = 999;
//            index++;
//            position = 0;
//            deckCards->capacity++;
//            deckCards->cards = realloc(deckCards->cards, sizeof(Card*) *
//                                                         deckCards->capacity);
//            deckCards->cards[deckCards->capacity - 1] = malloc(sizeof(Card));
//            deckCards->cards[deckCards->capacity - 1]->tokens = malloc(sizeof
//                                                                       (Tokens));
//        } else {
//            if (position == 0) {
//                deckCards->cards[index]->discount = (char) next;
//            } else if (position == 2) {
//                deckCards->cards[index]->point = next - 48;
//            } else if (position == 4) {
//                deckCards->cards[index]->tokens->tokensP = next - 48;
//            } else if (position == 6) {
//                deckCards->cards[index]->tokens->tokensB = next - 48;
//            } else if (position == 8) {
//                deckCards->cards[index]->tokens->tokensY = next - 48;
//            } else if (position == 10) {
//                deckCards->cards[index]->tokens->tokensR = next - 48;
//            }
//            position++;
//        }
//        next = fgetc(file);
//    }
//    if (deckCards->capacity > 8) {
//        deckCards->capacity--;
//    }
//    free(deckCards->cards);
    return deckCards;
}

void check_discount(char colour) {
    if (colour != 'P' && colour != 'B' && colour != 'Y' && colour != 'R') {
        fprintf(stderr, "Communication Error\n");
        exit(6);
    }
}

Card* split_newCard(char *string) {
    Card* card = malloc(sizeof(Card));
    card->tokens = malloc(sizeof(Tokens));
    char* p;
    const char * colon = ":";
    p = strtok(string, colon);
    card->discount = p[7];      // "newcard" is length 7
    check_discount(card->discount);
    char* tokens;
    while(p!=NULL) {
        if (strlen(p) <= 2)
            card->point = atoi(p);
        else
            tokens = p;
        p = strtok(NULL, colon);
    }
//    const char *comma = ",";
//    p = strtok(tokens, comma);
//    card->tokens->tokensP = atoi(p);
//    for(int i = 0; p!=NULL; i++) {
//        if (i == 1) {
//            card->tokens->tokensB = atoi(p);
//        } else if (i == 2) {
//            card->tokens->tokensY = atoi(p);
//        } else {
//            card->tokens->tokensR = atoi(p);
//        }
//        p = strtok(NULL, comma);
//    }
    card->tokens = split_tokens(tokens);
    return card;
}

Tokens* split_tokens(char *string) {
    Tokens* tokens = malloc(sizeof(Tokens));
    const char *comma = ",";
    char* p = strtok(string, comma);
    tokens->tokensP = atoi(p);
    int i = 0;
    for(i = 0; p!=NULL; i++) {
        if (i == 1) {
            tokens->tokensB = atoi(p);
        } else if (i == 2) {
            tokens->tokensY = atoi(p);
        } else if (i == 3){
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

Tokens* split_tokens_start(char *string) {
    Tokens* tokens = malloc(sizeof(Tokens));
    const char *comma = ",";
    char* p = strtok(string, comma);
    for (int j = 0; j <= strlen(p) - 1; j++) {
        if (p[j] < '0' || p[j] > '9') {
            fprintf(stderr, "Invalid deck file contents\n");
            exit(4);
        }
    }
    tokens->tokensP = atoi(p);
    for(int i = 0; p!=NULL; i++) {
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
        } else if (i == 3){
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

Tokens* split_tokens_wild(char *string) {
    Tokens* tokens = malloc(sizeof(Tokens));
    const char *comma = ",";
    char* p = strtok(string, comma);
    tokens->tokensP = atoi(p);
    int i = 0;
    for(i = 0; p!=NULL; i++) {
        if (i == 1) {
            tokens->tokensB = atoi(p);
        } else if (i == 2) {
            tokens->tokensY = atoi(p);
        } else if (i == 3){
            tokens->tokensR = atoi(p);
        } else if (i == 4){
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

Card* split_purchase_card(char* string) {
    Card* card = malloc(sizeof(Card));
    card->tokens = malloc(sizeof(Tokens));
    const char * colon = ":";
    char* p;
    p = strtok(string, colon);
    char* p1 = &p[8];
    card->faceUp = atoi(p1);        // "purchase" is length 8; 48 stands
    // for 0 in ascii
    char* tokens;
    while(p!=NULL) {
        if (strlen(p) <= 2)
            card->point = atoi(p);
        else
            tokens = p;
        p = strtok(NULL, colon);
    }
    card->tokens = split_tokens_wild(tokens);
    return card;
}

Card* split_purchased_card(char* string) {
    Card* card = malloc(sizeof(Card));
    card->tokens = malloc(sizeof(Tokens));
    const char * colon = ":";
    char* p;
    p = strtok(string, colon);
    char p1 = p[9];
    card->discount = p1;            //use card discount to store player ID
    char* string1;
    for(int i = 0; p!=NULL; i++) {
        if (i == 1) {
            card->faceUp = atoi(p);
        } else if (i == 2) {
            string1 = p;
        }
        p = strtok(NULL, colon);
    }
    const char *comma = ",";
    Tokens* tokens = malloc(sizeof(Tokens));
    p = strtok(string1, comma);
    tokens->tokensP = atoi(p);
    for(int j = 0; p!=NULL; j++) {
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


Card* read_new_card(char* string) {
    Card* card = malloc(sizeof(Card));
    card->tokens = malloc(sizeof(Tokens));
    const char * colon = ":";
    char* p;
    p = strtok(string, colon);
    card->discount = *p;
//    printf("discount %s\n", p);
    char* tokens;
    for (int i = 0; p != NULL; i++) {
        if (i == 1) {
            card->point = atoi(p);
        } else if (i == 2){
            tokens = p;
        }
        p = strtok(NULL, colon);
    }
    card->tokens = split_tokens_start(tokens);
    return card;
}

void update_cards(int faceUpId, Cards* faceUpCards) {
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
               faceUpCards->cards[faceUpCards->faceUpNumber -1]->discount,
               faceUpCards->cards[faceUpCards->faceUpNumber -1]->point,
               faceUpCards->cards[faceUpCards->faceUpNumber
                                  -1]->tokens->tokensP,
               faceUpCards->cards[faceUpCards->faceUpNumber
                                  -1]->tokens->tokensB,
               faceUpCards->cards[faceUpCards->faceUpNumber
                                  -1]->tokens->tokensY,
               faceUpCards->cards[faceUpCards->faceUpNumber
                                  -1]->tokens->tokensR);
    }
//    faceUpCards->faceUpNumber--;
}
void report_update(Cards* faceUpCards, int count) {
    if (faceUpCards->faceUpNumber == 8) {
        int index = faceUpCards->faceUpNumber - count;
        for (int i = index; i < faceUpCards->faceUpNumber; i++){
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
