all: austerity shenzi banzai ed

OPTS=-std=c99 --pedantic -Wall -Werror -g

austerity: austerity.o deck.o game.o player.o
	gcc -o austerity austerity.o deck.o game.o player.o

shenzi: shenzi.o deck.o game.o player.o
	gcc -o shenzi shenzi.o deck.o game.o player.o

banzai: banzai.o deck.o game.o player.o
	gcc -o banzai banzai.o deck.o game.o player.o

ed: ed.o deck.o game.o player.o
	gcc -o ed ed.o deck.o game.o player.o

austerity.o: austerity.c deck.h game.h player.h
	gcc $(OPTS) -c austerity.c

shenzi.o: shenzi.c deck.h game.h player.h
	gcc $(OPTS) -c shenzi.c

banzai.o: banzai.c deck.h game.h player.h
	gcc $(OPTS) -c banzai.c

ed.o: ed.c deck.h game.h player.h
	gcc $(OPTS) -c ed.c

deck.o: deck.c deck.h player.h
	gcc $(OPTS) -c deck.c

game.o: game.c game.h deck.h player.h
	gcc $(OPTS) -c game.c

player.o: player.c player.h deck.h
	gcc $(OPTS) -c player.c

clean:
	rm -f austerity.o deck.o game.o player.o austerity	
	rm -f shenzi.o deck.o game.o player.o shenzi
	rm -f banzai.o deck.o game.o player.o banzai
	rm -f ed.o deck.o game.o player.o ed
