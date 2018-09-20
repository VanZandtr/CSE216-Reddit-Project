package edu.lehigh.cse216.buzzboys.admin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * App is our basic admin app.  For now, it is a demonstration of the six key 
 * operations on a database: connect, insert, update, query, delete, disconnect
 */
public class App {

    /**
     * Print the menu for our program
     */
    static void menu() {
        System.out.println("Main Menu");
        System.out.println("  [T] Create tables (Users and Messages)");
        System.out.println("  [D] Drop tables (Users and Messages");
        System.out.println("  [1] Query for a specific row in Messages");
        System.out.println("  [2] Query for a specific row in Users");
        System.out.println("  [3] Query for all rows in Messages");
        System.out.println("  [4] Query for all rows in Users");
        System.out.println("  [5] Delete a row in Messages");
        System.out.println("  [6] Delete a row in Users");
        System.out.println("  [7] Insert a new row in Messages");
        System.out.println("  [8] Insert a new row in Users");
        System.out.println("  [9] Update a row in Messages");
        System.out.println("  [0] Update a row in Users");
        System.out.println("  [-] Downvote a message in the Messages Table");
        System.out.println("  [+] Upvote a message in the Messages Table");
        System.out.println("  [V] Query for all rows in Votes Table");
        System.out.println("  [q] Quit Program");
        System.out.println("  [?] Help (this message)");
    }

