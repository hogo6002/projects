# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 3.12

# Delete rule output on recipe failure.
.DELETE_ON_ERROR:


#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:


# Remove some rules from gmake that .SUFFIXES does not remove.
SUFFIXES =

.SUFFIXES: .hpux_make_needs_suffix_list


# Suppress display of executed commands.
$(VERBOSE).SILENT:


# A target that is always out of date.
cmake_force:

.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

# The shell in which to execute make rules.
SHELL = /bin/sh

# The CMake executable.
CMAKE_COMMAND = /Applications/CLion.app/Contents/bin/cmake/mac/bin/cmake

# The command to remove a file.
RM = /Applications/CLion.app/Contents/bin/cmake/mac/bin/cmake -E remove -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = /Users/holly/Desktop/csse2310/ass4/code_v1.3

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = /Users/holly/Desktop/csse2310/ass4/code_v1.3/cmake-build-debug

# Include any dependencies generated for this target.
include CMakeFiles/code_v1_3.dir/depend.make

# Include the progress variables for this target.
include CMakeFiles/code_v1_3.dir/progress.make

# Include the compile flags for this target's objects.
include CMakeFiles/code_v1_3.dir/flags.make

CMakeFiles/code_v1_3.dir/lib/deck.c.o: CMakeFiles/code_v1_3.dir/flags.make
CMakeFiles/code_v1_3.dir/lib/deck.c.o: ../lib/deck.c
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/Users/holly/Desktop/csse2310/ass4/code_v1.3/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_1) "Building C object CMakeFiles/code_v1_3.dir/lib/deck.c.o"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -o CMakeFiles/code_v1_3.dir/lib/deck.c.o   -c /Users/holly/Desktop/csse2310/ass4/code_v1.3/lib/deck.c

CMakeFiles/code_v1_3.dir/lib/deck.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/code_v1_3.dir/lib/deck.c.i"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E /Users/holly/Desktop/csse2310/ass4/code_v1.3/lib/deck.c > CMakeFiles/code_v1_3.dir/lib/deck.c.i

CMakeFiles/code_v1_3.dir/lib/deck.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/code_v1_3.dir/lib/deck.c.s"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S /Users/holly/Desktop/csse2310/ass4/code_v1.3/lib/deck.c -o CMakeFiles/code_v1_3.dir/lib/deck.c.s

CMakeFiles/code_v1_3.dir/lib/game.c.o: CMakeFiles/code_v1_3.dir/flags.make
CMakeFiles/code_v1_3.dir/lib/game.c.o: ../lib/game.c
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/Users/holly/Desktop/csse2310/ass4/code_v1.3/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_2) "Building C object CMakeFiles/code_v1_3.dir/lib/game.c.o"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -o CMakeFiles/code_v1_3.dir/lib/game.c.o   -c /Users/holly/Desktop/csse2310/ass4/code_v1.3/lib/game.c

CMakeFiles/code_v1_3.dir/lib/game.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/code_v1_3.dir/lib/game.c.i"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E /Users/holly/Desktop/csse2310/ass4/code_v1.3/lib/game.c > CMakeFiles/code_v1_3.dir/lib/game.c.i

CMakeFiles/code_v1_3.dir/lib/game.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/code_v1_3.dir/lib/game.c.s"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S /Users/holly/Desktop/csse2310/ass4/code_v1.3/lib/game.c -o CMakeFiles/code_v1_3.dir/lib/game.c.s

CMakeFiles/code_v1_3.dir/lib/player.c.o: CMakeFiles/code_v1_3.dir/flags.make
CMakeFiles/code_v1_3.dir/lib/player.c.o: ../lib/player.c
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/Users/holly/Desktop/csse2310/ass4/code_v1.3/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_3) "Building C object CMakeFiles/code_v1_3.dir/lib/player.c.o"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -o CMakeFiles/code_v1_3.dir/lib/player.c.o   -c /Users/holly/Desktop/csse2310/ass4/code_v1.3/lib/player.c

CMakeFiles/code_v1_3.dir/lib/player.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/code_v1_3.dir/lib/player.c.i"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E /Users/holly/Desktop/csse2310/ass4/code_v1.3/lib/player.c > CMakeFiles/code_v1_3.dir/lib/player.c.i

