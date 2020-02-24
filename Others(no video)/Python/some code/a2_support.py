"""
Support Code for Scrabble Model
CSSE1001 Assignment 2
Semester 2, 2017
"""

__author__ = "Benjamin Martin"
__version__ = "1.0.1"
__date__ = "14/08/2017"

################################################################################
############################### Useful constants ###############################
################################################################################

# Board size & starting position
BOARD_DIMENSION = 15
START_POSITION = 7, 7

# Minimum length of a word
MIN_WORD_LENGTH = 2

# Maximum number of letters per player
MAX_LETTERS = 7

# Bonus points for using all letters at once
ALL_LETTER_BONUS = 50

# Acceptable number of players
MIN_PLAYERS = 1
MAX_PLAYERS = 4

# The wildcard character
WILDCARD_CHAR = '?'

# Letters
LETTERS = {
    # letter: (count, score)
    '?': (2, 0),
    'A': (9, 1),
    'B': (2, 3),
    'C': (2, 3),
    'D': (4, 2),
    'E': (12, 1),
    'F': (2, 4),
    'G': (3, 2),
    'H': (2, 4),
    'I': (9, 1),
    'J': (1, 8),
    'K': (1, 5),
    'L': (4, 1),
    'M': (2, 3),
    'N': (6, 1),
    'O': (8, 1),
    'P': (2, 3),
    'Q': (1, 10),
    'R': (6, 1),
    'S': (4, 1),
    'T': (6, 1),
    'U': (4, 1),
    'V': (2, 4),
    'W': (2, 4),
    'X': (1, 8),
    'Y': (2, 4),
    'Z': (1, 10)
}

################################################################################
#           The following are used by the provided modelling classes           #
################################################################################

# Format of the game string
GAME_STRING = """
{board}

{bag_count} letters remaining

Players:
{players}
""".strip()

# Simple named constants for each axis
HORIZONTAL = 'horz'
VERTICAL = 'vert'

# Change in position for words on each axis
ALL_DELTAS = (
    (((-1, 0), (1, 0)), VERTICAL),  # up & down
    (((0, -1), (0, 1)), HORIZONTAL)  # left & right
)


################################################################################
#                              Custom exceptions                               #
################################################################################

class ScrabbleError(Exception):
    """A generic Scrabble error"""


class ActionError(ScrabbleError):
    """An error with a high-level action in a turn"""


class PositionError(ScrabbleError):
    """An error with a position on the Scrabble board (i.e. invalid)"""


################################################################################
#                           Positions of all bonuses                           #
################################################################################

DOUBLE_WORD_BONUSES = [(4, 10), (12, 2), (13, 1), (3, 3), (10, 4), (10, 10), (11, 11), (1, 13), (13, 13), (4, 4),
                       (12, 12), (3, 11), (7, 7), (11, 3), (2, 12), (2, 2), (1, 1)]

TRIPLE_WORD_BONUSES = [(7, 14), (0, 0), (7, 0), (0, 14), (14, 7), (0, 7), (14, 0), (14, 14)]

DOUBLE_LETTER_BONUSES = [(14, 11), (2, 6), (8, 6), (6, 8), (8, 2), (6, 6), (3, 0), (2, 8), (8, 12), (12, 6), (11, 0),
                         (0, 11), (6, 2), (8, 8), (11, 14), (14, 3), (6, 12), (0, 3), (3, 14), (12, 8)]

TRIPLE_LETTER_BONUSES = [(5, 9), (9, 13), (5, 5), (9, 1), (5, 13), (9, 5), (9, 9), (13, 5), (1, 5), (1, 9), (13, 9),
                         (5, 1)]

WORD_BONUSES = {
    2: DOUBLE_WORD_BONUSES,
    3: TRIPLE_WORD_BONUSES
}

LETTER_BONUSES = {
    2: DOUBLE_LETTER_BONUSES,
    3: TRIPLE_LETTER_BONUSES
}
