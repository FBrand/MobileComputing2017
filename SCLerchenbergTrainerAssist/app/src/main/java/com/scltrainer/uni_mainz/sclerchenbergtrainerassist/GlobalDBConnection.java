package com.scltrainer.uni_mainz.sclerchenbergtrainerassist;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;

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
    //gibt debugnachrichten aus
    static boolean debug = true;
    //deaktiviert die verbindung zur globalen datenbank
    static boolean offline = false;

    //Server adresse. Unbedingt anpassen.
    static String host = "http://XMG:80";//10.0.2.2:80"http://134.93.143.94:80"

    /**
     * function to add new data from the global to the local database
     *
     * @param tableName  name of the table to download
     * @param context    context of the calling activity
     * @param lastChange last time the table was updated
     * @throws IOException
     */
    public static void fetch(final String tableName, final Context context, final String lastChange) {
        if(offline)
            return;
        new Thread(new Runnable() {
            @Override
            public void run() {
// do the thing that takes a long time
                //TODO Get date
                String result = null;
                try {
                    result = GlobalDBConnection.get(host + "/" + tableName + "/" + DBInfo.EXERCISE_COLUMN_NAME_LASTCHANGE + "/" + "\"" + lastChange + "\"");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    if(result.length()<20){
                        return;
                    }
                    JSONArray ja = new JSONArray(result.substring(17));
                    //Log.i("get",jo.toString());
                    //JSONArray ja = jo.getJSONArray("");
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jline = ja.getJSONObject(i);

                        storeInDB(tableName, jline, context);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * function to add a row from the local to the global database
     *
     * @param tableName name of the table to upload to
     * @param localId   id of the row to upload
     * @param context   context of the calling activity
     * @return true if successfull, else false
     */
    public static void upload(final String tableName, final int localId, final Activity context) {
        Log.i("upload","/////////////////////////////////////////////////////////////////////////upload starting/////////////////////////////////////////////////////////////////////////////////////");
        if(offline)
            return;
        new Thread(new Runnable() {
            @Override
            public void run() {
// do the thing that takes a long time

                SharedPreferences shared = context.getSharedPreferences("SHAREDPREFERENCES", Context.MODE_PRIVATE);
                String autorMail = shared.getString("USEREMAIL", "");

                JSONObject data = loadFromDB(localId, tableName, context);
                String result = "";
                try {
                    result = GlobalDBConnection.post(host + "/" + autorMail + "/" + tableName, data.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // write globalId in local db
                Log.d("result",result);
                int globalId = Integer.parseInt(result.substring(result.indexOf("lastInsertId:")+14, result.indexOf("{")-1).trim());
                DBConnection dbc = DBHelper.getConnection(context);
                ContentValues row = new ContentValues();
                row.put(DBInfo.EXERCISE_COLUMN_NAME_ID, globalId);
                String[] args = {"" + localId};

                dbc.update(tableName, row, DBInfo.EXERCISE_COLUMN_NAME_IDLOCAL + " = ?", args);
                row.clear();
            }
        }).start();
        //return result.contains("201");
    }

    /**
     * function to update a row in the global database with data from the local database
     *
     * @param tableName name of the table to update
     * @param localId   local id of the row to update
     * @param context   context of the calling activity
     * @return true if successfull, else false
     */
/*   public static Boolean update(String tableName, int localId, Context context) {
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
*/

    /**
     * @param tableName name of the table to delete from
     * @param localId   local id of the row to delete
     * @return true if successfull, else false
     */
    public static void delete(final String tableName, final int localId, final Activity context) {
        if(offline)
            return;

        new Thread(new Runnable() {
            @Override
            public void run() {
// do the thing that takes a long time

                SharedPreferences shared = context.getSharedPreferences("SHAREDPREFERENCES", Context.MODE_PRIVATE);
                String autorMail = shared.getString("USEREMAIL", "");

                String result = null;//TODO + globalId);
                try {
                    result = GlobalDBConnection.delete(host + "/" + autorMail + "/" + tableName + "/");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        //return result.contains("200");
    }


    /**
     * fetches one element from the local database
     *
     * @param localId id of the element to fetch
     * @param table   DBInfo.EXERCISE_TABLE_NAME or DBInfo.TRAININGSUNIT_TABLE_NAME
     * @param context
     * @return element in JSON representation
     */
    private static JSONObject loadFromDB(int localId, String table, Context context) {
        return loadFromDB(localId, table, context, false);
    }

    /**
     * fetches one element from the local database
     *
     * @param localId id of the element to fetch
     * @param table   DBInfo.EXERCISE_TABLE_NAME or DBInfo.TRAININGSUNIT_TABLE_NAME
     * @param context
     * @param put     if true, IDGlobal will be returned too, so you can update the global db
     * @return element in JSON representation
     */
    private static JSONObject loadFromDB(int localId, String table, Context context, boolean put) {

        if (debug) {
            System.out.println("///////////////////////////////////////////////////////// trying to read from Database //////////////////////////////////////////////////////////");
        }

        JSONObject data = new JSONObject();
        DBConnection dbc = DBHelper.getConnection(context);

        String[] args = {"" + localId};

        Log.d("db", table + " " + null + " " + DBInfo.EXERCISE_COLUMN_NAME_IDLOCAL + " " + args[0]);

        Cursor dbCursor = dbc.select(table, null, DBInfo.EXERCISE_COLUMN_NAME_IDLOCAL + " = ?", args);
        dbCursor.moveToFirst();
        try {
            for (String column : dbCursor.getColumnNames()) {
                Log.d("db",column + "   " + !column.equals("_id"));
                if (put || ( !column.equals(DBInfo.EXERCISE_COLUMN_NAME_ID )&& !column.equals("_id") ) ) {
                    if(dbCursor.getString(dbCursor.getColumnIndex(column))!=null) {
                        Log.d("db", dbCursor.getString(dbCursor.getColumnIndex(column)));
                    }
                    data.put(column, dbCursor.getString(dbCursor.getColumnIndex(column)));

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (debug) {
            System.out.println("///////////////////////////////////////////////////////// finished to read from Database //////////////////////////////////////////////////////////\n" + data.toString());
            Log.i("JSON", data.toString());

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
     * writes data to the local Database
     *
     * @param table   table to write to
     * @param data    data to write in json format
     * @param context
     */
    private static void storeInDB(String table, JSONObject data, Context context) {
        if (debug) {
            System.out.println("///////////////////////////////////////////////////////// trying to write into Database //////////////////////////////////////////////////////////\n" + data.toString());
        }
        //String[] columns = (String[]) data.keySet().toArray(new String[json.size()]);
        DBConnection dbc = DBHelper.getConnection(context);
        Iterator<String> it = data.keys();
        ContentValues row = new ContentValues();
        it.next();
        try {
            row.put(DBInfo.EXERCISE_COLUMN_NAME_ID, data.getString("id"));
            while (it.hasNext()) {
                String key = it.next();
                if(!key.equals("idLocal") ){//&& !key.equals("lastChange")) {
                    Log.i("DBRow", key);
                    /*if (ints.contains(key)) {
                        System.out.println("///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////");
                        Log.i("RowContent int",""+ data.getInt(key));
                        row.put(key, data.getInt(key));
                    }else {*/
                        Log.i("RowContent", data.getString(key));
                        row.put(key, data.getString(key));
                    //}
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        dbc.insert(table, row);
        row.clear();

        if (debug) {
            System.out.println("///////////////////////////////////////////////////////// finished writing into Database //////////////////////////////////////////////////////////");
        }

    }


    //http get auf url1
    private static String get(String url1) throws IOException {
        if (debug) {
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
        if (debug) {
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
        if (debug) {
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
        if (debug) {
            System.out.println("put wird ausgeführt auf \" " + url1 + " mit \" " + s);
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
