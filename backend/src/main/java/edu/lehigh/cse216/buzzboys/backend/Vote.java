package edu.lehigh.cse216.buzzboys.backend;

import java.util.Date;


/**
 * Class that defines the rows from the votes table
 * Schema: "CREATE TABLE votes (id SERIAL PRIMARY KEY, message_id INT NOT NULL, " 
 * + "username VARCHAR(20) NOT NULL, " + "is_upvote INT, " + "voteDate TIMESTAMP NOT NULL)");//added
 * id and date is got from abstract row type
 */
public class Vote extends Row {
    /**
     * Representation of foreign key message_id from message in vote
     */
    int message_id;

    /**
     * Representation of foreign key user from user table in vote
     */
    String username;

    /**
     * is_upvote depends if this user upvote, downvoted or abstained to vote on a certain post
     */
    Integer is_upvote;

    public Vote(int voteid, Date voteCreated, int msg_id, String user, Integer vote) {
        super(voteid, voteCreated);
        message_id = msg_id;
        username = user;
        is_upvote = vote;
    }

}