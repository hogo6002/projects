import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class FeedAnalyserTest {

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

    }

    @Test(timeout=1000)
    public void testGetPostAfterDate() {
        assertEquals(sampleFeed[1],
                sampleAnalyser.getPostAfterDate("james.gvr",
                        Util.parseDate("01/01/2019 07:00:00")));

        assertEquals(sampleFeed[8],
                sampleAnalyser.getPostAfterDate("hob",
                        Util.parseDate("01/01/2019 07:00:00")));
    }

    @Test(timeout=1000)
    public void testGetHighestUpvote() {
        assertEquals(sampleFeed[8], sampleAnalyser.getHighestUpvote());
        assertEquals(sampleFeed[9], sampleAnalyser.getHighestUpvote());
    }

    @Test(timeout=1000)
    public void testGetPostsWithText() {
        assertEquals(Collections.singletonList(sampleFeed[2]),
                sampleAnalyser.getPostsWithText("jiaozi"));

        assertEquals(Arrays.asList(sampleFeed[4], sampleFeed[5], sampleFeed[9]),
                sampleAnalyser.getPostsWithText("no"));
    }
}
