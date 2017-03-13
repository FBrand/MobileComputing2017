package com.scltrainer.uni_mainz.sclerchenbergtrainerassist;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.DBConnection;

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
    public long insert(String table, ContentValues values) {
        return localdb.insert(table, null, values);
    }

    @Override
    public long update(String table, ContentValues values, String selection, String[] selectionargs) {
        return localdb.update(table,values,selection,selectionargs);
    }

    @Override
    public int delete(String table, String selection, String[] selectionargs) {
        return localdb.delete(table,selection,selectionargs);
    }
}
