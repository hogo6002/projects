#!/usr/bin/env python3
from testrunner import DEFAULTS, PythonTestCase, TestMaster, create_naming_test_case, create_docstring_test_case

# Camel case has been used for test cases to maintain readability while removing need for adding title to each test

__version__ = "1.0.0"

DEFAULTS["SCRIPT"] = "a2"
DEFAULTS["TEST_DATA"] = None
DEFAULTS["TEST_DATA_RAW"] = None
DEFAULTS["HIDE_TRACEBACK_PATHS"] = True
DEFAULTS["VERSION"] = "2017s2"


class TileTests(PythonTestCase):
    def testMethodsDefined(self):
        self.assertMethodDefined(self._module.Tile, "__init__", 3)
        self.assertMethodDefined(self._module.Tile, "get_letter", 1)
        self.assertMethodDefined(self._module.Tile, "get_score", 1)
        self.assertMethodDefined(self._module.Tile, "reset", 1)

    def testGetLetter(self):
        self.assertEqual(self._module.Tile('a', 10).get_letter(), 'a')
        self.assertEqual(self._module.Tile('z', 0).get_letter(), 'z')

    def testGetScore(self):
        self.assertEqual(self._module.Tile('a', 10).get_score(), 10)
        self.assertEqual(self._module.Tile('z', 0).get_score(), 0)

    def testStrMethod(self):
        self.assertEqual(self._module.Tile('a', 10).__str__(), "a:10")
        self.assertEqual(self._module.Tile('z', 0).__str__(), "z:0")

    def testReprMethod(self):
        # Same as __str__
        self.assertEqual(self._module.Tile('a', 10).__repr__(), "a:10")
        self.assertEqual(self._module.Tile('z', 0).__repr__(), "z:0")

    def testReset(self):
        # Should do nothing and return nothing
        self.assertEqual(self._module.Tile('a', 10).reset(), None)
        self.assertEqual(self._module.Tile('z', 0).reset(), None)


class WildcardTests(PythonTestCase):
    def testInheritance(self):
        self.assertIsSubclass(self._module.Wildcard, self._module.Tile)

    def testMethodsDefined(self):
        """ Tests methods are defined correctly """
        self.assertMethodDefined(self._module.Wildcard, "__init__", 2)
        self.assertMethodDefined(self._module.Wildcard, "get_letter", 1)
        self.assertMethodDefined(self._module.Wildcard, "get_score", 1)
        self.assertMethodDefined(self._module.Wildcard, "reset", 1)
        self.assertMethodDefined(self._module.Wildcard, "set_letter", 2)

    def testGetLetter(self):
        w = self._module.Wildcard(5)
        self.assertEqual(w.get_letter(), "?")

    def testSetLetter(self):
        w = self._module.Wildcard(5)
        w.set_letter("r")
        self.assertEqual(w.get_letter(), "r")

    def testReprMethod(self):
        # Same as __str__
        w = self._module.Wildcard(5)

        self.assertEqual(w.__repr__(), "?:5")
        w.set_letter("r")
        self.assertEqual(w.__repr__(), "r:5")

    def testReset(self):
        w = self._module.Wildcard(5)
        w.set_letter("r")
        self.assertEqual(w.__repr__(), "r:5")

        w.reset()
        self.assertEqual(w.__repr__(), "?:5")


class BonusTests(PythonTestCase):
    def testMethodsDefined(self):
        self.assertMethodDefined(self._module.Bonus, "__init__", 2)

    def testMethodsDefined(self):
        self.assertMethodDefined(self._module.Bonus, "get_value", 1)

    def testGetValue(self):
        b = self._module.Bonus(10)
        self.assertEqual(b.get_value(), 10)


class WordBonusTests(PythonTestCase):
    def testMethodsDefined(self):
        self.assertMethodDefined(self._module.WordBonus, "__init__", 2)

    def testInheritance(self):
        self.assertIsSubclass(self._module.WordBonus, self._module.Bonus)

    def testStr(self):
        w = self._module.WordBonus(10)
        self.assertEqual(w.__str__(), "W10")


class LetterBonusTests(PythonTestCase):
    def testMethodsDefined(self):
        self.assertMethodDefined(self._module.LetterBonus, "__init__", 2)

    def testInheritance(self):
        self.assertIsSubclass(self._module.LetterBonus, self._module.Bonus)

    def testStr(self):
        w = self._module.LetterBonus(2)
        self.assertEqual(w.__str__(), "L2")


