package com.example.bagaaga;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private EditText user;
    private Button button_check;
    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = findViewById(R.id.user);
        button_check = findViewById(R.id.button_check);
        result = findViewById(R.id.result);

        button_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.getText().toString().trim().equals(""))
                    Toast.makeText(MainActivity.this,R.string.no_user_input, Toast.LENGTH_LONG).show();
                else{
                    String city = user.getText().toString();
                    String key = "964b2796b3fb4b51b7f380aabcb8b56d";
                    String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city +"&apikey=" + key + "&units=metric&lang=ru";

                    new GetUrlData().execute(url);
                }
            }
        });

    }
    private class GetUrlData extends AsyncTask<String, String, String> {
        protected void onPreExecute(){
            super.onPreExecute();
            result.setText("Ожидайте кек");
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection;
            connection = null;
            BufferedReader reader;
            reader = null;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null)
                    buffer.append(line).append("\n");
                return buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null)
                    connection.disconnect();

                if (reader != null)
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
            return null;
        }
        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result_i){
            super.onPostExecute(result_i);
            try {
                JSONObject obj = new JSONObject((result_i));
                result.setText("Температура: "+ obj.getJSONObject("main").getDouble("temp"));
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

}