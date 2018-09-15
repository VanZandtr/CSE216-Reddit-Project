package edu.lehigh.cse216.buzzboys.backend;

import java.util.Date;

public abstract class Row {
    /**
     * Every row needs an id
     */
    public final int id;

    /**
     * Every row needs a created date
     */
    public final Date cDate;

    /**
     * Constructor for a Row
     * @param id
     */
    public Row(int id) {
        this.id = id;
        cDate = new Date();
    }
}