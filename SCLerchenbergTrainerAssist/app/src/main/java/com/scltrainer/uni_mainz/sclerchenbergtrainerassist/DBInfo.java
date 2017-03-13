package com.scltrainer.uni_mainz.sclerchenbergtrainerassist;

/**
 * Created by lars on 09.03.17.
 * Class containing all information about the local database.
 */

public class DBInfo {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "test3.db";

    public static final String EXERCISE_TABLE_NAME = "exercise";
    public static final String EXERCISE_COLUMN_NAME_ID = "_id";
    public static final String EXERCISE_COLUMN_NAME_IDLOCAL = "idLocal";
    public static final String EXERCISE_COLUMN_NAME_AGE = "age";
    public static final String EXERCISE_COLUMN_NAME_AUTORMAIL = "autorMail";
    public static final String EXERCISE_COLUMN_NAME_AUTORNAME = "autorName";
    public static final String EXERCISE_COLUMN_NAME_DESCRIPTION = "description";
    public static final String EXERCISE_COLUMN_NAME_RATING = "rating";
    public static final String EXERCISE_COLUMN_NAME_DURATION = "duration";
    public static final String EXERCISE_COLUMN_NAME_GRAPHIC = "graphic";
    public static final String EXERCISE_COLUMN_NAME_GROUPSIZE = "groupsize";
    public static final String EXERCISE_COLUMN_NAME_LASTCHANGE = "lastChange";
    public static final String EXERCISE_COLUMN_NAME_MATIRIAL = "matirial";
    public static final String EXERCISE_COLUMN_NAME_NAME = "name";
    public static final String EXERCISE_COLUMN_NAME_KEYWORDS = "keywords";
    public static final String EXERCISE_COLUMN_NAME_PHYSIS = "physis";
    public static final String EXERCISE_COLUMN_NAME_TACTIC = "tactic";
    public static final String EXERCISE_COLUMN_NAME_TECHNIC = "technic";
    public static final String EXERCISE_COLUMN_NAME_SPORT = "sport";
    public static final String EXERCISE_COLUMN_NAME_VIDEOLINK = "videolink";

    public static final String TRAININGSUNIT_TABLE_NAME = "trainingsunit";
    public static final String TRAININGSUNIT_COLUMN_NAME_ID = "_id";
    public static final String TRAININGSUNIT_COLUMN_NAME_IDLOCAL = "idLocal";
    public static final String TRAININGSUNIT_COLUMN_NAME_AGE = "age";
    public static final String TRAININGSUNIT_COLUMN_NAME_AUTORMAIL = "autorMail";
    public static final String TRAININGSUNIT_COLUMN_NAME_AUTORNAME = "autorName";
    public static final String TRAININGSUNIT_COLUMN_NAME_DESCRIPTION = "description";
    public static final String TRAININGSUNIT_COLUMN_NAME_RATING = "rating";
    public static final String TRAININGSUNIT_COLUMN_NAME_DURATION = "duration";
    public static final String TRAININGSUNIT_COLUMN_NAME_LASTCHANGE = "lastChange";
    public static final String TRAININGSUNIT_COLUMN_NAME_NAME = "name";
    public static final String TRAININGSUNIT_COLUMN_NAME_KEYWORDS = "keywords";
    public static final String TRAININGSUNIT_COLUMN_NAME_EXERCISE = "exercise";

    public static final String GRAPHIC_TABLE_NAME = "graphic";
    public static final String GRAPHIC_COLUMN_NAME_ID = "_id";
    public static final String GRAPHIC_COLUMN_NAME_EXERCIXE = "exercise";
    public static final String GRAPHIC_COLUMN_NAME_DESCRIPTION = "description";
    public static final String GRAPHIC_COLUMN_NAME_MAINTYPE = "maintype";
    public static final String GRAPHIC_COLUMN_NAME_TYPE = "type";

    public static final String TRAININGSUNITEXERCISE_TABLE_NAME = "trainingsunitexercise";
    public static final String TRAININGSUNITEXERCISE_COLUMN_NAME_TRAININGSUNIT = "trainingsunit";
    public static final String TRAININGSUNITEXERCISE_COLUMN_NAME_EXERCISE = "exercise";
    public static final String TRAININGSUNITEXERCISE_COLUMN_NAME_DURATION = "duration";

}