class PlayerTests(PythonTestCase):
    def testMethodsDefined(self):
        self.assertMethodDefined(self._module.Player, "__init__", 2)
        self.assertMethodDefined(self._module.Player, "get_name", 1)
        self.assertMethodDefined(self._module.Player, "add_tile", 2)
        self.assertMethodDefined(self._module.Player, "remove_tile", 2)
        self.assertMethodDefined(self._module.Player, "get_tiles", 1)
        self.assertMethodDefined(self._module.Player, "get_score", 1)
        self.assertMethodDefined(self._module.Player, "add_score", 2)
        self.assertMethodDefined(self._module.Player, "get_rack_score", 1)
        self.assertMethodDefined(self._module.Player, "reset", 1)
        self.assertMethodDefined(self._module.Player, "__contains__", 2)
        self.assertMethodDefined(self._module.Player, "__len__", 1)
        self.assertMethodDefined(self._module.Player, "__str__", 1)

    def testGetName(self):
        p = self._module.Player("Bob")
        self.assertEqual(p.get_name(), "Bob")

    def testAddAndGetTile(self):
        p = self._module.Player("Bob")

        t1 = self._module.Tile('t', 1)
        p.add_tile(t1)

        self.assertEqual(p.get_tiles(), [t1])

        t2 = self._module.Tile('a', 2)
        p.add_tile(t2)

        self.assertEqual(p.get_tiles(), [t1, t2])

    def testAddAndGetScore(self):
        p = self._module.Player("Bob")

        p.add_score(10)
        self.assertEqual(p.get_score(), 10)

        p.add_score(20)
        self.assertEqual(p.get_score(), 30)

    def testGetRackScore(self):
        p = self._module.Player("Bob")

        t1 = self._module.Tile('t', 1)
        p.add_tile(t1)

        self.assertEqual(p.get_rack_score(), 1)

        t2 = self._module.Tile('a', 2)
        p.add_tile(t2)

        self.assertEqual(p.get_rack_score(), 3)

    def testRemoveTile(self):
        p = self._module.Player("Bob")

        t1 = self._module.Tile('t', 1)
        p.add_tile(t1)

        self.assertEqual(p.get_tiles(), [t1])

        p.remove_tile(0)

        self.assertEqual(p.get_tiles(), [])

    def testRemoveMultipleTiles(self):
        p = self._module.Player("Bob")

        t1 = self._module.Tile('t', 1)
        p.add_tile(t1)

        t2 = self._module.Tile('a', 2)
        p.add_tile(t2)

        t3 = self._module.Tile('b', 4)
        p.add_tile(t3)

        self.assertEqual(p.get_tiles(), [t1, t2, t3])

        p.remove_tile(1)
        self.assertEqual(p.get_tiles(), [t1, t3])

        p.remove_tile(1)
        self.assertEqual(p.get_tiles(), [t1])

        p.remove_tile(0)
        self.assertEqual(p.get_tiles(), [])

    def testLen(self):
        p = self._module.Player("Bob")

        self.assertEqual(len(p), 0)

        t1 = self._module.Tile('t', 1)
        p.add_tile(t1)

        self.assertEqual(len(p), 1)

        t2 = self._module.Tile('a', 2)
        p.add_tile(t2)

        self.assertEqual(len(p), 2)

        t3 = self._module.Tile('b', 4)
        p.add_tile(t3)

        self.assertEqual(len(p), 3)

        p.remove_tile(1)
        self.assertEqual(len(p), 2)

        p.remove_tile(1)
        self.assertEqual(len(p), 1)

        p.remove_tile(0)
        self.assertEqual(len(p), 0)

    def testContains(self):
        p = self._module.Player("Bob")

        t1 = self._module.Tile('t', 1)

        self.assertNotIn(t1, p)

        p.add_tile(t1)

        self.assertIn(t1, p)

        t2 = self._module.Wildcard(0)
        p.add_tile(t2)

        self.assertIn(t2, p)

        p.remove_tile(0)

        self.assertNotIn(t1, p)
        self.assertIn(t2, p)

    def testStr(self):
        p = self._module.Player("Bob")

        t1 = self._module.Tile('t', 1)
        p.add_tile(t1)

        self.assertEqual(str(p), "Bob:0\nt:1")

        t2 = self._module.Wildcard(0)
        p.add_tile(t2)

        self.assertEqual(str(p), "Bob:0\nt:1, ?:0")

    def testReset(self):
        p = self._module.Player("Bob")

        t1 = self._module.Tile('t', 1)
        p.add_tile(t1)
        self.assertEqual(len(p), 1)

        p.reset()
        self.assertEqual(len(p), 0)