CMakeFiles/code_v1_3.dir/lib/player.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/code_v1_3.dir/lib/player.c.s"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S /Users/holly/Desktop/csse2310/ass4/code_v1.3/lib/player.c -o CMakeFiles/code_v1_3.dir/lib/player.c.s

CMakeFiles/code_v1_3.dir/lib/protocol.c.o: CMakeFiles/code_v1_3.dir/flags.make
CMakeFiles/code_v1_3.dir/lib/protocol.c.o: ../lib/protocol.c
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/Users/holly/Desktop/csse2310/ass4/code_v1.3/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_4) "Building C object CMakeFiles/code_v1_3.dir/lib/protocol.c.o"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -o CMakeFiles/code_v1_3.dir/lib/protocol.c.o   -c /Users/holly/Desktop/csse2310/ass4/code_v1.3/lib/protocol.c

CMakeFiles/code_v1_3.dir/lib/protocol.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/code_v1_3.dir/lib/protocol.c.i"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E /Users/holly/Desktop/csse2310/ass4/code_v1.3/lib/protocol.c > CMakeFiles/code_v1_3.dir/lib/protocol.c.i

CMakeFiles/code_v1_3.dir/lib/protocol.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/code_v1_3.dir/lib/protocol.c.s"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S /Users/holly/Desktop/csse2310/ass4/code_v1.3/lib/protocol.c -o CMakeFiles/code_v1_3.dir/lib/protocol.c.s

CMakeFiles/code_v1_3.dir/lib/server.c.o: CMakeFiles/code_v1_3.dir/flags.make
CMakeFiles/code_v1_3.dir/lib/server.c.o: ../lib/server.c
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/Users/holly/Desktop/csse2310/ass4/code_v1.3/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_5) "Building C object CMakeFiles/code_v1_3.dir/lib/server.c.o"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -o CMakeFiles/code_v1_3.dir/lib/server.c.o   -c /Users/holly/Desktop/csse2310/ass4/code_v1.3/lib/server.c

CMakeFiles/code_v1_3.dir/lib/server.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/code_v1_3.dir/lib/server.c.i"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E /Users/holly/Desktop/csse2310/ass4/code_v1.3/lib/server.c > CMakeFiles/code_v1_3.dir/lib/server.c.i

CMakeFiles/code_v1_3.dir/lib/server.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/code_v1_3.dir/lib/server.c.s"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S /Users/holly/Desktop/csse2310/ass4/code_v1.3/lib/server.c -o CMakeFiles/code_v1_3.dir/lib/server.c.s

CMakeFiles/code_v1_3.dir/lib/token.c.o: CMakeFiles/code_v1_3.dir/flags.make
CMakeFiles/code_v1_3.dir/lib/token.c.o: ../lib/token.c
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/Users/holly/Desktop/csse2310/ass4/code_v1.3/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_6) "Building C object CMakeFiles/code_v1_3.dir/lib/token.c.o"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -o CMakeFiles/code_v1_3.dir/lib/token.c.o   -c /Users/holly/Desktop/csse2310/ass4/code_v1.3/lib/token.c

CMakeFiles/code_v1_3.dir/lib/token.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/code_v1_3.dir/lib/token.c.i"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E /Users/holly/Desktop/csse2310/ass4/code_v1.3/lib/token.c > CMakeFiles/code_v1_3.dir/lib/token.c.i

CMakeFiles/code_v1_3.dir/lib/token.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/code_v1_3.dir/lib/token.c.s"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S /Users/holly/Desktop/csse2310/ass4/code_v1.3/lib/token.c -o CMakeFiles/code_v1_3.dir/lib/token.c.s

CMakeFiles/code_v1_3.dir/lib/util.c.o: CMakeFiles/code_v1_3.dir/flags.make
CMakeFiles/code_v1_3.dir/lib/util.c.o: ../lib/util.c
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/Users/holly/Desktop/csse2310/ass4/code_v1.3/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_7) "Building C object CMakeFiles/code_v1_3.dir/lib/util.c.o"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -o CMakeFiles/code_v1_3.dir/lib/util.c.o   -c /Users/holly/Desktop/csse2310/ass4/code_v1.3/lib/util.c

CMakeFiles/code_v1_3.dir/lib/util.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/code_v1_3.dir/lib/util.c.i"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E /Users/holly/Desktop/csse2310/ass4/code_v1.3/lib/util.c > CMakeFiles/code_v1_3.dir/lib/util.c.i

