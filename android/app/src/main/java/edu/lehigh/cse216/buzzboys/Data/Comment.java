package edu.lehigh.cse216.buzzboys.Data;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

//TODO - Check these constructors with backend's

public class Comment {


    public int cid;
    public String uid;
    public int mid;
    public String content;


    public Comment(int id, String user_id, int message_id, String comment) {
        cid = id;
        uid = user_id;
        mid = message_id;
        content = comment;
    }

    public Comment(int id, String user_id, int message_id, String comment, Date d) {
        mid = message_id;
        content = comment;
        uid = user_id;
        cid = id;
    }

    public Comment(String comment) {
        content = comment;
    }

    public static Comment getFromJSON(JSONObject json) throws JSONException {
        Comment c = null;

        int id = json.getInt("cid");
        int message_id = json.getInt("messageID");
        String comment = json.getString("comment");
        String uid = json.getString("userId");

        Date nullDate = null;
        c = new Comment(id, uid, message_id, comment, nullDate);
        return c;
    }

    public static Comment[] TestComments = new Comment[] {
            new Comment(0, "testUser", 0, "Comment")

    };


}
