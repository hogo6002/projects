import java.util.Date;
import java.util.Objects;

/**
 * A single item in a social media feed
 */
public class FeedItem {
    private long id; // a unique identifier of this item
    private String username; // the username of the person who posted this item
    private Date date; // the date and time when this item was posted
    private int upvotes; // the number of upvotes that this post has
    private String content; // the content of this post

    /**
     * Constructs a new FeedItem with the given parameters
     *
     * @param id the item's unique identifier
     * @param username the user who posted this item
     * @param date the date and time when this item was posted
     * @param upvotes the number of upvotes that this post has
     * @param content the content of this post
     */
    public FeedItem(long id, String username, Date date, int upvotes, String content) {
        this.id = id;
        this.username = username;
        this.date = date;
        this.upvotes = upvotes;
        this.content = content;
    }

    /**
     * @return this item's identifier
     */
    public long getId() {
        return id;
    }

    /**
     * @return the user who posted this item
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return when this item was posted
     */
    public Date getDate() {
        return date;
    }

    /**
     * @return the number of upvotes this item holds
     */
    public int getUpvotes() {
        return upvotes;
    }

    /**
     * @return the content of this post
     */
    public String getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FeedItem feedItem = (FeedItem) o;
        return id == feedItem.id &&
                upvotes == feedItem.upvotes &&
                Objects.equals(username, feedItem.username) &&
                Objects.equals(date, feedItem.date) &&
                Objects.equals(content, feedItem.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, date, upvotes, content);
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%d,%s", id, username, Util.formatDate(date),
                upvotes, content);
    }
}
