package csse2002.block.world;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

/**
 * A class to store a world map.
 * @serial exclude
 */
public class WorldMap {

    // the sparse tile array to store tiles
    private SparseTileArray tileArray;

    // the position of tileArray.getTiles().get(0)
    private Position startPosition;

    // the builder
    private Builder builder;

    // store the system line separator ("\n", "\r\n" or "\r")
    private static final String LINE_SEP = System.lineSeparator();

    /**
     * A helper class for reading lines. It wraps a BufferedReader
     * and maintains the line number for error reporting.
     */
    private class LineReader {

        // the buffered reader to use
        private BufferedReader reader;

        // the number of lines read
        int lineNumber;

        /**
         * Create a line reader from a BufferedReader.
         * @param reader the BufferedReader to read from
         */
        LineReader(BufferedReader reader) {
            this.reader = reader;
            lineNumber = 0;
        }

        /**
         * Get an error string "Error on line {@literal<line-number>}.
         * @return an error string
         */
        String errorOnLine() {
            return "Error on line " + lineNumber + ": ";
        }

        /**
         * Read a line and return it, or throw an
         * exception if the WorldMap file is invalid. If a WorldMapFormat
         * exception is thrown, it will have the error message
         * "File ended abruptly".
         *
         * @return the line that was read
         * @throws IOException if BufferedReader.readLine() fails
         * @throws WorldMapFormatException if BufferedReader.readLine()
         *         returns null.
         */
        String readLineOrThrow() throws IOException,
                WorldMapFormatException {
            return readLineOrThrow("File ended abruptly");
        }

        /**
         * Read a line and return it, or throw an
         * exception if the WorldMap file is invalid. If a WorldMapFormat
         * exception is thrown, it will have the error message given
         * by errorMsg.
         * @param errorMsg the error message to pass to a WorldMapFormat
         *         exception
         * @return the line that was read
         * @throws IOException if BufferedReader.readLine() fails
         * @throws WorldMapFormatException if BufferedReader.readLine()
         *         returns null.
         */
        String readLineOrThrow(String errorMsg) throws
                IOException, WorldMapFormatException {
            String line = reader.readLine();

            if (line == null) {
                throw new WorldMapFormatException(errorOnLine() + errorMsg);
            }

            // whenever we succesfully call BufferedReader.readLine() we
            // increment the lineNumber
            lineNumber++;
            return line;
        }

        /**
         * Read a blank line, or throw an
         * exception if the WorldMap file is invalid or if there is no
         * blank line. The WorldMapFormat exception will have an error
         * message given by errorMsgFileEnd, if the file ends, or
         * errorMsgNotBlank, if the next line is not blank.
         * @param errorMsgFileEnd the error message to pass to a
         *         WorldMapFormatException if the file ends
         * @param errorMsgNotBlank the error message to pass to a
         *         WorldMapFormatException if the next line is not blank.
         * @throws IOException if BufferedReader.readLine() fails
         * @throws WorldMapFormatException if BufferedReader.readLine()
         *         returns null.
         */
        void readBlankLineOrThrow(String errorMsgFileEnd,
                                         String errorMsgNotBlank) throws
                IOException, WorldMapFormatException {
            String line = reader.readLine();

            if (line == null) {
                throw new WorldMapFormatException(errorOnLine()
                        + errorMsgFileEnd);
            } else if (!line.equals("")) {
                throw new WorldMapFormatException(errorOnLine()
                        + errorMsgNotBlank);
            }

            lineNumber++;
        }

        /**
         * Read the EOF or throw an
         * exception if the WorldMap file has not ended.
         * The WorldMapFormatException will have an error
         * message given by errorMsgNotEof.
         * @param errorMsgNotEof error message to pass to a
         *         WorldMapFormatException if the file ends
         * @throws IOException if BufferedReader.readLine() fails
         * @throws WorldMapFormatException if BufferedReader.readLine()
         *         does not return null.
         */
        void readEofOrThrow(String errorMsgNotEof) throws
                IOException, WorldMapFormatException {
            String line = reader.readLine();

            if (line != null) {
                throw new WorldMapFormatException(errorOnLine()
                        + errorMsgNotEof);
            }
        }
    }

