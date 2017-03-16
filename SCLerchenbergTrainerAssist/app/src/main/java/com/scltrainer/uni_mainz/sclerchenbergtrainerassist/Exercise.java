package com.scltrainer.uni_mainz.sclerchenbergtrainerassist;

import android.database.Cursor;

import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component.Layout;

/**
 * Created by Yorrick on 16.03.2017.
 *
 * Exercise-Klasse für Kommunikation zwischen Fragments,
 * insb. zum Erstellen einer neuen Übung.
 */

public class Exercise {

    private String name;
    private String autorname;
    private String autormail;
    private String description;
    private String keywords;
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

    public void setField(Layout field) {
        this.field = field;
    }


    /**
     * Fügt alle Eigenschaften einer Übung in die DB ein
     *
     * TODO: Mit Funktion füllen.
     */
    public void saveExerciseToDB(){

    }
}
