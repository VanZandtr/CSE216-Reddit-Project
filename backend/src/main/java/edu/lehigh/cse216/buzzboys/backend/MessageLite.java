package edu.lehigh.cse216.buzzboys.backend;

import java.util.Date;

public class MessageLite extends Row {

    /**
     * Add the user of who posted message
     */
    public String userId;

    /**
     * The title for this message
     */
    public String mTitle;

    public MessageLite(int id, Date dateCreated, String user, String title) {
        super(id, dateCreated);
        userId = user;
        mTitle = title;
    }

    public MessageLite(MessageLite data) {
        super(data.id, data.cDate);
        userId = data.userId;
        mTitle = data.mTitle;
    }

}