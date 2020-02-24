#include "token.h"

char print_token(enum Token token) {
    switch (token) {
        case TOKEN_PURPLE:
            return 'P';
        case TOKEN_BROWN:
            return 'B';
        case TOKEN_YELLOW:
            return 'Y';
        case TOKEN_RED:
            return 'R';
        case TOKEN_WILD:
            return 'W';
        default:
            return '?';
    }
}

int distinct_tokens_available(const int* tokens, int tokenCount) {
    int output = 0;
    for (int i = 0; i < tokenCount; ++i) {
        if (tokens[i]) {
            ++output;
        }
    }

    return output;
}

void take_if_possible(int* takePool, const int* tokenPool, enum Token choice) {
    if (tokenPool[choice] && takePool[choice] == 0 &&
            distinct_tokens_available(takePool, TOKEN_MAX - 1)
            < TAKE_NUMBER) {
        ++takePool[choice];
    }
}
