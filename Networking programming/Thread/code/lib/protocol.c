#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "protocol.h"
#include "util.h"
#include "game.h"

int parse_card(struct Card* output, const char* input) {
    switch (*input++) {
        case 'P':
            output->discount = TOKEN_PURPLE;
            break;
        case 'B':
            output->discount = TOKEN_BROWN;
            break;
        case 'Y':
            output->discount = TOKEN_YELLOW;
            break;
        case 'R':
            output->discount = TOKEN_RED;
            break;
        default:
            return -1;
    }

    if (*input++ != ':') {
        return -1;
    }

    char* next;
    output->points = parse_int(input, &next);
    if (next == input || *next != ':') {
        return -1;
    } else {
        input = ++next;
    }

    for (int i = 0; i < TOKEN_MAX - 1; ++i) {
        output->cost[i] = parse_int(input, &next);
        // The delimiter we expect changes if this is the last element of the
        // list
        if (next == input ||
                (*next != ((i + 1 == TOKEN_MAX - 1) ? '\0' : ','))) {
            return -1;
        } else {
            input = ++next;
        }
    }

    return 0;
}

enum MessageFromHub classify_from_hub(const char* message) {
    if (strcmp(message, "eog") == 0) {
        return END_OF_GAME;
    } else if (strcmp(message, "dowhat") == 0) {
        return DO_WHAT;
    } else if (strstr(message, "purchased") == message) {
        return PURCHASED;
    } else if (strstr(message, "took") == message) {
        return TOOK;
    } else if (strstr(message, "wild") == message) {
        return TOOK_WILD;
    } else if (strstr(message, "newcard") == message) {
        return NEW_CARD;
    } else if (strstr(message, "tokens") == message) {
        return TOKENS;
    } else if (strstr(message, "disco") == message) {
        return DISCO;
    } else if (strstr(message, "invalid") == message) {
        return INVALID;
    } else {
        return -1;
    }
}

int parse_purchased_message(struct PurchaseMessage* output, int* playerId,
        const char* message) {
    if (strstr(message, "purchased") != message) {
        return -1;
    }
    message += strlen("purchased");

    if (*message < 'A' || *message > 'Z') {
        return -1;
    }
    *playerId = *message++ - 'A';

    if (*message++ != ':') {
        return -1;
    }

    char* next;
    output->cardNumber = parse_int(message, &next);
    if (next == message || *next != ':') {
        return -1;
    }
    message = ++next;

    for (int i = 0; i < TOKEN_MAX; ++i) {
        output->costSpent[i] = parse_int(message, &next);
        // The delimeter we expect changes if this is the last element of the
        // list
        if (next == message ||
                (*next != ((i + 1 == TOKEN_MAX) ? '\0' : ','))) {
            return -1;
        } else {
            message = ++next;
        }
    }

    // check validity of numbers in messages
    for (int i = 0; i < TOKEN_MAX; ++i) {
        if (output->costSpent[i] < 0) {
            return -1;
        }
    }

    return 0;
}

char* print_purchased_message(struct PurchaseMessage input, int playerId) {
    int length = snprintf(NULL, 0, "purchased%c:%d:%d,%d,%d,%d,%d\n",
            'A' + playerId, input.cardNumber, input.costSpent[TOKEN_PURPLE],
            input.costSpent[TOKEN_BROWN], input.costSpent[TOKEN_YELLOW],
            input.costSpent[TOKEN_RED], input.costSpent[TOKEN_WILD]);
    char* output = malloc(length + 1);
    sprintf(output, "purchased%c:%d:%d,%d,%d,%d,%d\n",
            'A' + playerId, input.cardNumber, input.costSpent[TOKEN_PURPLE],
            input.costSpent[TOKEN_BROWN], input.costSpent[TOKEN_YELLOW],
            input.costSpent[TOKEN_RED], input.costSpent[TOKEN_WILD]);

    return output;
}

