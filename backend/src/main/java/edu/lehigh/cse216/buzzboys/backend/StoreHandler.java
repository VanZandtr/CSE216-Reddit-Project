package edu.lehigh.cse216.buzzboys.backend;

public class StoreHandler {
    final MessageStore msg;
    final UserStore user;
    final VoteStore vote;

    public StoreHandler() {
        msg = new MessageStore();
        user = new UserStore();
        vote = new VoteStore();
    }
}