    /**
     * Ask the user to enter a menu option; repeat until we get a valid option
     * @param in A BufferedReader, for reading from the keyboard
     * @return The character corresponding to the chosen menu option
     */
    static char prompt(BufferedReader in) {
        // The valid actions:
        String actions = "TDVq?1234567890-+";
        // We repeat until a valid single-character option is selected
        while (true) {
            System.out.print("[" + actions + "] :> ");
            String action;
            try {
                action = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            if (action.length() != 1){
                System.out.println("Command are one character.");
                continue;
            }
            if (actions.contains(action)) {
                return action.charAt(0);
            }
            System.out.println("Invalid Command");
        }
    }

    /**
     * Ask the user to enter a String message
     * 
     * @param in A BufferedReader, for reading from the keyboard
     * @param message A message to display when asking for input
     * 
     * @return The string that the user provided.  May be "".
     */
    static String getString(BufferedReader in, String message) {
        String s;
        try {
            System.out.print(message + " :> ");
            s = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return s;
    }

    /**
     * Ask the user to enter an integer
     * 
     * @param in A BufferedReader, for reading from the keyboard
     * @param message A message to display when asking for input
     * 
     * @return The integer that the user provided.  On error, it will be -1
     */
    static int getInt(BufferedReader in, String message) {
        int i = -1;
        try {
            System.out.print(message + " :> ");
            i = Integer.parseInt(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return i;
    }

    /**
     * The main routine runs a loop that gets a request from the user and
     * processes it
     * 
     * @param argv Command-line options.  Ignored by this program.
     */
    public static void main(String[] argv) {
        // get the Postgres configuration from the environment
        Map<String, String> env = System.getenv();
        String ip = env.get("POSTGRES_IP");
        String port = env.get("POSTGRES_PORT");
        String user = env.get("POSTGRES_USER");
        String pass = env.get("POSTGRES_PASS");

        //Heroku Connection
        //String db_url = env.get("DATABASE_URL");
        //db_url += "?sslmode=require";


        // Get a fully-configured connection to the database, or exit
        // immediately
        Database db = Database.getDatabase(ip, port, user, pass);
        //Database db = Database.getDatabase(db_url); //Heroku Connection
        if (db == null)
            return;

        // Start our basic command-line interpreter:
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            // Get the user's request, and do it
            //
            // NB: for better testability, each action should be a separate
            //     function call
            char action = prompt(in);
            if (action == '?') { //get the menu
                menu();
            } else if (action == 'q') { //quit
                break;
            } else if (action == 'T') { //Create all the tables
                db.createUsersTable(); 
                db.createMessagesTable();
                db.createVotesTable();
            } else if (action == 'D') { //Drop all the tables
                db.dropMessagesTable();
                db.dropUsersTable();
                db.dropVotesTable();
            } else if (action == '1') { //Select a message
                int id = getInt(in, "Enter the row ID");
                if (id == -1)
                    continue;
                Database.MessageRowData res = db.selectOneMessage(id);
                if (res != null) {
                    System.out.println("  [" + res.mId + "] " + "["+res.mUsername+"] "+ res.mSubject ); //added
                    System.out.println("  --> " + res.mMessage +" [" + res.mUpvotes + "/"+ res.mDownvotes+"]"); //added
                }
            } else if (action == '2'){ //Select a user
                int id = getInt(in, "Enter the row ID");
                if (id == -1)
                    continue;
                Database.UserRowData res = db.selectOneUser(id);
                if (res != null) {
                    System.out.println("  [" + res.mId + "] "+ " ["+res.mUsername+"] " + res.mFirstname +" "+ res.mLastName + " " + res.mEmail );//added
                }
            }else if (action == '3') { //Select all Messages
                ArrayList<Database.MessageRowData> res = db.selectAllFromMessages();
                if (res == null)
                    continue;
                System.out.println("  Current Database Contents");
                System.out.println("  -------------------------");
                for (Database.MessageRowData rd : res) {
                    System.out.println("  [" + rd.mId + "] "+ "["+rd.mUsername+"] " + rd.mSubject +" [" + rd.mUpvotes + "/"+ rd.mDownvotes+"]");//added
                }
            } else if (action == '4') { //Select all Users
                ArrayList<Database.UserRowData> res = db.selectAllFromUsers();
                if (res == null)
                    continue;
                System.out.println("  Current Database Contents");
                System.out.println("  -------------------------");
                for (Database.UserRowData rd : res) {
                    System.out.println("  [" + rd.mId + "] "+ " ["+rd.mUsername+"] " + rd.mFirstname +" "+ rd.mLastName + " " + rd.mEmail );//added
                }
            }else if (action == 'V') { //Select all votes
                ArrayList<Database.VoteRowData> res = db.selectAllFromVotes();
                if (res == null)
                    continue;
                System.out.println("  Current Database Contents");
                System.out.println("  -------------------------");
                for (Database.VoteRowData rd : res) {
                    System.out.println("  [" + rd.mId + "] "+ "["+rd.mMessage_Id+"] " + rd.mUsername +" [" + rd.mIs_upvote+"]");//added
                }
            }else if (action == '5') { //Delete a message
                int id = getInt(in, "Enter the row ID");
                if (id == -1)
                    continue;
                ArrayList<Database.VoteRowData> votes = db.selectVotesByMessageID(id); //Delete all votes associated with that message
                for (Database.VoteRowData rd : votes) {
                    int deleteRow = db.deleteVote(rd.mId);
                    if (deleteRow == -1)
                        continue;
                }
                int res = db.deleteMessageRow(id);
                if (res == -1)
                    continue;
                System.out.println("  " + res + " rows deleted");
            }else if (action == '6') { //Delete a user
                int id = getInt(in, "Enter the row ID");
                if (id == -1)
                    continue;
                int res = db.deleteUserRow(id);
                if (res == -1)
                    continue;
                System.out.println("  " + res + " rows deleted");
            }else if (action == '7') { //Insert new message
                String subject = getString(in, "Enter the subject");
                String message = getString(in, "Enter the message");
                String username = db.globalUsername;//added
                int upvotes = 0;//added
                int downvotes = 0;
                if (subject.equals("") || message.equals("") || username == null)
                    continue;
                int res = db.insertMessageRow(subject, message, username, upvotes, downvotes);//added
                System.out.println(res + " rows added");
            } else if (action == '8') { //Insert new user
                String username = db.globalUsername;//added
                String firstname = getString(in, "Enter your firstname (optional)");
                String lastname = getString(in, "Enter your lastname (optional)");
                String email = getString(in, "Enter your email (optional)");
                if (username == null)
                    continue;
                int res = db.insertUserRow(username, firstname, lastname, email);//added
                System.out.println(res + " rows added");
            }else if (action == '9') {//Update a message
                int id = getInt(in, "Enter the row ID :> ");
                if (id == -1)
                    continue;
                String message = getString(in, "Enter the new message");
                int upvotes = getInt(in, "Enter the new amount of upvotes");
                int downvotes = getInt(in, "Enter the new amount of downvotes");
                String username = db.globalUsername;
                int res = db.updateOneMessage(id, message, username, upvotes, downvotes);
                if (res == -1)
                    continue;
                System.out.println("  " + res + " rows updated");
            }else if (action == '0') {//Update a user
                int id = getInt(in, "Enter the row ID :> ");
                if (id == -1)
                    continue;
                String newUsername = getString(in, "Enter the new username");
                if(newUsername.isEmpty()){
                    newUsername = db.globalUsername;
                }
                String firstname = getString(in, "Enter your firstname (optional)");
                String lastname = getString(in, "Enter your lastname (optional)");
                String email = getString(in, "Enter your email (optional)");

                int res = db.updateOneUser(id, newUsername, firstname,lastname, email);
                if (res == -1)
                    continue;
                System.out.println("  " + res + " rows updated");
            }else if (action == '-') { //Downvote a message
                int id = getInt(in, "Enter the row ID :> ");
                if (id == -1){
                    continue;
                }
                else{
                    String username = "postgres";
                    int downvotes = db.getDownvotes(id);//get number of downvotes for that message;
                    System.out.println("DownVotes: " + downvotes);
                    Database.VoteRowData voteRow = db.selectVotesByMessageIDUserID(id, username);

                    if(voteRow != null && voteRow.mIs_upvote == 0){//found a downvote
                        System.out.println("Found a downvote");
                        int deleteRow = db.deleteVote(voteRow.mId); //undo downvote
                        if (deleteRow == -1)
                            continue;
                        downvotes -=1;
                    }

                    else if(voteRow != null && voteRow.mIs_upvote == 1){//found an upvote
                        System.out.println("Found an upvote");
                        int deleteRow = db.deleteVote(voteRow.mId); //undo up_vote
                        if (deleteRow == -1)
                            continue;
                        System.out.println("undo upvote");
                        System.out.println("undo downvote");
                        int upvotes = db.getUpvotes(id);//get number of downvotes for that message;
                        upvotes -= 1;
                        int res = db.updateOneMessageUp(id, upvotes);//reflect the change in downvotes
                        if (res == -1)
                            continue;

                        int insertRow = db.insertVote(id, username, 0);//insert the downvote
                        if (insertRow == -1)
                            continue;
                        System.out.println("Insert a downvote");
                        downvotes +=1;
                    }

                    else{//vote row is null
                        System.out.println("No vote found");
                        int insertRow = db.insertVote(id, username, 0);//insert the downvote
                        if (insertRow == -1)
                            continue;
                        System.out.println("Insert a downvote");
                        downvotes +=1;
                    }

                    int res = db.updateOneMessageDown(id, downvotes);//reflect the change in downvotes
                    if (res == -1)
                        continue;
                    System.out.println("  " + res + " rows updated");
                }
            }else if (action == '+') {// Upvote a message
                int id = getInt(in, "Enter the row ID :> ");
                if (id == -1){
                    continue;
                }
                else{
                    String username = db.globalUsername;
                    int upvotes = db.getUpvotes(id);//get number of downvotes for that message;
                    System.out.println("Upvotes: " + upvotes);
                    Database.VoteRowData voteRow = db.selectVotesByMessageIDUserID(id, username);
                    if(voteRow != null && voteRow.mIs_upvote == 1){//found an upvote
                        System.out.println("Found an upvote");
                        int deleteRow = db.deleteVote(voteRow.mId); //undo downvote
                        if (deleteRow == -1)
                            continue;
                        upvotes -=1;
                    }
                    
                    else if(voteRow != null && voteRow.mIs_upvote == 0){//found a downvote
                        System.out.println("Found a downvote");
                        int deleteRow = db.deleteVote(voteRow.mId); //undo downvote
                        if (deleteRow == -1)
                            continue;

                        System.out.println("undo downvote");
                        int downvotes = db.getDownvotes(id);//get number of downvotes for that message;
                        downvotes -= 1;
                        int res = db.updateOneMessageDown(id, downvotes);//reflect the change in downvotes
                        if (res == -1)
                            continue;

                        int insertRow = db.insertVote(id, username, 1);//insert the upvote
                        if (insertRow == -1)
                            continue;
                        System.out.println("Insert a upvote");
                        upvotes +=1;
                    }

                    else{//vote row is null
                        System.out.println("No vote found");
                        int insertRow = db.insertVote(id, username, 1);//insert the upvote
                        if (insertRow == -1)
                            continue;
                        System.out.println("Insert an upvote");
                        upvotes +=1;
                    }
                                        
                    int res = db.updateOneMessageUp(id, upvotes);//reflect the change in upvotes
                    if (res == -1)
                        continue;
                    System.out.println("  " + res + " rows updated");
                }
            }
        }
        // Always remember to disconnect from the database when the program
        // exits
        db.disconnect();
    }
}

