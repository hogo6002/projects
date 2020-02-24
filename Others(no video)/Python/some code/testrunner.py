#!/usr/bin/env python3

VERSION = "1.2.0"

# #############################################################################
# DEFAULTS #

# TODO: Should be able to export this
DEFAULTS = {
    "VERSION": VERSION,
    "CSSE7030": False,
    "SCRIPT": "",
    "TEST_DATA": "",
    "TEST_DATA_RAW": '',
    "MAXDIFF": 2500,
    "SHOW_VERSION": True,
    "REMOVE_TRACEBACK_DUPLICATES": True,
    "HIDE_TRACEBACK_PATHS": False,
    "USE_JSON": False,
}
# END DEFAULTS #
# #############################################################################

__CSSE1001TEST = True
GLOBAL = "__CSSE1001TEST"
UT_GLOBAL = "__unittest"

import unittest
import sys
import difflib
from io import StringIO
import contextlib
from collections import OrderedDict
import traceback
import re
import json
import argparse
import time
import os
import imp
from enum import Enum, unique
from types import FunctionType, MethodType
from inspect import signature

@unique
class TestOutcome(Enum):
    SUCCEED = 0
    FAIL = 1
    SKIP = 2


def relative_import(module_path, module_name=None):
    """
    Imports a module relatively, regardless of whether relevant directories are python modules.

    :param module_path: The path to the module to import.
    :param module_name: The name of the module. If None, the filename of the module_path is used, sans extension.
    :return: The module.
    """
    if module_name is None:
        module_name = os.path.basename(module_path).split('.')[0]

    with open(module_path, "r") as fd:
        sys.modules[module_name] = module = imp.new_module(module_name)
        exec(fd.read(), sys.modules[module_name].__dict__)

    return module


def _is_relevant_tb_level(tb, *globals):
    """
    Determines if a given traceback occurred in a file with any of the given globals.

    :param tb: The traceback.
    :param globals: The globals to check.
    :return: Returns True iff traceback occurred in a file with ANY of the given globals.
    """
    for g in globals:
        if g in tb.tb_frame.f_globals:
            return True

    return False


def _exc_info_to_string(err, ignored_exceptions=(AssertionError,),
                        ignored_module_globals=(), suppress_paths=False,
                        capture_locals=False):
    """
    Converts a sys.exc_info()-style tuple of values into a string.

    :param err: sys.exc_info()-style tuple.
    :param ignored_exceptions: Collection of Exceptions for which to ignore traceback lines.
    :param ignored_module_globals: Collection of global flags used to ignore tracebacks that occur in files with any of
        the given globals.
    :param suppress_paths: Remove file paths from traceback, leaving only the filename.
    :param capture_locals: If True, Local variables at the source of the error are included in the output.
    :return: Formatted error string.
    """
    """"""
    exctype, value, tb = err

    # Skip test runner traceback levels
    while tb and _is_relevant_tb_level(tb, *ignored_module_globals):
        tb = tb.tb_next

    # if exctype is test.failureException:
    # # Skip assert*() traceback levels
    # length = self._count_relevant_tb_levels(tb)
    # else:
    # length = None

    length = None

    tb_e = traceback.TracebackException(
        exctype, value, tb, limit=length, capture_locals=capture_locals)
    msgLines = list(tb_e.format())

    if suppress_paths:
        for i, line in enumerate(msgLines):
            msgLines[i] = re.sub(r'File ".*[\\/]([^\\/]+.py)"', r'File "\1"',
                                 line)

    # from unittest.TestResult, but not needed at present
    # commented due to unresolved reference to STDOUT/STDERR_LINE
    # if self.buffer:
    # output = sys.stdout.getvalue()
    # error = sys.stderr.getvalue()
    # if output:
    # if not output.endswith('\n'):
    # output += '\n'
    # msgLines.append(STDOUT_LINE % output)
    # if error:
    # if not error.endswith('\n'):
    #             error += '\n'
    #         msgLines.append(STDERR_LINE % error)
    return ''.join(msgLines)


