package edu.lehigh.cse216.buzzboys.backend;

public class StoreHandler {
    final MessageStore msg;
    final UserStore user;
    final VoteStore vote;
    final CommentStore comment;

    public StoreHandler() {
        msg = new MessageStore();
        user = new UserStore();
        vote = new VoteStore();
        comment = new CommentStore();
    }
}