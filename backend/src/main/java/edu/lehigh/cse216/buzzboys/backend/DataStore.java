package edu.lehigh.cse216.buzzboys.backend;

import java.util.ArrayList;

/**
 * DataStore provides access to a set of objects, and makes sure that each has
 * a unique identifier that remains unique even after the object is deleted.
 * 
 * We follow the convention that member fields of a class have names that start
 * with a lowercase 'm' character, and are in camelCase.
 * 
 * NB: The methods of DataStore are synchronized, since they will be used from a 
 * web framework and there may be multiple concurrent accesses to the DataStore.
 */
public abstract class DataStore {
    /**
     * The rows of data in our DataStore
     */
    Database db;

    /**
     * A counter for keeping track of the next ID to assign to a new row
     */
    public int counter;



    /**
     * Construct the DataStore by resetting its counter and creating the
     * ArrayList for the rows of data.
     */
    DataStore(String ip, String port, String user, String pass) {
        counter = 0;
        db = Database.getDatabase(ip, port, user, pass);
    }

    /**
     * Add a new row to the DataStore
     * 
     * Note: we return -1 on an error.  There are many good ways to handle an 
     * error, to include throwing an exception.  In robust code, returning -1 
     * may not be the most appropriate technique, but it is sufficient for this 
     * tutorial.
     * 
     * @param title The title for this newly added row
     * @param content The content for this row
     * @return the ID of the new row, or -1 if no row was created
     */


    /**
     * Get one complete row from the DataStore using its ID to select it
     * 
     * @param id The id of the row to select
     * @return A copy of the data in the row, if it exists, or null otherwise
     */
    public synchronized DataRow readOne(int id) {
        Database.RowData data = db.selectOne(id);
        return data == null ? null : new DataRow(data.getId(), data.getSubject(), data.getMessage());
    }

    /**
     * Get all of the ids and titles that are present in the DataStore
     * @return An ArrayList with all of the data
     */
    public synchronized ArrayList<DataRowLite> readAll() {
    ArrayList<DataRowLite> data = new ArrayList<>();
        // NB: we copy the data, so that our ArrayList only has ids and titles
        for (Database.RowData row : db.selectAll()) {
            if (row != null)
                data.add(new DataRowLite(new DataRow(row.getId(), row.getSubject(), null)));
        }
        return data;
    }
    
    /**
     * Update the title and content of a row in the DataStore
     *
     * @param id The Id of the row to update
     * @param title The new title for the row
     * @param content The new content for the row
     * @return a copy of the data in the row, if it exists, or null otherwise
     */
    public synchronized DataRow updateOne(int id, String title, String content) {
        // Do not update if we don't have valid data
        if (title == null || content == null)
            return null;
        if(readOne(id) == null)
            return null;
        
        // Only update if the current entry is valid (not null)
        return (db.updateOne(id, title, content) == -1) ? null : new DataRow(id, title, content);

    }

    /**
     * Delete a row from the DataStore
     * 
     * @param id The Id of the row to delete
     * @return true if the row was deleted, false otherwise
     */
    public synchronized boolean deleteOne(int id) {
        // Deletion fails for an invalid Id or an Id that has already been 
        // deleted
        if(readOne(id) == null) 
            return false;
        return (db.deleteRow(id) == -1) ? false : true;
    }
}