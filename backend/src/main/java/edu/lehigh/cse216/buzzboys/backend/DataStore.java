package edu.lehigh.cse216.buzzboys.backend;

import java.util.ArrayList;
import java.util.List;

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
public abstract class DataStore<T extends Row, U extends Row> {
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
    DataStore() {
        counter = 0;
        db = Database.getDatabase();
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
     * Removes nulls from a list
     * Useful when there is a chance that null rows will be apparent
     * Be careful when using this because the time is O(kn)
     * k being the number of nulls and n being the length of the list
     * @param list
     */
    public synchronized void removeNulls(List<T> list) {
        while(list.remove(null));
    }
    
    //Every type of store should have these methods
    //Also need update and insert statements but they are hard to abstract
    //There will be many different types of updates

    //Might have to change type if java does allow me to return a Message in place of a Row

    /**
     * Get one complete row from the DataStore using its ID to select it
     * 
     * @param id The id of the row to select
     * @return A copy of the data in the row, if it exists, or null otherwise
     */
    public abstract U readOne(int id);

    /**
     * Get all of the ids and titles that are present in the DataStore
     * @return A copy of the populated ArrayList with all of the data
     */
    public abstract List<T> readAll(); 
    

    /**
     * Delete a row from the DataStore
     * 
     * @param id The Id of the row to delete
     * @return true if the row was deleted, false otherwise
     */
    public abstract boolean deleteOne(int id);
}