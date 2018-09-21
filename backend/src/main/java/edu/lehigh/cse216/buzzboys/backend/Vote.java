package edu.lehigh.cse216.buzzboys.backend;

import java.util.Date;


/**
 * Class that defines the rows from the votes table
 * Schema: "CREATE TABLE votes (id SERIAL PRIMARY KEY, message_id INT NOT NULL, " 
 * + "username VARCHAR(20) NOT NULL, " + "is_upvote INT, " + "voteDate TIMESTAMP NOT NULL)");//added
 * id and date is got from abstract row type
 */
public class Vote extends VoteLite {
    /**
     * is_upvote depends if this user upvote, downvoted or abstained to vote on a certain post
     */
    Integer is_upvote;
    /**
     * Constructor for the complete vote row
     * @param voteid
     * @param voteCreated
     * @param msg_id
     * @param user
     * @param vote
     */
    public Vote(int voteid, Date voteCreated, int msg_id, String user, Integer vote) {
        super(voteid, voteCreated, msg_id, user);
        is_upvote = vote;
    }

}