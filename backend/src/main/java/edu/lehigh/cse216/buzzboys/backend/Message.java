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
 */
public class Message extends Row {
    /**
     * Add the user of who posted message
     */
    public String userId;

    /**
     * The title for this row of data
     */
    public String mTitle;

    /**
     * The content for this row of data
     */
    public String mContent;

    /**
     * The number of upvotes for this row
     */
     public long upVotes;

     /**
      * The number of downvotes for this D
      */
      public long downVotes;


    /**
     * Create a new Message row with the provided id and title/content, and a 
     * creation date based on the system clock at the time the constructor was
     * called
     * 
     * @param id The id to associate with this row.  Assumed to be unique 
     *           throughout the whole program.
     * 
     * @param title The title string for this row of data
     * 
     * @param content The content string for this row of data
     */
    Message(int id, String title, String content) {
        super(id);
        mTitle = title;
        mContent = content;
    }

    /**
     * Copy constructor to create one message from another
     */
    Message(Message data) {
        super(data.id);
        // NB: Strings and Dates are immutable, so copy-by-reference is safe
        mTitle = data.mTitle;
        mContent = data.mContent;
    }
}
