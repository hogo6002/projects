#include <stdio.h>
#include <stdlib.h>
#include <zconf.h>
#include <memory.h>
#include "player.h"


Player *current_player(int round, Players *players) {
    Player *currentPlayer = players->player[round % players->size];
    return currentPlayer;
}

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
        return 1;
    }
    return 0;
}

int can_purchase(Player *player, Cards *faceUpCards) {
    for (int i = 0; i < faceUpCards->faceUpNumber; i++) {
        if (can_afford(player, faceUpCards->cards[i])) {
            return 1;
        }
    }
    return 0;
}


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
    player->tokens->tokensP += tokenP;
    player->tokens->tokensB += tokenB;
    player->tokens->tokensY += tokenY;
    player->tokens->tokensR += tokenR;
    player->tokens->tokensW += tokenW;
    printf("purchase%d:%d,%d,%d,%d,%d\n", card->faceUp, tokenP,
           tokenB, tokenY, tokenR, tokenW);
    fflush(stdout);

//    printf("card %c:%d:%d,%d,%d,%d\n", card->discount,card
//    ->faceUp,card->tokens->tokensP,
//           card->tokens->tokensB, card->tokens->tokensY,
//           card->tokens->tokensR);
    return card;
}

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

int total_tokens(Card *card, Player* player) {
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

int max_tokens(Cards *cards, Player* player) {
    int maxTokens = 0;
    for (int i = 0; i < cards->faceUpNumber; i++) {
        int tokens;
        if ((tokens = total_tokens(cards->cards[i], player))> maxTokens) {
            maxTokens = tokens;
        }
    }
    return maxTokens;
}

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
        players->player[i]->playerId = i;
        players->player[i]->point = 0;
        players->player[i]->pLetter = (char) (65 + i);
        players->player[i]->playerType = 'A';
        players->player[i]->discount->tokensP = 0;
        players->player[i]->discount->tokensB = 0;
        players->player[i]->discount->tokensY = 0;
        players->player[i]->discount->tokensR = 0;
//        players->player[i]->setupComms = player_setup_comms;

    }
    return players;
}
//
//bool player_setup_comms(Player *this, int in, int out) {
//    this->in = fdopen(in, "w");
//    this->out = fdopen(out, "r");
//
//    if (!this->in || !this->out) {
//        return false;
//    }
//
//    return true;
//}

Player *create_player() {
    Player *player = malloc(sizeof(Player));
    player->tokens = malloc(sizeof(Tokens));
    player->discount = malloc(sizeof(Tokens));
    player->tokens->tokensP = 0;
    player->tokens->tokensB = 0;
    player->tokens->tokensY = 0;
    player->tokens->tokensR = 0;
    player->tokens->tokensW = 0;
    player->discount->tokensP = 0;
    player->discount->tokensB = 0;
    player->discount->tokensY = 0;
    player->discount->tokensR = 0;
    player->playerId = 0;
    player->pLetter = (char) 65;
    return player;
}

Player *split_took_tokens(char *string) {
    Player *player = malloc(sizeof(Player));
    player->tokens = malloc(sizeof(Player));
    const char *colon = ":";
    char *p;
    p = strtok(string, colon);
    player->pLetter = p[4];
    // for 0 in ascii
    char *tokens;
    while (p != NULL) {
        tokens = p;
        p = strtok(NULL, colon);
    }
    player->tokens = split_tokens(tokens);
    return player;
}

Player *current(Players *totalPlayer, Player *lastPlayer) {
    int size = totalPlayer->size;
    int last = lastPlayer->playerId;
    if (last < size - 1) {
        last++;
    } else {
        last = 0;
    }
    return totalPlayer->player[last];
}

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


void check_space(char *message) {
    for (int i = 0; i < strlen(message); i++) {
        if (message[i] == ' ') {
            fprintf(stderr, "Communication Error\n");
            exit(6);
        }
    }
}

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

void took_wild(Player *player, Players *totalPlayer) {
    for (int i = 0; i < totalPlayer->size; i++) {
        if (totalPlayer->player[i]->pLetter == player->pLetter) {
            totalPlayer->player[i]->tokens->tokensW += 1;
        }
    }
}

int is_equal(char *message, char *string) {
    for (int i = 0; i < strlen(string); i++) {
        if (message[i] != string[i]) {
            return 0;
        }
    }
    return 1;
}

int is_dowhat(char *message) {
    char *string = "dowhat";
    return is_equal(message, string);
}

int is_eog(char *message) {
    char *string = "eog";
    return is_equal(message, string);
}

int is_tokens(char *message) {
    char *string = "tokens";
    return is_equal(message, string);
}

int is_newcard(char *message) {
    char *string = "newcard";
    return is_equal(message, string);
}

int is_purchased(char *message) {
    char *string = "purchased";
    return is_equal(message, string);
}

int is_took(char *message) {
    char *string = "took";
    return is_equal(message, string);
}

int is_wild(char *message) {
    char *string = "wild";
    return is_equal(message, string);
}

Cards *face_up_card() {
    Cards *deckCards = malloc(sizeof(Cards));
    deckCards->capacity = 0;
    deckCards->faceUpNumber = 0;
    deckCards->cards = malloc(sizeof(Card *) * deckCards->capacity);
    return deckCards;
}

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


Player* find_winner(Players* players) {
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

void decode_purchased (Hub *hub, Card* card, Cards* faceUpCards, Player*
player) {
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



