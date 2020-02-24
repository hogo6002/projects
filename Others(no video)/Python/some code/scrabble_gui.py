"""
Simple Scrabble GUI
CSSE1001 Assignment 2
Semester 2, 2017
"""

import tkinter as tk
from tkinter import font
from tkinter import messagebox
from tkinter import simpledialog
from enum import Enum
from collections import ChainMap

# from a2 import Bonus, Player, TileBag, Board
# from scrabble import Word, WordBook
from a2 import WordBonus, LetterBonus, Tile, Wildcard
from scrabble import Scrabble

import a2_support as support
from ee import EventEmitter

__author__ = "Benjamin Martin"
__version__ = "1.0.1"
__date__ = "14/08/2017"

# Some helpful controls
# If true, simple errors in the model do not crash the GUI
# For better error messages, set this to False
GRACEFUL_ERRORS = True

# Shows splash screen iff True
SHOW_SPLASH_SCREEN = True

# Names for the players
# If an empty list is given, the user is prompted to enter names
# Uncomment to skip prompting
PLAYER_NAMES = [
    'Michael Scott',
    'Jim Halpert',
    'Dwight Schrute'
]

# String for displaying a tile (letter, score)
# Linux users may need to replace with a single space on each line
TILE_STRING = "{}      \n      {}"

# The base unit of the GUI; decrease this to reduce the overall size of the GUI
UNIT = 5

# Sizes derived from unit
TILE_BORDER = UNIT
TILE_SIZE = UNIT * 6
CELL_SIZE = TILE_SIZE + TILE_BORDER
FONT_SIZE = UNIT * 2

RACK_SCALE = 2

SPLASH_SCREEN_SCALE = 2

# Sourced from http://colorpalettes.net/
VIBRANT_COLOURS = {
    "red": "#a30e15",
    "orange": "#e67e22",
    "yellow": "#f9bf3b",
    "green": "#5d8402",
    "lime": "#bacb4d",
    "blue": "#508ebf",
    "dark_blue": "#1f3a93",
    "pale_blue": "#ccdbea",
    "pink": "#d77082",
    "purple": "#643d6e",
    "brown": "#96411b",
    "cream": "#fceee3",
    "blue_purple": "#4a2f48",
    "beige": "#aea38c",
    "grey": "#aeb8b8",
    "white": "#ffffff",
    "dark_grey": "#333333"
}


