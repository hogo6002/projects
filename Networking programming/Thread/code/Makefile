CC=gcc
CFLAGS=-Wall -Wextra -pedantic -std=gnu99 -g -pthread -Iinclude -fPIC

.PHONY: all clean

LIBRARY_SOURCES=\
				lib/game.c \
				lib/util.c \
				lib/protocol.c \
				lib/token.c \
				lib/server.c \
				lib/player.c \
				lib/deck.c \

RAFIKI_SOURCES=\
				  rafiki.c \

GOPHER_SOURCES=\
			   gopher.c \

ZAZU_SOURCES=\
			   zazu.c \

all: \
	liba4.so \
	rafiki \
	gopher \
	zazu \

clean:
	rm -f $(LIBRARY_SOURCES:.c=.o) liba4.so
	rm -f $(AUSTERITY_SOURCES:.c=.o) rafiki
	rm -f $(SHENZI_SOURCES:.c=.o) gopher
	rm -f $(BANZAI_SOURCES:.c=.o) zazu

liba4.so: $(LIBRARY_SOURCES:.c=.o)
	$(CC) $(CFLAGS) -o liba4.so -shared $(LIBRARY_SOURCES:.c=.o)

rafiki: $(RAFIKI_SOURCES:.c=.o) $(LIBRARY_SOURCES:.c=.o)

gopher: $(GOPHER_SOURCES:.c=.o) $(LIBRARY_SOURCES:.c=.o)

zazu: $(ZAZU_SOURCES:.c=.o) $(LIBRARY_SOURCES:.c=.o)
