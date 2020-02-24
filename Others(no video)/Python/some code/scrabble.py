"""
Simple Modelling Classes for Scrabble
CSSE1001 Assignment 2
Semester 2, 2017
"""

import itertools
import random

# from a2 import Bonus, Tile, Wildcard
from a2 import WordBonus, LetterBonus, Player, TileBag, Board
import a2_support

__author__ = "Benjamin Martin"
__version__ = "1.0.1"
__date__ = "14/08/2017"


class WordBook:
    """A scrabble dictionary; little more than, but not to be confused with Python's built-in dictionary type"""

    def __init__(self, words):
        """Constructor

        Parameters:
            words (iterable): A collection of words
        """
        self._words = words

    @classmethod
    def load_from_file(cls, filename, min_length):
        """(WordBook) Loads a scrabble dictionary from a file

        Parameters:
            filename (str): The filename of the word file
            min_length (int): The minimum length of word to load
        """
        words = set()
        with open(filename) as file:
            for line in file:
                line = line.strip()
                if len(line) >= min_length:
                    words.add(line)

        return cls(words)

    def __contains__(self, word):
        """(bool) Returns True iff word is contained in this word book"""
        return word in self._words

    def __str__(self):
        return "WordBook({} words)".format(len(self._words))


class Word:
    """A scrabble word on the board"""

    def __init__(self):
        """Constructor"""
        self._tiles = {}

    def __setitem__(self, position, tile):
        """Adds a tile to this word, or updates if the position is already filled

        Parameters:
            position (tuple<int, int>): Row, column position to add the tile
            tile (Tile): The tile
        """
        self._tiles[position] = tile

    def __len__(self):
        """(int) Returns the number of tiles in this word"""
        return len(self._tiles)

    def get_string(self):
        """(str) Returns the word text"""
        return "".join(tile.get_letter() for _, tile in sorted(self._tiles.items()))

    def __iter__(self):
        """Yields position, tile pairs, in ascending order

        Yield:
            (tuple<tuple<int, int>, Letter>): (Row, column position), tile
        """
        return iter(sorted(self._tiles.items()))

    def __str__(self):
        """(str) Returns a human readable representation of this word, including its position range"""
        tiles = list(self)
        start, end = tiles[0][0], tiles[-1][0]
        return f"{self.get_string()} @ {start}-{end}"