class GridView(EventEmitter, tk.Canvas):
    """Canvas which displays a grid of tiles representing the Scrabble game board"""

    # Sourced from http://colorpalettes.net/
    COLOURS = {
        "blank": VIBRANT_COLOURS["beige"],
        "tile": VIBRANT_COLOURS["cream"],
        "triple_word": VIBRANT_COLOURS["red"],
        "double_word": VIBRANT_COLOURS["pink"],
        "triple_letter": VIBRANT_COLOURS["dark_blue"],
        "double_letter": VIBRANT_COLOURS["pale_blue"]
    }

    FONT_COLOURS = {
        "blank": "black",
        "tile": "black",
        "triple_word": "white",
        "double_word": "black",
        "triple_letter": "white",
        "double_letter": "black"
    }

    LABELS = {
        "triple_word": "Word\nx3",
        "double_word": "Word\nx2",
        "triple_letter": "Letter\nx3",
        "double_letter": "Letter\nx2",
        "start": "Start!",
        "blank": ""
    }

    def __init__(self, master, size, cell_size=(CELL_SIZE, CELL_SIZE), tile_size=(TILE_SIZE, TILE_SIZE),
                 border=(TILE_BORDER, TILE_BORDER), font_size=FONT_SIZE, scale=1, colours=None, **kwargs):
        """
        Constructs a GridView based off a tkinter parent

        Parameters:
            master (tk.Frame|tk.Tk|tk.Toplevel): The parent widget
            size (tuple<int, int>): The (row, column) size of the board
            cell_size (tuple<int, int>): The size of each cell in pixels
            tile_size (tuple<int, int>): The size of each tile in pixels
            border (tuple<int, int>): Size of the gap between cells in pixels
            font_size (tuple<int, int>): The size of the font, in points
            scale (int): Factor by which to scale each size (defaults to 1)
            colours (dict): Map between the tile type and the colour to display
                            Extends COLOURS property on class
            kwargs (dict): Any other keyword arguments for the Frame constructor
        """
        # Set dimensions
        self.size = size
        self.cell_size = tuple(i * scale for i in cell_size)
        self.tile_size = tuple(i * scale for i in tile_size)
        self.border = tuple(i * scale for i in border)
        self.offsets = self._calculate_offsets()
        self._font_size = font_size * scale

        # Override default colours
        if colours is None:
            colours = {}
        colours = ChainMap(colours, self.COLOURS)

        # Super inits
        EventEmitter.__init__(self)

        width, height = self.calculate_size()
        tk.Canvas.__init__(self, master, width=width, height=height,
                           **kwargs, highlightthickness=0)
        self._master = master

        self._tiles = {}
        self._cells = {}
        self._colours = colours

        self._cell_texts = {}
        self._tile_texts = {}

        self.bind("<Button-1>", self._handle_click)

    def calculate_size(self):
        """(tuple<int, int>) Returns the widget's required xy dimensions."""
        rows, columns = self.size
        cell_x, cell_y = self.cell_size
        pad_x, pad_y = self.border
        width = columns * cell_x + (columns + 1) * pad_x
        print(columns, cell_x, pad_x)
        height = rows * cell_y + (rows + 1) * pad_y

        return width, height

    def _calculate_offsets(self):
        """Calculates the offsets between each cell for showing connections."""
        x, y = self.border

        return (
            ((0, y), (0, 0), (0, -y)),
            ((x, 0), (0, 0), (-x, 0))
        )

    def xy_to_rc(self, xy_position):
        """(tuple<int, int>) Converts xy position into row-column position."""
        x, y = xy_position
        cell_x, cell_y = self.cell_size
        pad_x, pad_y = self.border

        column = x // (pad_x + cell_x)
        on_column_padding = x % (pad_x + cell_x) < pad_x
        row = y // (pad_y + cell_y)
        on_row_padding = y % (pad_y + cell_y) < pad_y

        if on_column_padding or on_row_padding:
            return None

        return row, column

    def _handle_click(self, event):
        """Handle a mouse click on the game board by starting a move

        Parameters:
            event (tk.MouseEvent): The event caused by a mouse click
        """

        position = self.xy_to_rc((event.x, event.y))

        if position is None:
            return

        self.emit('select', position)

    def calculate_bounds(self, position):
        """Calculates the bounds of a tile at the given position in the board

        Parameters:
            position (tuple<int, int>): The (row, column) position of the tile

        Return:
            (tuple<int, int, int>): The top left, middle and bottom right
                                    position of the tile on the GridView
        """

        row, column = position

        cell_x, cell_y = self.cell_size
        pad_x, pad_y = self.border

        top = row * (pad_y + cell_y) + pad_y
        left = column * (pad_x + cell_x) + pad_x
        bottom = top + cell_y
        right = left + cell_x

        top_left = left, top
        bottom_right = right, bottom

        middle = left + cell_x // 2, top + cell_y // 2

        return top_left, middle, bottom_right

    def draw_board(self, start, bonuses):
        """Draws the board

        Parameters:
            start (tuple<int, int>): The starting position
            bonuses (dict<tuple<int, int>, model.Bonus>): Mapping of cell positions to corresponding bonuses
        """
        rows, columns = self.size

        for row in range(rows):
            for column in range(columns):
                position = row, column
                self.draw_cell(position, bonuses.get(position), start=position == start)

    def draw_cell(self, position, bonus=None, start=False):
        """Draws a tile at the given position

        Parameters:
            position (tuple<int, int>): The position of the tile
            bonus (model.Bonus): The bonus to draw, if any. Defaults to None
            start (bool): Is the cell a starting tile?

        Precondition:
            position is a valid position on the board
        """

        if bonus is None:
            tile_type = "blank"
        elif isinstance(bonus, LetterBonus):
            tile_type = "double_letter" if bonus.get_value() == 2 else "triple_letter"
        elif isinstance(bonus, WordBonus):
            tile_type = "double_word" if bonus.get_value() == 2 else "triple_word"
        else:
            raise KeyError("Unknown bonus")

        colour = self._colours[tile_type]

        top_left, middle, bottom_right = self.calculate_bounds(position)

        if self._cells.get(position) is None:
            tile_id = self.create_rectangle(*top_left, *bottom_right,
                                            fill=colour, outline=colour)
            self._cells[position] = tile_id
        else:
            self.itemconfig(self._cells[position], fill=colour, outline=colour)

        font_colour = self.FONT_COLOURS[tile_type]
        if start:
            tile_type = 'start'
        self._cell_texts[position] = self.create_text(*middle, text=self.LABELS[tile_type], justify=tk.CENTER,
                                                      fill=font_colour, font=font.Font(size=self._font_size))

    def draw_tile(self, position, tile):
        """Draws a tile on the board

        Parameters:
            position (tuple<int, int>): The cell position at which to draw the tile
            tile (model.Tile): The tile to draw
        """

        tile_type = 'tile'
        colour = self._colours[tile_type]

        if tile is None:
            return self.delete(self._tiles.pop(position), self._tile_texts.pop(position))

        top_left, middle, bottom_right = self.calculate_bounds(position)

        if self._tiles.get(position) is None:
            tile_id = self.create_rectangle(*top_left, *bottom_right,
                                            fill=colour, outline=colour)
            self.tag_raise(tile_id)
            self._tiles[position] = tile_id
        else:
            self.itemconfig(self._tiles[position], fill=colour, outline=colour)

        font_colour = self.FONT_COLOURS[tile_type]
        text = TILE_STRING.format(tile.get_letter(), tile.get_score())
        self._tile_texts[position] = self.create_text(*middle,
                                                      text=text, justify=tk.CENTER,
                                                      fill=font_colour, font=font.Font(size=self._font_size))

    def undraw_all_tiles(self):
        """Undraws all tiles from the board, but leaves cells as is"""
        for _, tag in self._tiles.items():
            self.delete(tag)
        for _, tag in self._tile_texts.items():
            self.delete(tag)

        self._tiles = {}
        self._tile_texts = {}


