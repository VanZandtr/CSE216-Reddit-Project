package edu.lehigh.cse216.buzzboys.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.ArrayList;
import java.util.List;

import java.util.Date;

public class Database {

    /**
     * singleton instance of db
     */
    private static Database db = null;

    /**
     * The connection to the database.  When there is no connection, it should
     * be null.  Otherwise, there is a valid open connection
     */
    private Connection mConnection;

    /**
     * Selecting rows
     */

    //Messages
    private PreparedStatement mSelectOneMessage;
    private PreparedStatement mSelectAllFromMessages;

    //Users
    private PreparedStatement mSelectAllFromUsers;
    private PreparedStatement mSelectOneUser;

    //Votes
    private PreparedStatement mSelectOneVote;
    private PreparedStatement mSelectAllFromVotes;
    private PreparedStatement mSelectVoteByMessageAndUsername;
    private PreparedStatement mSelectVotesByMessageID;
    private PreparedStatement mSelectVoteByUsername;

    /**
     * Inserting rows
     */
    //Messages
    private PreparedStatement mInsertOneMessage;

    //Users
    private PreparedStatement mInsertOneUser;

    //Votes
    private PreparedStatement mInsertVote;

    /**
     * Updating rows
     */
    //Messages
    private PreparedStatement mUpdateOneMessage;
    private PreparedStatement mUpdateOneMessageTitle;
    private PreparedStatement mUpdateOneMessageUp;
    private PreparedStatement mUpdateOneMessageDown;
    

    //Users -> add more to update user
    private PreparedStatement mUpdateOneUser;

    //Votes
    private PreparedStatement mUpdateOneVote;

    /**
     * Deleting rows
     */

     //Messages
     private PreparedStatement mDeleteOneMessage;
     //Users
     private PreparedStatement mDeleteOneUser;

     //Rows
     private PreparedStatement mDeleteOneVote;
    
    /**
     * Create table statements
     */
    private PreparedStatement mCreateMessageTable;        
    private PreparedStatement mCreateUserTable;
    private PreparedStatement mCreateVoteTable;

    /**
     * Drop table statments
     */
    private PreparedStatement mDropMessagesTable;
    private PreparedStatement mDropUsersTable;
    private PreparedStatement mDropVotesTable;

    /**
     * Added a global username variable in order to retain Heroku's username
     */
    String globalUsername = "postgres";
    

    /**
     * The Database constructor is private: we only create Database objects 
     * through the getDatabase() method.
     */
    private Database() {
    }

    /**
     * Get a fully-configured connection to the database
     * 
     * @param ip   The IP address of the database server
     * @param port The port on the database server to which connection requests
     *             should be sent
     * @param user The user ID to use when connecting
     * @param pass The password to use when connecting
     * 
     * @return A Database object, or null if we cannot connect properly
     */
    
