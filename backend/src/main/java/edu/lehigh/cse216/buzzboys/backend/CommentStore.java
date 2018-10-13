package edu.lehigh.cse216.buzzboys.backend;
import java.util.List;

class CommentStore extends DataStore<Comment, Comment> {

    public CommentStore() {
        super();
    }

    @Override
    public synchronized Comment readOne(int id) {
        Comment data = db.selectOneComment(id);
        return data;
    }

    @Override
    public synchronized List<Comment> readAll() {
        return db.selectAllFromComments();
    }
    
    @Override
    public synchronized boolean deleteOne(int id) {
        return (readOne(id) == null) ? false : (db.deleteCommentRow(id) == -1) ? false : true;
    }

    public synchronized List<Comment> readAllWithMessageId(int msgId) {
        return db.selectCommentsWithMessageId(msgId);
    }

    public synchronized int createEntry(int uid, int mid, String content) {
        if (content == null)
            return -1;
        return (db.insertCommentRow(uid, mid, content) == -1) ? -1 : counter++;
    }
}