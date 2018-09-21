package edu.lehigh.cse216.buzzboys.backend;

/**
 * DataRowLite is for communicating back a subset of the information in a
 * DataRow.  Specifically, we only send back the id since it is an abstract class.  Note that
 * in order to keep the client code as consistent as possible, we ensure 
 * that the field names in DataRowLite match the corresponding names in
 * DataRow.  As with DataRow, we plan to convert DataRowLite objects to
 * JSON, so we need to make their fields public.
 */
public abstract class RowLite {
    /**
     * The id for this row; see DataRow.mId
     */
    public int id;

    /**
     * Create a DataRowLite by copying fields from a DataRow
     */
    public RowLite(Row data) {
        id = data.id;
    }
}