class CsseTestResult(unittest.TestResult):
    _tb_no_duplicates = True
    _tb_hide_paths = True
    _tb_locals = False

    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self._results = OrderedDict()
        self._test_cases = {}
        self._skips = []

    def get_test_case_name(self, test):
        if isinstance(test, OrderedTestCase):
            return test.get_name()

        return test.__class__.__name__

    @staticmethod
    def get_test_name(test):
        return test.id().split('.')[-1].strip().split('test', 1)[-1]

    def startTest(self, test):
        name = self.get_test_case_name(test)
        if name not in self._results:
            self._results[name] = {
                "name": name,
                "total": 0,
                "passed": 0,
                "failed": 0,
                "skipped": 0,
                "tests": OrderedDict()
            }

        test_name = self.get_test_name(test)
        self._results[name]['tests'][test_name] = {}

        super().startTest(test)

    def add_outcome(self, test, outcome, message=None):
        test_case_name = self.get_test_case_name(test)
        test_name = self.get_test_name(test)

        res = self._results[test_case_name]
        res[outcome] += 1
        res['total'] += 1

        res['tests'][test_name] = {
            "outcome": outcome,
            # "subTests": OrderedDict()
        }

        if message:
            res['tests'][test_name]['message'] = message

            # print("{}.{} {}".format(test_case_name, test_name, outcome))

    def addError(self, test, err):
        type, value, traceback = err

        formatted_err = _exc_info_to_string(err, ignored_module_globals=(
        GLOBAL, UT_GLOBAL),
                                            suppress_paths=self._tb_hide_paths,
                                            capture_locals=self._tb_locals)

        self.add_outcome(test, 'failed', formatted_err)

        self.errors.append((test, formatted_err))
        self._mirrorOutput = True

    def addFailure(self, test, err):
        type, value, traceback = err

        formatted_err = _exc_info_to_string(err, ignored_module_globals=(
        GLOBAL, UT_GLOBAL),
                                            suppress_paths=self._tb_hide_paths,
                                            capture_locals=self._tb_locals)

        self.add_outcome(test, 'failed', formatted_err)

        self.failures.append((test, formatted_err))
        #
        # self.add_outcome(test, 'failed', str(value))
        #
        # self.failures.append((test, str(value)))
        self._mirrorOutput = True

    def addSuccess(self, test):
        self.add_outcome(test, 'passed')
        super().addSuccess(test)

    # def addSubTest(self, test, subtest, err):
    # print("Adding {} {} {}".format(test.id(), subtest.id(), err))

    def addSkip(self, test, reason):
        self.add_outcome(test, 'skipped', reason)

        self._skips.append((test, reason))
        self._mirrorOutput = True

        super().addSkip(test, reason)

    def getDescription(self, test, include_case=True):
        """The description of the test, displayed in the test runner output
        """
        try:
            key = test.id().split('test')[-1].strip()

            i = int(key) + 1
            name = test.get_test(key)

            case = test.get_name()
            order = test.get_order()
            # i = order.index("test" + name) + 1

            width = len(str(len(order) + 1))

            if not include_case:
                case = ""

            prefix = "{} {}.{} ".format(case, i, (width - len(str(i))) * " ")

        except:
            # The name should be the last thing after test
            name = test.id().split('test')[-1].strip()

            return concatenate_and_indent('', name)

        return concatenate_and_indent(prefix, name)

class CssePrintTestResult(CsseTestResult, unittest.TextTestResult):
    outcome_symbols = {
        'failed': '-',
        'passed': '+',
        'skipped': '?'
    }

    def startTest(self, test):
        name = self.get_test_case_name(test)
        if name not in self._results:
            # print("-" * 80)
            print(name)
            # print("-" * 80)

        super().startTest(test)

    def add_outcome(self, test, outcome, message=None):
        super().add_outcome(test, outcome, message)

        symbol = self.outcome_symbols[outcome]
        test_case_name = self.get_test_name(test).split('test', 1)[-1].strip()
        desc = self.getDescription(test, False)
        prefix = "{:<4}{} ".format("", symbol)
        print(concatenate_and_indent(prefix, desc))

    def printErrors(self):
        if self.errors or self.failures:
            print('-' * 80)
            print_block("Failed Tests")

        if len(self.errors) and self._tb_no_duplicates:
            # remove duplicates
            test, err = self.errors[-1]

            # iterate over indices [n-1, ..., 0]
            for i in range(len(self.errors) - 2, -1, -1):

                last_test, last_err = self.errors[i]
                if err == last_err:
                    self.errors[i + 1] = test, "AS ABOVE"

                test, err = last_test, last_err

        self.printErrorList('ERROR', self.errors)
        self.printErrorList('FAIL', self.failures)
        self.printErrorList('SKIP', self._skips)

    def printErrorList(self, flavour, errors):
        TAB = " " * 4
        for test, err in errors:
            print("=" * 80)
            print(concatenate_and_indent("{}: ".format(flavour),
                                         self.getDescription(test)))
            print("-" * 80)

            print(concatenate_and_indent(TAB, str(err).strip()))
            print("")



def concatenate_and_indent(prefix, suffix, offset=0, char=" "):
    """
    Concatenates two strings, indenting each line of the suffix to line up with the first.

    :param prefix: The prefix (should be single line only).
    :param suffix: The suffix (can be multiple lines).
    :param offset: The amount to increase the indent. Defaults to 0.
    :param char: The character to use to indent. Defaults to <space>.
    :return:
    """
    return prefix + suffix.replace("\n", "\n" + (len(prefix) + offset) * char)


