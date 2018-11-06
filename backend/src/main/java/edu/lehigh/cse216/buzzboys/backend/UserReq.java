package edu.lehigh.cse216.buzzboys.backend;

/**
 * The class holds user data from a POST request to the server 
 * 
 * NB: since this will be created from JSON, all fields must be public, and we
 *     do not need a constructor.
 */
public class UserReq {
    public String token;//Raymond: Added token string to userReq
    public String realName;
    public String userName;
    public String email;
    public String password;
}