    static Database getDatabase(String ip, String port, String user, String pass) {
    //static Database getDatabase(String db_url) {

        // Create an un-configured Database object

        // Java program implementing Singleton class 
        // with getInstance() method 

        if(db == null)
            db = new Database();
/*
        //Give the Database object a connection, fail if we cannot get one
        try {
            Class.forName("org.postgresql.Driver");
            URI dbUri = new URI(db_url);
            String username = dbUri.getUserInfo().split(":")[0];
            db.globalUsername = username;//added to be able to use username
            String password = dbUri.getUserInfo().split(":")[1];
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
            Connection conn = DriverManager.getConnection(dbUrl, username, password);
            if (conn == null) {
                System.err.println("Error: DriverManager.getConnection() returned a null object");
                return null;
            }
            db.mConnection = conn;
        } catch (SQLException e) {
            System.err.println("Error: DriverManager.getConnection() threw a SQLException");
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Unable to find postgresql driver");
            return null;
        } catch (URISyntaxException s) {
            System.out.println("URI Syntax Error");
            return null;
        }
*/
        // Give the Database object a connection, fail if we cannot get one
        try {
            Connection conn = DriverManager.getConnection("jdbc:postgresql://" + ip + ":" + port + "/", user, pass);
            if (conn == null) {
                System.err.println("Error: DriverManager.getConnection() returned a null object");
                return null;
            }
            db.mConnection = conn;
        } catch (SQLException e) {
            System.err.println("Error: DriverManager.getConnection() threw a SQLException");
            e.printStackTrace();
            return null;
        }

        // Attempt to create all of our prepared statements.  If any of these 
        // fail, the whole getDatabase() call should fail
        try {
            // NB: we can easily get ourselves in trouble here by typing the
            //     SQL incorrectly.  We really should have things like "tblData"
            //     as constants, and then build the strings for the statements
            //     from those constants.

            // Note: no "IF NOT EXISTS" or "IF EXISTS" checks on table 
            // creation/deletion, so multiple executions will cause an exception
            db.mCreateMessageTable = db.mConnection.prepareStatement(
                    "CREATE TABLE messages (id SERIAL PRIMARY KEY, subject VARCHAR(50) NOT NULL, " + "message VARCHAR(500) NOT NULL, " + "username VARCHAR(20) NOT NULL, " + "upvotes INT NOT NULL, " + "downvotes INT NOT NULL, " + "date_created TIMESTAMP NOT NULL, " + "last_updated TIMESTAMP NOT NULL)");//added
            db.mCreateUserTable = db.mConnection.prepareStatement(
                    "CREATE TABLE users(id SERIAL PRIMARY KEY, username VARCHAR(20) NOT NULL, " + "firstname VARCHAR(50), " + "lastname VARCHAR(50), " + "email VARCHAR(100), " + "date_created TIMESTAMP NOT NULL)");
            db.mCreateVoteTable = db.mConnection.prepareStatement(
                    "CREATE TABLE votes (id SERIAL PRIMARY KEY, message_id INT NOT NULL, " + "username VARCHAR(20) NOT NULL, " + "is_upvote INT, " + "vote_date TIMESTAMP NOT NULL)");//added
            db.mDropUsersTable = db.mConnection.prepareStatement("DROP TABLE users");
            db.mDropMessagesTable = db.mConnection.prepareStatement("DROP TABLE messages");
            db.mDropVotesTable = db.mConnection.prepareStatement("DROP TABLE votes");
            // Standard CRUD operations
            db.mDeleteOneMessage = db.mConnection.prepareStatement("DELETE FROM messages WHERE id = ?");
            db.mDeleteOneUser = db.mConnection.prepareStatement("DELETE FROM users WHERE id = ?");
            db.mDeleteOneVote = db.mConnection.prepareStatement("DELETE FROM votes WHERE id = ?");

            db.mInsertOneMessage = db.mConnection.prepareStatement("INSERT INTO messages VALUES (default, ?, ?, ?, ?, ?, ?, ?)");//added 2 ?'s'
            db.mInsertOneUser = db.mConnection.prepareStatement("INSERT INTO users VALUES (default, ?, ?, ?, ?, ?)");//added 2
            db.mInsertVote = db.mConnection.prepareStatement("INSERT INTO votes VALUES (default, ?, ?, ?, ?)");
            db.mSelectAllFromMessages = db.mConnection.prepareStatement("SELECT id, subject, username, upvotes, downvotes FROM messages");//added
            db.mSelectAllFromUsers = db.mConnection.prepareStatement("SELECT id, username, firstname, lastname, email FROM users");//added
            db.mSelectOneMessage = db.mConnection.prepareStatement("SELECT * from messages WHERE id = ?");
            db.mSelectOneUser = db.mConnection.prepareStatement("SELECT * from users WHERE id = ?");
            db.mUpdateOneMessage = db.mConnection.prepareStatement("UPDATE messages SET message = ?, lastUpdated = ? WHERE id = ?");
            db.mUpdateOneMessageTitle = db.mConnection.prepareStatement("UPDATE messages set title = ? lastUpdated = ? WHERE id = ?");
            //Add functionality to update one field each
            db.mUpdateOneUser = db.mConnection.prepareStatement("UPDATE users SET username = ?, firstname = ?, lastname = ?, email = ? WHERE id = ?");
            db.mUpdateOneMessageUp = db.mConnection.prepareStatement("UPDATE messages SET upvotes = ? WHERE id = ?");
            db.mUpdateOneMessageDown = db.mConnection.prepareStatement("UPDATE messages SET downvotes = ? WHERE id = ?");
            db.mSelectOneVote = db.mConnection.prepareStatement("SELECT * from votes WHERE id=?");
            db.mSelectAllFromVotes= db.mConnection.prepareStatement("SELECT id, message_id, username, is_upvote FROM votes");//added
            db.mSelectVoteByMessageAndUsername = db.mConnection.prepareStatement("SELECT * from votes WHERE message_id = ? AND username = ?");
            db.mSelectVotesByMessageID = db.mConnection.prepareStatement("SELECT * from votes WHERE message_id = ?");
            db.mSelectVoteByUsername = db.mConnection.prepareStatement("SELECT * from votes WHERE username = ?");
            db.mUpdateOneVote = db.mConnection.prepareStatement("UPDATE votes SET is_upvote = ? WHERE message_id = ? AND username = ?"); //add method


        } catch (SQLException e) {
            System.err.println("Error creating prepared statement");
            e.printStackTrace();
            db.disconnect();
            return null;
        }
        return db;
    }

