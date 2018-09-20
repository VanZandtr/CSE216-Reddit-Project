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

import java.util.Date;

public class Database {
    /**
     * The connection to the database.  When there is no connection, it should
     * be null.  Otherwise, there is a valid open connection
     */
    private Connection mConnection;
    
    private PreparedStatement mInsertOneMessage;
    
    private PreparedStatement mInsertOneUser;
    
    private PreparedStatement mSelectAllFromMessages;
    
    private PreparedStatement mSelectAllFromUsers;

    private PreparedStatement mUpdateOneMessage;
    private PreparedStatement mUpdateOneMessageUp;
    private PreparedStatement mUpdateOneMessageDown;

    
    private PreparedStatement mUpdateOneUser;

    private PreparedStatement mCreateMessageTable;
            
    private PreparedStatement mCreateUserTable;

    private PreparedStatement mSelectOneMessage;

    private PreparedStatement mSelectOneUser;

    private PreparedStatement mCreateVoteTable;

    private PreparedStatement mInsertVote;

    private PreparedStatement mDeleteVote;

    private PreparedStatement mSelectOneVote;

    private PreparedStatement mSelectAllFromVotes;
    private PreparedStatement mselectVoteByMessageID;

    private PreparedStatement mUpdateOneVote;



    /**
     * A prepared statement for deleting a row from the database
     */
    private PreparedStatement mDeleteOneMessage;

    private PreparedStatement mDeleteOneUser;


    /**
     * A prepared statement for dropping the tables in our database
     */
    private PreparedStatement mDropMessagesTable;

    private PreparedStatement mDropUsersTable;

    private PreparedStatement mDropVotesTable;

    /**
     * Added a global username variable in order to retain Heroku's username
     */
    String globalUsername = "postgres";
    
    /**
     * RowData is like a struct in C: we use it to hold data, and we allow 
     * direct access to its fields.  In the context of this Database, RowData 
     * represents the data we'd see in a row.
     * 
     * We make RowData a static class of Database because we don't really want
     * to encourage users to think of RowData as being anything other than an
     * abstract representation of a row of the database.  RowData and the 
     * Database are tightly coupled: if one changes, the other should too.
     */
    public abstract static class RowData {
        /**
         * Every row has an id
         */
        int id;
        
        /**
         * Every row has a title
         */
        Date cDate;
        
        /**
         * Constructor for RowData with fields that every type of row has
         */
        public RowData(int rid, Date date) {
            this.id = id;
            cDate = date;
        }
    }

    /**
     * MessageData is an extension of RowData that holds fields for a message specifically
     * The static feature is explained in the javaodc for RowData
     */
    public static class MessageRowData extends RowData {
        /**
         * The subject stored in this row
         */
        String mSubject;
        /**
         * The message stored in this row
         */
        String mMessage;
        

        /**
         * The username stored in this row
         */
        String mUsername;//added
        /**
         * The number of likes stored in this row
         */
        int mUpvotes;//added
        int mDownvotes;

        /**
         * The last updated date
         */
        Date lastUpdate;

        /**
         * Construct a RowData object by providing values for its fields
         */
        public MessageRowData(int id, String subject, String message, String username, int upvotes, int downvotes) {
            super(id, new Date());
            mSubject = subject;
            mMessage = message;
            mUsername = username; //added
            mUpvotes = upvotes;//added
            mDownvotes = downvotes;
            lastUpdate = new Date();
        }
    }

    public static class UserRowData extends RowData {

        /**
         * The username stored in this row
         */
        String mUsername;
        String mFirstname;
        String mLastName;
        String mEmail;

        /**
         * Construct a RowData object by providing values for its fields
         */
        public UserRowData(int id, String username, String firstname, String lastname, String email) {
            super(id, new Date());
            mUsername = username;
            mFirstname = firstname;
            mLastName = lastname;
            mEmail = email;
        }
    }

    public static class VoteRowData {
        /**
         * The ID of this row of the database
         */
        int mId;

