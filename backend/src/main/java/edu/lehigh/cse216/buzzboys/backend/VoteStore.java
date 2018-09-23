package edu.lehigh.cse216.buzzboys.backend;

import java.util.List;
import java.util.ArrayList;

import java.util.Date;

public class VoteStore extends DataStore<VoteLite, Vote> {


     /**
     * Construct message store by calling super constructor DataStore.
     * Reset counter and create a new ArrayList
     * TODO: for all methods, get the database implementation
     */
    public VoteStore(String ip, String port, String user, String pass) {
        super(ip, port, user, pass);
    }

    public int createEntry(int msg_id, String username, Integer is_upvote) {
        if(username == null)
            return -1;
        return (db.insertVote(msg_id, username, is_upvote) == -1) ? -1 : counter++; 
    }

    /**
     * Get one complete row from Votes using its ID to select it
     * 
     * @param id The id of the row to select
     * @return A copy of the data in the row, if it exists, or null otherwise
     */
    @Override
    public synchronized Vote readOne(int id) {
        Vote data = db.selectOneVote(id);
        (data == null) ? null : data;
    }

    /**
     * Get all of the VoteLites that are present in the Votes
     * @return A copy of the populated ArrayList with all of the data
     */
    @Override
    public synchronized List<VoteLite> readAll() {
        List<VoteLite> voteList = new ArrayList<VoteLite>(db.selectAllFromVotes());
        (voteList == null) ? null : voteList;
    }

    public synchronized Vote readVoteByMessageAndUsername(int msgId, String userId) {
        Vote vote = new Vote(db.selectVotesByMessageIDAndUsername(msgId, userId));
        (vote == null) ? null : vote;
    }

    public synchronized List<Vote> readAllByMessageid(int msgId) {
        List<Vote> voteList = new ArrayList<Vote>(db.selectVotesByMessageID(msgId));
        (voteList == null) ? null : voteList;
    }



    public synchronized List<Vote> readAllByUserId(String userId) {
        List<Vote> voteList = new ArrayList<Vote>(db.selectVotesByMessageID(msgId));
        (voteList == null) ? null : voteList;
    }

    public synchronized Vote mUpdateOneVote(int msg_id, String username, Integer is_upvote) {
        //need to add protection against inept voteid and msg_id
        if(username == null)
            return null;
        User data = db.updateVote(msg_id, username, is_upvote);
        return (data == null) ? null : data;
    }

    /**
     * Delete a row from the DataStore
     * 
     * @param id The Id of the row to delete
     * @return true if the row was deleted, false otherwise
     */
    @Override
    public synchronized boolean deleteOne(int id) {
        return (readOne(id) == null) ? false : (db.deleteVoteRow(id) == -1) ? false : true;
    }




    
}