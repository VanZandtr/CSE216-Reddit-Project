package edu.lehigh.cse216.buzzboys.Data;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class User {
    public int ID;
    public String name;
    public String firstName;
    public String lastName;
    public String email;
    private String password;

    public static User currentUser;

    public User(int i, String n, String fn, String ln, String e, String p, Date date) {
        ID = i;
        name = n;
        firstName = fn;
        lastName = ln;
        email = e;
        password = p;
        //TODO- add hash and salt here?
    }

    public User(int i, String n, String fn, String ln, String e, String p) {
        ID = i;
        name = n;
        firstName = fn;
        lastName = ln;
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

    public static User getFromJSON(JSONObject json) throws JSONException {
        User u = null;

        int id = json.getInt("userID");
        String name = json.getString("username");
        String firstname = json.getString("firstname");
        String lastname = json.getString("lastname");
        String email = json.getString("email");
        String password = json.getString("password");


        u = new User(id, name, firstname, lastname, email, password, null);
        return u;
    }

    //A list of statically named users, for when
    public static User[] TestUsers = new User[] {
            new User(0, "User1", "John", "Doe", "john@gmail.com", "password"),
    };
}
