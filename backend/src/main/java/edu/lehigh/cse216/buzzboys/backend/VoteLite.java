package edu.lehigh.cse216.buzzboys.backend;
/**
 * A ligher row representation of vote used when returning all rows
 */
public class VoteLite extends Row {
    /**
     * Message id for a vote
     */
    int mId;
    /**
     * Username for the vote caster
     */
    String username;
    /**
     * Constructor for VoteLite
     */
    public VoteLite(int id, Date date, int msgId, String user) {
        super(id, date);
        mId = msgId;
        username = user;
    }
    /**
     * Copy constructor for VoteLite
     * @param data
     */
    public VoteLite(VoteLite data) {
        super(data.id, data.cDate);
        mId = data.mId;
        username = data.mId;
    }
}