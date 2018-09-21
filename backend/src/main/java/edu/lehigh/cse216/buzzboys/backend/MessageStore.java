package edu.lehigh.cse216.buzzboys.backend;

public class MessageStore extends DataStore {
    /**
     * Construct message store by calling super constructor DataStore.
     * Reset counter and create a new ArrayList
     * TODO: for all methods, get the database implementation
     */
    public MessageStore(String ip, String port, String user, String pass) {
        super(ip, port, user, pass);
    }

    /**
     * Method to add a new message
     * @param name
     * @return
     */
    public synchronized int createEntry(String name) {
        if (name == null)
            return -1;
        int id = counter++;
        Row data = new User(id, name);
        //db add message row
        return id;
    }

        /**
     * Get one complete row from the DataStore using its ID to select it
     * 
     * @param id The id of the row to select
     * @return A copy of the data in the row, if it exists, or null otherwise
     */
    public synchronized Message readOne(int id) {
        Database.MessageRowData data = db.selectOne(id);
        return data == null ? null : new Message(data.getId(), data.getSubject(), data.getMessage());
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