    /**
     * Parse an integer from a string, or throw a WorldMapFormatException with
     * error message given by errorMsg.
     * @param intString the string to convert
     * @param errorMsg the errorMsg to pass to WorldMapFormatException
     *         if an error is thrown.
     * @return an integer parsed from the string.
     * @throws WorldMapFormatException if the string does not contain
     *         a valid integer.
     */
    private static int parseInt(String intString, String errorMsg)
            throws WorldMapFormatException {
        int output;
        try {
            output = Integer.parseInt(intString);
        } catch (NumberFormatException nfe) {
            throw new WorldMapFormatException(errorMsg);
        }

        return output;
    }

    /**
     * Parse an integer from a string, or throw
     * a WorldMapFormatException if the string is not an integer,
     * if the value of the integer is less than min, or if the vvalue of
     * the integer is greater than or equal to max.
     * @param intString the string to convert
     * @param errorMsgNotInt the error message to pass to
     *         WorldMapFormatException if the string is not an integer.
     * @param errorMsgBelowMin the error meessage to pass to
     *         WorldMapFormatException if the integer is less than min.
     * @param errorMsgAboveOrEqualMax the error message to pass to
     *         WorldMapFormatException if the integer is greater than or
     *         equal to max.
     * @return an integer parsed from the string.
     * @throws WorldMapFormatException if the string does not contain a valid
     *         integer, or if the string is not between [min, max).
     */
    private static int parseIntBetween(String intString, int min, int max,
                                       String errorMsgNotInt,
                                       String errorMsgBelowMin,
                                       String errorMsgAboveOrEqualMax)
            throws WorldMapFormatException {
        int output = parseInt(intString, errorMsgNotInt);

        if (output < min) {
            throw new WorldMapFormatException(errorMsgBelowMin);
        } else if (output >= max) {
            throw new WorldMapFormatException(errorMsgAboveOrEqualMax);
        }

        return output;
    }

    /**
     * Parse an integer from a string, or throw
     * a WorldMapFormatException if the string is not an integer,
     * or if the value of the integer is less than 0.
     * @param intString the string to convert
     * @param errorMsgNotInt the error message to pass to
     *         WorldMapFormatException if the string is not an integer.
     * @param errorMsgNegative the error meessage to pass to
     *         WorldMapFormatException if the integer is less than 0.
     * @return an integer parsed from the string.
     * @throws WorldMapFormatException if the string does not contain a valid
     *         integer, or if the integer is negative.
     */
    private static int parsePositiveInt(String intString, String errorMsgNotInt,
                                        String errorMsgNegative)
            throws WorldMapFormatException {
        return parseIntBetween(intString, 0, Integer.MAX_VALUE,
                errorMsgNotInt, errorMsgNegative, "");
    }

    /**
     * Split a String into an array of two strings, or throw a
     * WorldMapFormatException. If the string has no delimiters (given by delim)
     * throws a WorldMapFormatException with the error message errorMsgTooFew
     * or if the string has more than one delimeter, throws a
     * WorldMapFormatException with the error message errorMsgTooMany.
     * @param str the string to split
     * @param delim the delimiter to split the string on (passed
     *         passed to String.split().
     * @param errorMsgTooFew the error message to pass to a
     *         WorldMapFormatException if there are no delimeters.
     * @param errorMsgTooMany the error message to pass to a
     *         WorldMapFormatException if there are too many delimiters.
     * @return Two strings in an array.
     * @throws WorldMapFormatException if the string does not contain exactly
     *         one delimiter
     */
    private static String [] splitInTwo(String str, String delim,
                                        String errorMsgTooFew,
                                        String errorMsgTooMany) throws
            WorldMapFormatException {
        String [] strings = str.split(delim, 3);
        if (strings.length == 1) {
            throw new WorldMapFormatException(errorMsgTooFew);
        } else if (strings.length == 3) {
            throw new WorldMapFormatException(errorMsgTooMany);
        }

        return strings;
    }

