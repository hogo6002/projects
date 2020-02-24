import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;

/**
 * This is my test for COMP3506 homework 4.
 * I hope it can help you a lot.
 * @author Ivan Chen
 */
public class IvanJcTest {

    private FeedAnalyser sampleAnalyser;
    private static FeedItem[] sampleFeed;

    static {
        sampleFeed = new FeedItem[27];
        Iterator<FeedItem> iter = new Util.FileIterator("tst/ivan-jc-sample.csv");
        while (iter.hasNext()) {
            FeedItem next = iter.next();
            sampleFeed[(int) next.getId()] = next;
        }
    }

    @Before
    public void setup() {
        sampleAnalyser = new FeedAnalyser("tst/ivan-jc-sample.csv");
    }

    @Test(timeout=100)
    public void testgetHighestUpvoteNormal() {
        assertEquals(sampleFeed[23], sampleAnalyser.getHighestUpvote());
        assertEquals(sampleFeed[21], sampleAnalyser.getHighestUpvote());
    }

    @Test(timeout=100)
    public void testgetHighestUpvoteLastAndException() {
        for (int i = 0; i < sampleFeed.length - 2; i++) {
            sampleAnalyser.getHighestUpvote();
        }
        assertEquals(sampleFeed[19], sampleAnalyser.getHighestUpvote());

        try {
            sampleAnalyser.getHighestUpvote();
            fail("Sorry, NoSuchElementException is not caught!");
        } catch (NoSuchElementException e) {}
    }

    @Test(timeout=100)
    public void testGetPostAfterDateStart() {
        assertEquals(sampleFeed[11],
                sampleAnalyser.getPostAfterDate("Ivan",
                        Util.parseDate("01/01/2019 13:02:00")));
        assertEquals(sampleFeed[11],
                sampleAnalyser.getPostAfterDate("Ivan",
                        Util.parseDate("01/01/2019 13:01:00")));
        assertEquals(sampleFeed[8],
                sampleAnalyser.getPostAfterDate("hob",
                        Util.parseDate("04/01/2019 10:00:00")));
    }

    @Test(timeout=100)
    public void testGetPostAfterDateMid() {
        assertEquals(sampleFeed[11],
                sampleAnalyser.getPostAfterDate("Ivan",
                        Util.parseDate("02/01/2019 13:02:00")));
        assertEquals(sampleFeed[11],
                sampleAnalyser.getPostAfterDate("Ivan",
                        Util.parseDate("02/01/2019 13:01:00")));
        assertEquals(sampleFeed[16],
                sampleAnalyser.getPostAfterDate("Lottie",
                        Util.parseDate("03/02/2019 13:01:00")));

    }

    @Test(timeout=100)
    public void testGetPostAfterDateDuplicate() {
        assertEquals(sampleFeed[24],
                sampleAnalyser.getPostAfterDate("JC",
                        Util.parseDate("25/09/1998 22:35:00")));
    }
    
    @Test(timeout=100)
    public void testGetPostAfterDateNull() {
        assertEquals(null,
                sampleAnalyser.getPostAfterDate("Lottie",
                        Util.parseDate("03/12/2019 13:02:01")));
        assertEquals(null,
                sampleAnalyser.getPostAfterDate("hob",
                        Util.parseDate("05/01/2019 10:00:00")));

        assertEquals(null,
                sampleAnalyser.getPostAfterDate("Ivan",
                        Util.parseDate("03/05/2019 13:03:00")));
    }

    @Test(timeout=1000)
    public void testGetPostsWithTextNormal() {
        assertEquals(Collections.singletonList(sampleFeed[19]),
                sampleAnalyser.getPostsWithText("stupid"));

        assertEquals(Arrays.asList(sampleFeed[11], sampleFeed[13]),
                sampleAnalyser.getPostsWithText("CSSE2310"));

        assertEquals(Arrays.asList(sampleFeed[15], sampleFeed[16], sampleFeed[25], sampleFeed[26]),
                sampleAnalyser.getPostsWithText("love"));
    }

    @Test(timeout=1000)
    public void testGetPostsWithTextEmpty() {
        assertEquals(new ArrayList<>(),
                sampleAnalyser.getPostsWithText("should return empty"));
    }

    @Test(timeout=1000)
    public void testgetPostsBetweenDatesDuplicates() {
        assertEquals(Arrays.asList(sampleFeed[24], sampleFeed[25]),
                sampleAnalyser.getPostsBetweenDates("JC",
                        Util.parseDate("25/09/1998 22:35:00"),
                        Util.parseDate("25/09/1998 22:37:00")));
    }

    @Test(timeout=1000)
    public void testGetPostsBetweenDatesNormal() {
        assertEquals(Collections.singletonList(sampleFeed[6]),
                sampleAnalyser.getPostsBetweenDates("tom",
                        Util.parseDate("03/01/2019 12:00:00"),
                        Util.parseDate("03/01/2019 14:00:00")));

        assertEquals(Arrays.asList(sampleFeed[5], sampleFeed[7]),
                sampleAnalyser.getPostsBetweenDates("emily",
                        Util.parseDate("03/01/2019 12:00:00"),
                        Util.parseDate("03/01/2019 14:00:00")));
    }

    @Test(timeout=1000)
    public void testGetPostsBetweenDatesNull() {
        assertEquals(Arrays.asList(sampleFeed[2], sampleFeed[3]),
                sampleAnalyser.getPostsBetweenDates("pah",
                        null,
                        Util.parseDate("02/01/2019 18:00:00")));

        assertEquals(Arrays.asList(sampleFeed[5], sampleFeed[7], sampleFeed[9]),
                sampleAnalyser.getPostsBetweenDates("emily",
                        Util.parseDate("03/01/2019 12:00:00"),
                        null));

        assertEquals(Arrays.asList(sampleFeed[4], sampleFeed[6]),
                sampleAnalyser.getPostsBetweenDates("tom",
                        null,
                        null));
    }


}