def print_block(text, width=80):
    print("/" + (width - 2) * '-' + "\\")

    for i in range(0, len(text), width - 4):
        line = text[i:i + width - 4]

        space = (width - 4) - len(line)

        if space:
            line = int(space / 2 + .5) * ' ' + line + int(space / 2) * ' '

        print('| ' + line + ' |')

    print("\\" + (width - 2) * '-' + "/")


def attribute_best_guess(object, attribute, guesses=3):
    """
    Attempts to guess the most likely attribute belonging to object that matches the given attribute.

    :param object: The object to search.
    :param attribute: The attribute to search for.
    :param guesses: The number of guesses to make. Defaults to 3.

    :return: A pair of (has_attribute, possible_matches):
        has_attribute is True iff object has attribute.
        possible_matches is a list of potential matches, ordered by likelihood, whose length is <= guesses.
    """

    if getattr(object, attribute, None):
        return True, [attribute]

    return False, difflib.get_close_matches(attribute, dir(object), n=guesses)


def end_test(test_case, reason, outcome):
    """
    Ends a test by performing the given action.

    :param test_case: The unittest.TestCase to act upon.
    :param reason: The reason for ending the test.
    :param outcome: The outcome of the test (i.e. TestOutcome).
    """
    if outcome == TestOutcome.FAIL:
        test_case.fail(reason)
    elif outcome == TestOutcome.SKIP:
        test_case.skipTest(reason)

@contextlib.contextmanager
def hijack_stdio():
    save_stdout = sys.stdout
    save_stderr = sys.stderr
    save_stdin = sys.stdin

    try:
        sys.stdout = StringIO()
        sys.stderr = StringIO()
        sys.stdin = StringIO()
        yield sys.stdout, sys.stderr, sys.stdin
    finally:
        sys.stdout = save_stdout
        sys.stderr = save_stderr
        sys.stdin = save_stdin


@contextlib.contextmanager
def hijack_stdout():
    save_stdout = sys.stdout

    try:
        sys.stdout = StringIO()
        yield sys.stdout
    finally:
        sys.stdout = save_stdout


@contextlib.contextmanager
def hijack_stderr():
    save_stderr = sys.stderr

    try:
        sys.stderr = StringIO()
        yield sys.stderr
    finally:
        sys.stderr = save_stderr


@contextlib.contextmanager
def hijack_stdin():
    save_stdin = sys.stdin

    try:
        sys.stdin = StringIO()
        yield sys.stdin
    finally:
        sys.stdin = save_stdin

