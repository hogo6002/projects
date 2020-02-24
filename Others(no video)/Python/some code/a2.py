"""
CSSE1001 Assignment 2
Semester 2, 2017
"""

# Import statements go here
import a2_support

# Fill these in with your details
__author__ = "Wenxin Gong (s4452873)"
__email__ = ""
__date__ = ""

# Write your classes here


class Tile(object):
    def __init__(self, letter, score):
        """
        The Tile class is used to represent a regular Scrabble tile.

        Parameters:
            Instances of Tile should be initialized with Tile(letter, score).
        """
        self._letter = letter
        self._score = score

    def get_letter(self):
        """Returns the letter of the tile."""
        return self._letter

    def get_score(self):
        """Returns the base score of the tile."""
        return self._score

    def __str__(self):
        """Returns a human readable string, of the form {letter}:{score}"""
        return "{0}:{1}".format(self.get_letter(), self.get_score())

    def __repr__(self):
        """Returns Same as __str__"""
        return self.__str__()

    def reset(self):
        """Returns nothing (this method will be overridden in the following subclass)"""
        pass

class Wildcard(Tile):
    def __init__(self, score):
        """
        The Wildcard class is used to represent a wildcard Scrabble tile.The user can choose the letter this tile represents when they play it on the board.

        Parameters:
            Wildcard inherits from Tile and should be initialized with Wildcard(score)
        """

        super().__init__(self, score)
        self._letter = "?"

    def set_letter(self, letter):
        """Sets the letter of the tile"""
        self._letter = letter
        return self._letter

    def reset(self):
        """Resets this tile back to its wildcard state"""
        self._letter = "?"

class Bonus(object):
    """Bonus is a very simple superclass that is used to represent a generic bonus."""
    
    def __init__(self, value):
        """Constructor for Bonus should take a single argument, value, which is the value of this bonus. """
        self._value = value

    def get_value(self):
        """Returns the value of this bonus"""
        return self._value

    def __str__(self):
        """Returns a human readable string, of the form {type}{value}, where type is W for WordBonus & L for LetterBonus"""
        pass

class WordBonus(Bonus):
    def __str__(self):
        """Returns a human readable string, of the form {type}{value}, where type is W for WordBonus"""
        return "{0}{1}".format("W", self.get_value())

class LetterBonus(Bonus):
    def __str__(self):
        """Returns a human readable string, of the form {type}{value}, where type is L for LetterBonus"""
        return "{0}{1}".format("L", self.get_value())

class Player(object):
    """The Player class represents a player and their rack of tiles."""
    
    def __init__(self, name):
        """The constructor for Player should take a single argument, the player's name. """
        self._name = name
        self._rack = []
        self._score = 0

    def get_name(self):
        """Return's the player's name"""
        return self._name

    def add_tile(self, tile):
        """Adds a tile to the player's rack"""
        self._rack.append(tile)
        
    def remove_tile(self, index):
        """Removes and returns the tile at index from the player's rack"""
        return self._rack.pop(index)

    def get_tiles(self):
        """Removes and returns the tile at index from the player's rack"""
        return self._rack

    def get_score(self):
        """Return's the player's score"""
        return self._score

    def add_score(self, score):
        """ Adds score to the player's total score"""
        self._score += score

    def get_rack_score(self):
        """Returns the total score of all letters in the player's rack"""
        score = 0
        for tile in self._rack:
            score += tile.get_score()
        return score
        
    def reset(self):
        """Resets the player for a new game, emptying their rack and clearing their score"""
        self._rack = []
        self._score = 0

    def __contains__(self, tile):
        """ Returns True iff the player has tile in their rack"""
        if tile in self._rack:
            return True
        else:
            return False
        
    def __len__(self):
        """Returns the number of letters in the player's rack"""
        return len(self._rack)

    def __str__(self):
        """Returns a string representation of this player and their rack, of the form {name}:{score}\n{tiles}, where tiles is a comma (&space) separated list of all the tiles in the player's rack, in order"""
        tiles = self.get_tiles()
        return "{0}:{1}\n{2}".format(self.get_name(), self.get_score(), ', '.join("%s" % tile for tile in tiles))


