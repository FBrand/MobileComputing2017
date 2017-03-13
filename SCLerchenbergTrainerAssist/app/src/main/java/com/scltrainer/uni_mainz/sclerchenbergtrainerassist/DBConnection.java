package com.scltrainer.uni_mainz.sclerchenbergtrainerassist;

/**
 * Created by lars on 09.03.17.
 */

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Interface for connecting to SQL-DB
 */
public interface DBConnection {

    /**
     * Method representing SQL SELECT. (SELECT col FROM table WHERE col = x).
     * @param table representing the table from the query.
     * @param projection representing the column names of the result of the query.
     * @param selection representing the WHERE part from the query in the form (col1 = ? AND col2 = ?).
     * @param selectionargs representing the selection args the ? from the selection String are replaced with selechtionargs entries.
     * @return The result of the select-query is returned as Cursor.
     */
    public Cursor select(String table, String[] projection, String selection, String[] selectionargs);

    /**
     * Method representing SQL INSERT (INSERT INTO table (val1, val2,...))
     * @param table representing the table from the query.
     * @param values representing the values of the query.
     * @return The row number from the entry created by the insert-query is returned as long. -1 is returned if there was an error.
     */
    public long insert(String table, ContentValues values);

    /**
     * Method representing SQL UPDATE. (UPDATE table SET col1 = val1, col2 = val2 WHERE col = x )
     * @param table representing the table from the query.
     * @param values representing the values of the query.
     * @param selection representing the WHERE part from the query in the form (col1 = ? AND col2 = ?).
     * @param selectionargs representing the selection args the ? from the selection String are replaced with selechtionargs entries.
     * @return The row number from the entry updated by the update-query is returned as long. -1 is returned if there was an error.
     */
    public long update(String table, ContentValues values, String selection, String[] selectionargs);

    /**
     * Method representing SQL DELETE (DELTE FROM table WHERE col = x)
     * @param table representing the table from the query.
     * @param selection representing the WHERE part from the query in the form (col1 = ? AND col2 = ?).
     * @param selectionargs representing the selection args the ? from the selection String are replaced with selechtionargs entries.
     * @return The count of deleted entries is returnded.
     */
    public int delete(String table, String selection, String[] selectionargs);

}