class TestGenerator(object):
    class NoReturnValue(object):
        pass

    @staticmethod
    def function_naming_test(module, function):
        """
        Returns a function that tests whether a module has function.

        :param module: The module that contains function.
        :param function: The function to check for.
        """

        def fn(self):
            match, guesses = attribute_best_guess(module, function)

            if not match:
                if not len(guesses):
                    self.fail("No function named {!r}".format(function))
                guesses = ", ".join([repr(g) for g in guesses])
                text = "No function named {!r}. Perhaps: {}".format(function,
                                                                    guesses)

                self.fail(text)

        return fn

    @staticmethod
    def class_naming_test(module, klass, methods=[]):
        """
        Returns a function that tests whether a module has class, and if that class has all of the given methods.

        :param module: The module that contains function.
        :param klass: The class to check for.
        :param methods: A list of methods to check for.
        """

        def fn(self):
            match, guesses = attribute_best_guess(module, klass)

            if not match:
                if not len(guesses):
                    return self.fail("No class named {!r}".format(klass))

                # todo: should this be a subTest?
                # with self.subTest(klass):
                guesses_text = ", ".join([repr(g) for g in guesses])
                self.fail("No class named {!r}. Perhaps: {}".format(klass,
                                                                    guesses_text))


        return fn

    @staticmethod
    def class_method_naming_test(module, klass, method,
                                 undefined_outcome=TestOutcome.SKIP):
        """
        Returns a function that tests whether a module has class, and if that class has all of the given methods.

        :param module: The module that contains function.
        :param klass: The class to check for.
        :param method: The method on klass to check for.
        :param undefined_outcome: Action to perform (i.e. result of test) if the function is undefined,
            or if there is no close match.
        """

        def fn(self):
            # Get most likely function, if not the function itself
            match, guesses = attribute_best_guess(module, klass)

            if not match and not len(guesses):
                return end_test(self,
                                "No method {!r} for undefined class {!r}.".format(
                                    method, klass),
                                undefined_outcome)

            guess = guesses[0]
            klass_guess = getattr(module, guess)

            match, guesses = attribute_best_guess(klass_guess, method)

            if not match:
                if not len(guesses):
                    self.fail(
                        "No method named {!r} on class {!r}".format(method,
                                                                    guess))

                guesses_text = ", ".join([repr(g) for g in guesses])
                self.fail(
                    "No method named {!r} on class {!r}. Perhaps: {}".format(
                        method, guess, guesses_text))

        return fn

    @staticmethod
    def function_docstring_test(module, function,
                                undefined_outcome=TestOutcome.FAIL):
        """
        Returns a function that tests whether a module's function has a docstring.

        :param module: The module that contains function.
        :param function: The function to check for a docstring.
        :param undefined_outcome: Action to perform (i.e. result of test) if the function is undefined,
            or if there is no close match.
        """

        def fn(self):
            # Get most likely function, if not the function itself
            match, guesses = attribute_best_guess(module, function)

            if match:
                fn = getattr(module, function)
            elif len(guesses):
                fn = getattr(module, guesses[0])
            else:
                return end_test(self,
                                "No docstring for undefined function {!r}.".format(
                                    function), undefined_outcome)

            # Check for a docstring
            if fn.__doc__ is None or not fn.__doc__.strip():
                self.fail("No docstring for function {!r}.".format(function))

        return fn

    @staticmethod
    def class_docstring_test(module, klass, methods=[],
                             undefined_outcome=TestOutcome.FAIL):
        """
        Returns a function that tests, for a given module, whether class and each of the given methods have docstrings.

        :param module: The containing module.
        :param klass: The class to check for a docstring.
        :param methods: A list of methods on klass to check for docstrings.
        :param undefined_outcome: Action to perform (i.e. result of test) if the class or a method is undefined,
            or if there is no close match.
        """

        def fn(self):
            match, guesses = attribute_best_guess(module, klass)

            # handle undefined class
            if not len(guesses):
                return end_test(self,
                                "No docstring for undefined class {!r}".format(
                                    klass), undefined_outcome)


        return fn

    @staticmethod
    def class_method_docstring_test(module, klass, method,
                                    undefined_outcome=TestOutcome.SKIP):
        """
        Returns a function that tests, for a given module, whether class and each of the given methods have docstrings.

        :param module: The containing module.
        :param klass: The class to check for a docstring.
        :param method: The method on klass to check for docstrings.
        :param undefined_outcome: Action to perform (i.e. result of test) if the class or a method is undefined,
            or if there is no close match.
        """

        def fn(self):
            match, guesses = attribute_best_guess(module, klass)

            # handle undefined class
            if not len(guesses):
                return end_test(self,
                                "No docstring for method {!r} on undefined class {!r}".format(
                                    method, klass),
                                undefined_outcome)

            guess = guesses[0]
            klass_guess = getattr(module, guess)

            match, guesses = attribute_best_guess(klass_guess, method)

            # handle undefined method
            if not len(guesses):
                return end_test(self,
                                "No docstring for undefined method {!r} on class {!r}".format(
                                    method, klass),
                                undefined_outcome)

            fn = getattr(klass_guess, guesses[0])

            # Check for a docstring
            if fn.__doc__ is None or not fn.__doc__.strip():
                self.fail(
                    "No docstring for method {!r} on class {!r}.".format(method,
                                                                         guess))

        return fn

    @staticmethod
    def class_inheritance_test(module, klass_name, parent_name,
                               undefined_outcome=TestOutcome.FAIL):
        """
        Returns a function that tests, for a given module, whether class inherits from parent.

        :param module: The containing module.
        :param klass_name: The class to check.
        :param parent_name: The parent class to check for.
        :param undefined_outcome: Action to perform (i.e. result of test) if the class is undefined,
            or if there is no close match.
        """

        def fn(self):
            # get class from module
            klass_match, klass_guesses = attribute_best_guess(module,
                                                              klass_name)

            # handle undefined class
            if not len(klass_guesses):
                return end_test(self,
                                "No parent class for undefined class {!r}.".format(
                                    klass_name), undefined_outcome)

            klass_guess = klass_guesses[0]
            klass_guess_obj = getattr(module, klass_guess)

            # get parent from module
            parent = getattr(module, parent_name, None)
            # if not found, try builtins
            if parent is None:
                parent = getattr(sys.modules['__main__'].__builtins__,
                                 parent_name, None)

            # handle undefined parent
            # no parent is strict fail
            if parent is None:
                return end_test(self,
                                "Class {0!r} must inherit from {1!r}, but {1!r} is not defined.".format(
                                    klass_name,
                                    parent_name),
                                TestOutcome.FAIL)

            if not issubclass(klass_guess_obj, parent):
                parents_text = ", ".join(
                    [repr(p.__name__) for p in klass_guess_obj.__bases__])
                self.fail(
                    "Class {!r} must inherit from {!r}, but instead inherits from {}.".format(
                        klass_name,
                        parent_name, parents_text))

        return fn

    @staticmethod
    def function_comparison_test(module, function, args, result,
                                 undefined_outcome=TestOutcome.SKIP, **kwargs):
        """
        Returns a function that tests, for a given module, whether function returns result.

        :param module: The containing module.
        :param function: The function to test.
        :param args: A tuple of arguments to supply to the function.
        :param result: The expected return value.
        :param undefined_outcome: Action to perform (i.e. result of test) if the function is undefined,
            or if there is no close match.
        """

        # todo: suppress stdout/stderr?
        def fn(self):
            # Get most likely function, if not the function itself
            match, guesses = attribute_best_guess(module, function)

            if match:
                fn = getattr(module, function)
            elif len(guesses):
                fn = getattr(module, guesses[0])
            else:
                return end_test(self,
                                "Undefined function {!r}.".format(function),
                                undefined_outcome)

            self.assertEqual(fn(*args), result)

        return fn

    @staticmethod
    def function_io_test(module, function, args, result=None,
                         stdin="", stdout="", stderr="",
                         undefined_outcome=TestOutcome.SKIP, exit_allowed=False,
                         exit_error=None, formatter=lambda self, x: x,
                         result_formatter=lambda self, x: x, **kwargs):
        """
        Returns a function that tests, for a given module, whether function returns result, using stdio.

        :param module: The containing module.
        :param function: The function to test.
        :param args: A tuple of arguments to supply to the function.
        :param result: The expected return value. Use TestGenerator.NoReturnValue to ignore return value.
        :param stdin: The standard input to supply to the function.
        :param stdout: The expected standard output from the function. Set to None to ignore comparison.
        :param stderr: The expected standard error from the function. Set to None to ignore comparison.
        :param undefined_outcome: Action to perform (i.e. result of test) if the function is undefined,
            or if there is no close match.
        :param exit_allowed: If True, the function is allowed to end by calling exit()/quit()/etc.
        :param exit_error: The error text to use if an unallowed SystemExit occurs.
        """

        ignore_return = isinstance(result, TestGenerator.NoReturnValue)

        # todo: add timeout
        def fn(self):
            # Get most likely function, if not the function itself
            match, guesses = attribute_best_guess(module, function)

            if match:
                fn = getattr(module, function)
            elif len(guesses):
                fn = getattr(module, guesses[0])
            else:
                return end_test(self,
                                "Undefined function {!r}.".format(function),
                                undefined_outcome)

            with hijack_stdio() as (stdout_stream, stderr_stream, stdin_stream):
                # TODO: Had to add this here to get context correctly
                with mock_load_words(module):
                    sys.stdin.write(stdin)
                    sys.stdin.seek(0)

                    # ignore quit/exit
                    exited = False
                    try:
                        real_res = fn(*args)

                        if not ignore_return:
                            self.assertEqual(result_formatter(self, real_res), result_formatter(self, result))
                    except SystemExit as e:
                        exited = True
                        if not exit_allowed:
                            if exit_error is not None:
                                return self.fail(exit_error)
                            else:
                                raise e

                    sys.stdout.seek(0)
                    if stdout is not None:
                        actual_stdout = sys.stdout.read()
                        if formatter(self, actual_stdout) != formatter(self, stdout):
                            self.assertEqual(actual_stdout, stdout)

                    sys.stderr.seek(0)
                    if stderr is not None:
                        actual_stderr = sys.stderr.read()
                        if formatter(self, actual_stderr) != formatter(self, stderr):
                            self.assertEqual(actual_stderr, stderr)

        return fn


