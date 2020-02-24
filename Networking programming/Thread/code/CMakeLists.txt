cmake_minimum_required(VERSION 3.12)
project(code_v1_3 C)

set(CMAKE_C_STANDARD 11)

include_directories(.)
include_directories(include)

add_executable(code_v1_3
        include/deck.h
        include/game.h
        include/player.h
        include/protocol.h
        include/server.h
        include/token.h
        include/util.h
        lib/deck.c
        lib/game.c
        lib/player.c
        lib/protocol.c
        lib/server.c
        lib/token.c
        lib/util.c
        zazu.c
        rafiki.h)
