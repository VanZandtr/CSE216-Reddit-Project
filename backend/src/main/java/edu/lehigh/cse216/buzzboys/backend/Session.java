package edu.lehigh.cse216.buzzboys.backend;

import java.util.LinkedHashMap;

/**
 * This class handles all data/logic for keeping track of user sessions.
 */
public class Session {
    
    /**
     * This hashmap is responsible for storing user session data. 
     * Key: The userToken assigned by the server to a session on a given device.
     * Value: The username associated with that session. 
     */
    private static LinkedHashMap<String, String> tokenMap;

    /**
     * Get the tokenmap, initializing it first if null.
     * @return The TokenMap 
     */
    private static LinkedHashMap<String, String> getTokenMap() {
        if (tokenMap == null)
            tokenMap = new LinkedHashMap<String, String>();
        return tokenMap;
    }

    /**
     * Log a user in by associating the string token with the username.
     */
    public static void Login(String username, String token) {
        LinkedHashMap<String, String> map = getTokenMap();
        if (map.containsValue(username))
            map.values().remove(username);
        map.put(token, username);
    }

    /**
     * Log out a given user by removing the token-username pair from tokenMap.
     */
    public static void Logout(String token) {
        if (getTokenMap().containsKey(token)) 
            getTokenMap().remove(token);
    }
}