DIFF_OMITTED = ('\nDiff is %s characters long. '
                'Set --diff to see it.')


class UnorderedTestCase(unittest.TestCase):
    _name = None

    def _truncateMessage(self, message, diff):
        max_diff = self.maxDiff
        if max_diff is None or len(diff) <= max_diff:
            return message + diff
        return message + (DIFF_OMITTED % len(diff))

    def get_name(self):
        if self._name is not None:
            return self._name

        return self.__class__.__name__.replace('TestCase', '')

    def assertMultiLineEqual(self, first, second, msg=None):
        """Assert that two multi-line strings are equal."""
        self.assertIsInstance(first, str, 'First argument is not a string')
        self.assertIsInstance(second, str, 'Second argument is not a string')

        if first != second:
            # don't use difflib if the strings are too long
            if (len(first) > self._diffThreshold or
                        len(second) > self._diffThreshold):
                self._baseAssertEqual(first, second, msg)
            firstlines = first.splitlines(keepends=True)
            secondlines = second.splitlines(keepends=True)
            if len(firstlines) == 1 and first.strip('\r\n') == first:
                firstlines = [first + '\n']
                secondlines = [second + '\n']
            _common_shorten_repr = unittest.util._common_shorten_repr
            standardMsg = '%s != %s' % _common_shorten_repr(first, second)
            diff = '\n' + '\n'.join(difflib.ndiff(firstlines, secondlines))
            diff = "\n".join([x for x in diff.split('\n') if x.strip()])
            standardMsg = self._truncateMessage(standardMsg,
                                                "\n" + diff) + "\n\n"
            self.fail(self._formatMessage(msg, standardMsg))


