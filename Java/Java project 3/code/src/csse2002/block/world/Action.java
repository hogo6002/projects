package csse2002.block.world;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents an Action which can be performed on the block world (also
 * called world map). <br>
 * An action is something that a builder can do on a tile in the block world.
 * The actions include, moving the builder in a direction, moving a block in
 * a direction, digging on the current tile the builder is standing on and
 * dropping an item from a builder's inventory.
 */
public class Action {

    /**
     * MOVE_BUILDER action which is represented by
     * integer 0.
     */
    public static final int MOVE_BUILDER = 0;

    /**
     * MOVE_BLOCK action which is represented by integer 1.
     */
    public static final int MOVE_BLOCK = 1;

    /**
     * DIG action which is represented by integer 2.
     */
    public static final int DIG = 2;

    /**
     * DROP action which is represented by integer 3.
     */
    public static final int DROP = 3;

    private int primaryAction;
    private String secondaryAction;

    /**
     * Create an Action that represents a manipulation of the blockworld.
     * An action is represented by a primary action (one of MOVE_BUILDER,
     * MOVE_BLOCK, DIG or DROP), and a secondary action <br>
     *
     * Whether a secondary action is required depends on the primary action:
     * <ol>
     *    <li> MOVE_BUILDER and MOVE_BLOCK require a direction as the
     *    secondaryAction (one of "north", "east", "south" or "west"). </li>
     *    <li> DROP requires the index of at which a Block from the inventory
     *    should be dropped (stored as a string in this class, e.g., "1"). </li>
     *    <li> DIG does not require a secondary action, so an empty string
     *    can be passed to secondaryAction. </li>
     * </ol>
     *
     * This constructor does not need to check primaryAction or secondaryAction,
     * it just needs to construct an action such that
     * getPrimaryAction() == primaryAction, and
     * getSecondaryAction().equals(secondaryAction).
     *
     * @param primaryAction   the action to be created
     * @param secondaryAction the supplementary information associated with the
     *                        primary action
     * @require secondaryAction != null
     */
    public Action(int primaryAction, String secondaryAction) {
        this.primaryAction = primaryAction;
        this.secondaryAction = secondaryAction;
    }

    /**
     * Get the integer representing the Action (e.g., return 0 if Action is
     * MOVE_BUILDER)
     * @return the primary action
     */
    public int getPrimaryAction() {
        return primaryAction;
    }

    /**
     * Gets the supplementary information associated with the Action.
     * @return the secondary action, or "" (empty string) if no secondary
     *         action exists
     */
    public String getSecondaryAction() {
        return secondaryAction;
    }

    /**
     * Create a single Action if possible from the given reader. <br>
     *
     * Read a line from the given reader and load the Action on that line.
     * Only load one Action (<b>hint:</b> reader.readLine()) and return
     * the created action. <br>
     * Each line consists of a primary action, and optionally a secondary
     * action.<br>
     *
     * This function should do the following:
     * <ul>
     *     <li> If any line consists of 2 or more spaces (i.e. more than 2
     *          tokens) throws an ActionFormatException. </li>
     *     <li> If the primary action is not one of MOVE_BLOCK, MOVE_BUILDER,
     *          DROP or DIG, throw an ActionFormatException. </li>
     *     <li> If the primary action is MOVE_BLOCK, MOVE_BUILDER or DROP, and
     *          the primary action is not followed by a secondary action, throws
     *          an ActionFormatException.</li>
     *     <li> If the primary action is DIG, and DIG is not on a line by
     *          itself, with no trailing whitespace, throws an
     *          ActionFormatException. </li>
     *     <li> If the primary action is MOVE_BLOCK, MOVE_BUILDER or DROP, then
     *          creates and return a new Action with the primary action constant
     *          with the same name, and the secondary action. This method does
     *          not check the secondary action. </li>
     *     <li> If the primary action is DIG, returns a new Action with the
     *          primary action constant DIG, and an empty string ("") for the
     *          secondary action. </li>
     *     <li> If reader is at the end of the file, returns null. </li>
     *     <li> If an IOException is thrown by the reader, then throw an
     *          ActionFormatException. </li>
     * </ul>
     *
     * For details of the action format see Action.loadActions().
     *
     * @param reader the reader to read the action contents form
     * @return the created action, or null if the reader is at the end of
     *     the file.
     * @throws ActionFormatException if the line has invalid contents and
     *                               the action cannot be created
     * @require reader != null
     */
    public static Action loadAction(BufferedReader reader) throws
            ActionFormatException {

        try {
            String line = reader.readLine();

            // EOF reached, reader returns null
            if (line == null) {
                return null;
            }


            String [] tokens = line.split(" ", 3);

            if (tokens.length > 2) {
                throw new ActionFormatException("Too many tokens on line.");
            }

            Action action = null;

            if (tokens.length == 1) {
                if (tokens[0].equals("DIG")) {
                    action = new Action(DIG, "");
                }
            } else if (tokens.length == 2) {
                if (tokens[0].equals("MOVE_BUILDER")) {
                    action = new Action(MOVE_BUILDER, tokens[1]);
                } else if (tokens[0].equals("MOVE_BLOCK")) {
                    action = new Action(MOVE_BLOCK, tokens[1]);
                } else if (tokens[0].equals("DROP")) {
                    action = new Action(DROP, tokens[1]);
                }
            }


            if (action == null) {
                throw new ActionFormatException("Unrecognised action given");
            }

            return action;

        } catch (IOException e) {
            throw new ActionFormatException(e.toString());
        }
    }