class RackView(GridView):
    """Special grid view to display a player's rack of letters"""

    def __init__(self, master, width=support.MAX_LETTERS, scale=RACK_SCALE,
                 active_colour=VIBRANT_COLOURS['grey']):
        """Constructor

        Parameters:
            master (tk.Frame|tk.Tk|tk.Toplevel): The parent widget
            width (int): The number of tiles in the rack
            scale (int): The scale by which to multiply all sizes for the rack
             active_colour (str): The colour to highlight active tiles
        """
        super().__init__(master, size=(1, width), scale=scale)

        self._active_colour = active_colour

    def deactivate_tile(self, index):
        """Deactivates the tile at position

        Parameters:
            index (int): The zero-based index of the tile to deactivate
        """
        colour = self._colours['tile']
        self.itemconfig(self._tiles[(0, index)], fill=colour, outline=colour)

    def activate_tile(self, index):
        """Activates the tile at position

        Parameters:
            index (int): The zero-based index of the tile to activate
        """
        colour = self._active_colour
        self.itemconfig(self._tiles[(0, index)], fill=colour, outline=colour)


class PlayerView(tk.Frame):
    """Simple widget to display a player and interact with their tiles"""

    def __init__(self, master):
        """Constructor
        
        Parameters:
            master (tk.Frame|tk.Tk|tk.Toplevel): The parent widget
        """
        super().__init__(master)

        self._label = tk.Label(master)
        self._label.pack()

        self.rack = RackView(master)
        self.rack.pack()

    def draw_player(self, player):
        """Draws a player
        
        Parameters:
            player (Player): The player to draw
        """
        self._label.config(text="Current player is {}".format(player.get_name()))
        letters = player.get_tiles()

        self.rack.undraw_all_tiles()  # Lazy hack, but it works
        for i, letter in enumerate(letters):
            self.rack.draw_tile((0, i), letter)


