package com.example.lars.connector;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;

import java.net.HttpURLConnection;

/**
 * Created by lars on 07.03.17.
 */

public class Connector {
    public static String get(String url1) throws IOException {
        System.out.println("Hallo");
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
        URL url = null;
        String result = "";
        try {
            url = new URL(url1);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            urlConnection.setDoOutput(true);
            urlConnection.setChunkedStreamingMode(0);

            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            write(out,s);
            out.close();

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            convertInputStreamToString(in);
        } finally {
            urlConnection.disconnect();
        }

    }

    private static void write(OutputStream out, String s) {
        PrintWriter st = new PrintWriter(out);
        st.write("Content-Type: application/x-www-form-urlencoded\n" +
                "Content-Length: 12\n" +
                "\n" +
                "name=oledaef");
        st.close();
    }

}
