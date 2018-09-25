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

    public Message(int i, int ui, String sub, String mes, int up, int down) {
        ID = i;
        userID = ui;
        subject = sub;
        message = mes;
        upvotes = up;
        downvotes = down;
    }

    public static Message getMessageFromJSON(JSONObject json) throws JSONException {
        Message m = null;

        int id = json.getInt("ID");
        int uid = json.getInt("user_id");
        String sub = json.getString("subject");
        String mes = json.getString("message");
        int up = json.getInt("upvotes");
        int down = json.getInt("downvotes");

        m = new Message(id, uid, sub, mes, up, down);
        return m;
    }

    public static Message[] TestMessages = new Message[] {
            new Message(0, 0, "Subject1", "Message1", 1, 0),
            new Message(1, 0, "Subject2", "Message2", 0, 1)
    };
}
