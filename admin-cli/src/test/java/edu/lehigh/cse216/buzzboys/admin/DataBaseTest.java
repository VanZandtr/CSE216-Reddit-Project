package edu.lehigh.cse216.buzzboys.admin;

import edu.lehigh.cse216.buzzboys.admin.Database.MessageRowData;
import edu.lehigh.cse216.buzzboys.admin.Database.UserRowData;
import edu.lehigh.cse216.buzzboys.admin.Database.VoteRowData;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;


/**
 * Unit test for simple App.
 */
public class DataBaseTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public DataBaseTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(DataBaseTest.class);
    }

    /**
     * Ensure that the constructor populates every field of the object it
     * creates
     */
    public Database testDataBaseConstructor() {
        //  User(String username, String firstname, String lastname, String email)
        String ip = "127.0.0.1";
    
        String port = "5432";
        String user = "postgres";
        String pass = "Safari_3989";

        Database db = Database.getDatabase(ip, port, user, pass);
        assertTrue(db != null);

        return db;

    }

    public void testMessageRowConstructor() {
        String username = "Test User";
        String subject = "Test";
        String message = "This is a Test";
        int id = 17;
        int upvotes = 0;
        int downvotes = 0;

        Database.MessageRowData messageTest = new MessageRowData(id, subject, message, username, upvotes, downvotes);

        assertTrue(messageTest.mMessage.equals(message));
        assertTrue(messageTest.mSubject.equals(subject));
        assertTrue(messageTest.mUsername.equals(username));
        assertTrue(messageTest.mUpvotes == upvotes);
        assertTrue(messageTest.mDownvotes == downvotes);
        assertTrue(messageTest.mId == id);
    }
    

    public void testUserRowConstructor() {
        String username = "Test User";
        String firstname = "Test Firstname";
        String lastname = "Test Lastname";
        String email = "Test Email";
        int id = 17;

        Database.UserRowData userTest = new UserRowData(id, username, firstname, lastname, email);

        assertTrue(userTest.mFirstname.equals(firstname));
        assertTrue(userTest.mLastName.equals(lastname));
        assertTrue(userTest.mUsername.equals(username));
        assertTrue(userTest.mEmail.equals(email));
        assertTrue(userTest.mId == id);
    }

    public void testVoteRowConstructor() {
        //public VoteRowData(int id, int message_id, String username, int is_upvote) {
        String username = "Test User";
        int message_id = 17;
        int is_upvote = 0;
        int id = 17;

        Database.VoteRowData voteTest = new VoteRowData(id, message_id, username, is_upvote);
        assertTrue(voteTest.mUsername.equals(username));
        assertTrue(voteTest.mId == id);
        assertTrue(voteTest.mMessage_Id == message_id);
        assertTrue(voteTest.mIs_upvote == is_upvote);
    }

    /*
    public void testMessage() {
        String username = "Test User";
        String subject = "Test";
        String message = "This is a Test";
        int upvotes = 0;
        int downvotes = 0;
        int id = 1;

        Database db = testDataBaseConstructor();

        //test insert method
        int mess = db.insertMessageRow(subject, message, username, upvotes, downvotes);
        assertTrue(mess != -1);

        //test select method
        Database.MessageRowData getMess = db.selectOneMessage(mess);
        if(getMess == null){
            System.out.println("getMess is null");
        }        
        assertTrue(getMess.mMessage.equals(message));
        assertTrue(getMess.mSubject.equals(subject));
        assertTrue(getMess.mUsername.equals(username));
        assertTrue(getMess.mUpvotes == downvotes);
        assertTrue(getMess.mDownvotes == upvotes);
    }

    public void testUser() {
        String username = "Test User";
        String firstname = "Test Firstname";
        String lastname = "Test Lastname";
        String email = "Test Email";
        int id = 1;


        Database db = testDataBaseConstructor();

        //test insert method
        int user = db.insertUserRow(username, firstname, lastname, email);
        assertTrue(user != -1);

        //test select method
        Database.UserRowData getUser = db.selectOneUser(user);
        if(getUser == null){
            System.out.println("getUser is null");
        }
        assertTrue(getUser.mFirstname.equals(firstname));
        assertTrue(getUser.mLastName.equals(lastname));
        assertTrue(getUser.mUsername.equals(username));
        assertTrue(getUser.mEmail.equals(email));
    }
    */
}