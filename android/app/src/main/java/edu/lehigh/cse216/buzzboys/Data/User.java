package edu.lehigh.cse216.buzzboys.Data;

import java.util.ArrayList;

public class User {
    public int ID;
    public String name;
    public String firstName;
    public String lastName;
    public String email;

    public static User currentUser;

    public User(int i, String n, String fn, String ln, String e) {
        ID = i;
        name = n;
        firstName = fn;
        lastName = ln;
        email = e;
    }

    //A list of statically named users, for when
    public static User[] TestUsers = new User[] {
            new User(0, "User1", "John", "Doe", "john@gmail.com"),
            new User(0, "User2", "Jane", "Doe", "jane@gmail.com")
    };
}