class OrderedTestCase(UnorderedTestCase):
    _order = None
    _subTests = None

    def __init__(self, methodName='runTest'):
        super().__init__(methodName)
        self._subTests = {}

    @classmethod
    def ensure_order(cls):
        if cls._order is None:
            cls._order = []

            methods = [(method, getattr(cls, method)) for method in dir(cls) if
                       method.startswith("test")]
            tests = [(method, method.__doc__.strip() or name) for name, method in methods]

            cls.add_test_methods(tests)

    @classmethod
    def add_test(cls, name, fn):
        cls.ensure_order()

        key = len(cls._order)

        cls._order.append(name)

        setattr(cls, "test" + str(key), fn)

    @classmethod
    def add_test_methods(cls, methods):
        for method, name in methods:
            if name is None:
                if method.startswith('test'):
                    name = method.split('test', 1)[-1]
                else:
                    name = method

            cls.add_test(name, method)

    @classmethod
    def get_order(cls):
        cls.ensure_order()

        return ["test" + str(i) for i in range(len(cls._order))]

    @classmethod
    def get_test(cls, key):
        return cls._order[int(key)]



import tkinter as tk
class TkDisabler(object):
    _count = 0

    @classmethod
    def reset_count(cls):
        cls._count = 0

    @classmethod
    def get_count(cls):
        return cls._count

    @classmethod
    def patch(cls):
        cls._real_tk = tk.__dict__['Tk']

        class FakeTk(tk.Tk):
            def mainloop(tk_self, *args):
                cls._count += 1

        tk.__dict__['Tk'] = FakeTk

    @classmethod
    def unpatch(cls):
        tk.__dict__['Tk'] = cls._real_tk

class NoTkTestCase(OrderedTestCase):
    # @classmethod
    # def setUpClass(cls):
    #     TkDisabler.reset_count()

    def test_0_mainloops(self):
        """No global tkinter usage"""

        count = TkDisabler.get_count()

        if count:
            self.fail("A tkinter mainloop was run incorrectly {} time(s).\nEnsure that tk is not being instantiated in the global scope.".format(count))


def create_subclass(name, *parents):
    return type(name, parents, {})


def create_test_case(name):
    test_case = create_subclass(name + 'TestCase', OrderedTestCase)
    set_test_case_name(name, test_case)

    return test_case


def create_naming_test_case(module, functions=(), klasses=()):
    test_case = create_test_case('Naming')

    for function in functions:
        test_case.add_test(function,
                           TestGenerator.function_naming_test(module, function))

    for klass, methods, *_ in klasses:
        test_case.add_test(klass,
                           TestGenerator.class_naming_test(module, klass))

        for method in methods:
            test_case.add_test("    {}.{}".format(klass, method),
                               TestGenerator.class_method_naming_test(module,
                                                                      klass,
                                                                      method))

    return test_case


def create_docstring_test_case(module, functions=(), klasses=(),
                               undefined_outcome=TestOutcome.SKIP):
    test_case = create_test_case('Docstrings')

    for function in functions:
        test_case.add_test(function,
                           TestGenerator.function_docstring_test(module,
                                                                 function,
                                                                 undefined_outcome=undefined_outcome))

    for klass, methods, *_ in klasses:
        test_case.add_test(klass,
                           TestGenerator.class_docstring_test(module, klass))

        for method in methods:
            test_case.add_test("    {}.{}".format(klass, method),
                               TestGenerator.class_method_docstring_test(module,
                                                                         klass,
                                                                         method))

    return test_case


def create_inheritance_test_case(module, klasses=(),
                                 undefined_outcome=TestOutcome.SKIP):
    test_case = create_test_case('Inheritance')

    for klass, _, *parents in klasses:
        for parent in parents:
            if parent is None:
                continue
            test_case.add_test("{} inherits from {}".format(klass, parent),
                               TestGenerator.class_inheritance_test(module,
                                                                    klass,
                                                                    parent,
                                                                    undefined_outcome=undefined_outcome))

    return test_case