    /**
     * Constructs a new block world map from a startingTile, position and
     * builder, such that getBuilder() == builder,
     * getStartPosition() == startPosition, and getTiles() returns a list
     * of tiles that are linked to startingTile. <br>
     * Hint: create a SparseTileArray as a member, and use the
     * addLinkedTiles to populate it.
     * @param startingTile the tile which the builder starts on
     * @param startPosition the position of the starting tile
     * @param builder the builder who will traverse the block world
     * @throws WorldMapInconsistentException if there are inconsistencies
     *         in the positions of tiles (such as two tiles at a single
     *         position)
     * @require startingTile != null
     * @require startPosition != null
     * @require builder != null
     * @require builder.getCurrentTile() == startingTile
     */
    public WorldMap(Tile startingTile, Position startPosition, Builder builder)
            throws WorldMapInconsistentException {
        reset(startingTile, startPosition, builder);
    }

    /**
     * Gets the builder associated with this block world.
     *
     * @return the builder object
     */
    public Builder getBuilder() {
        return builder;
    }

    /**
     * Gets the starting position.
     *
     * @return the starting position.
     */
    public Position getStartPosition() {
        return startPosition;
    }

    /**
     * Get a tile by position. <br>
     * Hint: call SparseTileArray.getTile()
     *
     * @param position get the Tile at this position
     * @return the tile at that position
     * @require position != null
     */
    public Tile getTile(Position position) {
        return tileArray.getTile(position);
    }

    /**
     * Get a list of tiles in a breadth-first-search
     * order (see {@link SparseTileArray SparseTileArray.getTiles()}
     * for details). <br>
     * Hint: call SparseTileArray.getTiles().
     *
     * @return a list of ordered tiles
     */
    public List<Tile> getTiles() {
        return tileArray.getTiles();
    }

