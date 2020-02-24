import java.util.*;

/**
 * Class that implements the social media feed searches
 */
public class FeedAnalyser {
    private List<FeedItem> orderList;
    private Map<String, List<FeedItem>> itemMap;
    private int numberVisit;

    /**
     * Loads social media feed data from a file
     *
     * @param filename the file to load from
     */
    public FeedAnalyser(String filename) {
        Iterator<FeedItem> iter = new Util.FileIterator(filename);
        orderList = new ArrayList<>();
        itemMap = new HashMap<>();
        while (iter.hasNext()) {
            FeedItem item = iter.next();
            orderList.add(item);
            try {
                itemMap.get(item.getUsername()).add(item);
            } catch (Exception e) {
                List<FeedItem> itemDataList = new ArrayList<>();
                itemDataList.add(item);
                itemMap.put(item.getUsername(), itemDataList);
            }
        }
        orderList.sort((o1, o2) -> {
            if (o1.getUpvotes() > o2.getUpvotes()) {
                return -1;
            } else if (o1.getUpvotes() < o2.getUpvotes()) {
                return 1;
            } else {
                return 0;
            }
        });
        numberVisit = 0;
        for (List<FeedItem> itemData : itemMap.values()) {
            itemData.sort((o1, o2) -> {
                if (o1.getDate().before(o2.getDate())) {
                    return -1;
                } else if (o1.getDate().after(o2.getDate())) {
                    return 1;
                } else {
                    return 0;
                }
            });
        }
    }

    /**
     * Return all feed items posted by the given username between startDate and endDate (inclusive)
     * If startDate is null, items from the beginning of the history should be included
     * If endDate is null, items until the end of the history should be included
     * The resulting list should be ordered by the date of each FeedItem
     * If no items that meet the criteria can be found, the empty list should be returned
     *
     * @param username  the user to search the posts of
     * @param startDate the date to start searching from
     * @param endDate   the date to stop searching at
     * @return a list of FeedItems made by username between startDate and endDate
     * @require username != null
     * @ensure result != null
     */
    public List<FeedItem> getPostsBetweenDates(String username,
                                               Date startDate, Date endDate) {
        List<FeedItem> feedItemList = new ArrayList<>();
        feedItemList.addAll(itemMap.get(username));
        List<FeedItem> feedItems = new ArrayList<>();
        if (startDate == null) {
            startDate = new Date(Long.MIN_VALUE);
        }
        if (endDate == null) {
            endDate = new Date(Long.MAX_VALUE);
        }
        int mid = feedItemList.size() / 2;
        int lastMid = 0;
        while (true) {
            if (lastMid - mid == 0) {
                break;
            }
            if (feedItemList.get(mid).getDate().equals(startDate)) {
                break;
            }
            if (feedItemList.get(mid).getDate().after(startDate)) {
                mid = mid / 2;
                lastMid = mid;
            } else if (feedItemList.get(mid).getDate().before(startDate)) {
                mid = (feedItemList.size() - mid) / 2;
                lastMid = mid;
            }
        }
        for (int i = mid; i < feedItemList.size(); i++) {
            if (feedItemList.get(i).getDate().after(endDate)) {
                break;
            }
            if (feedItemList.get(i).getDate().after(startDate)) {
                feedItems.add(feedItemList.get(i));
            }
        }
        return feedItems;
    }

    /**
     * Return the first feed item posted by the given username at or after searchDate
     * That is, the feed item closest to searchDate that is greater than or equal to searchDate
     * If no items that meet the criteria can be found, null should be returned
     *
     * @param username   the user to search the posts of
     * @param searchDate the date to start searching from
     * @return the first FeedItem made by username at or after searchDate
     * @require username != null && searchDate != null
     */
    public FeedItem getPostAfterDate(String username, Date searchDate) {
        List<FeedItem> feedItemList = new ArrayList<>();
        feedItemList.addAll(itemMap.get(username));
        int mid = feedItemList.size() / 2;
        int lastMid = 0;
        if (searchDate == null) {
            searchDate = new Date(Long.MIN_VALUE);
        }
        int number = 0;
        while (true) {
            if (lastMid - mid == 0 && mid != 0) {
                break;
            }
            if (lastMid - mid == 0) {
                number++;
            }
            if (number > 1) {
                break;
            }
            if (feedItemList.get(mid).getDate().equals(searchDate)) {
                break;
            }
            if (feedItemList.get(mid).getDate().after(searchDate)) {
                mid = mid / 2;
                lastMid = mid;
            } else if (feedItemList.get(mid).getDate().before(searchDate)) {
                mid = (feedItemList.size() - mid) / 2;
                lastMid = mid;
            }
        }
        if (feedItemList.get(mid).getDate().after(searchDate) ||
                feedItemList.get(mid).getDate().equals(searchDate)) {
            return feedItemList.get(mid);
        } else {
            return null;
        }

    }

    /**
     * Return the feed item with the highest upvote
     * Subsequent calls should return the next highest item
     * i.e. the nth call to this method should return the item with the nth highest upvote
     * Posts with equal upvote counts can be returned in any order
     *
     * @return the feed item with the nth highest upvote value,
     * where n is the number of calls to this method
     * @throws NoSuchElementException if all items in the feed have already been returned
     *                                by this method
     */
    public FeedItem getHighestUpvote() throws NoSuchElementException {
        numberVisit++;
        if (numberVisit == orderList.size() + 1) {
            throw new NoSuchElementException();
        }
        return orderList.get(numberVisit - 1);

    }


    /**
     * Return all feed items containing the specific pattern in the content field
     * Case should not be ignored, eg. the pattern "hi" should not be matched in the text "Hi there"
     * The resulting list should be ordered by FeedItem ID
     * If the pattern cannot be matched in any content fields the empty list should be returned
     *
     * @param pattern the substring pattern to search for
     * @return all feed items containing the pattern string
     * @require pattern != null && pattern.length() > 0
     * @ensure result != null
     */
    public List<FeedItem> getPostsWithText(String pattern) {
        // code reference from week8 lecture code.
        int[] lastOcc = lastOccurenceFunction(pattern);
        int patternLength = pattern.length();
        List<FeedItem> feedItems = new ArrayList<>();
        for (int index = 0; index < orderList.size(); index++) {
            int textIndex = pattern.length() - 1;
            int patternIndex = pattern.length() - 1;
            while (textIndex <= orderList.get(index).
                    getContent().length() - 1) {
                if (orderList.get(index).getContent().charAt(textIndex) ==
                        pattern.charAt(patternIndex)) {
                    if (patternIndex == 0) {
                        feedItems.add(orderList.get(index));
                        break;
                    } else {
                        textIndex--;
                        patternIndex--;
                    }
                } else {
                    int l =
                            lastOcc[orderList.get(index).getContent().
                                    charAt(textIndex) - 32];
                    textIndex = textIndex + patternLength -
                            Math.min(patternIndex, 1 + l);
                    patternIndex = patternLength - 1;
                }
            }
        }
        feedItems.sort((o1, o2) -> {
            if (o1.getId() > o2.getId()) {
                return 1;
            } else if (o1.getId() < o2.getId()) {
                return -1;
            } else {
                return 0;
            }
        });
        return feedItems;
    }

    private int[] lastOccurenceFunction(String pattern) {
        int i;
        int[] chars = new int[126 - 32];
        for (i = 0; i < (126 - 32); i++) {
            chars[i] = -1;
        }
        for (i = 0; i < pattern.length(); i++) {
            chars[(int) pattern.charAt(i) - 32] = i;
        }
        return chars;
    }
}