int parse_took_message(struct TakeMessage* output, int* playerId,
        const char* message) {
    if (strstr(message, "took") != message) {
        return -1;
    }
    message += strlen("took");

    if (*message < 'A' || *message > 'Z') {
        return -1;
    }
    *playerId = *message++ - 'A';

    if (*message++ != ':') {
        return -1;
    }

    char* next;
    for (int i = 0; i < TOKEN_MAX - 1; ++i) {
        output->tokens[i] = parse_int(message, &next);
        // The delimiter we expect changes if this is the last element of the
        // list
        if (next == message ||
                (*next != ((i + 1 == TOKEN_MAX - 1) ? '\0' : ','))) {
            return -1;
        } else {
            message = ++next;
        }
    }

    // ensure that exactly 3 tokens of different colours were taken
    int max = 0;
    for (int i = 0; i < TOKEN_MAX - 1; ++i) {
        if (output->tokens[i] < 0 || output->tokens[i] > 1) {
            return -1;
        }
        max += output->tokens[i];
    }
    if (max != TAKE_NUMBER) {
        return -1;
    }

    return 0;
}

char* print_took_message(struct TakeMessage input, int playerId) {
    int length = snprintf(NULL, 0, "took%c:%d,%d,%d,%d\n", 'A' + playerId,
            input.tokens[TOKEN_PURPLE], input.tokens[TOKEN_BROWN],
            input.tokens[TOKEN_YELLOW], input.tokens[TOKEN_RED]);
    char* output = malloc(length + 1);
    sprintf(output, "took%c:%d,%d,%d,%d\n", 'A' + playerId,
            input.tokens[TOKEN_PURPLE], input.tokens[TOKEN_BROWN],
            input.tokens[TOKEN_YELLOW], input.tokens[TOKEN_RED]);

    return output;
}

int parse_took_wild_message(int* output, const char* message) {
    if (strstr(message, "wild") != message) {
        return -1;
    }
    message += strlen("wild");

    if (*message < 'A' || *message > 'Z') {
        return -1;
    }

    *output = *message - 'A';

    if (*++message != '\0') {
        return -1;
    }

    return 0;
}

char* print_took_wild_message(int input) {
    int length = snprintf(NULL, 0, "wild%c\n", 'A' + input);
    char* output = malloc(length + 1);
    sprintf(output, "wild%c\n", 'A' + input);
    return output;
}

int parse_new_card_message(struct Card* output, const char* message) {
    if (strstr(message, "newcard") != message) {
        return -1;
    }
    message += strlen("newcard");

    return parse_card(output, message);
}

char* print_new_card_message(struct Card input) {
    int length = snprintf(NULL, 0, "newcard%c:%d:%d,%d,%d,%d\n",
            print_token(input.discount), input.points,
            input.cost[TOKEN_PURPLE], input.cost[TOKEN_BROWN],
            input.cost[TOKEN_YELLOW], input.cost[TOKEN_RED]);
    char* output = malloc(length + 1);
    sprintf(output, "newcard%c:%d:%d,%d,%d,%d\n", print_token(input.discount),
            input.points, input.cost[TOKEN_PURPLE], input.cost[TOKEN_BROWN],
            input.cost[TOKEN_YELLOW], input.cost[TOKEN_RED]);

    return output;
}

int parse_tokens_message(int* output, const char* message) {
    if (strstr(message, "tokens") != message) {
        return -1;
    }
    message += strlen("tokens");

    char* next;
    *output = parse_int(message, &next);

    if (next == message || *next || *output < 0) {
        return -1;
    }

    return 0;
}

char* print_tokens_message(int input) {
    int length = snprintf(NULL, 0, "tokens%d\n", input);
    char* output = malloc(length + 1);
    sprintf(output, "tokens%d\n", input);

    return output;
}

int parse_disco_message(int* output, const char* message) {
    if (strstr(message, "disco") != message) {
        return -1;
    }
    message += strlen("disco");

    char player = *message++;
    if (player < 'A' || player > 'Z') {
        return -1;
    }

    if (*message) {
        return -1;
    }

    *output = player - 'A';
    return 0;
}

char* print_disco_message(int input) {
    int length = snprintf(NULL, 0, "disco%c\n", 'A' + input);
    char* output = malloc(length + 1);
    sprintf(output, "disco%c\n", 'A' + input);

    return output;
}

