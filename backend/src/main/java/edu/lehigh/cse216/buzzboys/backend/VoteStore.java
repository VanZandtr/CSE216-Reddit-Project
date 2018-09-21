package edu.lehigh.cse216.buzzboys.backend;

public class VoteStore extends DataStore {


     /**
     * Construct message store by calling super constructor DataStore.
     * Reset counter and create a new ArrayList
     * TODO: for all methods, get the database implementation
     */
    public VoteStore(String ip, String port, String user, String pass) {
        super(ip, port, user, pass);
    }
}