    /**
     * Construct a block world map from the given filename. <br>
     * The block world map format is as follows:
     * <pre>{@literal
     *<startingX>
     *<startingY>
     *<builder's name>
     *<inventory1>,<inventory2>, ... ,<inventoryN>
     *
     *total:<number of tiles>
     *<tile0 id> <block1>,<block2>, ... ,<blockN>
     *<tile1 id> <block1>,<block2>, ... ,<blockN>
     *    ...
     *<tileN-1 id> <block1>,<block2>, ... ,<blockN>
     *
     *exits
     *<tile0 id> <name1>:<id1>,<name2>:<id2>, ... ,<nameN>:<idN>
     *<tile1 id> <name1>:<id1>,<name2>:<id2>, ... ,<nameN>:<idN>
     *    ...
     *<tileN-1 id> <name1>:<id1>,<name2>:<id2>, ... ,<nameN>:<idN>
     *}</pre>
     *
     *
     * For example: <br>
     * <pre>{@literal
     *1
     *2
     *Bob
     *wood,wood,wood,soil
     *
     *total:4
     *0 soil,soil,grass,wood
     *1 grass,grass,soil
     *2 soil,soil,soil,wood
     *3 grass,grass,grass,stone
     *
     *exits
     *0 east:2,north:1,west:3
     *1 south:0
     *2 west:0
     *3 east:0
     *}</pre>
     *
     * Note: Files may end with or without a single newline character, but
     * there should not be any blank lines at the end of the file. <br>
     *
     * Tile IDs are the ordering of tiles returned by getTiles()
     * i.e. tile 0 is getTiles().get(0). <br>
     *
     * Tiles must have IDs bewteen 0 and N-1, where N is the number of tiles.
     * <br>
     *
     * The ordering does not need to be checked when loading a map (but
     * the saveMap function below does when saving). <br>
     * Note: A blank line is required for an empty inventory, and lines with
     * just an ID followed by a space are required for:
     * <ul>
     *     <li> A tile entry below "total:N", if the tile has no blocks </li>
     *     <li> A tile entry below "exits", if the tile has no exits </li>
     * </ul>
     *
     * The function should do the following:
     * <ol>
     *     <li> Open the filename and read a map in the format
     *          given above. </li>
     *     <li> Construct a new Builder with the name and inventory from the
     *          file (to be returned by getBuilder()), and a starting tile set
     *          to the tile with ID 0 </li>
     *     <li> Construct a new Position for the starting position from the
     *          file to be returned as getStartPosition() </li>
     *     <li> Construct a Tile for each tile entry in the file (to be
     *          returned by getTiles() and getTile()) </li>
     *     <li> Link each tile by the exits that are given. </li>
     *     <li> Throw a WorldMapFormatException if the format of the
     *          file is incorrect. This includes:
     *          <ul>
     *                  <li> Any lines are missing, including the blank lines
     *                          before "total:N", and before exits </li>
     *                  <li> startingX or startingY (lines 1 and 2) are not
     *                          valid integers </li>
     *                  <li> There are not N entries under the line that says
     *                          "total:N" </li>
     *                  <li> There are not N entries under the "exits" line
     *                          (there should be exactly N entries and then the
     *                          file should end.) </li>
     *                  <li> N is not a valid integer, or N is negative </li>
     *                  <li> The names of blocks in inventory and on tiles are
     *                          not one of "grass", "soil", "wood", "stone"
     *                          </li>
     *                  <li> The names of exits in the "exits" sections are not
     *                          one of "north", "east", "south", "west" </li>
     *                  <li> The ids of tiles are not valid integers, are less
     *                          than 0 or greater than N - 1 </li>
     *                  <li> The ids that the exits refer to do not exist in the
     *                          list of tiles </li>
     *                  <li> loaded tiles contain too many blocks, or
     *                          GroundBlocks that have an index that is too
     *                          high (i.e., if the Tile or constructors would
     *                          throw exceptions). </li>
     *                  <li> A file operation throws an IOException that is not
     *                          a FileNotFoundException </li>
     *          </ul></li>
     *     <li> Throw a WorldMapInconsistentException if the format is
     *          correct, but tiles would end up in geometrically impossible
     *          locations (see SparseTileArray.addLinkedTiles()). </li>
     *     <li> Throw a FileNotFoundException if the file does not exist. </li>
     * </ol>
     *
     * Hint: create a SparseTileArray as a member and call
     * SparseTileArray.addLinkedTiles() to populate it.
     *
     * @param filename the name to load the file from
     * @throws WorldMapFormatException if the file is incorrectly formatted
     * @throws WorldMapInconsistentException if the file is correctly
     *         formatted, but has inconsistencies (such as overlapping tiles)
     * @throws FileNotFoundException if the file does not exist
     * @require filename != null
     * @ensure the loaded map is geometrically consistent
     */
    public WorldMap(String filename)
            throws WorldMapFormatException, WorldMapInconsistentException,
            FileNotFoundException {

        LineReader reader = new LineReader(
                new BufferedReader(new FileReader(filename)));

        try {
            // read in starting position
            String xString = reader.readLineOrThrow();
            int x = parseInt(xString, reader.errorOnLine()
                    + "Invalid integer for starting position x");

            String yString = reader.readLineOrThrow();
            int y = parseInt(yString, reader.errorOnLine()
                            + "Invalid integer for starting position y");

            Position startPosition = new Position(x, y);

            // read builder information
            String builderName = reader.readLineOrThrow();

            String inventoryString = reader.readLineOrThrow();

            List<Block> inventory = createBlockArray(inventoryString);

            reader.readBlankLineOrThrow(
                    "File ended abruptly after inventory",
                    "No blank line following inventory");

            String tileCount = reader.readLineOrThrow();

            String [] totalNumTokens = splitInTwo(tileCount, ":",
                    reader.errorOnLine() + "No colon"
                            + "separating 'total' and N",
                    reader.errorOnLine() + "Multiple colons on"
                            + " total:N line.");

            if (!totalNumTokens[0].equals("total")) {
                throw new WorldMapFormatException(reader.errorOnLine()
                        + "Missing token 'total' on total:N line.");
            }

            int numTiles = parsePositiveInt(totalNumTokens[1],
                    reader.errorOnLine()
                            + "In total:N, N is not a valid integer",
                    reader.errorOnLine()
                            + "In total:N, N is negative");

            Tile[] tiles = new Tile[numTiles];
            for (int i = 0; i < numTiles; i++) {
                String tileEntry = reader.readLineOrThrow(
                        "Missing tile under 'total:N'");

                String [] tileParts = splitInTwo(tileEntry, " ",
                        reader.errorOnLine()
                                + "No space in tile entry",
                        reader.errorOnLine()
                                + "Too many spaces in tile entry");

                int tileId = parseIntBetween(tileParts[0],
                        0, numTiles,
                        reader.errorOnLine()
                                + "Tile ID  is not a valid integer",
                        reader.errorOnLine()
                                + "Tile ID is negative",
                        reader.errorOnLine()
                                + "Tile ID is too high");

                Tile tile = createTile(tileParts[1]);
                tiles[tileId] = tile;
            }

            boolean [] hasExitLine = new boolean[numTiles];
            for (int i = 0; i < tiles.length; i++) {
                if (tiles[i] == null) {
                    throw new WorldMapFormatException("Missing entry"
                            + " for tile with ID " + i);
                }
                hasExitLine[i] = false;
            }


            // blank line, followed by an exits header
            reader.readBlankLineOrThrow("File ends abruptly"
                    + " after tile entries.",
                    "Missing blank line "
                            + "after tile entries (or too many entries).");

            String exitsLine = reader.readLineOrThrow("File ends abruptly"
                    + " after tile entries.");

            if (!exitsLine.equals("exits")) {
                throw new WorldMapFormatException(reader.errorOnLine()
                        + "Missing 'exits' token.");
            }

            // parse the exits for each Tile
            for (int i = 0; i < numTiles; i++) {
                String tileExitEntry = reader.readLineOrThrow(
                        "Missing tile under 'exits'");
                int tileId = addTileExits(tiles, tileExitEntry);
                hasExitLine[tileId] = true;

            }

            for (int i = 0; i < numTiles; i++) {
                if (!hasExitLine[i]) {
                    throw new WorldMapFormatException("Missing exit entry"
                            + "for tile." + i);
                }
            }

            reader.readEofOrThrow("Extra content in file.");


            Tile startTile = tiles[0];
            Builder builder = new Builder(builderName, startTile, inventory);
            reset(startTile, startPosition, builder);

        } catch (TooHighException e) {
            throw new WorldMapFormatException("A TooHighException would be "
                    + "thrown.");
        } catch (InvalidBlockException e) {
            throw new WorldMapFormatException(
                    "An InvalidBlockException would be thrown.");
        } catch (NoExitException e) {
            throw new WorldMapFormatException("A NoExitException would be "
                    + "thrown.");
        } catch (IOException e) {
            throw new WorldMapFormatException("Readline would throw"
                    + " an IOException");
        }
    }

