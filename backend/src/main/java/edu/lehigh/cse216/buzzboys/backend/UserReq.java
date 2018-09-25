package edu.lehigh.cse216.buzzboys.backend;

/**
 * SimpleRequest provides a format for clients to present title and message 
 * strings to the server.
 * 
 * NB: since this will be created from JSON, all fields must be public, and we
 *     do not need a constructor.
 */
public class UserReq {
    /**
     * First name of the user
     */
    public String first;

    /**
     * Last name of user
     */
    public String last;

    /**
     * Username of new user
     */
    public String username;
    
    /**
     * User email
     */
    public String email;
}