package edu.lehigh.cse216.buzzboys.backend;
/**
 * Light row representation for User
 * It is a superclass of user because of the less amounts of elements
 */
public class UserLite extends Row {
    /**
     * First name of the user
     */
    String ufirst;

    /**
     * Last name of the user
     */
    String ulast;
    
    public UserLite(int id, Date date, String first, String last) {
        super(id, date);
        ufirst = first;
        ulast = last;
    }

    public UserLite(UserLite data) {
        super(data.id, data.cDate);
        ufirst = data.ufirst;
        ulast = data.ulast;
    }
}