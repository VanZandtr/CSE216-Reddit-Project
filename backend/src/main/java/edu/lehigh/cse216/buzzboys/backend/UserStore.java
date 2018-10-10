package edu.lehigh.cse216.buzzboys.backend;

import java.util.List;
import java.util.ArrayList;


/**
 * DataStore for users. This implementation uses UserLite exclusively, because the only 
 * extra fields from the full User class are the password and salt, which should never 
 * be returned to the client.   
 */
public class UserStore extends DataStore<UserLite, UserLite> {
    
    public UserStore() {
        super();
    }

    /**
     * Inserts a user into the database
     * @param realname
     * @param username
     * @param email
     * @param password
     * @param salt
     * @return The id of the user just entered, or -1 if the insert was unsuccessful
     */
    public synchronized int createEntry(String realname, String username, String email, byte[] password, byte[] salt) {
        System.out.println(realname + " " + username + " " + email); //don't print out pass or salt
        if(realname == null || username == null || email == null || password == null || salt == null)
            return -1;
        return (db.insertUserRow(realname, username, email, password, salt) == -1) ? -1 : counter++;
    }

    /**
     * Get one user and its full contents
     */
    @Override
    public synchronized UserLite readOne(int id) {
        UserLite data = db.selectOneUser(id);
        return (data == null) ? null : data;
    }

    /**
     * Get all users with limited data
     */
    @Override
    public synchronized List<UserLite> readAll() {
        List<UserLite> userList = new ArrayList<UserLite>(db.selectAllFromUsers());
        return (userList == null) ? null : userList;
    }

    /**
     * Update the user with the given id
     * @param id
     * @param realname
     * @param username
     * @param email
     * @param password
     * @param salt
     * @return The updated user object, or null if the update failed 
     */
    public synchronized UserLite updateOne(int id, String realname, String username, String email, byte[] password, byte[] salt) {
        System.out.println(id + " " + username +  " " + realname + " " + email);
        if(readOne(id) == null || username == null || realname == null || password == null || salt == null)
            return null;
        return (db.updateOneUser(id, realname, username, email, password, salt) == -1) ? null : new UserLite(db.selectOneUser(id));
    }

    /**
     * Delete a row from the DataStore
     * 
     * @param id The Id of the row to delete
     * @return true if the row was deleted, false otherwise
     */
    @Override
    public synchronized boolean deleteOne(int id) {
        return (readOne(id) == null) ? false : (db.deleteUserRow(id) == -1) ? false : true;
    }
}