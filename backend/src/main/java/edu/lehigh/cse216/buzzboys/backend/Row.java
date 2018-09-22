package edu.lehigh.cse216.buzzboys.backend;

import java.util.Date;

/**
 * Abstract Row class that is the superclass for all types of data rows
 * Makes implementing new Rows much easier since each one will have
 * an id and a creation date
 */
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
    public Row(int id, Date date) {
        this.id = id;
        cDate = date;
    }
}