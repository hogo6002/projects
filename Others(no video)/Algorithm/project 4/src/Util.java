import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Utility class for this assignment
 * The methods inside this class are used to assist file reading and parsing
 * You are not required to understand the code in this class,
 *      nor are you required to use it in your implementation
 */
public class Util {
    // string format for all dates in this assignment
    private static final DateFormat FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    /**
     * Takes a date as a string and outputs a Date object
     *
     * @param date the string representation of the date
     * @return the java.util.Date representation of the date
     *
     * @require the date is a valid date string
     * @ensure result != null
     */
    public static Date parseDate(String date) {
        try {
            return FORMAT.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String formatDate(Date date) {
        return FORMAT.format(date);
    }

    /**
     * Takes a line of a feed file and parses it into a FeedItem
     *
     * @param line a line in a feed csv file
     * @return the FeedItem representation of that line
     *
     * @require the input is formatted correctly
     * @ensure result != null
     */
    public static FeedItem parseFeedItem(String line) {
        try {
            String[] spl = line.split(",");
            long id = Long.parseLong(spl[0]);
            String username = spl[1];
            Date date = FORMAT.parse(spl[2]);
            int upvotes = Integer.parseInt(spl[3]);
            String content = spl[4];
            return new FeedItem(id, username, date, upvotes, content);
        } catch (IndexOutOfBoundsException | NumberFormatException |
                ParseException ex) {
            System.err.println("Unable to parse file!");
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Iterates over the lines in a feed CSV file
     */
    public static class FileIterator implements Iterator<FeedItem> {
        private String nextLine; // the next line to return from next
        private BufferedReader br; // the file reader

        /**
         * Constructs a new iterator for the given filename
         *
         * @param filename the file to iterate over the lines of
         */
        public FileIterator(String filename) {
            try {
                br = Files.newBufferedReader(Paths.get(filename));
                nextLine = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                nextLine = null;
            }
        }

        @Override
        public boolean hasNext() {
            return nextLine != null;
        }

        @Override
        public FeedItem next() {
            if (nextLine == null) {
                throw new NoSuchElementException();
            }
            FeedItem result = parseFeedItem(nextLine);
            try {
                nextLine = br.readLine();
                if (nextLine == null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
    }
}
