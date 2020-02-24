import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class FeedAnalyserTest2 {

    private FeedAnalyser sampleAnalyser;
    private static FeedItem[] sampleFeed;

    static {
        sampleFeed = new FeedItem[11];
        Iterator<FeedItem> iter = new Util.FileIterator("tst/feed-sample.csv");
        while (iter.hasNext()) {
            FeedItem next = iter.next();
            sampleFeed[(int)next.getId()] = next;
        }
    }

    @Before
    public void setup() {
        sampleAnalyser = new FeedAnalyser("tst/feed-sample.csv");
    }

    @Test(timeout=1000)
    public void testGetPostsBetweenDates() {
        assertEquals(Collections.singletonList(sampleFeed[6]),
                sampleAnalyser.getPostsBetweenDates("tom",
                        Util.parseDate("03/01/2019 12:00:00"),
                        Util.parseDate("03/01/2019 14:00:00")));

        assertEquals(Arrays.asList(sampleFeed[5], sampleFeed[7]),
                sampleAnalyser.getPostsBetweenDates("emily",
                        Util.parseDate("03/01/2019 12:00:00"),
                        Util.parseDate("03/01/2019 14:00:00")));

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

    @Test(timeout=1000)
    public void testGetPostAfterDate() {
        assertEquals(sampleFeed[1],
                sampleAnalyser.getPostAfterDate("james.gvr",
                        Util.parseDate("01/01/2019 07:00:00")));

        assertEquals(sampleFeed[8],
                sampleAnalyser.getPostAfterDate("hob",
                        Util.parseDate("01/01/2019 07:00:00")));

        assertEquals(null,
                sampleAnalyser.getPostAfterDate("emily",
                        Util.parseDate("05/01/2019 10:01:00")));
    }

    @Test(timeout=1000)
    public void testGetHighestUpvote() {
        assertEquals(sampleFeed[8], sampleAnalyser.getHighestUpvote());
        assertEquals(sampleFeed[9], sampleAnalyser.getHighestUpvote());
        assertEquals(sampleFeed[6], sampleAnalyser.getHighestUpvote());
        assertEquals(sampleFeed[5], sampleAnalyser.getHighestUpvote());
        assertEquals(sampleFeed[1], sampleAnalyser.getHighestUpvote());
        assertEquals(sampleFeed[2], sampleAnalyser.getHighestUpvote());
        assertEquals(sampleFeed[7], sampleAnalyser.getHighestUpvote());
        assertEquals(sampleFeed[3], sampleAnalyser.getHighestUpvote());
        assertEquals(sampleFeed[4], sampleAnalyser.getHighestUpvote());
        assertEquals(sampleFeed[10], sampleAnalyser.getHighestUpvote());

        try {
            sampleAnalyser.getHighestUpvote();
            fail();
        } catch (NoSuchElementException e) {

        }
    }

    @Test(timeout=1000)
    public void testGetPostsWithText() {
        assertEquals(Collections.singletonList(sampleFeed[2]),
                sampleAnalyser.getPostsWithText("jiaozi"));

        assertEquals(Arrays.asList(sampleFeed[4], sampleFeed[5], sampleFeed[9]),
                sampleAnalyser.getPostsWithText("no"));

        assertEquals(new ArrayList<FeedItem>(),
                sampleAnalyser.getPostsWithText("fdhsjkafhdjksahjkfds"));
    }
}