    /**
     * Close the current connection to the database, if one exists.
     * 
     * NB: The connection will always be null after this call, even if an 
     *     error occurred during the closing operation.
     * 
     * @return True if the connection was cleanly closed, false otherwise
     */
    boolean disconnect() {
        if (mConnection == null) {
            System.err.println("Unable to close connection: Connection was null");
            return false;
        }
        try {
            mConnection.close();
        } catch (SQLException e) {
            System.err.println("Error: Connection.close() threw a SQLException");
            e.printStackTrace();
            mConnection = null;
            return false;
        }
        mConnection = null;
        return true;
    }

    /**
     * Insert a message row into the database
     * 
     * @param subject The subject for this new row
     * @param message The message body for this new row
     * @param username The username associated with the message
     * 
     * @return The number of rows that were inserted
     */
    int insertMessageRow(String subject, String message, String username, int upvotes, int downvotes) {//added
        int count = 0;
        try {
            mInsertOneMessage.setString(1, subject);
            mInsertOneMessage.setString(2, message);
            mInsertOneMessage.setString(3, username); //added
            mInsertOneMessage.setInt(4, 0);
            mInsertOneMessage.setInt(5, 0);
            mInsertOneMessage.setTimestamp(6, new Timestamp(new Date().getTime()));
            mInsertOneMessage.setTimestamp(7, new Timestamp(new Date().getTime()));
            count += mInsertOneMessage.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    int insertUserRow(String username, String firstname, String lastname, String email) {//added
        int count = 0;
        try {
            mInsertOneUser.setString(1, username);
            mInsertOneUser.setString(2, firstname);
            mInsertOneUser.setString(3, lastname);
            mInsertOneUser.setString(4, email);
            mInsertOneUser.setTimestamp(5, new Timestamp(new Date().getTime()));
            count += mInsertOneUser.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Query the database for a list of all subjects and their IDs
     * Constructor: //public MessageRowData(int id, Date created, String subject, 
     *                                      String message, String username, 
     *                                      Integer upvotes, Integer downvotes, Date mostRecent)
     * Schema: "CREATE TABLE messages (id SERIAL PRIMARY KEY, subject VARCHAR(50) NOT NULL, " + 
     *                                 "message VARCHAR(500) NOT NULL, " + "username VARCHAR(20) NOT NULL, " + 
     *                                 "upvotes INT NOT NULL, " + "downvotes INT NOT NULL, " + 
     *                                  "date_created TIMESTAMP NOT NULL, " + "last_updated TIMESTAMP NOT NULL)")
     * @return All rows, as an ArrayList
     */
    List<MessageLite> selectAllFromMessages() {
        List<MessageLite> res = new ArrayList<MessageLite>();
        try {
            ResultSet rs = mSelectAllFromMessages.executeQuery();
            
            while (rs.next()) {
                res.add(new MessageLite(rs.getInt("id"), rs.getDate("date_created"), rs.getString("user"), rs.getString("subject"));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    

     /**
     * Query the database for a list of all users and their IDs
     * Constructor: public UserRowData(int id, Date created, String username, 
     *                                 String firstname, String lastname, String email)

     * Schema: "CREATE TABLE users(id SERIAL PRIMARY KEY, username VARCHAR(20) NOT NULL, " + 
     *                             "firstname VARCHAR(50), " + "lastname VARCHAR(50), " + 
     *                              "email VARCHAR(100), " + "date_created TIMESTAMP NOT NULL)");
     * 

     * @return All rows, as an ArrayList
     */
    List<UserLite> selectAllFromUsers() {
        List<UserLite> res = new ArrayList<UserLite>();
        try {
            ResultSet rs = mSelectAllFromUsers.executeQuery();
            while (rs.next())
                res.add(new UserLite(rs.getInt("id"), new Date(rs.getTimestamp("date_created").getTime()), rs.getString("firstname"), rs.getString("lastname")));//added
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return res;
    }

    /**
     * Get all data for a specific row, by ID
     * 
     * @param id The id of the row being requested
     * 
     * @return The data for the requested row, or null if the ID was invalid
     */
    Message selectOneMessage(int id) {
        Message res = null;
        try {
            mSelectOneMessage.setInt(1, id);
            ResultSet rs = mSelectOneMessage.executeQuery();
            if (rs.next()) {
                res = new Message(rs.getInt("id"), rs.getDate("date_created"), rs.getString("subject"), rs.getString("message"), rs.getString("username"), rs.getInt("upvotes"), rs.getInt("downvotes"), rs.getDate("last_updated"));//added
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    User selectOneUser(int id) {
        User res = null;
        try {
            mSelectOneUser.setInt(1, id);
            ResultSet rs = mSelectOneUser.executeQuery();
            if (rs.next()) {
                res = new User(rs.getInt("id"), rs.getDate("date_created"), rs.getString("firstname"), rs.getString("lastname"), rs.getString("username"), rs.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Delete a row by ID
     * 
     * @param id The id of the row to delete
     * 
     * @return The number of rows that were deleted.  -1 indicates an error.
     */
    int deleteMessageRow(int id) {
        int res = -1;
        try {
            mDeleteOneMessage.setInt(1, id);
            res = mDeleteOneMessage.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    int deleteUserRow(int id) {
        int res = -1;
        try {
            mDeleteOneUser.setInt(1, id);
            res = mDeleteOneUser.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Update the message for a row in the database
     * 
     * @param id The id of the row to update
     * @param message The new message contents
     * 
     * @return The number of rows that were updated.  -1 indicates an error.
     */
    int updateOneMessage(int id, String message) {
        int res = -1;
        try {
            mUpdateOneMessage.setString(1, message);
            mUpdateOneMessage.setTimestamp(2, new Timestamp(new Date().getTime()));
            mUpdateOneMessage.setInt(3, id);
            res = mUpdateOneMessage.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
    
    int updateOneMessageTitle(int id, String title) {
        int res = -1;
        try {
            mUpdateOneMessageTitle.setString(1, title);
            mUpdateOneMessageTitle.setTimestamp(2, new Timestamp(new Date().getTime()));
            mupdateOneMessageTitle.setInt(3, id);
            res = mUpdateOneMessageTitle.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    int updateOneMessageUp(int id, int upvotes) {
        int res = -1;
        try {
            mUpdateOneMessageUp.setInt(1, upvotes);
            mUpdateOneMessageUp.setInt(2, id);
            res = mUpdateOneMessageUp.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    int updateOneMessageDown(int id, int downvotes) {
        int res = -1;
        try {
            mUpdateOneMessageDown.setInt(1, downvotes);
            mUpdateOneMessageDown.setInt(2, id);
            res = mUpdateOneMessageDown.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    int updateOneUser(int id, String username, String firstname, String lastname, String email) {
        int res = -1;
        try {
            mUpdateOneUser.setString(1, username);
            mUpdateOneUser.setString(2, firstname);
            mUpdateOneUser.setString(3, lastname);
            mUpdateOneUser.setString(4, email);
            mUpdateOneUser.setInt(5, id);
            res = mUpdateOneUser.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    int getUpvotes(int message_id){
        int upvotes = 0;
        try {
            mSelectOneMessage.setInt(1, message_id);
            ResultSet rs = mSelectOneMessage.executeQuery();
            if (rs.next()) {
                upvotes = rs.getInt("upvotes");//added
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return upvotes;
    }

    int getDownvotes(int message_id){
        int downvotes = 0;
        try {
            mSelectOneMessage.setInt(1, message_id);
            ResultSet rs = mSelectOneMessage.executeQuery();
            if (rs.next()) {
                downvotes = rs.getInt("downvotes");//added
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return downvotes;
    }

    int insertVote(int message_id, String username, Integer is_upvote) {
        int count = 0;
        try {
            mInsertVote.setInt(1, message_id);
            mInsertVote.setString(2, username);
            mInsertVote.setInt(3, is_upvote);
            mInsertVote.setTimestamp(4, new Timestamp(new Date().getTime()));
            count += mInsertVote.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    int deleteVoteRow(int id){
        int res = -1;
        try {
            mDeleteVote.setInt(1, id);
            res = mDeleteVote.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }


    /**
     * Adds all votes to an arraylist
     * Constructor: public VoteLite(int id, Date created, 
     *                                 int message_id, String username)
     * Schema: "CREATE TABLE votes (id SERIAL PRIMARY KEY, message_id INT NOT NULL, " + 
     *                              "username VARCHAR(20) NOT NULL, " + "is_upvote INT, " + 
     *                              "vote_date TIMESTAMP NOT NULL);//added

     * @return An arrayList with all votes excluding the is_upvote category
     */
    List<VoteLite> selectAllFromVotes() {
        List<VoteLite> res = new ArrayList<VoteLite>();
        try {
            ResultSet rs = mSelectAllFromVotes.executeQuery();
            while (rs.next()) {
                res.add(new VoteLite(rs.getInt("id"), new Date(rs.getTimestamp("vote_date").getTime()), rs.getInt("message_id"), rs.getString("username")));//added
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return res;
    }


    Vote selectOneVote(int id) {
        Vote res = null;
        try {
            mSelectOneVote.setInt(1, id);
            ResultSet rs = mSelectOneVote.executeQuery();
            if (rs.next()) {
                res = new Vote(rs.getInt("id"), rs.getDate("vote_date"), rs.getInt("message_id"), rs.getString("username"), rs.getInt("is_upvote"));//added
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    Vote selectVotesByMessageIDAndUsername(int message_id, String username) {
        Vote res = null;
        try {
            mSelectVoteByMessageIDAndUsername.setInt(1, message_id);
            mSelectVoteByMessageIDAndUsername.setString(2, username);
            ResultSet rs = mSelectVoteByMessageIDAndUsername.executeQuery();
            res = new Vote(rs.getInt("id"), rs.getDate("vote_date"), rs.getInt("message_id"), rs.getString("username"), rs.getInt("is_upvote")));//added
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
    

    List<Vote> selectVotesMessageId(int message_id) {
        List<Vote> res = new ArrayList<Vote>();
        try {
            mSelectVoteByMessageID.setInt(1, message_id);
            ResultSet rs = mSelectVoteByMessageID.executeQuery();
            while(rs.next()) {
                res.add(new Vote(rs.getInt("id"), new Date(rs.getTimestamp("vote_date").getTime()), rs.getString("message_id"), rs.getString("username"), rs.getInt("is_upvote")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return res;
    }

    List<Vote> selectVoteByUsername(String username) {
        List<Vote> res = new ArrayList<Vote>();
        try {
            mSelectVoteByUsername.setString(1, username);
            ResultSet rs = mSelectVoteByUsername.executeQuery();
            while(rs.next()) {
                res.add(new Vote(rs.getInt("id"), new Date(rs.getTimestamp("vote_date").getTime()), rs.getString("message_id"), rs.getString("username"), rs.getInt("is_upvote")));
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return res;
    }

    int updateVote(int message_id, String username, Integer is_upvote) {
        int res = -1;
        try {
            mUpdateOneVote.setInt(1, is_upvote);
            mUpdateOneVote.setInt(2, message_id);
            mUpdateOneVote.setString(3, username);
            res = mUpdateOneVote.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }


    /**
     * Create user table.  If it already exists, this will print an error
     */
    void createUsersTable() {
        try {
            mCreateUserTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create message table.  If it already exists, this will print an error
     */
    void createMessagesTable() {
        try {
            mCreateMessageTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void createVotesTable() {
        try {
            mCreateVoteTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove tables from the database.  If it does not exist, this will print
     * an error.
     */
    void dropUsersTable() {
        try {
            mDropUsersTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void dropVotesTable() {
        try {
            mDropVotesTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void dropMessagesTable() {
        try {
            mDropMessagesTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