class TileBag(object):
    """The TileBag class is used to hold Scrabble tiles."""
    
    def __init__(self, data):
        """TileBag's constructor takes a single argument, a data dictionary whose keys are letters (lowercase) and whose values are pairs of (count, score)"""
        self._data = data
        self._list = []
        for key in self._data:
            count = self._data[key][0]
            while count > 0:
                count -= 1
                self._list.append(Tile(key, self._data[key][1]))

    def get_list(self):
        return self._list
    
    def __len__(self):
        """Returns the number of tiles remaining in the bag"""
        return len(self._list)

    def __str__(self):
        """Returns a human readable string, of each tile joined by a comma and a space; i.e. "b:3, o:1, o:1, m:3" — the order the tiles are displayed does not matter"""
        return "{0}".format(self.get_list())
    
    def draw(self):
        """Draws and returns a random tile from the bag"""
        from random import choice
        return choice(self._list)
    
    def drop(self, tile):
        """Drops a tile into the bag"""
        self._list.append(tile)

    def shuffle(self):
        """Shuffles the bag"""
        import random
        random.shuffle(self._list)

    def reset(self):
        """efills the bag and shuffles it, ready for a new game"""
        self._list = []
        for key in self._data:
            count = self._data[key][0]
            while count > 0:
                count -= 1
                self._list.append(Tile(key, self._data[key][1]))
        self.shuffle()
        
class Board(object):
    """The Scrabble tiles can be played on the Board class. It also keeps track of which cells have bonuses."""
    def __init__(self, size, word_bonuses, letter_bonuses, start):
        """A Board should be initialized with Board(size, word_bonuses, letter_bonuses, start)

            Parameters:
                size is the number of rows/columns on the board (i.e. 15)
                word_bonuses is a dictionary with scale of word bonuses as the key, and a list of positions where this scale occurs (see the examples below)
                lecture_Aug_8etter_bonuses as with word_bonuses above, but with letter bonuses instead
                start is the (row, column) position of the starting cell
        """
        self._size = size
        self._word_bonuses = word_bonuses
        self._letter_bonuses = letter_bonuses
        self._start = start
        self._TileDict = {}
        self._all_bonuses = {}

    def get_start(self):
        """Returns the starting position"""
        return self._start

    def get_size(self):
        """Returns the number of (rows, columns) on the board"""
        return (self._size, self._size)

    def is_position_valid(self, position):
        """Returns True iff the position is valid (i.e. it is on the board)"""
        x, y = position
        a = self.get_size()[0]
        if x <= a:
            if y <= a:
                return True
            else:
                return False
        else:
                return False

    def get_bonus(self, position):
        """Returns the bonus for a position on the board, else None if there is no bonus (Return type: Bonus)"""
        for i in self._word_bonuses.keys():
            for c in self._word_bonuses[i]:
                if position == c:
                    return WordBonus(i)
        for i in self._letter_bonuses.keys():
            for c in self._letter_bonuses[i]:
                if position == c:
                    return LetterBonus(i)

    def get_all_bonuses(self):
        """Returns a dictionary of all bonuses, keys being positions and values being bonuses (Return type: dict<tuple<int, int>, Bonus>)"""
        for i in self._word_bonuses.keys():
            list1 = WordBonus(i)
            for c in self._word_bonuses[i]:
                self._all_bonuses.setdefault(c, WordBonus(i))
        for i in self._letter_bonuses.keys():
            for c in self._letter_bonuses[i]:
                self._all_bonuses.setdefault(c, LetterBonus(i))
        return self._all_bonuses
    
    def get_tile(self, position):
        """Returns the tile at position, else None if no tile has been placed there yet (Return type: Tile)"""
        return self._TileDict.get(position, None)

    def place_tile(self, position, tile):
        """Places a tile at position; raises an IndexError if position is invalid"""
        self._position = position
        if self.is_position_valid(self._position) == False:
            raise IndexError("Position is false.")
        for c in self._TileDict.keys():
            if position == c:
                self._TileDict[c] = tile
        self._TileDict.setdefault(self._position, tile)
            
    def __str__(self):
        """ Returns a human readable representation of the game board"""
        rows, columns = self.get_size()
        table = '-' * 51
        for row in range(rows):
            new_row = "\n|"
            for column in range(columns):
                if self.get_bonus((row, column)) == None:
                    str_bonus = "  "
                else:
                    str_bonus = str(self.get_bonus((row, column)))
                if self.get_tile((row, column)) != None and len(str(tile)) == 3:
                    str_tile = str(self.get_tile((row, column))) + " "
                else:
                    str_tile = str(self.get_tile((row, column)))
                new_row += (" " + str_tile + " " + str_bonus + " |")
            table += (new_row + "\n" + '-' * 51)
        return table
    
    def reset(self):
        """Resets the board for a new game"""
        self._TileDict = {}
        
