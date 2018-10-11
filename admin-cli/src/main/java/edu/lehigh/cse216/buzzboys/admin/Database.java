package edu.lehigh.cse216.buzzboys.admin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.ArrayList;

public class Database {
    /**
     * The connection to the database.  When there is no connection, it should
     * be null.  Otherwise, there is a valid open connection
     */
    private Connection mConnection;

    /**
     * All PreparedStatements
     */
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
    private PreparedStatement mCreateCommentTable;
    private PreparedStatement mSelectOneMessage;
    private PreparedStatement mSelectOneUser;
    private PreparedStatement mCreateVoteTable;
    private PreparedStatement mInsertVote;
    private PreparedStatement mDeleteVote;
    private PreparedStatement mSelectOneVote;
    private PreparedStatement mSelectAllFromVotes;
    private PreparedStatement mselectVotesByMessageIDUserID;
    private PreparedStatement mselectVotesByMessageID;
    private PreparedStatement mDeleteOneMessage;
    private PreparedStatement mDeleteOneUser;
    private PreparedStatement mDropMessagesTable;
    private PreparedStatement mDropUsersTable;
    private PreparedStatement mDropVotesTable;

    /**
     * Added a global username variable in order to retain Heroku's username
     */
    String globalUsername = "postgres";

    /**
     * MessageRowData holds message columns
     */
    public static class MessageRowData {
        int mId;
        String mSubject;
        String mMessage;
        String mUsername;
        int mUpvotes;
        int mDownvotes;

        public MessageRowData(int id, String subject, String message, String username, int upvotes, int downvotes) {
            mId = id;
            mSubject = subject;
            mMessage = message;
            mUsername = username;
            mUpvotes = upvotes;
            mDownvotes = downvotes;
        }
    }

    /**
     * UserRowData holds user information
     */
    public static class UserRowData {
        int mId;
        String mUsername;
        String mFirstname;
        String mLastName;
        String mEmail;

        public UserRowData(int id, String username, String firstname, String lastname, String email) {
            mId = id;
            mUsername = username;
            mFirstname = firstname;
            mLastName = lastname;
            mEmail = email;
        }
    }

    /**
     * VoteRowData holds messages' vote records
     */
    public static class VoteRowData {
        int mId;
        int mMessage_Id;
        String mUsername;//added
        int mIs_upvote;

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
    
