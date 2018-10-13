package edu.lehigh.cse216.buzzboys.backend;

import java.util.List;
import java.util.ArrayList;

import java.util.Date;

public class MessageStore extends DataStore<MessageLite, Message> {
    /**
     * Construct message store by calling super constructor DataStore.
     * Reset counter and create a new ArrayList
     * TODO: for all methods, get the database implementation
     */
    public MessageStore() {
        super();
    }

    /**
     *  
     * Add a new row to the DataStore
     * 
     * Note: we return -1 on an error.  There are many good ways to handle an 
     * error, to include throwing an exception.  In robust code, returning -1 
     * may not be the most appropriate technique, but it is sufficient for this 
     * tutorial.
     * 
     * @param title
     * @param message
     * @return int representing the id
     */
    public synchronized int createEntry(String title, String message, String username) {
        if (title == null || username == null)
            return -1;
        return (db.insertMessageRow(title, message, username, 0, 0) == -1) ? -1 : counter++;
    }

    /**
     * Get one complete row from the DataStore using its ID to select it
     * 
     * @param id The id of the row to select
     * @return A copy of the data in the row, if it exists, or null otherwise
     */
    @Override
    public synchronized Message readOne(int id) {
        Message data = db.selectOneMessage(id);
        return (data == null) ? null : data;
    }

    /**
     * Get all of the ids and titles that are present in the DataStore
     * @return An ArrayList with all of the data
     */
    @Override
    public synchronized List<MessageLite> readAll() {
        // NB: we copy the data returned from selectAllFrom Messages
        List<MessageLite> msgList = new ArrayList<MessageLite>(db.selectAllFromMessages());
        //remove all nulls from list
        //removeNulls(msgList);
        return (msgList == null) ? null : msgList;
    }

    /*/**
     * This reads everything from the user
     * TODO: need to make a query with joins to get this
     * @param username
     * @return
    
    public synchronized List<Message> readAllFromUser(String username) {

    }*/
    
    /**
     * Update the title and content of a row in the DataStore
     *
     * @param id The Id of the row to update
     * @param content The new content for the row
     * @return a copy of the data in the row, if it exists, or null otherwise
     */
    public synchronized Message updateMessage(int id, String title, String content) {
        // Do not update if we don't have valid data
        if (content == null || readOne(id) == null)
            return null;
        
        // Only update if the current entry is valid (not null)
        return (db.updateOneMessage(id, title, content) == -1) ? null : new Message(db.selectOneMessage(id));
    }
   
    /*For updating the upvotes and downvotes, do the computation to get the current
      number of upvotes/downvotes + 1 response from the front end, that way we don't
      have to make a call to the database when we wanna increment the number of votes
    */
    /**
     * Update the upvotes of a message
     * @param id
     * @param i - the number to increase/decrease the upvote count by
     * @return
     */
    public synchronized boolean updateUpvote(int id, int i) {
        return (readOne(id) == null) ? false : (db.updateOneMessageUp(id, i) == -1) ? false : true;
    }

    /**
     * Update the downvotes of the message
     * @param id
     * @param i - the number to increase/decrease the downvote  count by
     * @return
     */
    public synchronized boolean updateDownvote(int id, int i) {
        return (readOne(id) == null) ? false : (db.updateOneMessageDown(id, i) == -1) ? false : true;
    }

    /**
     * Delete a row from the DataStore
     * 
     * @param id The Id of the row to delete
     * @return true if the row was deleted, false otherwise
     */
    @Override
    public synchronized boolean deleteOne(int id) {
        return (readOne(id) == null) ? false : (db.deleteMessageRow(id) == -1) ? false : true;
    }
}