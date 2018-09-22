package edu.lehigh.cse216.buzzboys.backend;

import java.util.Date;

/**
 * UserRow holds a row of user information.  A row of information consists of
 * an an identifier, creation date, the first and last name, and the email
 * 
 * Because we will ultimately be converting instances of this object into JSON
 * directly, we need to make the fields public.  That being the case, we will
 * not bother with having getters and setters... instead, we will allow code to
 * interact with the fields directly.
 * 
 * SCHEMA: "CREATE TABLE users(id SERIAL PRIMARY KEY, username VARCHAR(20) NOT NULL, " + 
 *                             "firstname VARCHAR(50), " + "lastname VARCHAR(50), " + 
 *                             "email VARCHAR(100), " + "date_created TIMESTAMP NOT NULL)");
 */
public class User extends UserLite {
    /**
     * Every user enters a name field
     */
    public String email;

    /**
     * Every user has a username
     */
    public String username;

    /**
     * Full constructor for user
     * @param uid
     * @param dateCreated
     * @param first
     * @param last
     * @param user
     * @param mail
     */
    User(int uid, Date dateCreated, String first, String last, String user, String mail) {
        super(uid, dateCreated, first, last);
        email = mail;
        username = name;
    }

    /**
     * Copy constructor to create one User from another
     */
    User(User data) {
        super(data.id, data.cDate, data.first, data.last);
        email = data.email;
        username = data.username;
    }
}