    /**
     * Read all the actions from the given reader and perform them on the
     * given block world. <br>
     *
     * All actions that can be performed should print an appropriate message
     * (as outlined in processAction()), any invalid actions that cannot
     * be created or performed on the world map, should also print an error
     * message (also described in processAction()). <br>
     *
     * Each message should be printed on a new line (Use System.out.println()).
     * <br>
     *
     * Each action is listed on a single line, and one file can contain
     * multiple actions. <br>
     *
     * Each action must be processed after it is read (i.e. do not read the
     * whole file first, read and process each action one at a time).
     *
     * The file format is as follows:
     * <br>
     *
     * <pre>{@literal
     * primaryAction1 secondaryAction1
     * primaryAction2 secondaryAction2
     * ...
     * primaryActionN secondaryActionN
     * }</pre>
     *
     * There is a single space " " between each primaryAction and
     * secondaryAction. <br>
     * The primaryAction should be one of the following values:
     * <ul>
     *     <li> MOVE_BUILDER </li>
     *     <li> MOVE_BLOCK </li>
     *     <li> DIG </li>
     *     <li> DROP </li>
     * </ul>
     *
     *
     * If the secondaryAction is present, it should be one of the following
     * values:
     * <ul>
     *     <li> north </li>
     *     <li> east </li>
     *     <li> south </li>
     *     <li> west </li>
     *     <li> (a number) for DROP action </li>
     * </ul>
     *
     * An example file may look like this:
     * <pre>{@literal
     * MOVE_BUILDER north
     * MOVE_BUILDER south
     * MOVE_BUILDER west
     * DROP 1
     * DROP 3
     * DROP text
     * DIG
     * MOVE_BUILDER south
     * MOVE_BLOCK north
     * RANDOM_ACTION
     * }</pre>
     *
     * If all actions can be performed on the map, the output from the above
     * file is:
     * <pre>{@literal
     * Moved builder north
     * Moved builder south
     * Moved builder west
     * Dropped a block from inventory
     * Dropped a block from inventory
     * Error: Invalid action
     * Top block on current tile removed
     * Moved builder south
     * Moved block north
     *} </pre>
     * (The line "RANDOM_ACTION" should then cause an ActionFormatException to
     * be thrown) <br>
     *
     * Hint: Repeatedly call Action.loadAction() to get the next Action, and
     * then Action.processAction() to process the action. <br>
     *
     * @param reader the reader to read actions from
     * @param startingMap the starting map that actions will be applied to
     * @throws ActionFormatException if loadAction throws an
     *         ActionFormatException
     * @require reader != null
     * @require startingMap != null
     */
    public static void processActions(BufferedReader reader,
                                      WorldMap startingMap)
            throws ActionFormatException {

        Action action = Action.loadAction(reader);
        while (action != null) {
            processAction(action, startingMap);
            action = Action.loadAction(reader);
        }
    }

