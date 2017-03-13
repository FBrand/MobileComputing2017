package com.scltrainer.uni_mainz.sclerchenbergtrainerassist;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lars on 09.03.17.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS " + DBInfo.EXERCISE_TABLE_NAME + " (" +
            DBInfo.EXERCISE_COLUMN_NAME_ID + " INTEGER," +
            DBInfo.EXERCISE_COLUMN_NAME_AGE + " STRING,"+
            DBInfo.EXERCISE_COLUMN_NAME_AUTORMAIL + " STRING," +
            DBInfo.EXERCISE_COLUMN_NAME_AUTORNAME + " STRING," +
            DBInfo.EXERCISE_COLUMN_NAME_DESCRIPTION + " STRING," +
            DBInfo.EXERCISE_COLUMN_NAME_DURATION + " INTEGER," +
            DBInfo.EXERCISE_COLUMN_NAME_GRAPHIC + " STRING," +
            DBInfo.EXERCISE_COLUMN_NAME_GROUPSIZE + " INTEGER," +
            DBInfo.EXERCISE_COLUMN_NAME_IDLOCAL + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            DBInfo.EXERCISE_COLUMN_NAME_KEYWORDS + " STRING," +
            DBInfo.EXERCISE_COLUMN_NAME_LASTCHANGE +" DATE," +
            DBInfo.EXERCISE_COLUMN_NAME_MATIRIAL + " STRING," +
            DBInfo.EXERCISE_COLUMN_NAME_NAME + " STRING," +
            DBInfo.EXERCISE_COLUMN_NAME_PHYSIS + " INTEGER," +
            DBInfo.EXERCISE_COLUMN_NAME_RATING + " DOUBLE," +
            DBInfo.EXERCISE_COLUMN_NAME_SPORT + " STRING," +
            DBInfo.EXERCISE_COLUMN_NAME_TACTIC + " INTEGER," +
            DBInfo.EXERCISE_COLUMN_NAME_TECHNIC + " INTEGER," +
            DBInfo.EXERCISE_COLUMN_NAME_VIDEOLINK + " STRING);" +

            "CREATE TABLE IF NOT EXISTS "+ DBInfo.TRAININGSUNIT_TABLE_NAME + " (" +
            DBInfo.TRAININGSUNIT_COLUMN_NAME_AGE + " STRING," +
            DBInfo.TRAININGSUNIT_COLUMN_NAME_AUTORMAIL + " STRING," +
            DBInfo.TRAININGSUNIT_COLUMN_NAME_AUTORNAME + " STRING," +
            DBInfo.TRAININGSUNIT_COLUMN_NAME_DESCRIPTION + " STRING," +
            DBInfo.TRAININGSUNIT_COLUMN_NAME_DURATION + " INTEGER," +
            DBInfo.TRAININGSUNIT_COLUMN_NAME_EXERCISE + " STRING," +
            DBInfo.TRAININGSUNIT_COLUMN_NAME_ID + " INTEGER," +
            DBInfo.TRAININGSUNIT_COLUMN_NAME_IDLOCAL + "INTEGER PRIMARY KEY AUTOINCREMENT," +
            DBInfo.TRAININGSUNIT_COLUMN_NAME_KEYWORDS + " STRINGS," +
            DBInfo.TRAININGSUNIT_COLUMN_NAME_LASTCHANGE + " DATE," +
            DBInfo.TRAININGSUNIT_COLUMN_NAME_NAME + " STRING," +
            DBInfo.TRAININGSUNIT_COLUMN_NAME_RATING + " DOUBLE);" +

            "CREATE TABLE IF NOT EXISTS "+ DBInfo.GRAPHIC_TABLE_NAME + " (" +
            DBInfo.GRAPHIC_COLUMN_NAME_EXERCIXE + " INTEGER," +
            DBInfo.GRAPHIC_COLUMN_NAME_DESCRIPTION + " STRING," +
            DBInfo.GRAPHIC_COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            DBInfo.GRAPHIC_COLUMN_NAME_MAINTYPE + " INTEGER," +
            DBInfo.GRAPHIC_COLUMN_NAME_TYPE + " INTEGER); " +


            "CREATE TABLE IF NOT EXISTS " + DBInfo.TRAININGSUNITEXERCISE_TABLE_NAME + " (" +
            DBInfo.TRAININGSUNITEXERCISE_COLUMN_NAME_EXERCISE + " INTEGER," +
            DBInfo.TRAININGSUNITEXERCISE_COLUMN_NAME_TRAININGSUNIT + " INTEGER," +
            DBInfo.TRAININGSUNITEXERCISE_COLUMN_NAME_DURATION + " INTEGER," +
            "PRIMARYKEY (" + DBInfo.TRAININGSUNITEXERCISE_COLUMN_NAME_EXERCISE +","+DBInfo.TRAININGSUNITEXERCISE_COLUMN_NAME_TRAININGSUNIT
            ;

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DBInfo.EXERCISE_TABLE_NAME + "; " +
                    "DROP TABLE IF EXISTS " + DBInfo.TRAININGSUNIT_TABLE_NAME + "; " +
                    "DROP TABLE IF EXISTS " + DBInfo.GRAPHIC_TABLE_NAME + "; " +
                    "DROP TABLE IF EXISTS " + DBInfo.TRAININGSUNITEXERCISE_TABLE_NAME + "; ";


    public DBHelper(Context context){
        super(context, DBInfo.DATABASE_NAME, null, DBInfo.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        insertTestdata(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    /**
     * Method for getting a database connection.
     * @param context Contains context.
     * @return A connection to the localdatabase ist returned.
     */
    public static DBConnection getConnection (Context context){
        DBHelper db = new  DBHelper(context);
        db.onUpgrade(db.getWritableDatabase(),1,1);
        return new LocalDBConnection(db.getReadableDatabase());
    }

    public static void insertTestdata(SQLiteDatabase db){
        LocalDBConnection con = new LocalDBConnection(db);
        ContentValues row = new ContentValues();
        row.put(DBInfo.EXERCISE_COLUMN_NAME_ID, 1);
        row.put(DBInfo.EXERCISE_COLUMN_NAME_AGE, "A B C");
        row.put(DBInfo.EXERCISE_COLUMN_NAME_AUTORMAIL, "mustermax@mail.com");
        row.put(DBInfo.EXERCISE_COLUMN_NAME_AUTORNAME, "mustermax");
        row.put(DBInfo.EXERCISE_COLUMN_NAME_DESCRIPTION, "Dies ist eine Übung für Flanken von außen.");
        row.put(DBInfo.EXERCISE_COLUMN_NAME_DURATION, 30);
        row.put(DBInfo.EXERCISE_COLUMN_NAME_GRAPHIC, "HFASFJK");
        row.put(DBInfo.EXERCISE_COLUMN_NAME_GROUPSIZE, 5);
        row.put(DBInfo.EXERCISE_COLUMN_NAME_KEYWORDS, "Passen");
        row.put(DBInfo.EXERCISE_COLUMN_NAME_MATIRIAL, "Ball, Tor");
        row.put(DBInfo.EXERCISE_COLUMN_NAME_NAME, "Flanken");
        row.put(DBInfo.EXERCISE_COLUMN_NAME_PHYSIS, 1);
        row.put(DBInfo.EXERCISE_COLUMN_NAME_RATING, 3.5);
        row.put(DBInfo.EXERCISE_COLUMN_NAME_SPORT, "Fußball");
        row.put(DBInfo.EXERCISE_COLUMN_NAME_TACTIC,2);
        row.put(DBInfo.EXERCISE_COLUMN_NAME_TECHNIC,4);
        row.put(DBInfo.EXERCISE_COLUMN_NAME_VIDEOLINK, "https://www.youtube.com/watch?v=C8oi-Q8oWCE");
        con.insert(DBInfo.EXERCISE_TABLE_NAME, row);
        row.clear();

        row.put(DBInfo.EXERCISE_COLUMN_NAME_ID, 2);
        row.put(DBInfo.EXERCISE_COLUMN_NAME_AGE, "A B");
        row.put(DBInfo.EXERCISE_COLUMN_NAME_AUTORMAIL, "mustermax@mail.com");
        row.put(DBInfo.EXERCISE_COLUMN_NAME_AUTORNAME, "mustermax");
        row.put(DBInfo.EXERCISE_COLUMN_NAME_DESCRIPTION, "Dies ist eine Übung für Schießen aus der Distanz.");
        row.put(DBInfo.EXERCISE_COLUMN_NAME_DURATION, 25);
        row.put(DBInfo.EXERCISE_COLUMN_NAME_GRAPHIC, "HFadASFJK");
        row.put(DBInfo.EXERCISE_COLUMN_NAME_GROUPSIZE, 4);
        row.put(DBInfo.EXERCISE_COLUMN_NAME_KEYWORDS, "Schuss");
        row.put(DBInfo.EXERCISE_COLUMN_NAME_MATIRIAL, "Ball, Tor");
        row.put(DBInfo.EXERCISE_COLUMN_NAME_NAME, "Topspin");
        row.put(DBInfo.EXERCISE_COLUMN_NAME_PHYSIS, 1);
        row.put(DBInfo.EXERCISE_COLUMN_NAME_RATING, 4.5);
        row.put(DBInfo.EXERCISE_COLUMN_NAME_SPORT, "Fußball");
        row.put(DBInfo.EXERCISE_COLUMN_NAME_TACTIC,1);
        row.put(DBInfo.EXERCISE_COLUMN_NAME_TECHNIC,5);
        row.put(DBInfo.EXERCISE_COLUMN_NAME_VIDEOLINK, "https://www.youtube.com/watch?v=USVxAreHDow");
        con.insert(DBInfo.EXERCISE_TABLE_NAME, row);
        row.clear();

        row.put(DBInfo.EXERCISE_COLUMN_NAME_ID, 3);
        row.put(DBInfo.EXERCISE_COLUMN_NAME_AGE, "A B C D");
        row.put(DBInfo.EXERCISE_COLUMN_NAME_AUTORMAIL, "mustermax@mail.com");
        row.put(DBInfo.EXERCISE_COLUMN_NAME_AUTORNAME, "mustermax");
        row.put(DBInfo.EXERCISE_COLUMN_NAME_DESCRIPTION, "Dies ist eine Übung 1 gegen 1.");
        row.put(DBInfo.EXERCISE_COLUMN_NAME_DURATION, 15);
        row.put(DBInfo.EXERCISE_COLUMN_NAME_GRAPHIC, "HFdadsadASFJK");
        row.put(DBInfo.EXERCISE_COLUMN_NAME_GROUPSIZE, 6);
        row.put(DBInfo.EXERCISE_COLUMN_NAME_KEYWORDS, "Zweikampf");
        row.put(DBInfo.EXERCISE_COLUMN_NAME_MATIRIAL, "3 Ball");
        row.put(DBInfo.EXERCISE_COLUMN_NAME_NAME, "Körpertäuschung");
        row.put(DBInfo.EXERCISE_COLUMN_NAME_PHYSIS, 3);
        row.put(DBInfo.EXERCISE_COLUMN_NAME_RATING, 1.5);
        row.put(DBInfo.EXERCISE_COLUMN_NAME_SPORT, "Fußball");
        row.put(DBInfo.EXERCISE_COLUMN_NAME_TACTIC,3);
        row.put(DBInfo.EXERCISE_COLUMN_NAME_TECHNIC,3);
        row.put(DBInfo.EXERCISE_COLUMN_NAME_VIDEOLINK, "https://www.youtube.com/watch?v=HnHatjXLBY8");
        System.out.println("TESTDATAINSERT " + con.insert(DBInfo.EXERCISE_TABLE_NAME, row));
        row.clear();

    }
}
