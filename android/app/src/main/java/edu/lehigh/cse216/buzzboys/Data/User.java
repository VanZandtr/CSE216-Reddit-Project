package edu.lehigh.cse216.buzzboys.Data;

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
}