    static Database getDatabase(String ip, String port, String user, String pass) {//Postgres Connection
    //static Database getDatabase(String db_url) {//Heroku Connection

        // Create an un-configured Database object
        Database db = new Database();
/*
        //Heroku Connection
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

            //Create Tables
            db.mCreateMessageTable = db.mConnection.prepareStatement(
                    "CREATE TABLE msg (mid SERIAL PRIMARY KEY, title VARCHAR(50) " + "NOT NULL, body VARCHAR(140) NOT NULL," + "uid INTEGER NOT NULL," + "date_created DATETIME," + "last_updated DATETIME" + "upvotes INTEGER NOT NULL," + " downvotes INTEGER NOT NULL)");//added
            db.mCreateUserTable = db.mConnection.prepareStatement(
                    "CREATE TABLE user(uid SERIAL PRIMARY KEY, username VARCHAR(20) " + "NOT NULL, realname VARCHAR(255)," + "email VARCHAR(100),"+"salt BYTEA,"+"password BYTEA)");
            db.mCreateVoteTable = db.mConnection.prepareStatement(
                    "CREATE TABLE vote (vid SERIAL PRIMARY KEY, mid INTEGER " + "NOT NULL, uid INTEGER NOT NULL," + "is_upvote INT NOT NULL)");
            db.mCreateCommentTable = db.mConnection.prepareStatement(
                    "CREATE TABLE com (cid SERIAL PRIMARY KEY, uid INTEGER NOT NULL, mid INTEGER NOT NULL, content VARCHAR(140), date_created DATETIME)");

            //Drop Tables                    
            db.mDropUsersTable = db.mConnection.prepareStatement("DROP TABLE user");
            db.mDropMessagesTable = db.mConnection.prepareStatement("DROP TABLE msg");
            db.mDropVotesTable = db.mConnection.prepareStatement("DROP TABLE vote");

            // Standard CRUD operations
            db.mDeleteOneMessage = db.mConnection.prepareStatement("DELETE FROM messages WHERE id = ?");
            db.mDeleteOneUser = db.mConnection.prepareStatement("DELETE FROM users WHERE id = ?");
            db.mInsertOneMessage = db.mConnection.prepareStatement("INSERT INTO messages VALUES (default, ?, ?, ?, ?, ?)");
            db.mInsertOneUser = db.mConnection.prepareStatement("INSERT INTO users VALUES (default, ?, ?, ?, ?)");
            db.mSelectAllFromMessages = db.mConnection.prepareStatement("SELECT id, subject, username, upvotes, downvotes FROM messages");
            db.mSelectAllFromUsers = db.mConnection.prepareStatement("SELECT id, username, firstname, lastname, email FROM users");
            db.mSelectOneMessage = db.mConnection.prepareStatement("SELECT * from messages WHERE id = ?");
            db.mSelectOneUser = db.mConnection.prepareStatement("SELECT * from users WHERE id = ?");
            db.mUpdateOneMessage = db.mConnection.prepareStatement("UPDATE messages SET message = ?, username = ?, upvotes = ?, downvotes = ? WHERE id = ?");
            db.mUpdateOneUser = db.mConnection.prepareStatement("UPDATE users SET username = ?, firstname = ?, lastname = ?, email = ? WHERE id = ?");
            db.mUpdateOneMessageUp = db.mConnection.prepareStatement("UPDATE messages SET upvotes = ? WHERE id = ?");
            db.mUpdateOneMessageDown = db.mConnection.prepareStatement("UPDATE messages SET downvotes = ? WHERE id = ?");
            db.mInsertVote = db.mConnection.prepareStatement("INSERT INTO votes VALUES (default, ?, ?, ?)");
            db.mDeleteVote = db.mConnection.prepareStatement("DELETE FROM votes WHERE id = ?");
            db.mSelectOneVote = db.mConnection.prepareStatement("SELECT * from votes WHERE id=?");
            db.mSelectAllFromVotes= db.mConnection.prepareStatement("SELECT id, message_id, username, is_upvote FROM votes");//added
            db.mselectVotesByMessageIDUserID = db.mConnection.prepareStatement("SELECT * from votes WHERE message_id = ? AND username = ?");
            db.mselectVotesByMessageID = db.mConnection.prepareStatement("SELECT * from votes WHERE message_id = ?");

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
     * Methods to insert, delete, and change rows in tables: Users, Messages, and Votes
    */

    /**
     *  insert new message
     */ 
    int insertMessageRow(String subject, String message, String username, int upvotes, int downvotes) {//added
        int count = 0;
        try {
            mInsertOneMessage.setString(1, subject);
            mInsertOneMessage.setString(2, message);
            mInsertOneMessage.setString(3, username); //added
            mInsertOneMessage.setInt(4, 0);
            mInsertOneMessage.setInt(5, 0);
            count += mInsertOneMessage.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     *  insert new user
     */ 
    int insertUserRow(String username, String firstname, String lastname, String email) {//added
        int count = 0;
        try {
            mInsertOneUser.setString(1, username);
            mInsertOneUser.setString(2, firstname);
            mInsertOneUser.setString(3, lastname);
            mInsertOneUser.setString(4, email);
            count += mInsertOneUser.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     *  select all messages
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
     *  select all users
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
     *  select one message by id
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

    
    /**
     *  select one user by id
     */ 
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
     *  delete a message by id
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

    /**
     *  delete a user by id
     */ 
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
     */
    int updateOneMessage(int id, String message, String username, int upvotes, int downvotes) {
        int res = -1;
        try {
            mUpdateOneMessage.setString(1, message);
            mUpdateOneMessage.setString(2, username);
            mUpdateOneMessage.setInt(3, upvotes);
            mUpdateOneMessage.setInt(4, downvotes);
            mUpdateOneMessage.setInt(5, id);
            res = mUpdateOneMessage.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Update the upvotes for a message by id
     */
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

      /**
     * Update the downvotes for a message by id
     */
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

    /**
     * Update a user with new username, first and lastname, and email
     */
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

    /**
     * return the number of upvotes a message has
     */
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

    /**
     * return the number of downvotes a message has
     */
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

    /**
     * insert a new vote on a message into votes table
     */
    int insertVote(int message_id, String username, int is_upvote) {
        int count = 0;
        try {
            mInsertVote.setInt(1, message_id);
            mInsertVote.setString(2, username);
            mInsertVote.setInt(3, is_upvote);
            count += mInsertVote.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

     /**
     * delete a vote by id 
     * 
     * this happens on message deletion or a change in vote by a user
     */
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

     /**
     * select all votes
     */
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


     /**
     * selct one vote by id its id
     */
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

     /**
     * select one user's vote on one message
     */
    VoteRowData selectVotesByMessageIDUserID(int message_id, String username) {
        VoteRowData res = null;
        try {
            mselectVotesByMessageIDUserID.setInt(1, message_id);
            mselectVotesByMessageIDUserID.setString(2, username);
            ResultSet rs = mselectVotesByMessageIDUserID.executeQuery();
            if (rs.next()) {
                res = new VoteRowData(rs.getInt("id"), rs.getInt("message_id"), rs.getString("username"), rs.getInt("is_upvote"));//added
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

     /**
     * select all votes that a message has 
     * 
     * this is used if a message is deleted
     */
    ArrayList<VoteRowData> selectVotesByMessageID(int message_id) {
        ArrayList<VoteRowData> res = new ArrayList<VoteRowData>();
        try {
            mselectVotesByMessageID.setInt(1, message_id);
            ResultSet rs = mselectVotesByMessageIDUserID.executeQuery();
            while (rs.next()) {
                res.add(new VoteRowData(rs.getInt("id"), rs.getInt("message_id"), rs.getString("username"), rs.getInt("is_upvote")));//added
            }
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

    
    /**
     * Create vote table.  If it already exists, this will print an error
     */
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