    /**
     * Saves the given WorldMap to a file specified by the filename. <br>
     * See the WorldMap(filename) constructor for the format of the map. <br>
     * The Tile IDs need to relate to the ordering of tiles returned by
     * getTiles() i.e. tile 0 is getTiles().get(0) <br>
     * The function should do the following:
     * <ol>
     *     <li> Open the filename and write a map in the format
     *     given in the WorldMap constructor. </li>
     *     <li> Write the starting position (given by getStartPosition())
     *     </li>
     *     <li> Write the current builder's (given by getBuilder()) name
     *     and inventory.</li>
     *     <li> Write the number of tiles </li>
     *     <li> Write the index, and then each tile as given by
     *     getTiles() (in the same order). </li>
     *     <li> Write each tile's exits, as given by
     *     getTiles().get(id).getExits() </li>
     *     <li> Throw an IOException if the file cannot be opened for
     *     writing, or if writing fails. </li>
     *
     * </ol>
     *
     * Hint: call getTiles()
     *
     * @param filename the filename to be written to
     * @throws IOException if the file cannot be opened or written to.
     * @require filename != null
     */
    public void saveMap(String filename) throws
            IOException {


        // add everything to a string and then write that to a file
        StringBuilder toWrite = new StringBuilder();

        // start position
        toWrite.append(getStartPosition().getX()).append(LINE_SEP);
        toWrite.append(getStartPosition().getY()).append(LINE_SEP);

        // builder
        toWrite.append(getBuilder().getName()).append(LINE_SEP);
        toWrite.append(encodeBlocks(getBuilder().getInventory()));
        toWrite.append(LINE_SEP);

        List<Tile> tiles = getTiles();

        StringBuilder exits = new StringBuilder();

        // total tiles
        toWrite.append("total:").append(tiles.size()).append(LINE_SEP);

        // tile blocks (and handle tile exits using a second string builder)
        for (int i = 0; i < tiles.size(); i++) {
            toWrite.append(encodeTile(tiles.get(i), i));
            exits.append(encodeExits(tiles, tiles.get(i), i));
        }
        toWrite.append(LINE_SEP);

        // write exits
        toWrite.append("exits").append(LINE_SEP);
        toWrite.append(exits.toString());

        // write the string builder toWrite to a file
        BufferedWriter writer =
                new BufferedWriter(new FileWriter(filename));
        writer.write(toWrite.toString());

        writer.close();
    }

