package com.example.lars.connector;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by lars on 09.03.17.
 */

public class LocalDBConnection implements DBConnection {

    private SQLiteDatabase localdb;

    public LocalDBConnection(SQLiteDatabase db){
        localdb = db;
    }

    @Override
    public Cursor select(String table, String[] projection, String selection, String[] selectionargs) {
        return localdb.query(table, projection, selection,selectionargs,null,null,null);
    }

    @Override
    public boolean insert(String table, ContentValues values) {
        return false;
    }

    @Override
    public boolean update(String table, ContentValues values, String selection, String[] selectionargs) {
        return false;
    }

    @Override
    public boolean delete(String table, String selection, String[] selectionargs) {
        return false;
    }
}