def create_comparison_test_case(module, function, tests,
                                undefined_outcome=TestOutcome.SKIP):
    test_case = create_test_case(function)

    for i, test in enumerate(tests):
        kwargs = {
            "undefined_outcome": undefined_outcome
        }

        kwargs.update(test)

        test_case.add_test(kwargs['title'],
                           TestGenerator.function_comparison_test(module,
                                                                  function,
                                                                  **kwargs))

    return test_case


def create_io_test_case(module, function, tests,
                        undefined_outcome=TestOutcome.SKIP):
    test_case = create_test_case(function)

    for i, test in enumerate(tests):
        kwargs = {
            "undefined_outcome": undefined_outcome
        }

        kwargs.update(test)

        test_case.add_test(kwargs['title'],
                           TestGenerator.function_io_test(module, function,
                                                          **kwargs))

    return test_case


def set_test_case_name(name, *test_cases):
    for test_case in test_cases:
        test_case._name = name


class CsseTestLoader(unittest.TestLoader):
    def __init__(self, test_cases):
        super().__init__()
        self._test_cases = test_cases

    def getTestCaseNames(self, testCaseClass):
        if issubclass(testCaseClass, OrderedTestCase):
            return testCaseClass.get_order()

        return super().getTestCaseNames(testCaseClass)

    def loadTestsFromModule(self, module, *args, pattern=None, **kwargs):
        tests = []
        for test_case in self._test_cases:
            obj = test_case
            if isinstance(obj, type) and issubclass(obj, unittest.TestCase):
                tests.append(self.loadTestsFromTestCase(obj))
            else:
                raise TypeError(
                    "Class {!r} is not a subclass of unittest.TestCase.".format(
                        test_case))

        return self.suiteClass(tests)


class TestMaster(object):
    _tests = None

    def __init__(self, config=DEFAULTS):
        self._meta = {}
        self._config = config

        # ensure correct version of Python is used
        if not self.ensure_version():
            print(
                "Unsupported Python version {}".format(tuple(sys.version_info)))
            exit(1)

    def load_module(self, module_path):
        try:
            self._module = __import__(
                module_path.rstrip('.py').replace("/", "."))
            return None
        except ImportError as e:
            err = sys.exc_info()

            result = {
                "message": "Tests not run due to file not found",
                # "details": "File {!r} does not exist.".format(module_path),
                "error": e,
                "type": "import",
                "code": 3
            }

        except SyntaxError as e:
            err = sys.exc_info()

            result = {
                "message": "Tests not run due to syntax error",
                # "details": "Syntax error in {!r}.".format(module_path),
                "error": e,
                "type": "syntax",
                "code": 4
            }

        except Exception as e:
            err = sys.exc_info()

            result = {
                "message": "Tests not run due to arbitrary exception",
                # "details": "Syntax error in {!r}.".format(module_path),
                "error": e,
                "type": "exception",
                "code": 5
            }

        text = _exc_info_to_string(err, ignored_module_globals=(GLOBAL,),
                                   suppress_paths=True)
        result['details'] = text

        return result

    def set_meta(self, property, value):
        self._meta[property] = value

    def get_meta(self, property):
        return self._meta[property]

    def setup_args(self):
        parser = argparse.ArgumentParser()

        parser.add_argument("script",
                            help="The script you want to run the tests against.",
                            nargs="?",
                            default=self._config["SCRIPT"])
        parser.add_argument("test_data",
                            help="The file containing test data to use.",
                            nargs="?",
                            default=self._config["TEST_DATA"])
        parser.add_argument("-d", "--diff",
                            help="The maximum number of characters in a diff",
                            action="store",
                            type=int,
                            default=self._config["MAXDIFF"])
        parser.add_argument("-m", "--masters",
                            help="Whether or not to utilize master's tests.",
                            action='store_true',
                            default=self._config["CSSE7030"])
        parser.add_argument("-j", "--json",
                            help="Whether or not to display output in JSON format.",
                            action='store_true',
                            default=self._config["USE_JSON"])
        parser.add_argument("--tb-hide-paths",
                            help="Hide paths from traceback output.",
                            action="store_true",
                            default=self._config["HIDE_TRACEBACK_PATHS"])
        parser.add_argument("--tb-no-duplicates",
                            help="Remove duplicates from test output.",
                            action="store_true",
                            default=self._config["REMOVE_TRACEBACK_DUPLICATES"])
        parser.add_argument('unittest_args', nargs='*', default=[])

        self._args = parser.parse_args()
        return self._args

    def prepare(self):
        raise NotImplemented(
            "Prepare method must be implemented by TestMaster child class.")

    def ensure_version(self):
        """
        Returns None if Python version is okay, else error message.
        """
        return sys.version_info >= (3, 5, 1)

    def load_test_data(self):
        if self._args.test_data:
            data = __import__(self._args.test_data.rstrip('.py'))
            # data = relative_import(self._args.test_data, "data")
        else:
            if self._config["TEST_DATA_RAW"] is None:
                self._test_data = None
                return
            import imp

            data = imp.new_module('data')
            exec(self._config["TEST_DATA_RAW"], data.__dict__)

        self._test_data = data.get_data(self._args)

    # todo: clean this up and abstract
    def main(self):
        output = {
            "version": self._config["VERSION"]
        }

        self.setup_args()

        output_json = self._args.json

        try:
            self.load_test_data()
        except Exception as e:
            err = sys.exc_info()
            text = _exc_info_to_string(err, ignored_module_globals=(GLOBAL,),
                                       suppress_paths=True)

            if self._args.json:
                output['error'] = 'test_data'
                output[
                    'error_message'] = "Tests couldn't be run due to failure to load test data." + '\n' + text
            else:
                print_block("Fatal error loading test_data.")
                print(text)
            sys.exit(2)

        if not output_json and self._config['SHOW_VERSION']:
            print("Version: {}".format(output['version']))

        error = self.load_module(self._args.script)

        if error:
            output['error'] = error['type']
            output['error_message'] = error['message'] + '\n' + error['details']

            if output_json:
                print(json.dumps(output, indent=" " * 4))
            else:
                print_block(error['message'])
                print(error['details'])

            return sys.exit(error['code'])

        self.prepare()

        result_class = CsseTestResult if output_json else CssePrintTestResult

        result_class._tb_no_duplicates = self._args.tb_no_duplicates
        result_class._tb_hide_paths = self._args.tb_hide_paths

        with hijack_stderr():
            runner = unittest.TextTestRunner(verbosity=9, stream=None,
                                             resultclass=result_class)

        for test_case in self._tests:
            setattr(test_case, "maxDiff", self._args.diff or None)

        loader = CsseTestLoader(self._tests)
        if not output_json:
            print_block("Summary of Results")
        start = time.time()
        program = unittest.main(exit=False, testRunner=runner,
                                testLoader=loader,
                                argv=[sys.argv[0]] + self._args.unittest_args)
        stop = time.time()

        result = program.result

        output['total'] = result.testsRun
        fails, skips, errors = map(len, (
        result.failures, result.skipped, result.errors))
        output['failed'] = fails + errors
        output['skipped'] = skips
        output['passed'] = result.testsRun - (fails + errors + skips)

        output['time'] = stop - start

        output['results'] = result._results

        if not output_json:
            print("-" * 80)
            print(
                "Ran {total} tests in {time:.3f} seconds with {passed} passed/{skipped} skipped/{failed} failed.".format(
                    **output))

        if output_json:
            print(json.dumps(output, indent=" " * 4))

