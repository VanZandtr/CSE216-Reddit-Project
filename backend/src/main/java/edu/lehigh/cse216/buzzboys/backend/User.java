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
    

    public byte[] uPassword;

    public String uSalt;

    /**
     * Full constructor for user
     */
    User(int id, Date date, String real, String user, String email, byte[] pass, String salt) {
        super(id, date, real, user, email);
        uPassword = pass;
        uSalt = salt;
    }

    /**
     * Copy constructor to create one User from another
     */
    User(User data) {
        super(data.id, data.cDate, data.uRealName, data.uUserName, data.uEmail);
        uPassword = data.uPassword;
        uSalt = data.uSalt;
    }
}