int parse_invalid_message(int* output, const char* message) {
    if (strstr(message, "invalid") != message) {
        return -1;
    }
    message += strlen("invalid");

    char player = *message++;
    if (player < 'A' || player > 'Z') {
        return -1;
    }

    if (*message) {
        return -1;
    }

    *output = player - 'A';
    return 0;
}

char* print_invalid_message(int input) {
    int length = snprintf(NULL, 0, "invalid%c\n", 'A' + input);
    char* output = malloc(length + 1);
    sprintf(output, "invalid%c\n", 'A' + input);

    return output;
}

enum MessageFromPlayer classify_from_player(const char* message) {
    if (strstr(message, "purchase") == message) {
        return PURCHASE;
    } else if (strstr(message, "take") == message) {
        return TAKE;
    } else if (strcmp(message, "wild") == 0) {
        return WILD;
    } else {
        return -1;
    }
}

int parse_purchase_message(struct PurchaseMessage* output,
        const char* message) {
    if (strstr(message, "purchase") != message) {
        return -1;
    }
    message += strlen("purchase");

    char* next;
    output->cardNumber = parse_int(message, &next);
    if (next == message || *next != ':') {
        return -1;
    }
    message = ++next;

    for (int i = 0; i < TOKEN_MAX; ++i) {
        output->costSpent[i] = parse_int(message, &next);
        // The delimiter we expect changes if this is the last element of the
        // list
        if (next == message ||
                (*next != ((i + 1 == TOKEN_MAX) ? '\0' : ','))) {
            return -1;
        } else {
            message = ++next;
        }
    }

    // check validity of numbers in messages
    for (int i = 0; i < TOKEN_MAX; ++i) {
        if (output->costSpent[i] < 0) {
            return -1;
        }
    }

    return 0;
}

char* print_purchase_message(struct PurchaseMessage input) {
    int length = snprintf(NULL, 0, "purchase%d:%d,%d,%d,%d,%d\n",
            input.cardNumber, input.costSpent[TOKEN_PURPLE],
            input.costSpent[TOKEN_BROWN], input.costSpent[TOKEN_YELLOW],
            input.costSpent[TOKEN_RED], input.costSpent[TOKEN_WILD]);
    char* output = malloc(length + 1);
    sprintf(output, "purchase%d:%d,%d,%d,%d,%d\n", input.cardNumber,
            input.costSpent[TOKEN_PURPLE], input.costSpent[TOKEN_BROWN],
            input.costSpent[TOKEN_YELLOW], input.costSpent[TOKEN_RED],
            input.costSpent[TOKEN_WILD]);

    return output;
}

int parse_take_message(struct TakeMessage* output, const char* message) {
    if (strstr(message, "take") != message) {
        return -1;
    }
    message += strlen("take");

    char* next;
    for (int i = 0; i < TOKEN_MAX - 1; ++i) {
        output->tokens[i] = parse_int(message, &next);
        // The delimiter we expect changes if this is the last element of the
        // list
        if (next == message ||
                (*next != ((i + 1 == TOKEN_MAX - 1) ? '\0' : ','))) {
            return -1;
        } else {
            message = ++next;
        }
    }

    // ensure that exactly 3 tokens of different colours were taken
    int max = 0;
    for (int i = 0; i < TOKEN_MAX - 1; ++i) {
        if (output->tokens[i] < 0 || output->tokens[i] > 1) {
            return -1;
        }
        max += output->tokens[i];
    }
    if (max != TAKE_NUMBER) {
        return -1;
    }

    return 0;
}

char* print_take_message(struct TakeMessage input) {
    int length = snprintf(NULL, 0, "take%d,%d,%d,%d\n",
            input.tokens[TOKEN_PURPLE], input.tokens[TOKEN_BROWN],
            input.tokens[TOKEN_YELLOW], input.tokens[TOKEN_RED]);
    char* output = malloc(length + 1);
    sprintf(output, "take%d,%d,%d,%d\n", input.tokens[TOKEN_PURPLE],
            input.tokens[TOKEN_BROWN], input.tokens[TOKEN_YELLOW],
            input.tokens[TOKEN_RED]);

    return output;
}
