package csse2002.block.world;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Handles top-level interaction with performing actions on a WorldMap.
 * @serial exclude
 */
public class Main {

    /**
     * The entry point of the application.<br>
     *
     * Takes 3 parameters an input map file (args[0]), actions (args[1]), and an
     * output map file (args[2]). <br>
     *
     * The actions parameter can be either a filename, or the string
     * "System.in". <br>
     *
     * This function does the following:
     * <ol>
     *     <li> If there are not 3 parameters, (i.e. args.length
     *          {@literal != 3}), print
     *          "Usage: program inputMap actions outputMap"
     *           using System.err.println() and then exit with status 1
     *           (Hint: use System.exit()) </li>
     *     <li> Create a new WorldMap using the input map file. If an
     *          exception is thrown, print the exception to the console using
     *          System.err.println(), and then exit with status 2. </li>
     *     <li> Create a BufferedReader to read actions. If parameter 2 is
     *          a filename, the BufferedReader should be initialised using a
     *          new FileReader. If parameter 2 is the string "System.in", the
     *          buffered reader should be initialised using System.in and
     *          a new InputStreamReader. If an exception is thrown, print
     *          the exception to the console using System.err.println, and
     *          then exit with status 3. </li>
     *     <li> Call Action.processActions() using the created BufferedReader
     *          and WorldMap. If an exception is thrown, print the exception to
     *          the console using System.err.println, and then exit with
     *          status 4. </li>
     *     <li> Call WorldMap.saveMap() using the 3rd parameter to save the map
     *          to an output file. If an exception is thrown, print the
     *          exception to the console using System.err.println() and then
     *          exit with status 5. </li>
     * </ol>
     *
     * To print an exception to System.err, use System.err.println(e), where e
     * is the caught exception.
     *
     * @param args the input arguments to the program
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println(
                    "Usage: program inputMap inoutActions outputMap");
            System.exit(1);
        }

        String inputMap = args[0];
        String inputActions = args[1];
        String outputMap = args[2];

        // read in a WorldMap
        WorldMap map = null;
        try {
            map = new WorldMap(inputMap);
        } catch (BlockWorldException | IOException e) {
            System.err.println(e);
            System.exit(2);
        }

        // Setup a buffered reader to either read from System.in, or from
        // a file.
        BufferedReader reader = null;
        try {

            if (inputActions.equals("System.in")) {
                reader = new BufferedReader(new InputStreamReader(System.in));
            } else {
                reader = new BufferedReader(new FileReader(inputActions));
            }
        } catch (IOException io) {
            System.err.println(io);
            System.exit(3);
        }

        try {
            Action.processActions(reader, map);
        } catch (ActionFormatException format) {
            System.err.println(format);
            System.exit(4);
        }

        try {
            map.saveMap(outputMap);
        } catch (IOException ioException) {
            System.err.println(ioException);
            System.exit(5);
        }
    }

}