CMakeFiles/code_v1_3.dir/lib/util.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/code_v1_3.dir/lib/util.c.s"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S /Users/holly/Desktop/csse2310/ass4/code_v1.3/lib/util.c -o CMakeFiles/code_v1_3.dir/lib/util.c.s

CMakeFiles/code_v1_3.dir/zazu.c.o: CMakeFiles/code_v1_3.dir/flags.make
CMakeFiles/code_v1_3.dir/zazu.c.o: ../zazu.c
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/Users/holly/Desktop/csse2310/ass4/code_v1.3/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_8) "Building C object CMakeFiles/code_v1_3.dir/zazu.c.o"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -o CMakeFiles/code_v1_3.dir/zazu.c.o   -c /Users/holly/Desktop/csse2310/ass4/code_v1.3/zazu.c

CMakeFiles/code_v1_3.dir/zazu.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/code_v1_3.dir/zazu.c.i"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E /Users/holly/Desktop/csse2310/ass4/code_v1.3/zazu.c > CMakeFiles/code_v1_3.dir/zazu.c.i

CMakeFiles/code_v1_3.dir/zazu.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/code_v1_3.dir/zazu.c.s"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S /Users/holly/Desktop/csse2310/ass4/code_v1.3/zazu.c -o CMakeFiles/code_v1_3.dir/zazu.c.s

# Object files for target code_v1_3
code_v1_3_OBJECTS = \
"CMakeFiles/code_v1_3.dir/lib/deck.c.o" \
"CMakeFiles/code_v1_3.dir/lib/game.c.o" \
"CMakeFiles/code_v1_3.dir/lib/player.c.o" \
"CMakeFiles/code_v1_3.dir/lib/protocol.c.o" \
"CMakeFiles/code_v1_3.dir/lib/server.c.o" \
"CMakeFiles/code_v1_3.dir/lib/token.c.o" \
"CMakeFiles/code_v1_3.dir/lib/util.c.o" \
"CMakeFiles/code_v1_3.dir/zazu.c.o"

# External object files for target code_v1_3
code_v1_3_EXTERNAL_OBJECTS =

code_v1_3: CMakeFiles/code_v1_3.dir/lib/deck.c.o
code_v1_3: CMakeFiles/code_v1_3.dir/lib/game.c.o
code_v1_3: CMakeFiles/code_v1_3.dir/lib/player.c.o
code_v1_3: CMakeFiles/code_v1_3.dir/lib/protocol.c.o
code_v1_3: CMakeFiles/code_v1_3.dir/lib/server.c.o
code_v1_3: CMakeFiles/code_v1_3.dir/lib/token.c.o
code_v1_3: CMakeFiles/code_v1_3.dir/lib/util.c.o
code_v1_3: CMakeFiles/code_v1_3.dir/zazu.c.o
code_v1_3: CMakeFiles/code_v1_3.dir/build.make
code_v1_3: CMakeFiles/code_v1_3.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --bold --progress-dir=/Users/holly/Desktop/csse2310/ass4/code_v1.3/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_9) "Linking C executable code_v1_3"
	$(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/code_v1_3.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
CMakeFiles/code_v1_3.dir/build: code_v1_3

.PHONY : CMakeFiles/code_v1_3.dir/build

CMakeFiles/code_v1_3.dir/clean:
	$(CMAKE_COMMAND) -P CMakeFiles/code_v1_3.dir/cmake_clean.cmake
.PHONY : CMakeFiles/code_v1_3.dir/clean

CMakeFiles/code_v1_3.dir/depend:
	cd /Users/holly/Desktop/csse2310/ass4/code_v1.3/cmake-build-debug && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /Users/holly/Desktop/csse2310/ass4/code_v1.3 /Users/holly/Desktop/csse2310/ass4/code_v1.3 /Users/holly/Desktop/csse2310/ass4/code_v1.3/cmake-build-debug /Users/holly/Desktop/csse2310/ass4/code_v1.3/cmake-build-debug /Users/holly/Desktop/csse2310/ass4/code_v1.3/cmake-build-debug/CMakeFiles/code_v1_3.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : CMakeFiles/code_v1_3.dir/depend

