package edu.lehigh.cse216.buzzboys.Data;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class User {
    public int uid;
    public String username;
    public String realname;
    public String email;
    private String password;

    public static User currentUser;

    public User(int i, String n, String rn, String e, String p, Date date) {
        uid = i;
        username = n;
        realname = rn;
        email = e;
        password = p;
        //TODO- add hash and salt here?
    }

    public User(int i, String n, String rn, String e, String p) {
        uid = i;
        username = n;
        realname = rn;
        email = e;
        password = p;
        //TODO- add hash and salt here?
    }

    public static void setCurrentUser(User user) throws JSONException {
        currentUser = user;
    }

    public static User getCurrentUser() throws JSONException {
        return currentUser;
    }

    public String getPassword(){
        return password;
    }

    public String setPassword(String password){
        this.password = password;
        return password;
    }

    public static User getFromJSON(JSONObject json) throws JSONException {
        User u = null;

        int uid = json.getInt("useruid");
        String username = json.getString("username");
        String realname = json.getString("realname");
        String email = json.getString("email");
        String password = json.getString("password");


        u = new User(uid, username, realname, email, password, null);
        return u;
    }

    //A list of statically named users, for when
    public static User[] TestUsers = new User[] {
            new User(0, "User1", "John Doe", "john@gmail.com", "password"),
    };
}
