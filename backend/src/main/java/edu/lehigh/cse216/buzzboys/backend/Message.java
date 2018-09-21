package edu.lehigh.cse216.buzzboys.backend;

import java.util.Date;

/**
 * DataRow holds a row of information.  A row of information consists of
 * an identifier, strings for a "title" and "content", and a creation date.
 * 
 * Because we will ultimately be converting instances of this object into JSON
 * directly, we need to make the fields public.  That being the case, we will
 * not bother with having getters and setters... instead, we will allow code to
 * interact with the fields directly.
 * 
 * Schema: CREATE TABLE messages (id SERIAL PRIMARY KEY, subject VARCHAR(50) NOT NULL, " 
 *                                + "message VARCHAR(500) NOT NULL, " + "username VARCHAR(20) NOT NULL, "
 *                                + "upvotes INT NOT NULL, " + "downvotes INT NOT NULL, " 
 *                                + "dateCreated TIMESTAMP NOT NULL, " + "lastUpdated TIMESTAMP NOT NULL)"
 */
public class Message extends Row {
    /**
     * Add the user of who posted message
     */
    public String userId;

    /**
     * The title for this message
     */
    public String mTitle;

    /**
     * The content for this message
     */
    public String mContent;

    /**
     * The number of upvotes for this message
     */
    public int upVotes;

    /**
     * The number of downvotes for this message
    */
    public int downVotes;

    /**
     * The last updated date of a message
     */
    public Date lastUpdated;
      
    /**
     * Create a new Message row with the provided id and date created, user, 
     * title, content and a creation date based on the system clock at the time 
     * the constructor was called
     * @param id
     * @param dateCreated
     * @param user
     * @param title
     * @param content
     * @param up
     * @param down
     * @param last
     */
    Message(int id, Date dateCreated, String user, String title, String content, int up, int down, Date last) {
        super(id, dateCreated);
        mTitle = title;
        mContent = content;
        userId = user;
        upVotes = up;
        downVotes = down;
        lastUpdated = last;
    }

    /**
     * Copy constructor to create one message from another
     */
    Message(Message data) {
        super(data.id, data.cDate);
        // NB: Strings and Dates are immutable, so copy-by-reference is safe
        mTitle = data.mTitle;
        mContent = data.mContent;
        userId = data.userId;
        upVotes = data.upVotes;
        downVotes = data.downVotes;
        lastUpdated = data.lastUpdated;
    }
}
