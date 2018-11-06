package edu.lehigh.cse216.buzzboys.Data;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class Message {
    public int mid;
    public int uid;
    public String title;
    public String content;
    public int upvotes;
    public int downvotes;

    //-1 = Current User downvoted
    // 0 = Current User did not vote on this item
    // 1 = Current User upvoted
    public int upvoted;

    public Message(int i, int ui, String sub, String mes, int up, int down) {
        mid = i;
        uid = ui;
        title = sub;
        content = mes;
        upvotes = up;
        downvotes = down;
    }

    public Message(int messageId, String sub, Date d) {
        title = sub;
       // uid = userId;
        mid = messageId;
    }

    public Message(String sub, String mes) {
        title = sub;
        content = mes;
    }

    public static Message getFromJSON(JSONObject json) throws JSONException {
        Message m = null;

        int id = json.getInt("id");
        String uid = json.getString("userId");
        String sub = json.getString("mTitle");
        //String mes = json.getString("message");
        //int up = json.getInt("upvotes");
        //int down = json.getInt("downvotes");

        m = new Message(id, sub, null);
        return m;
    }

    public static Message[] TestMessages = new Message[] {
            new Message(0, 0, "Title1", "Message1", 1, 0),
            new Message(1, 0, "Title2", "Message2", 0, 1)
    };
}