        int mMessage_Id;
        /**
         * The username stored in this row
         */
        String mUsername;//added

        int mIs_upvote;

        /**
         * Construct a RowData object by providing values for its fields
         */
        public VoteRowData(int id, int message_id, String username, int is_upvote) {
            mId = id;
            mUsername = username;
            mIs_upvote = is_upvote;
            mMessage_Id = message_id;
        }
    }




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
        Database db = new Database();
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
                    "CREATE TABLE messages (id SERIAL PRIMARY KEY, subject VARCHAR(50) NOT NULL, " + "message VARCHAR(500) NOT NULL, " + "username VARCHAR(20) NOT NULL, " + "upvotes INT NOT NULL, " + "downvotes INT NOT NULL, " + "dateCreated TIMESTAMP NOT NULL, " + "lastUpdated TIMESTAMP NOT NULL)");//added
            db.mCreateUserTable = db.mConnection.prepareStatement(
                    "CREATE TABLE users(id SERIAL PRIMARY KEY, username VARCHAR(20) NOT NULL, " + "firstname VARCHAR(50), " + "lastname VARCHAR(50), " + "email VARCHAR(100), " + "dateCreated TIMESTAMP NOT NULL)");
            db.mCreateVoteTable = db.mConnection.prepareStatement(
                    "CREATE TABLE votes (id SERIAL PRIMARY KEY, message_id INT NOT NULL, " + "username VARCHAR(20) NOT NULL, " + "is_upvote INT, " + "voteDate TIMESTAMP NOT NULL)");//added
            db.mDropUsersTable = db.mConnection.prepareStatement("DROP TABLE users");
            db.mDropMessagesTable = db.mConnection.prepareStatement("DROP TABLE messages");
            db.mDropVotesTable = db.mConnection.prepareStatement("DROP TABLE votes");
            // Standard CRUD operations
            db.mDeleteOneMessage = db.mConnection.prepareStatement("DELETE FROM messages WHERE id = ?");
            db.mDeleteOneUser = db.mConnection.prepareStatement("DELETE FROM users WHERE id = ?");
            db.mInsertOneMessage = db.mConnection.prepareStatement("INSERT INTO messages VALUES (default, ?, ?, ?, ?, ?, ?, ?)");//added 2 ?'s'
            db.mInsertOneUser = db.mConnection.prepareStatement("INSERT INTO users VALUES (default, ?, ?, ?, ?, ?)");//added 2
            db.mSelectAllFromMessages = db.mConnection.prepareStatement("SELECT id, subject, username, upvotes, downvotes FROM messages");//added
            db.mSelectAllFromUsers = db.mConnection.prepareStatement("SELECT id, username, firstname, lastname, email FROM users");//added
            db.mSelectOneMessage = db.mConnection.prepareStatement("SELECT * from messages WHERE id = ?");
            db.mSelectOneUser = db.mConnection.prepareStatement("SELECT * from users WHERE id = ?");
            db.mUpdateOneMessage = db.mConnection.prepareStatement("UPDATE messages SET message = ?, lastUpdated = ? WHERE id = ?");
            db.mUpdateOneUser = db.mConnection.prepareStatement("UPDATE users SET username = ?, firstname = ?, lastname = ?, email = ? WHERE id = ?");
            db.mUpdateOneMessageUp = db.mConnection.prepareStatement("UPDATE messages SET upvotes = ? WHERE id = ?");
            db.mUpdateOneMessageDown = db.mConnection.prepareStatement("UPDATE messages SET downvotes = ? WHERE id = ?");
            db.mInsertVote = db.mConnection.prepareStatement("INSERT INTO votes VALUES (default, ?, ?, ?, ?)");
            db.mDeleteVote = db.mConnection.prepareStatement("DELETE FROM votes WHERE id = ?");
            db.mSelectOneVote = db.mConnection.prepareStatement("SELECT * from votes WHERE id=?");
            db.mSelectAllFromVotes= db.mConnection.prepareStatement("SELECT id, message_id, username, is_upvote FROM votes");//added
            db.mselectVoteByMessageID = db.mConnection.prepareStatement("SELECT * from votes WHERE message_id = ? AND username = ?");
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
     * 
     * @return All rows, as an ArrayList
     */
    ArrayList<MessageRowData> selectAllFromMessages() {
        ArrayList<MessageRowData> res = new ArrayList<MessageRowData>();
        try {
            ResultSet rs = mSelectAllFromMessages.executeQuery();
            while (rs.next()) {
                res.add(new MessageRowData(rs.getInt("id"), rs.getString("subject"), null, rs.getString("username"), rs.getInt("upvotes"), rs.getInt("downvotes")));//added
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
     * 
     * @return All rows, as an ArrayList
     */
    ArrayList<UserRowData> selectAllFromUsers() {
        ArrayList<UserRowData> res = new ArrayList<UserRowData>();
        try {
            ResultSet rs = mSelectAllFromUsers.executeQuery();
            while (rs.next()) {
                res.add(new UserRowData(rs.getInt("id"), rs.getString("username"), rs.getString("firstname"), rs.getString("lastname"), rs.getString("email")));//added
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get all data for a specific row, by ID
     * 
     * @param id The id of the row being requested
     * 
     * @return The data for the requested row, or null if the ID was invalid
     */
    MessageRowData selectOneMessage(int id) {
        MessageRowData res = null;
        try {
            mSelectOneMessage.setInt(1, id);
            ResultSet rs = mSelectOneMessage.executeQuery();
            if (rs.next()) {
                res = new MessageRowData(rs.getInt("id"), rs.getString("subject"), rs.getString("message"), rs.getString("username"), rs.getInt("upvotes"), rs.getInt("downvotes"));//added
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    UserRowData selectOneUser(int id) {
        UserRowData res = null;
        try {
            mSelectOneUser.setInt(1, id);
            ResultSet rs = mSelectOneUser.executeQuery();
            if (rs.next()) {
                res = new UserRowData(rs.getInt("id"), rs.getString("username"), rs.getString("firstname"), rs.getString("lastname"), rs.getString("email"));
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

    int insertVote(int message_id, String username, int is_upvote) {
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

    int deleteVote(int id){
        int res = -1;
        try {
            mDeleteVote.setInt(1, id);
            res = mDeleteVote.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    ArrayList<VoteRowData> selectAllFromVotes() {
        ArrayList<VoteRowData> res = new ArrayList<VoteRowData>();
        try {
            ResultSet rs = mSelectAllFromVotes.executeQuery();
            while (rs.next()) {
                res.add(new VoteRowData(rs.getInt("id"), rs.getInt("message_id"), rs.getString("username"), rs.getInt("is_upvote")));//added
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    VoteRowData selectOneVote(int id) {
        VoteRowData res = null;
        try {
            mSelectOneVote.setInt(1, id);
            ResultSet rs = mSelectOneVote.executeQuery();
            if (rs.next()) {
                res = new VoteRowData(rs.getInt("id"), rs.getInt("message_id"), rs.getString("username"), rs.getInt("is_upvote"));//added
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    VoteRowData selectVoteByMessageID(int message_id, String username) {
        VoteRowData res = null;
        try {
            mselectVoteByMessageID.setInt(1, message_id);
            mselectVoteByMessageID.setString(2, username);
            ResultSet rs = mselectVoteByMessageID.executeQuery();
            if (rs.next()) {
                res = new VoteRowData(rs.getInt("id"), rs.getInt("message_id"), rs.getString("username"), rs.getInt("is_upvote"));//added
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    int updateVote(int message_id, String username, Integer is_upvote) {
        int res = -1;
        try {
            if(is_upvote == null)
                mUpdateOneVote.setInt(1, is_upvote);
            else 
                mUpdateOneVote.setInt(1, is_upvote.intValue());
            
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

