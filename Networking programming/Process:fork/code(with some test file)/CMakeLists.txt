cmake_minimum_required(VERSION 3.12)
project(game1 C)

set(CMAKE_C_STANDARD 99)

add_executable(game1 austerity.c player.c player.h deck.c deck.h game.c game.h banzai.c ed.c)