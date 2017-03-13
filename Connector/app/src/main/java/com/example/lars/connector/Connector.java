package com.example.lars.connector;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;

import java.net.HttpURLConnection;

/**
 * Created by lars on 07.03.17.
 */

public class Connector {
    public static String get(String url1) throws IOException {
        System.out.println("get wird ausgeführt: " + url1);
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
    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public static void post(String url1, String s) throws IOException {
        System.out.println("post wird ausgeführt: " + s);
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
    System.out.println(result);
    }

    private static void write(OutputStream out, String s) {
        PrintWriter st = new PrintWriter(out);
        st.write("Content-Type: application/x-www-form-urlencoded\n" +           //
                "Content-Length: 12\n" +
                "\n" +
                "name=oledaef");
        st.close();
    }

    public static void delete(String url1) throws IOException {
        System.out.println("delete wird ausgeführt");

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
        System.out.println(result);
        return;

    }

    public static void put(String url1, String s) throws IOException {
        System.out.println("put wird ausgeführt: " + s);
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
        System.out.println(result);
    }

}
