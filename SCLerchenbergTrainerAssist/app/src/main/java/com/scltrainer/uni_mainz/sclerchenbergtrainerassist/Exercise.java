package com.scltrainer.uni_mainz.sclerchenbergtrainerassist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component.Layout;

/**
 * Created by Yorrick on 16.03.2017.
 *
 * Exercise-Klasse für Kommunikation zwischen Fragments,
 * insb. zum Erstellen einer neuen Übung.
 */

public class Exercise {


    private Context context;

    private String name;
    private String autorname;
    private String autormail;
    private String description;
    private String keywords;
    //TODO: ages mit Enum ersetzten?
    private String ages;
    private String videolink;

    private int techniqueRating;
    private int tacticRating;
    private int physiqueRating;
    private int numbPlayerMin;
    private int numbPlayerMax;
    //TODO: Ist groupSize in der DB ein Int oder ein String?
    private int groupSize;
    private int duration;

    private boolean favourite;

    private Layout field;

    // Schnittstelle zur Datenbank
    private DBConnection dbConnection;




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAutorname() {
        return autorname;
    }

    public void setAutorname(String autorname) {
        this.autorname = autorname;
    }

    public String getAutormail() {
        return autormail;
    }

    public void setAutormail(String autormail) {
        this.autormail = autormail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getVideolink() {
        return videolink;
    }

    public void setVideolink(String videolink) {
        this.videolink = videolink;
    }

    public int getTechniqueRating() {
        return techniqueRating;
    }

    public void setTechniqueRating(int techniqueRating) {
        this.techniqueRating = techniqueRating;
    }

    public int getTacticRating() {
        return tacticRating;
    }

    public void setTacticRating(int tacticRating) {
        this.tacticRating = tacticRating;
    }

    public int getPhysiqueRating() {
        return physiqueRating;
    }

    public void setPhysiqueRating(int physiqueRating) {
        this.physiqueRating = physiqueRating;
    }

    public int getNumbPlayerMin() {
        return numbPlayerMin;
    }

    public void setNumbPlayerMin(int numbPlayerMin) {
        this.numbPlayerMin = numbPlayerMin;
    }

    public int getNumbPlayerMax() {
        return numbPlayerMax;
    }

    public void setNumbPlayerMax(int numbPlayerMax) {
        this.numbPlayerMax = numbPlayerMax;
    }

    public int getGroupSize() {
        return groupSize;
    }

    public void setGroupSize(int groupSize) {
        this.groupSize = groupSize;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public Layout getField() {
        return field;
    }

    public String getAges() {
        return ages;
    }

    public void setAges(String ages) {
        this.ages = ages;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setField(Layout field) {
        this.field = field;
    }


    /**
     * Fügt alle Eigenschaften einer Übung in die DB ein
     *
     * TODO: Mit Funktion füllen.
     */
    public void saveExerciseToDB(Context context){

        DBConnection dbConnection = DBHelper.getConnection(context);

        //TODO: ID richtig hochsetzen damit sie bei der übergabe zwischen liste und detailansicht richtig funktioniert
        String[] sArr = {"_id"};
        //int id = dbConnection.select(DBInfo.EXERCISE_TABLE_NAME, sArr, );
        ContentValues row = new ContentValues();
        //row.put(DBInfo.EXERCISE_COLUMN_NAME_ID, );
        row.put(DBInfo.EXERCISE_COLUMN_NAME_AGE, getAges());
        row.put(DBInfo.EXERCISE_COLUMN_NAME_AUTORMAIL, getAutormail());
        row.put(DBInfo.EXERCISE_COLUMN_NAME_AUTORNAME, getAutorname());
        row.put(DBInfo.EXERCISE_COLUMN_NAME_DESCRIPTION, getDescription());
        row.put(DBInfo.EXERCISE_COLUMN_NAME_DURATION, getDuration());
        row.put(DBInfo.EXERCISE_COLUMN_NAME_GRAPHIC, "HFASFJK");
        row.put(DBInfo.EXERCISE_COLUMN_NAME_GROUPSIZE, getGroupSize());
        row.put(DBInfo.EXERCISE_COLUMN_NAME_KEYWORDS, "Passen");
        row.put(DBInfo.EXERCISE_COLUMN_NAME_MATERIAL, "Ball, Tor");
        row.put(DBInfo.EXERCISE_COLUMN_NAME_NAME, getName());
        row.put(DBInfo.EXERCISE_COLUMN_NAME_PHYSIS, getPhysiqueRating());
        //row.put(DBInfo.EXERCISE_COLUMN_NAME_RATING, 3.5);
        row.put(DBInfo.EXERCISE_COLUMN_NAME_SPORT, "Fußball");
        row.put(DBInfo.EXERCISE_COLUMN_NAME_TACTIC,getTacticRating());
        row.put(DBInfo.EXERCISE_COLUMN_NAME_TECHNIC,getTechniqueRating());
        row.put(DBInfo.EXERCISE_COLUMN_NAME_VIDEOLINK, getVideolink());
        dbConnection.insert(DBInfo.EXERCISE_TABLE_NAME, row);
        row.clear();

    }

    public void createTestExercise(){
        setName("TestÜbung");
        setAutorname("Peter Enis");
        setAutormail("diesIstEineEmail@dresse.de");
        setAges("1 - 99");
        setDescription("Dies ist eine reine Testbeschreibung zum Testen der Exercise-Klasse");
        setDuration(3);
        setGroupSize(5);
        setPhysiqueRating(1);
        setTacticRating(2);
        setTechniqueRating(3);
        setVideolink("https://www.youtube.com/watch?v=BooEsbHD6SU");

        saveExerciseToDB(context);
    }
}
