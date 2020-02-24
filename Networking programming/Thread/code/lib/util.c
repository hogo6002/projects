#include "util.h"
#include <stdlib.h>

#define INITIAL_BUFFER_SIZE 80

int read_line(FILE* input, char** output, int offset) {
    int bytesRead = offset, capacity = INITIAL_BUFFER_SIZE, nextByte;

    // resize buffer if needed, to ensure already read bytes can fit
    while (bytesRead + 1 >= capacity) {
        capacity *= 2;
    }

    if (offset == 0 || *output == NULL) {
        *output = malloc(sizeof(char) * capacity);
    }

    while (nextByte = fgetc(input), nextByte != EOF && nextByte != '\n') {
        (*output)[bytesRead] = nextByte;
        bytesRead += 1;
        if (bytesRead + 1 >= capacity) {
            capacity *= 2; // double each time, for amortized linear cost
            *output = realloc(*output, capacity);
        }
    }

    if (nextByte == EOF && ferror(input)) {
        // an IO error occured
        return -1 * bytesRead;
    } else if (bytesRead == 0 && nextByte == EOF) {
        // reading failed
        free(*output);
        *output = NULL;
        return 0;
    } else {
        // reading succeeded
        (*output)[bytesRead] = '\0';
        return bytesRead;
    }
}

long parse_int(const char* input, char** output) {
    if (*input == ' ') {
        if (output != NULL) {
            *output = (char*) input;
            return 0;
        }
    }

    return strtol(input, output, 10);
}

int max(int val1, int val2) {
    return val1 > val2 ? val1 : val2;
}