class TurnControls(EventEmitter, tk.Frame):
    """Simple widget to manage actions a player can make each turn"""

    def __init__(self, master, labels):
        """
        Constructor
        
        Parameters:
            master (tk.Frame|tk.Tk|tk.Toplevel): The parent widget
             
        """
        EventEmitter.__init__(self)
        tk.Frame.__init__(self, master)

        labels = list(labels)

        self._labels = {key: label for key, label in labels}

        self._buttons = {}

        self._mode_label = tk.Label(self)
        self._mode_label.pack()

        self._button_frame = tk.Frame(self)
        self._button_frame.pack()

        for tag, label in labels:
            button = tk.Button(self._button_frame, text=label, command=lambda tag=tag: self.emit('action', tag))
            button.pack(side=tk.LEFT)
            self._buttons[tag] = button


class PlayerActions(Enum):
    """Actions a player can take on their turn"""
    # For simplicity, use labels as unique ids since l18n is not needed
    PLAY = 'Play'
    SWAP = 'Swap'
    PASS = 'Pass'
    RESET = 'Reset'


class ScrabbleApp:
    """Top-level app class for Scrabble GUI"""

    def __init__(self, master, splash_screen=SHOW_SPLASH_SCREEN):
        """Constructor
        
        Parameters:
            master (tk.Frame|tk.Tk|tk.Toplevel): The parent widget
        """
        self._master = master

        master.title("Scrabble!")

        self._splash_screen = None

        if splash_screen:
            self._splash_screen = SplashScreen(master, self._init_game)
        else:
            self._init_game()

    def _init_game(self):
        """Initializes the game"""
        if self._splash_screen:
            self._splash_screen.destroy()

        ScrabbleGame(self._master)


