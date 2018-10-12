package edu.lehigh.cse216.buzzboys.backend;

import java.util.Date;
/**
 * Light row representation for User
 * It is a superclass of user because of the less amounts of elements
 */
public class UserLite extends Row {
    
    
    String uRealName;
    
    String uUserName;

    String uEmail;

    public UserLite() {}

    public UserLite(int id, Date date, String real, String user, String email) {
        super(id, date);
        uRealName = real;   
        uUserName = user;
        uEmail = email;
    }

    public UserLite(UserLite data) {
        super(data.id, data.cDate);
        uRealName = data.uRealName;
        uUserName = data.uUserName;
        uEmail = data.uEmail;
    }
}