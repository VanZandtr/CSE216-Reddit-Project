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
public class User extends Row {
    /**
     * Every user enters a name field
     */
    public final String uName;

    /**
     * Create a new DataRow with the provided id and title/content, and a 
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
    User(int uid, String name) {
        super(uid);
        uName = name;
    }

    /**
     * Copy constructor to create one User from another
     */
    User(User data) {
        super(data.id);
        uName = data.uName;
    }
}