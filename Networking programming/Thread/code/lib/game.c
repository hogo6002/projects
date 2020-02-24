#include <stdlib.h>
#include <string.h>
#include <limits.h>
#include "game.h"
#include "util.h"
#include "token.h"

void initialize_player(struct Player* output, int id) {
    memset(output, 0, sizeof(struct Player));
    output->playerId = id;
}

int process_take_tokens(int* tokenPool, struct Player* player,
        struct TakeMessage tokens) {
    // check the numbers add up
    for (int i = 0; i < TOKEN_MAX - 1; ++i) {
        if (tokens.tokens[i]) {
            if (tokenPool[i] <= 0) {
                return -1;
            }
        }
    }

    // process the purchase
    for (int i = 0; i < TOKEN_MAX - 1; ++i) {
        if (tokens.tokens[i]) {
            player->tokens[i] += 1;
            tokenPool[i] -= 1;
        }
    }

    return 0;
}

int buy_card(int* tokenPool, struct Player* player, struct Card card) {
    struct Player copy = *player;
    int poolCopy[TOKEN_MAX];
    memcpy(poolCopy, tokenPool, sizeof(int) * TOKEN_MAX);

    int wildNeeded = 0;

    for (int i = 0; i < TOKEN_MAX - 1; ++i) {
        if (copy.tokens[i] >= card.cost[i] - copy.discounts[i]) {
            if (card.cost[i] >= copy.discounts[i]) {
                poolCopy[i] += card.cost[i] - copy.discounts[i];
                copy.tokens[i] -= card.cost[i] - copy.discounts[i];
            }
        } else {
            wildNeeded += card.cost[i] - copy.discounts[i] - copy.tokens[i];
            poolCopy[i] += copy.tokens[i];
            copy.tokens[i] = 0;
        }
    }
    if (copy.tokens[TOKEN_WILD] >= wildNeeded) {
        copy.tokens[TOKEN_WILD] -= wildNeeded;
        poolCopy[TOKEN_WILD] += wildNeeded;
        copy.discounts[card.discount] += 1;
        copy.score += card.points;
        *player = copy;
        memcpy(tokenPool, poolCopy, sizeof(int) * TOKEN_MAX);
        return 0;
    } else {
        return -1;
    }
}

int validate_costs(struct Player player, struct Card card, int* tokensUsed) {
    int tokens[TOKEN_MAX] = {0};
    if (buy_card(tokens, &player, card) != 0) {
        return -1;
    }

    if (memcmp(tokens, tokensUsed, sizeof(int) * TOKEN_MAX) != 0) {
        return -1;
    }

    return 0;
}

int get_purchaseable(const struct Card* board, int boardSize,
        struct Card* output, struct Player player) {
    int canPurchase = 0;

    for (int i = 0; i < boardSize; ++i) {
        int tokens[TOKEN_MAX] = { 0 };
        struct Player copy = player;
        if (buy_card(tokens, &copy, board[i]) == 0) {
            output[canPurchase++] = board[i];
        }
    }

    return canPurchase;
}

int find_best_purchases(struct Card* cards, int cardCount,
        int (*objective)(struct Card, const void* arg), const void* arg) {
    int output = 0;
    int best = INT_MIN;

    for (int i = 0; i < cardCount; ++i) {
        int value = objective(cards[i], arg);
        if (value > best) {
            output = 0;
        }

        if (value >= best) {
            cards[output++] = cards[i];
            best = value;
        }
    }

    return output;
}
