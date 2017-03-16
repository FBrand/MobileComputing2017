package com.scltrainer.uni_mainz.sclerchenbergtrainerassist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

/**
 * Created by Felix on 14.03.2017.
 */

public class GlobalDBConnection {

//TODO tabellen erzeugen, upload fehlender übungen triggern, neue tabelle(check), admin/whitelist interface

    static boolean debug = true;


    static String host = "http://10.0.2.2:80";//10.0.2.2:80"http://134.93.143.94:80"
    static String autorMail = "test@mail.de";
    //static String date = "11.03.2017";

    /**
     *                      function to add new data from the global to the local database
     * @param tableName     name of the table to download
     * @param context       context of the calling activity
     * @param lastChange    last time the table was updated
     * @throws IOException
     */
    public static void fetch(String tableName, Context context, String lastChange) throws IOException {
        //TODO Get date
        String result = GlobalDBConnection.get(host + "/" + tableName + "/" + DBInfo.EXERCISE_COLUMN_NAME_LASTCHANGE + "/" + lastChange);

        try {
            JSONObject jo = new JSONObject(result);
            JSONArray ja = jo.getJSONArray("");
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jline = ja.getJSONObject(i);

                storeInDB(tableName, jline, context);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     *                      function to add a row from the local to the global database
     * @param tableName     name of the table to upload to
     * @param localId       id of the row to upload
     * @param context       context of the calling activity
     * @return              true if successfull, else false
     */
    public static Boolean upload(String tableName, int localId, Context context) {

        //SharedPreferences shared = context.getSharedPreferences();
        //String autorMail = shared.getString(USEREMAIL, "");

        JSONObject data = loadFromDB(localId, tableName, context);
        String result = "";
        try {
            result = GlobalDBConnection.post(host + "/" + autorMail + "/" + tableName, data.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
//TODO write globalId in local db
        return result.contains("201");
    }

    /**
     *                      function to update a row in the global database with data from the local database
     * @param tableName     name of the table to update
     * @param localId       local id of the row to update
     * @param context       context of the calling activity
     * @return              true if successfull, else false
     */
    public static Boolean update(String tableName, int localId, Context context) {
        JSONObject data = loadFromDB(localId, tableName, context, true);
        String result = "";
        try {
            result = GlobalDBConnection.put(host + "/" + autorMail + "/" + tableName + "/" + data.getString(DBInfo.EXERCISE_COLUMN_NAME_ID), data.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result.contains("200");
    }

    /**
     *
     * @param tableName     name of the table to delete from
     * @param localId       local id of the row to delete
     * @return              true if successfull, else false
     */
    public static Boolean delete(String tableName, int localId) {
        String result = GlobalDBConnection.delete(host + "/" + autorMail + "/" + tableName + "/" + globalId);
        return result.contains("200");
    }


/*
    public static void fetchExercises(Context context) throws IOException {
        //TODO Get date
        String date = "11.03.2017";
        String result = GlobalDBConnection.get(host + "/" + DBInfo.EXERCISE_TABLE_NAME + "/" + DBInfo.EXERCISE_COLUMN_NAME_LASTCHANGE + "/" + date);

        try {
            JSONObject jo = new JSONObject(result);
            JSONArray ja = jo.getJSONArray("");
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jline = ja.getJSONObject(i);

                storeInDB(DBInfo.EXERCISE_TABLE_NAME,jline,context);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static Boolean uploadExercise(int localId, Context context){
        JSONObject data = loadFromDB(localId,DBInfo.EXERCISE_TABLE_NAME,context);
        String result="";
        try {
            result = GlobalDBConnection.post(host + "/" + autorMail + "/" + DBInfo.EXERCISE_TABLE_NAME,data.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result.contains("201");
    }

    public static Boolean updateExercise(int localId, Context context){
        JSONObject data = loadFromDB(localId,DBInfo.EXERCISE_TABLE_NAME,context, true);
        String result="";
        try {
            result = GlobalDBConnection.put(host + "/" + autorMail + "/" + DBInfo.EXERCISE_TABLE_NAME + "/" + data.getString(DBInfo.EXERCISE_COLUMN_NAME_ID),data.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result.contains("200");
    }

    public static Boolean deleteExercise(int localId) throws Exception {
        String result = GlobalDBConnection.delete(host + "/" + authorMail + "/" + DBInfo.EXERCISE_TABLE_NAME + "/" + localId);
        return result.contains("200");
    }



    public static void fetchTrainingsunits(Context context) throws IOException {
        //TODO Get date
        String date = "11.03.2017";
        String result = GlobalDBConnection.get(host + "/" + DBInfo.TRAININGSUNIT_TABLE_NAME + "/" + DBInfo.TRAININGSUNIT_COLUMN_NAME_LASTCHANGE + "/" + date);

        try {
            JSONObject jo = new JSONObject(result);
            JSONArray ja = jo.getJSONArray("");
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jline = ja.getJSONObject(i);

                storeInDB(DBInfo.TRAININGSUNIT_TABLE_NAME,jline,context);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static Boolean uploadTrainingsunit(int localId, Context context){
        JSONObject data = loadFromDB(localId,DBInfo.TRAININGSUNIT_TABLE_NAME,context);
        String result="";
        try {
            result = GlobalDBConnection.post(host + "/" + autorMail + "/" + DBInfo.TRAININGSUNIT_TABLE_NAME,data.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result.contains("201");
    }

    public static Boolean updateTrainingsunit(int localId, Context context){
        JSONObject data = loadFromDB(localId,DBInfo.TRAININGSUNIT_TABLE_NAME,context, true);
        String result="";
        try {
            result = GlobalDBConnection.put(host + "/" + autorMail + "/" + DBInfo.TRAININGSUNIT_TABLE_NAME + "/" + data.getString(DBInfo.TRAININGSUNIT_COLUMN_NAME_ID),data.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result.contains("200");
    }

    public static Boolean deleteTrainingsUnit(int localId) throws Exception {
        String result = GlobalDBConnection.delete(host + "/" + authorMail + "/" + DBInfo.TRAININGSUNIT_TABLE_NAME + "/" + localId);
        return result.contains("200");
    }
*/

    /**
     * fetches one element from the local database
     *
     * @param localId id of the element to fetch
     * @param table    DBInfo.EXERCISE_TABLE_NAME or DBInfo.TRAININGSUNIT_TABLE_NAME
     * @param context
     * @return element in JSON representation
     */
    private static JSONObject loadFromDB(int localId, String table, Context context) {
        return loadFromDB(localId, table, context, false);
    }
    /**
     * fetches one element from the local database
     *
     * @param localId   id of the element to fetch
     * @param table     DBInfo.EXERCISE_TABLE_NAME or DBInfo.TRAININGSUNIT_TABLE_NAME
     * @param context
     * @param put       if true, IDGlobal will be returned too, so you can update the global db
     * @return element in JSON representation
     */
    private static JSONObject loadFromDB(int localId, String table, Context context, boolean put) {

        if(debug){
            System.out.println("///////////////////////////////////////////////////////// trying to read from Database //////////////////////////////////////////////////////////");
        }

        JSONObject data = new JSONObject();
        DBConnection dbc = DBHelper.getConnection(context);

        String[] args = {"" + localId};
        Cursor dbCursor = dbc.select(table, null, DBInfo.EXERCISE_COLUMN_NAME_IDLOCAL, args);
        dbCursor.moveToFirst();
        try {
            for (String column : dbCursor.getColumnNames()) {
                if (put || column != "_id") {
                    data.put(column, dbCursor.getString(dbCursor.getColumnIndex(column)));

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(debug){
            System.out.println("///////////////////////////////////////////////////////// finished to read from Database //////////////////////////////////////////////////////////\n"+data.toString());
        }
/*
        String projection =
                DBInfo.EXERCISE_COLUMN_NAME_IDLOCAL+","+
                DBInfo.EXERCISE_COLUMN_NAME_AGE +","+
                DBInfo.EXERCISE_COLUMN_NAME_AUTORMAIL+","+
                DBInfo.EXERCISE_COLUMN_NAME_AUTORNAME+","+
                DBInfo.EXERCISE_COLUMN_NAME_DESCRIPTION+","+
                DBInfo.EXERCISE_COLUMN_NAME_RATING+","+
                DBInfo.EXERCISE_COLUMN_NAME_LASTCHANGE+","+
                DBInfo.EXERCISE_COLUMN_NAME_NAME+","+
                DBInfo.EXERCISE_COLUMN_NAME_KEYWORDS+",";
        if (type == DBInfo.EXERCISE_TABLE_NAME) {
            projection+=
            DBInfo.EXERCISE_COLUMN_NAME_DURATION+","+
            DBInfo.EXERCISE_COLUMN_NAME_GRAPHIC+","+
            DBInfo.EXERCISE_COLUMN_NAME_GROUPSIZE+","+
            DBInfo.EXERCISE_COLUMN_NAME_PHYSIS+","+
            DBInfo.EXERCISE_COLUMN_NAME_TACTIC+","+
            DBInfo.EXERCISE_COLUMN_NAME_TECHNIC+","+
            DBInfo.EXERCISE_COLUMN_NAME_VIDEOLINK+","+
            DBInfo.EXERCISE_COLUMN_NAME_MATIRIAL;
        }
        if(type == DBInfo.TRAININGSUNIT_TABLE_NAME){
            projection+=
            DBInfo.TRAININGSUNIT_COLUMN_NAME_EXERCISE;
        }

        String[] args = {"" + localId};
        Cursor dbCursor = dbc.select(type,projection.split(","),DBInfo.EXERCISE_COLUMN_NAME_IDLOCAL,args);
        dbCursor.moveToFirst();

        try {
            data.put(DBInfo.EXERCISE_COLUMN_NAME_IDLOCAL,   dbCursor.getString(0));
            data.put(DBInfo.EXERCISE_COLUMN_NAME_AGE ,      dbCursor.getString(1));
            data.put(DBInfo.EXERCISE_COLUMN_NAME_AUTORMAIL, dbCursor.getString(2));
            data.put(DBInfo.EXERCISE_COLUMN_NAME_AUTORNAME, dbCursor.getString(3));
            data.put(DBInfo.EXERCISE_COLUMN_NAME_DESCRIPTION,dbCursor.getString(4));
            data.put(DBInfo.EXERCISE_COLUMN_NAME_RATING,    dbCursor.getString(5));
            data.put(DBInfo.EXERCISE_COLUMN_NAME_LASTCHANGE, dbCursor.getString(6));
            data.put(DBInfo.EXERCISE_COLUMN_NAME_NAME,      dbCursor.getString(7));
            data.put(DBInfo.EXERCISE_COLUMN_NAME_KEYWORDS,  dbCursor.getString(8));

            if (type == DBInfo.EXERCISE_TABLE_NAME) {
                data.put(DBInfo.EXERCISE_COLUMN_NAME_DURATION, dbCursor.getString(9));
                data.put(DBInfo.EXERCISE_COLUMN_NAME_GRAPHIC, dbCursor.getString(10));
                data.put(DBInfo.EXERCISE_COLUMN_NAME_GROUPSIZE, dbCursor.getString(11));
                data.put(DBInfo.EXERCISE_COLUMN_NAME_PHYSIS, dbCursor.getString(12));
                data.put(DBInfo.EXERCISE_COLUMN_NAME_TACTIC, dbCursor.getString(13));
                data.put(DBInfo.EXERCISE_COLUMN_NAME_TECHNIC, dbCursor.getString(14));
                data.put(DBInfo.EXERCISE_COLUMN_NAME_VIDEOLINK, dbCursor.getString(15));
                data.put(DBInfo.EXERCISE_COLUMN_NAME_MATIRIAL, dbCursor.getString(16));
            }
            if(type == DBInfo.TRAININGSUNIT_TABLE_NAME){
                data.put(DBInfo.TRAININGSUNIT_COLUMN_NAME_EXERCISE, dbCursor.getString(9));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        return data;
    }

    /**
     *                  writes data to the local Database
     * @param table     table to write to
     * @param data      data to write in json format
     * @param context
     */
    private static void storeInDB(String table, JSONObject data, Context context) {
        if(debug){
            System.out.println("///////////////////////////////////////////////////////// trying to write into Database //////////////////////////////////////////////////////////\n"+data.toString());
        }
        //String[] columns = (String[]) data.keySet().toArray(new String[json.size()]);
        DBConnection dbc = DBHelper.getConnection(context);
        Iterator<String> it = data.keys();
        ContentValues row = new ContentValues();
        try {
            while (it.hasNext()) {
                String key = it.next();
                row.put(key, data.getString(key));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        dbc.insert(table, row);
        row.clear();

        if(debug){
            System.out.println("///////////////////////////////////////////////////////// finished writing into Database //////////////////////////////////////////////////////////");
        }

    }


    //http get auf url1
    private static String get(String url1) throws IOException {
        if(debug) {
            System.out.println("get wird ausgeführt auf: " + url1);
        }
        URL url = null;
        String result = "";
        try {
            url = new URL(url1);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        InputStream stream = null;
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            // Timeout for reading InputStream arbitrarily set to 3000ms.
            connection.setReadTimeout(30000);
            // Timeout for connection.connect() arbitrarily set to 3000ms.
            connection.setConnectTimeout(30000);
            // For this use case, set HTTP method to GET.
            connection.setRequestMethod("GET");
            // Already true by default but setting just in case; needs to be true since this request
            // is carrying an input (response) body.
            connection.setDoInput(true);
            // Open communications link (network traffic occurs here).
            connection.connect();

            // Retrieve the response body as an InputStream.
            stream = connection.getInputStream();
            if (stream != null) {
                // Converts Stream to String with max length of 500.
                result = convertInputStreamToString(stream);
            }
        } finally {
            // Close Stream and disconnect HTTPS connection.
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;

    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    //http post auf url1 mit s als /application/JSON und die ausgabe zurückgeben
    private static String post(String url1, String s) throws IOException {
        if(debug) {
            System.out.println("post wird ausgeführt auf: " + url1 + " mit " + s);
        }
        URL url = null;
        String result = "";
        try {
            url = new URL(url1);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        InputStream stream = null;
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setChunkedStreamingMode(0);

            OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());        //OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            wr.write(s);                                                                            //write(out,s);
            wr.flush();                                                                             //out.close();

            // Retrieve the response body as an InputStream.
            stream = urlConnection.getInputStream();
            if (stream != null) {
                // Converts Stream to String with max length of 500.
                result = convertInputStreamToString(stream);
            }

        } finally {
            urlConnection.disconnect();
        }
        return (result);
    }

    //http delete auf url1 und die ausgabe zurückgeben
    private static String delete(String url1) throws IOException {
        if(debug) {
            System.out.println("delete wird ausgeführt auf " + url1);
        }

        URL url = null;
        String result = "";
        try {
            url = new URL(url1);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        InputStream stream = null;
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            // Timeout for reading InputStream arbitrarily set to 3000ms.
            connection.setReadTimeout(30000);
            // Timeout for connection.connect() arbitrarily set to 3000ms.
            connection.setConnectTimeout(30000);
            // For this use case, set HTTP method to GET.
            connection.setRequestMethod("DELETE");
            // Already true by default but setting just in case; needs to be true since this request
            // is carrying an input (response) body.
            connection.setDoInput(true);
            // Open communications link (network traffic occurs here).
            connection.connect();

            // Retrieve the response body as an InputStream.
            stream = connection.getInputStream();
            if (stream != null) {
                // Converts Stream to String with max length of 500.
                result = convertInputStreamToString(stream);
            }
        } finally {
            // Close Stream and disconnect HTTPS connection.
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return (result);
    }

    //put auf url1 mit s und die ausgabe zurückgeben
    private static String put(String url1, String s) throws IOException {
        if(debug) {
            System.out.println("put wird ausgeführt auf \" " + url1 + " mit \" " + s );
        }
        URL url = null;
        String result = "";
        try {
            url = new URL(url1);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        InputStream stream = null;
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("PUT");
            urlConnection.setChunkedStreamingMode(0);

            OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());        //OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            wr.write(s);                                                                            //write(out,s);
            wr.flush();                                                                             //out.close();

            // Retrieve the response body as an InputStream.
            stream = urlConnection.getInputStream();
            if (stream != null) {
                // Converts Stream to String with max length of 500.
                result = convertInputStreamToString(stream);
            }

        } finally {
            urlConnection.disconnect();
        }
        return (result);
    }
}
