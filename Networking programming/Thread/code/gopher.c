#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>

/**
 * The main entrance of this server program.
 * it will send the "scores" to determinate itself, then receive the respond
 * if the respond is not "yes", then exit it with status 3. then it will
 * print all the message it received from server.
 * @param argc  -   the total number of argument
 * @param argv  -   all of the argument
 */
int main(int argc, char **argv) {
    if (argc != 2) {
        fprintf(stderr, "Usage: gopher port\n");
        exit(1);
    }
    if (atoi(argv[1]) == 0 || atoi(argv[1]) > 65535) {
        fprintf(stderr, "Failed to connect\n");
        exit(3);
    }
    int portNumber = atoi(argv[1]);
    int sock;
    sock = socket(AF_INET, SOCK_STREAM, 0);
    struct sockaddr_in servAddr;
    servAddr.sin_family = AF_INET;
    servAddr.sin_port = htons(portNumber);
    servAddr.sin_addr.s_addr = INADDR_ANY;
    int connectionStatus = connect(sock, (struct sockaddr *)
            &servAddr, sizeof(servAddr));
    if (connectionStatus == -1) {
        fprintf(stderr, "Failed to connect\n");
        exit(3);
    }
    char *message = "scores\n";
    send(sock, message, strlen(message), 0);

    int end;
    char servRes[4];
    end = recv(sock, &servRes, sizeof(servRes), 0);
    servRes[end] = '\0';
    char *yes = "yes";
    for (unsigned int i = 0; i < strlen(yes); i++) {
        if (yes[i] != servRes[i]) {
            fprintf(stderr, "Invalid server\n");
            exit(4);
        }
    }
    char info[40];
    while ((end = recv(sock, &info, sizeof(info), 0))) {
        info[end] = '\0';
        printf("%s", info);
        fflush(stdout);
    }
    return 0;
}

