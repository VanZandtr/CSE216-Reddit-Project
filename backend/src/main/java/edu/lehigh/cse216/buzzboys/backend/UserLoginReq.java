package edu.lehigh.cse216.buzzboys.backend;

/**
 * The class holds user data from a POST request to the server to "/users/login" 
 * 
 * NB: since this will be created from JSON, all fields must be public, and we
 *     do not need a constructor.
 */
public class UserLoginReq {
    public String email;
    public String password;
}