class ScrabbleGame:
    """Top-level game class for Scrabble GUI"""

    LABELS = [
        PlayerActions.PLAY,
        PlayerActions.SWAP,
        PlayerActions.PASS,
        PlayerActions.RESET
    ]

    def __init__(self, master):
        self._master = master

        master.title("Scrabble!")

        self._top = tk.Frame(self._master)
        self._top.pack(expand=True, fill=tk.BOTH)

        self._left = tk.Frame(self._top)
        self._left.pack(side=tk.LEFT, expand=True, fill=tk.BOTH)

        self._right = tk.Frame(self._top)
        self._right.pack(side=tk.RIGHT, fill=tk.BOTH)

        self._get_names()

        self._view = GridView(self._left, self._game.get_board().get_size())
        self._view.pack()
        self.draw_board()

        self._players = tk.Label(self._right)
        self._players.pack()

        self.draw_players_view()

        self._player_view = PlayerView(self._left)
        self._player_view.pack()

        self._player_view.draw_player(self._game.get_active_player())

        self._active_tiles = set()
        self._player_view.rack.on('select', self._handle_rack_click)
        self._view.on('select', self._handle_board_click)

        self._controls = TurnControls(self._right, labels=((action, action.value) for action in PlayerActions))
        self._controls.pack(side=tk.BOTTOM)
        self._controls.on('action', self._handle_action)

        # menu
        menubar = tk.Menu(self._master)
        filemenu = tk.Menu(menubar, tearoff=False)
        filemenu.add_command(label="New Game", command=self.reset)
        menubar.add_cascade(label="File", menu=filemenu)

        self._master.config(menu=menubar)

    def _get_names(self):
        """Initializes the game by asking for the player's names"""
        i = 0
        while True:
            if i == 0:
                names = PLAYER_NAMES
            else:
                names = []

            if len(names) == 0:
                while True:
                    num = len(names) + 1
                    name = simpledialog.askstring("Player {}".format(num),
                                                  "Please enter the name of player {} (or leave blank to finish)"
                                                  .format(num))

                    if name is None:
                        exit()
                    name = name.strip()

                    if not name:
                        break

                    if name in names:
                        messagebox.showerror("Bad Name", f"{name} is already registered as a player")
                        continue
                    names.append(name)

            try:
                self._game = Scrabble(names)
                return
            except support.ScrabbleError as error:
                messagebox.showerror("Error Starting Game", str(error))

            i += 1

    def reset(self):
        """Resets the game to a blank state; does not re-ask for player's names"""
        if not messagebox.askokcancel("Restart?", "Are you sure you want to start a new game?"):
            return
        self._game.restart()
        self._view.undraw_all_tiles()
        self._player_view.draw_player(self._game.get_active_player())
        self._active_tiles = set()

    def toggle_tile(self, index):
        """Toggles a tile in the active player's rack

        Parameters:
            index (int): The index of the tile to toggle, zero being the left-most            
        """
        if index in self._active_tiles:
            self._active_tiles.remove(index)
            self._player_view.rack.deactivate_tile(index)
        else:
            self._active_tiles.add(index)
            self._player_view.rack.activate_tile(index)

    def _handle_action(self, action):
        """Handles the current player's action"""
        if self._game.is_game_over():
            return

        if action == PlayerActions.PLAY:
            try:
                scores = self._game.confirm_place()

                if len(scores) == 0:
                    return messagebox.showerror("Invalid Move",
                                                f"You must form at least one {support.MIN_WORD_LENGTH}-letter word")
            except support.ActionError as error:
                return messagebox.showerror("Invalid Word", str(error))

        elif action == PlayerActions.SWAP:
            if len(self._game.get_play()) == 0 and \
                    not messagebox.askokcancel("Confirm Swap", "Are you sure you want to swap {} letter{}?".format(
                            len(self._active_tiles), 's' if len(self._active_tiles) != 1 else ''
                    )):
                return

            try:
                self._game.swap_letters(self._active_tiles)
                self._active_tiles = set()
            except support.ActionErrorsupport.ActionError as error:
                return messagebox.showerror("Invalid Action", str(error))
        elif action == PlayerActions.PASS:
            self._game.skip()
        elif action == PlayerActions.RESET:
            for position, _ in self._game.clear_play():
                self._view.draw_tile(position, None)

            self._active_tiles = set()
        else:
            # This should never happen, but as a precaution
            raise RuntimeError("Unknown event {}".format(action))

        if self._game.is_game_over():
            rankings = self._game.get_rankings()
            winners = []
            score = rankings[0].get_score()

            for player in rankings:
                if player.get_score() < score:
                    break

                winners.append(player.get_name())

            winner = " & ".join(winners)

            messagebox.showinfo("Game Over", f"Congratulations to {winner} for winning, with {score} points!")

            return

        self._player_view.draw_player(self._game.get_active_player())
        self.draw_players_view()

    def _handle_rack_click(self, position):
        """Handles a click on the player's tile rack
        
        Parameters:
            position (tuple<int, int>): Row, column position of the tile that was clicked
                                        For single row racks, this is interpreted as (0, index)
        """
        if self._game.is_game_over():
            return

        _, index = position

        self.toggle_tile(index)

    def _handle_board_click(self, position):
        """Handles a click on the board
        
        Parameters:
            position (tuple<int, int>): Row, column position of the tile that was clicked
        """

        if self._game.is_game_over():
            return

        player = self._game.get_active_player()

        if len(self._active_tiles) == 0:
            if not self._game.is_position_active(position):
                return
            # replace tile to player's rack
            self._game.pickup_letter(position)
            self._view.draw_tile(position, None)
        elif len(self._active_tiles) > 1:
            return messagebox.showwarning("Multiple Tiles Selected", "Only one tile can be placed at a time")
        else:  # place active tile

            # Ignore placement on existing tile
            if self._game.get_letter(position):
                return

            tile = self._active_tiles.pop()

            try:
                letter = self._game.place_letter(tile, position)

                if isinstance(letter, Wildcard):
                    while True:
                        char = simpledialog.askstring("Wildcard", "Please choose a letter for this wildcard")

                        if char is None:
                            self._game.pickup_letter(position)
                            return self._active_tiles.add(tile)

                        char = char.strip().upper()

                        if len(char) == 1 and char.isalpha():
                            letter.set_letter(char)
                            break

                self._view.draw_tile(position, letter)
            except support.PositionError as error:
                self._active_tiles.add(tile)
                return messagebox.showerror("Invalid Move", str(error))

        # redraw player_view
        self._player_view.draw_player(player)

    def draw_board(self):
        """Draws the board, start tile & all bonuses"""
        board = self._game.get_board()
        self._view.draw_board(board.get_start(), board.get_all_bonuses())

    def draw_players_view(self):
        """Draws the players view"""
        output = ["Players:"]

        for player in self._game.get_players():
            output.append("{:<20} {:>4}".format(player.get_name(), player.get_score()))

        self._players.config(text=("\n\n").join(output))


