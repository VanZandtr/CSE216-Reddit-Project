package edu.lehigh.cse216.buzzboys.backend;

import java.util.Date;

public class Comment extends Row {
    
    public int userId;
    public int messageId;
    public String content;

    public Comment() {}

    public Comment(int id, Date d, int uid, int mid, String c) {
        super(id, d);
        userId = uid;
        messageId = mid;
        content = c;
    }
}