    /**
     * Encodes the exits of the given tile as a correctly formatted line to be
     * written to a tileArray file.
     *
     * @param tiles all the tiles in the tileArray
     * @param tile the tile to encode the exits of
     * @param id the id of the tile in the file
     * @return an encoded string representing the tile's exits
     */
    private static String encodeExits(List<Tile> tiles, Tile tile, int id) {
        StringBuilder result = new StringBuilder();
        result.append(id).append(" ");

        String sep = "";

        // encode each exit into a StringBuilder
        for (String exitName : tile.getExits().keySet()) {
            result.append(sep);
            result.append(exitName).append(":");
            result.append(tiles.indexOf(tile.getExits().get(exitName)));
            sep = ",";
        }


        result.append(LINE_SEP);

        return result.toString();
    }

    /**
     * Encodes the given tile in the correct format to be written to a tileArray
     * file.
     * @param tile the tile to be encoded
     * @param id the id of the tile in the file
     * @return the encoded tile
     */
    private static String encodeTile(Tile tile, int id) {
        return id + " " + encodeBlocks(tile.getBlocks());
    }

    /**
     * Encodes a list of blocks in the correct format to be written to a
     * world map file.
     * @param blocks the list of blocks to be encoded
     * @return the encoded block list
     */
    private static String encodeBlocks(List<Block> blocks) {

        if (blocks.size() == 0) {
            return LINE_SEP;
        }

        StringBuilder result = new StringBuilder();
        for (Block item : blocks) {
            result.append(item.getBlockType()).append(',');
        }
        result.deleteCharAt(result.length() - 1);
        result.append(LINE_SEP);

        return result.toString();
    }