class Scrabble:
    """A game of scrabble"""

    def __init__(self, names, max_letters=a2_support.MAX_LETTERS,
                 all_letter_bonus=a2_support.ALL_LETTER_BONUS, bag=None, dictionary=None):
        """Constructor

        Parameters:
            names (tuple<str>): Names of each player, in order
            max_letters (int): The maximum number of letters in a player's rack
            bag (TileBag): The bag of letters to use for the game. Loads from "letters.txt" by default
            dictionary (WordBook): A dictionary of acceptable words. Loads from "words_alpha.txt" by default
        """

        if not a2_support.MIN_PLAYERS < len(names) < a2_support.MAX_PLAYERS:
            raise a2_support.ScrabbleError("Invalid number of players; must be between {} & {}".format(
                a2_support.MIN_PLAYERS, a2_support.MAX_PLAYERS))

        if dictionary is None:
            dictionary = WordBook.load_from_file('words_alpha.txt', a2_support.MIN_WORD_LENGTH)
        self._dictionary = dictionary

        self._players = [Player(name) for name in names]

        self._board = Board(a2_support.BOARD_DIMENSION, a2_support.WORD_BONUSES,
                            a2_support.LETTER_BONUSES, a2_support.START_POSITION)

        if bag is None:
            bag = TileBag(a2_support.LETTERS)
        self._bag = bag

        self._active_move = {}

        self._max_letters = max_letters
        self._all_letter_bonus = all_letter_bonus

        self._skips = 0
        self._active_player = None

        self.restart()

    def get_board(self):
        """(Board) Returns the game board"""
        return self._board

    def get_bag(self):
        """(LetterBag) Returns the letter bag"""
        return self._bag

    def restart(self):
        """Restarts the game"""
        self._bag.reset()
        self._skips = 0

        self._board.reset()

        for player in self._players:
            player.reset()

            for _ in range(self._max_letters):
                player.add_tile(self._bag.draw())

        self._active_player = self._starting_player = random.randrange(0, len(self._players))

        self._active_move = {}

        print("Game reset")

    def get_active_player(self):
        """(Player) Returns the active player"""
        return self._players[self._active_player]

    def get_player(self, index):
        """(Player) Returns a player by index, corresponding to the index in 'names' given to the constructor"""
        return self._players[index]

    def get_players(self):
        """(list<Player>) Returns all players in the game, in turn order"""
        return self._players[self._starting_player:] + self._players[:self._starting_player]

    def is_position_active(self, position):
        """(bool) Returns True iff the current move contains a tile at position

        Parameters:
            position (tuple<int, int>): The grid position to check
        """
        return position in self._active_move

    def _next_player(self):
        """Internal method to switch to the next player"""
        self._active_player = (self._active_player + 1) % len(self._players)

    def place_letter(self, index, position):
        """Places a letter at a given position during a move (not applied until play is confirmed)

        Parameters:
            index: The index of the letter in the player's rack, starting from 0
            position (tuple<int, int>): The row, column position at which to place the letter

        Return:
            Tile: The letter that was placed
        """
        player = self.get_active_player()
        if not self._board.is_position_valid(position):
            raise a2_support.PositionError(f"{position} is not a valid position")

        if self.get_letter(position):
            raise a2_support.PositionError(f"A letter has already been placed at {position}")

        # ensure tiles placed in single line
        if self._active_move:
            positions = list(itertools.islice(self._active_move, 2))
            positions.append(position)

            rows, columns = tuple(zip(*positions))

            if len(set(rows)) != 1 and len(set(columns)) != 1:
                raise a2_support.PositionError(f"Must place letters in a straight line")

        letter = player.remove_tile(index)

        self._active_move[position] = letter
        return letter

    def pickup_letter(self, position):
        """(Tile) Picks up a tile from the board and returns it

        Parameters:
            position (tuple<int, int>): The position from which to pick up the tile

        Preconditions:
            position is in active play
        """
        letter = self._active_move.pop(position)
        letter.reset()
        self.get_active_player().add_tile(letter)

        return letter

    def clear_play(self):
        """Clears the active play

        Yield:
            tuple<tuple<int, int>, Tile>: Position, tile pair for each move in the active play
        """
        for position in list(self._active_move.keys()):  # bad hack to avoid changed size iteration error
            yield position, self.pickup_letter(position)

    def get_play(self):
        """(dict<tuple<int, int>, Tile>) Returns the active play"""
        return dict(self._active_move)

    def calculate_word_score(self, word):
        """(int) Calculates and returns the score earned by playing a word on the board

        Parameters:
            word (Word): The word played
        """
        word_bonus = 1
        word_score = 0

        for position, letter in word:
            letter_bonus = 1
            if position in self._active_move:
                bonus = self._board.get_bonus(position)

                if isinstance(bonus, WordBonus):
                    word_bonus *= bonus.get_value()
                elif isinstance(bonus, LetterBonus):
                    letter_bonus = bonus.get_value()

            word_score += letter.get_score() * letter_bonus

        word_score *= word_bonus

        print(f"{word} scored {word_score} with w{word_bonus}")

        return word_score

    def confirm_place(self):
        """Confirms the current play and adjusts the player's score.

        Return:
            dict<str, int>: A mapping of words formed => amount scored
        """

        valid_words = self.validate_current()
        if not valid_words:
            return {}

        player = self.get_active_player()

        # Scoring
        scores = {}

        for word in valid_words:
            scores[word.get_string()] = self.calculate_word_score(word)

        score = sum(scores.values())

        if len(self._active_move) == self._max_letters:
            score += self._all_letter_bonus
            print(
                "Achieved {} bonus points for using all {} letters!".format(self._all_letter_bonus, self._max_letters))

        player.add_score(score)

        # Place all letters
        for position, letter in self._active_move.items():
            self._board.place_tile(position, letter)

        # Refill letters, if possible
        for _ in range(len(self._active_move)):
            if len(self._bag) == 0:
                break
            player.add_tile(self._bag.draw())

        # Next player's turn

        if len(scores):
            self._skips = 0

        self._active_move = {}
        self._next_player()

        return scores

    def swap_letters(self, indices):
        """Swaps letters from the active player for new ones

        Parameters:
            indices (list<int>): A list of indices of letters to return to the bag
        """
        if len(self._active_move) != 0:
            raise a2_support.ActionError("Cannot swap letters while a play is being made")

        player = self.get_active_player()

        if len(self._bag) < len(indices):
            raise a2_support.ActionError("Bag only has {len(self._bag} letters remaining")

        new_letters = []
        for index in sorted(indices, reverse=True):  # Reverse order, so removing doesn't affect subsequent indices
            new_letters.append(self._bag.draw())
            player.remove_tile(index)

        for index in new_letters:
            player.add_tile(index)

        if len(indices):
            self._skips = 0
        self._next_player()
        print(f"Swapped {indices} for {new_letters}")

    def skip(self):
        """Skips to the next player.

        Raises:
            ValueError: If tiles have been placed on the board
        """
        if len(self._active_move) != 0:
            raise a2_support.ActionError("Cannot skip while a play is being made")

        self._skips += 1
        self._next_player()

    def get_letter(self, position):
        """(Tile) Returns the letter at position, including the active move, else None if the tile is empty

        Parameters:
            position (tuple<int, int>): The row, column position to check
        """
        played = self._board.get_tile(position)

        if played:
            return played

        return self._active_move.get(position)

    def is_game_over(self):
        """(bool) Returns True iff the game is over"""
        return self._skips == len(
            self._players or (len(self._bag) == 0 and min(len(player.get_tiles()) == 0 for player in self._players)))

    def get_rankings(self):
        """(list<Player>) Returns players, ordered by score in descending order"""
        return sorted(self._players, key=lambda p: p.get_score(), reverse=True)

    def validate_current(self):
        """(list<Word>) Validates the current move and returns all (valid) words

        Raises:
            a2_support.ActionError: If any of the current words is invalid
        """
        words_by_axis = self.get_potential_words()

        if not self.get_letter(self._board.get_start()):
            raise a2_support.ActionError("First move must cover the start tile")

        # Dictionary check each word
        words = []

        for axial_words in words_by_axis.values():
            for positions in axial_words:

                word = self.convert_positions_to_word(positions)
                string = word.get_string()

                if string.lower() not in self._dictionary:
                    raise a2_support.ActionError(f"{string} is not a valid word")

                words.append(word)

        return words

    def convert_positions_to_word(self, positions):
        """(Word) Creates a word from characters found at positions, including the current play

        Parameters:
            positions (list<tuple<int, int>>): A list of positions of letters
        """
        word = Word()
        for position in sorted(positions):
            word[position] = self.get_letter(position)

        return word

    def get_potential_words(self):
        """Returns a list of all words formed by the current play, on each axis

        Words must be at least MIN_WORD_LENGTH, but are not validated against a dictionary

        Return:
            dict<str, list<Word>>: Mapping of axes => list of words formed
        """
        # Find every word formed by active move, and classify as horizontal or vertical
        visited = {axis: set() for _, axis in a2_support.ALL_DELTAS}
        words = {axis: [] for _, axis in a2_support.ALL_DELTAS}

        for starting_position in set(self._active_move):
            positions = set()
            positions.add(starting_position)

            for deltas, axis in a2_support.ALL_DELTAS:

                # Ignore visited letters, as each letter can only be used for one word on each axis
                if starting_position in visited[axis]:
                    continue

                word = set()
                word.add(starting_position)

                for d_row, d_column in deltas:
                    row, column = starting_position

                    while True:
                        row += d_row
                        column += d_column

                        position = row, column
                        if not self.get_letter(position):
                            break

                        word.add(position)

                for position in word:
                    visited[axis].add(position)

                if len(word) < a2_support.MIN_WORD_LENGTH:
                    continue

                words[axis].append(word)

        return words

    def __str__(self):
        """Returns an overview of the current state of the game"""
        players = [("* " if player is self.get_active_player() else "  ") + str(player).replace('\n', '\n  ') for player
                   in self._players]

        gameover = "\n\nGAMEOVER" if self.is_game_over() else ''

        return a2_support.GAME_STRING.format(board=str(self._board), bag_count=len(self._bag),
                                             players="\n\n".join(players)) + gameover


def test_game_model():
    """Tests the game model"""
    random.seed(1337)
    game = Scrabble([
        'Ben',  # Oh no, how can I win?
        'Yilmaz',  # No mercy for stooges
    ])

    game.restart()

    print(game)

    moves = [
        (
            'play',
            [6, 5, 4, 3, 2, 0],
            [(7, 7), (7, 5), (7, 4), (7, 6), (7, 3), (7, 2)]
        ),
        (
            'play',
            [6, 5, 3, 2, 1],
            [(6, 4), (9, 4), (8, 4), (11, 4), (10, 4)]
        ),
        (
            'play',
            [6, 1, 0],
            [(9, 6), (10, 6), (8, 6)]
        ),
        (
            'swap',
            [5, 3, 2],
            None
        ),
        (
            'skip',
            None,
            None
        ),
        (
            'skip',
            None,
            None
        )
    ]

    for move, indices, positions in moves:

        # player = game.get_active_player()
        # all_letters = player.get_tiles()

        if move == 'play':
            for index, position in zip(indices, positions):
                game.place_letter(index, position)

            game.confirm_place()
        elif move == 'swap':
            game.swap_letters(indices)
        elif move == 'skip':
            game.skip()
        print(game)
