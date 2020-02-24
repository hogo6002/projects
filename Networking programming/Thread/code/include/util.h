#ifndef _UTIL_H_
#define _UTIL_H_

#include <stdio.h>

/* Reads a line from a file. On success, the length of the line read will be
 * returned, and output will point to an allocated and null terminated buffer
 * containing the string that was read from the file. If the file ends halfway
 * through a line of input being read, then it will be treated only as the end
 * of the line. If the file ends at the start of the line of input being read,
 * 0 will be returned and output will point to NULL. If a read error of any
 * kind occurs, the return value of this function will be negative, with its
 * magnitude equal to the length of bytes read from the file before the error
 * occured. In the case of a read error, output will still point to the same
 * allocated buffer that it would have in case of success, but without the null
 * terminator. If the argument offset is nonzero, this function will assume
 * that output already points to a buffer of at least size offset, and that the
 * beginning of a line of input is contained in that buffer.
 */
int read_line(FILE* input, char** output, int offset);

/* Parses an integer from a string. Takes as input the string being parsed,
 * and a pointer to a char pointer. At the end of this function, if that output
 * pointer is not NULL, it will point to a position within the original string
 * at the end of this function being called. That position is the beginning of
 * the characters after the integer within the string being parsed. If *output
 * is equal to input after this function then the integer is invalid and the
 * return value of this function is meaningless. Otherwise, this function will
 * return the value of the parsed integer.
 */
long parse_int(const char* input, char** output);

/* Returns the maximum of two integer values.
 */
int max(int val1, int val2);

#endif
