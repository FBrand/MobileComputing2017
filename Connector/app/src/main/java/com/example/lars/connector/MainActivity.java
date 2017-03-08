package com.example.lars.connector;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    private TextView text;
    private Button button;

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... s) {

            URL url = null;
            String result = "";
            try {
                url = new URL(s[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                try {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    if(in != null)
                        result = convertInputStreamToString(in);
                    else
                        result = "Did not work!";
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } finally {
                urlConnection.disconnect();
            }
            return result;

        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
        }
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String s = "Hey";
        setContentView(R.layout.activity_main);
        text = (TextView) findViewById(R.id.textView);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //button.setText("Hallooooo");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
// do the thing that takes a long time
                        try {
                            //System.out.println( Connector.get("http://10.0.2.2:9000/persons"));
                            Connector.post("http://10.0.2.2:9000/person","Hallotest123");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                button.setText("done");
            }
        });
    }
}
