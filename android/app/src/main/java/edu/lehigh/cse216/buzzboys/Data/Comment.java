package edu.lehigh.cse216.buzzboys.Data;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

//TODO - Check these constructors with backend's

public class Comment {


    public int mId;
    public String mUserID;
    public int mMessageID;
    public String mComment;


    public Comment(int id, String user_id, int message_id, String comment) {
        mId = id;
        mUserID = user_id;
        mMessageID = message_id;
        mComment = comment;
    }

    public Comment(int id, String user_id, int message_id, String comment, Date d) {
        mMessageID = message_id;
        mComment = comment;
        mUserID = user_id;
        mId = id;
    }

    public Comment(String comment) {
        mComment = comment;
    }

    public static Comment getFromJSON(JSONObject json) throws JSONException {
        Comment c = null;

        int id = json.getInt("mId");
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