class PythonTestCase(unittest.TestCase):
    """Adds custom assertions that extend the default test case
    """

    _module = None

    def assertDefined(self, module, variable, var_type):
        """ Asserts that a variable is defined inside a module
        with the given type

        Raises an assertion error if it fails

        Parameters:
            module: The module or class
            variable (string): The name of the variable
            var_type (type): The type of the variable
        """

        if not hasattr(module, variable):
            raise AssertionError("'{}' is not defined correctly, check spelling".format(variable))

        var = getattr(module, variable)

        if type(var) != var_type:
            raise AssertionError("'{}' is type '{}', expected '{}'".format(variable, type(var), var_type))

    def assertFunctionDefined(self, module, function_name, parameters):
        """ Asserts that a function is correctly defined with the correct number
        of parameters

        Parameters:
            module: The module
            function_name (string): The name of the function
            parameters (int): The number of parameters the function should take
        """

        self.assertDefined(module, filename, FunctionType)

    def assertMethodDefined(self, class_instance, method_name, parameters):
        """ Asserts that a method (class function) is correctly defined with the correct number
        of parameters

        Parameters:
            class_instance: The class instance
            method_name (string): The name of the method
            parameters (int): The number of parameters the method should take
        """

        self.assertDefined(class_instance, method_name, FunctionType)

        sig = signature(getattr(class_instance, method_name))

        if len(sig.parameters) != parameters:
            raise AssertionError(
                    "'{}' does not have the correct number of parameters, expected {} found {}".format(
                        method_name, parameters, len(sig.parameters)
                        )
                    )

    def assertIsSubclass(self, sub_class, parent_class):
        """ Asserts that a class is a subclass of the parent class

        Parameters:
            sub_class: The subclass to check
            parent_class: The parent class
        """
        self.assertTrue(issubclass(sub_class, parent_class))