    /**
     * Adds the necessary exits (as laid out in exitString) to the appropriate
     * tile in the tiles array.
     *
     * @param tiles the tiles which can be involved in an exit
     * @param exitString gives the id of the current tile, the exit names for
     *     that tile, and the other tiles which those exits tileArray to
     * @return the id of the tile read from the file
     * @throws NoExitException if Tile.addExit throws a NoExitException (should
     *         not be possible).
     * @throws WorldMapFormatException if the tile string is formatted
     *         incorrectly.
     */
    private static int addTileExits(Tile[] tiles, String exitString)
            throws NoExitException, WorldMapFormatException {
        String[] parts = exitString.split(" ", 3);

        if (parts.length == 1) {
            throw new WorldMapFormatException("No space in exit line");
        } else if (parts.length == 3) {
            throw new WorldMapFormatException("Too many spaces in exit line");
        }

        int tileId;

        try {
            tileId = Integer.parseInt(parts[0]);
        } catch (NumberFormatException nfe) {
            throw new WorldMapFormatException("Tile id in exit line is not"
                   + " a valid number");
        }

        if (tileId < 0) {
            throw new WorldMapFormatException("Tile id in exit line is "
                    + " negative");
        }

        if (tileId >= tiles.length) {
            throw new WorldMapFormatException("Tile id in exit line does "
                    + " not refer to a valid tile");
        }

        if (parts[1].equals("")) {
            // no exits on this tile.
            return tileId;
        }

        Tile current = tiles[tileId];

        // exit string is the second part
        exitString = parts[1];

        // now split on "," to separate into exits
        String[] exits = exitString.split(",");

        for (String exit : exits) {
            // split each exit again on ":" to split into
            // name and tile id.
            String[] exitInfo = exit.split(":", 3);

            if (exitInfo.length == 1) {
                throw new WorldMapFormatException("Exit line"
                      + " is missing colon.");
            } else if (exitInfo.length == 3) {
                throw new WorldMapFormatException("Exit line"
                      + " has too many colons.");
            }

            String exitName = exitInfo[0];

            switch (exitName) {
                case "north":
                case "east":
                case "south":
                case "west":
                    // these are allowed
                    break;
                default:
                    throw new WorldMapFormatException("Exit name is "
                            + "invalid.");
            }


            int otherTileId;

            try {
                otherTileId = Integer.parseInt(exitInfo[1]);
            } catch (NumberFormatException nfe) {
                throw new WorldMapFormatException("Tile id in exit line is not"
                        + " a valid number");
            }

            if (otherTileId < 0) {
                throw new WorldMapFormatException("Tile id in exit line is "
                        + " negative");
            }

            if (otherTileId >= tiles.length) {
                throw new WorldMapFormatException("Tile id in exit line does "
                        + " not refer to a valid tile");
            }


            Tile otherTile = tiles[otherTileId];

            current.addExit(exitName, otherTile);
        }

        return tileId;
    }

    /**
     * Creates a list of blocks from the information contained in the
     * blockString.
     *
     * @param blockString a formatted string containing information about
     *                    various blocks
     * @return a list of Blocks
     */
    private static List<Block> createBlockArray(String blockString)
            throws WorldMapFormatException {
        List<Block> startingBlocks = new ArrayList<>();

        if (blockString.equals("")) {
            return startingBlocks;
        }

        String[] blocks = blockString.split(",");
        for (String type : blocks) {
            Block block = decodeBlock(type);
            startingBlocks.add(block);
        }

        return startingBlocks;
    }

    /**
     * Creates a Tile from the given formatted string.
     * @param blockString a formatted string describing the starting blocks on
     *                    the Tile to be created
     * @return a new Tile object based on the blockString
     * @throws TooHighException if there is an issue with the blocks provided
     *                          in the block string
     */
    private static Tile createTile(String blockString) throws TooHighException,
            WorldMapFormatException {
        List<Block> startingBlocks = createBlockArray(blockString);
        return new Tile(startingBlocks);
    }

    /**
     * Creates a block based on the required type provided.
     * @param blockType the type of block to be created
     * @return a new block of type blockType
     */
    private static Block decodeBlock(String blockType) throws
            WorldMapFormatException {
        switch (blockType) {
            case "grass":
                return new GrassBlock();
            case "soil":
                return new SoilBlock();
            case "stone":
                return new StoneBlock();
            case "wood":
                return new WoodBlock();
            default:
                throw new WorldMapFormatException(
                        "Invalid block name specified");
        }
    }

    /**
     * Reset the WorldMap to a starting state.
     * @param startingTile the starting tile
     * @param startPosition the position of the starting tile
     * @param builder the builder
     * @throws WorldMapInconsistentException if tiles linked to the
     *         startingTile are inconsistent
     */
    private void reset(Tile startingTile, Position startPosition,
                       Builder builder)
            throws WorldMapInconsistentException {
        this.startPosition = startPosition;
        this.builder = builder;
        this.tileArray = new SparseTileArray();
        tileArray.addLinkedTiles(startingTile, startPosition.getX(),
                startPosition.getY());
    }
}