    /**
     * Perform the given action on a WorldMap, and print output to System.out.
     * After this method
     * finishes, map should be updated. (e.g., If the action is DIG, the
     * Tile on which the builder is currently on should be updated to contain
     * 1 less block (Builder.digOnCurrentTile()). The builder to use for actions
     * is that given by map.getBuilder().
     *
     * Do the following for these actions:
     * <ul>
     *      <li> For DIG action: call Builder.digOnCurrentTile(), then print to
     *           console "Top block on current tile removed".</li>
     *
     *      <li> For DROP action: call Builder.dropFromInventory(), then print
     *           to console "Dropped a block from inventory". The dropped item
     *           is given by action.getSecondaryAction(), that is first
     *           converted to an int. If the action.getSecondaryAction() cannot
     *           be converted to an int, print "Error: Invalid action" to the
     *           console. Valid integers (including negative integers and large
     *           positive integers) should be passed to
     *           Builder.dropFromInventory(). </li>
     *
     *      <li> For the MOVE_BLOCK action: call Tile.moveBlock() on the
     *           builder's current tile (Builder.getCurrentTile()), then print
     *           to console "Moved block {direction}". The direction is given by
     *           action.getSecondaryAction()</li>
     *
     *      <li> For MOVE_BUILDER action: call Builder.moveTo(), then print to
     *           console "Moved builder {direction}". The direction is given by
     *           action.getSecondaryAction()</li>
     *
     *      <li> If action.getPrimaryAction() {@literal < 0}
     *           or action.getPrimaryAction() {@literal > 3},
     *           or action.getSecondary() is not a direction
     *           (for MOVE_BLOCK or MOVE_BUILDER),
     *           or a valid integer (for DROP) then print to console
     *           "Error: Invalid action" </li>
     * </ul>
     * "{direction}" is one of "north", "east", "south" or "west". <br>
     *
     * For handling exceptions do the following:
     * <ul>
     *     <li> If a NoExitException is thrown, print to the console
     *          "No exit this way" </li>
     *     <li> If a TooHighException is thrown, print to the console
     *          "Too high" </li>
     *     <li> If a TooLowException is thrown, print to the console
     *          "Too low" </li>
     *     <li> If an InvalidBlockException is thrown, print to the console
     *          "Cannot use that block" </li>
     * </ul>
     *
     * Each line printed to the console should have a trailing newline
     * (i.e., use System.out.println()).
     *
     * @param action the action to be done on the map
     * @param map    the map to perform the action on
     * @require action != null
     * @require map != null
     */
    public static void processAction(Action action, WorldMap map) {

        try {
            int primary = action.getPrimaryAction();
            switch (primary) {
                case Action.DIG:
                    handleDig(map);
                    System.out.println("Top block on current tile removed");
                    break;
                case Action.DROP:
                    int secondaryAction;
                    try {
                        secondaryAction =
                                Integer.parseInt(action.getSecondaryAction());
                    } catch (NumberFormatException numberFormat) {
                        System.out.println("Error: Invalid action");
                        return;
                    }
                    handleDrop(map, secondaryAction);
                    System.out.println("Dropped a block from inventory");
                    break;
                case Action.MOVE_BLOCK:
                    if (!isValidDirection(action.getSecondaryAction())) {
                        System.out.println("Error: Invalid action");
                        return;
                    }
                    handleMoveBlock(map, action.getSecondaryAction());
                    System.out.println("Moved block "
                            + action.getSecondaryAction());
                    break;
                case Action.MOVE_BUILDER:
                    if (!isValidDirection(action.getSecondaryAction())) {
                        System.out.println("Error: Invalid action");
                        return;
                    }
                    handleMoveBuilder(map, action.getSecondaryAction());
                    System.out.println("Moved builder "
                            + action.getSecondaryAction());
                    break;
                default:
                    System.out.println("Error: Invalid action");
            }
        } catch (NoExitException noExit) {
            System.out.println("No exit this way");
        } catch (TooHighException tooHigh) {
            System.out.println("Too high");
        } catch (TooLowException tooLow) {
            System.out.println("Too low");
        } catch (InvalidBlockException invalidBlock) {
            System.out.println("Cannot use that block");
        }
    }

    /**
     * Handle moving the builder.
     * @param map the map to use
     * @param direction the direction as a string
     * @throws NoExitException if the builder cannot move that direction
     */
    private static void handleMoveBuilder(WorldMap map, String direction)
            throws NoExitException {
        Tile movingTo = map.getBuilder().getCurrentTile().getExits()
                .get(direction);
        map.getBuilder().moveTo(movingTo);

    }

    /**
     * Handle moving a block.
     * @param map the map to use
     * @param direction the direction as a string
     * @throws TooHighException Tile.moveBlock() throws a TooHighException
     * @throws InvalidBlockException the block on the builder's tile is not
     *         moveable.
     * @throws NoExitException if there is no exit in that direction.
     */
    private static void handleMoveBlock(WorldMap map, String direction)
            throws TooHighException, InvalidBlockException, NoExitException {
        map.getBuilder().getCurrentTile().moveBlock(direction);
    }

    /**
     * Handle dropping a block.
     * @param map the map to use
     * @param index the block index in the Builder's inventory
     * @throws TooHighException if Builder.drop() would throw a
     *         TooHighException
     * @throws InvalidBlockException if Builder.drop() would throw an
     *         InvalidBlockException
     */
    private static void handleDrop(WorldMap map, int index)
            throws TooHighException, InvalidBlockException {
        map.getBuilder().dropFromInventory(index);
    }

    /**
     * Handle digging a block.
     * @param map the map to use
     * @throws TooLowException if Builder.digOnCurrentTIle() would throw a
     *         TooLowException
     * @throws InvalidBlockException if Builder.digOnCurrentTile() would throw
     *         an InvalidBlockException
     */
    private static void handleDig(WorldMap map)
            throws TooLowException, InvalidBlockException {
        map.getBuilder().digOnCurrentTile();
    }

    /**
     * Takes a directional string (north, south, east, or west), and returns an
     * integer representing that direction.
     *
     * @param direction the directional string to be parsed/converted
     * @return the integer representation of the direction
     * @throws ActionFormatException if the provided directional string does not
     *                               match one of the given options
     */
    private static boolean isValidDirection(String direction) {
        return Arrays.asList(new String[]{"north", "south", "east", "west"})
                .contains(direction);
    }
}