class TilebagTests(PythonTestCase):
    def setUp(self):
        self._data = {"b": (1, 5), "z": (2, 8), "e": (5, 1)}

    def testMethodsDefined(self):
        self.assertMethodDefined(self._module.TileBag, "__init__", 2)
        self.assertMethodDefined(self._module.TileBag, "__len__", 1)
        self.assertMethodDefined(self._module.TileBag, "__str__", 1)
        self.assertMethodDefined(self._module.TileBag, "draw", 1)
        self.assertMethodDefined(self._module.TileBag, "drop", 2)
        self.assertMethodDefined(self._module.TileBag, "shuffle", 1)
        self.assertMethodDefined(self._module.TileBag, "reset", 1)

    def testLen(self):
        t = self._module.TileBag(self._data)
        self.assertEqual(len(t), 8)

        t.drop(self._module.Wildcard(0))
        self.assertEqual(len(t), 9)

        t.drop(self._module.Tile('y', 2))
        self.assertEqual(len(t), 10)

    def testStr(self):
        t = self._module.TileBag(self._data)
        output = str(t)
        # Tilebag with print str in shuffled order, so assert correct number of elements
        self.assertEqual(len(output.split(", ")), 8)

    def testDraw(self):
        data = {"b": (1, 5)}
        t = self._module.TileBag(data)

        bTile = t.draw()
        self.assertIsInstance(bTile, self._module.Tile)
        self.assertEqual(bTile.get_letter(), "b")

    def testShuffle(self):
        t = self._module.TileBag(self._data)

        before = str(t)
        t.shuffle()
        after = str(t)
        self.assertNotEqual(before, after)

    def testReset(self):
        t = self._module.TileBag(self._data)
        before = str(t)
        self.assertEqual(len(t), 8)

        t.drop(self._module.Wildcard(0))
        t.drop(self._module.Wildcard(0))
        t.drop(self._module.Wildcard(0))
        self.assertEqual(len(t), 11)

        t.reset()
        self.assertEqual(len(t), 8)

        # Test for shuffle
        after = str(t)
        self.assertNotEqual(before, after)


class BoardTests(PythonTestCase):
    def testMethodsDefined(self):
        self.assertMethodDefined(self._module.Board, "__init__", 5)
        self.assertMethodDefined(self._module.Board, "get_start", 1)
        self.assertMethodDefined(self._module.Board, "get_size", 1)
        self.assertMethodDefined(self._module.Board, "is_position_valid", 2)
        self.assertMethodDefined(self._module.Board, "get_bonus", 2)
        self.assertMethodDefined(self._module.Board, "get_all_bonuses", 1)
        self.assertMethodDefined(self._module.Board, "get_tile", 2)
        self.assertMethodDefined(self._module.Board, "place_tile", 3)
        self.assertMethodDefined(self._module.Board, "__str__", 1)
        self.assertMethodDefined(self._module.Board, "reset", 1)

    def setUp(self):
        self._word_bonuses = {2: [(2, 2)], 3: [(0, 0), (0, 4), (4, 0), (4, 4)]}
        self._letter_bonuses = {2: [(0, 3), (4, 1)], 3: [(1, 0), (3, 4)]}

        self._board = self._module.Board(5, self._word_bonuses, self._letter_bonuses, (2, 2))

    def testGetStart(self):
        self.assertEqual(self._board.get_start(), (2, 2))

    def testGetSize(self):
        self.assertEqual(self._board.get_size(), (5, 5))

    def testIsPositionValid(self):
        self.assertTrue(self._board.is_position_valid((2, 1)))
        self.assertFalse(self._board.is_position_valid((2, 8)))

    def testStr(self):
        out = ("---------------------------------------------------\n"
               "| None W3 | None    | None    | None L2 | None W3 |\n"
               "---------------------------------------------------\n"
               "| None L3 | None    | None    | None    | None    |\n"
               "---------------------------------------------------\n"
               "| None    | None    | None W2 | None    | None    |\n"
               "---------------------------------------------------\n"
               "| None    | None    | None    | None    | None L3 |\n"
               "---------------------------------------------------\n"
               "| None W3 | None L2 | None    | None    | None W3 |\n"
               "---------------------------------------------------")

        self.assertEqual(str(self._board), out)

    def testGetBonus(self):
        self.assertIsInstance(self._board.get_bonus((2, 2)), self._module.WordBonus)

        self.assertEqual(str(self._board.get_bonus((2, 2))), "W2")
        self.assertEqual(str(self._board.get_bonus((3, 4))), "L3")
        self.assertEqual(self._board.get_bonus((1, 1)), None)

    def testGetAllBonuses(self):
        self.assertEqual(len(self._board.get_all_bonuses()), 9)

    def testPlaceAndGetTile(self):
        self.assertEqual(self._board.get_tile((2, 3)), None)

        zTile = self._module.Tile("Z", 10)
        self._board.place_tile((2, 3), zTile)

        self.assertEqual(self._board.get_tile((2, 3)), zTile)

    def testReset(self):
        zTile = self._module.Tile("Z", 10)
        self._board.place_tile((2, 3), zTile)
        self._board.place_tile((1, 1), zTile)

        self._board.reset()

        # Should reset these
        self.assertEqual(self._board.get_tile((2, 3)), None)
        self.assertEqual(self._board.get_tile((1, 1)), None)


class AssignmentMaster(TestMaster):
    """ Runs the tests """

    def prepare(self):
        module = self._module

        fncs = """
        """.strip().split()

        self._tests = [
            TileTests,
            WildcardTests,
            BonusTests,
            WordBonusTests,
            LetterBonusTests,
            PlayerTests,
            TilebagTests,
            BoardTests,

            create_naming_test_case(module, functions=fncs),
            create_docstring_test_case(module, functions=fncs),
        ]

        for test_case in self._tests:
            setattr(test_case, "_module", self._module)


if __name__ == "__main__":
    test_runner = AssignmentMaster()
    test_runner.main()
