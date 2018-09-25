package edu.lehigh.cse216.buzzboys.Data;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Message {
    public int ID;
    public int userID;
    public String subject;
    public String message;
    public int upvotes;
    public int downvotes;

    //-1 = Current User downvoted
    // 0 = Current User did not vote on this item
    // 1 = Current User upvoted
    public int upvoted;

    public Message(int i, int ui, String sub, String mes, int up, int down) {
        ID = i;
        userID = ui;
        subject = sub;
        message = mes;
        upvotes = up;
        downvotes = down;
    }

    public static Message getFromJSON(JSONObject json) throws JSONException {
        Message m = null;

        int id = json.getInt("id");
        String uid = json.getString("userId");
        String sub = json.getString("mTitle");
        //String mes = json.getString("message");
        //int up = json.getInt("upvotes");
        //int down = json.getInt("downvotes");

        m = new Message(id, Integer.getInteger(uid), sub, "", 0, 0);
        return m;
    }

    public static Message[] TestMessages = new Message[] {
            new Message(0, 0, "Title1", "Message1", 1, 0),
            new Message(1, 0, "Title2", "Message2", 0, 1)
    };
}