class SplashScreen:
    """A splash screen for scrabble that can fail graciously, for the students' sake"""

    def __init__(self, master, play_command=lambda: None):
        """Constructor

        Parameters:
            master (tk.Tk|tk.Toplevel): The root window
            play_command (function<>): The function to call when the user clicks the play game button
        """
        self.size = 5, 8
        self.border = 0, 0

        self._view = GridView(master, self.size, scale=SPLASH_SCREEN_SCALE)
        self._view.pack()

        self._btn = tk.Button(master, text="Play Game!", command=play_command)
        self._btn.pack()

        self.show_tiles()
        self.show_bonuses()

    def show_tiles(self):
        """Shows the tiles"""
        rows, _ = self.size

        tiles = [
            ('?', 0),
            ('C', 3),
            ('R', 1),
            ('A', 1),
            ('B', 3),
            ('B', 3),
            ('L', 1),
            ('E', 1)
        ]

        for i, (letter, score) in enumerate(tiles):
            try:
                if letter == support.WILDCARD_CHAR:
                    tile = Wildcard(score)
                else:
                    tile = Tile(letter, score)

                self._view.draw_tile((rows // 2, i), tile)

            except Exception as error:  # pylint: disable=broad-except
                if GRACEFUL_ERRORS:
                    print(error, f"tile={letter}:{score}")
                else:
                    raise error

    def show_bonuses(self):
        """Shows the bonuses"""
        dr, dc = self.border  # pylint: disable=invalid-name
        rows, columns = self.size

        bonuses = {
            (dr, dc): ('w', 2),
            (dr, columns - 1 - dc): ('w', 3),
            (rows - 1 - dr, dc): ('l', 2),
            (rows - 1 - dr, columns - 1 - dc): ('l', 3),
        }

        for position, (bonus_type, value) in bonuses.items():
            try:
                bonus = (WordBonus if bonus_type == 'w' else LetterBonus)(value)

                self._view.draw_cell(position, bonus)

            except Exception as error:  # pylint: disable=broad-except
                if GRACEFUL_ERRORS:
                    print(error, f"bonus={type} * {value}")
                else:
                    raise error

    def destroy(self):
        """Destroys the splash screen"""
        self._view.pack_forget()
        self._btn.pack_forget()


def test():
    """Runs a test game"""
    import random
    # random.seed(1337)
    random.seed(1)
    main()


def main():
    """Instantiates the GUI controller class"""
    root = tk.Tk()
    ScrabbleApp(root, splash_screen=SHOW_SPLASH_SCREEN)
    root.mainloop()


if __name__ == "__main__":
    main()
