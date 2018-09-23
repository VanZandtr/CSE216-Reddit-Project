package edu.lehigh.cse216.buzzboys.backend;

public class UserStore extends DataStore<UserLite, User> {
    /**
     * Constructor to build operations for userstore
     * @param ip
     * @param port
     * @param user
     * @param pass
     */
    public UserStore(String ip, String port, String user, String pass) {
        super(ip, port, user, pass);
    }

    /**
     * Create a new user and insert
     * @param first
     * @param last
     * @param user
     * @param mail
     * @return
     */
    public synchronized int createEntry(String first, String last, String user, String mail) {
        if(first == null || last == null || user == null)
            return null;
        return (db.insertUserRow(user, first, last, mail) == -1) ? -1 : counter++;
    }

    /**
     * Get one user and its full contents
     */
    @Override
    public synchronized User readOne(int id) {
        User data = db.mSelectOneUser(id);
        return (data == null) ? null : data;
    }

    /**
     * Get all users with limited data
     */
    @Override
    public synchronized List<UserLite> readAll() {
        List<UserLite> userList = new ArrayList<UserLite>(db.selectAllFromMessages());
        return (userList == null) ? null : userList;
        
    }

    /**
     * Update a whole user profile, but add functionality to update one each if data is null
     * Or just make separate methods
     * @param id
     * @param username
     * @param firstname
     * @param lastname
     * @param email
     * @return
     */
    public synchronized boolean updateOne(int id, String username, String firstname, String lastname, String email) {
        if(readOne(id) == null || username == null || firstname == null || lastname == null)
            return false;
        return (db.updateOneUser(id, username, firstname, lastname, email) == -1) ? false : true;
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