package com.example.student.guessmovie;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SharedPreferences scoresSharedPreferences = getSharedPreferences("scores", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = scoresSharedPreferences.edit();
        editor.putInt("total_score", 0);
        editor.putInt("current_score", 10);
        editor.putInt("hints_taken", 0);
        editor.commit();


        Button hintsButton = (Button) findViewById(R.id.hintsButton);
        Button submitButton = (Button) findViewById(R.id.submitButton);
        final TextView hintsTextView = (TextView) findViewById(R.id.hintsTextView);
        TextView totalScore = (TextView) findViewById(R.id.totalScoreDisplay);


        JSONObject currentMovie = null;
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            Iterator<String> keys = obj.keys();
            currentMovie = obj.getJSONObject(keys.next());
            hintsTextView.setText(currentMovie.getString("description"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String mov = currentMovie.toString();


        hintsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentScore = scoresSharedPreferences.getInt("current_score", 0);
                currentScore -= 2;
                int hints_taken = scoresSharedPreferences.getInt("hints_taken", 0);
                hints_taken++;
                editor.putInt("hints_taken", hints_taken);
                editor.putInt("current_score", currentScore);
                Intent intent = new Intent(MainActivity.this, HintsActivity.class);
                intent.putExtra("hints_taken",hints_taken);
                intent.putExtra("movie", mov);
                startActivity(intent);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentScore = scoresSharedPreferences.getInt("current_score", 0);
                int totalScore = scoresSharedPreferences.getInt("total_score", 0);
                totalScore += currentScore;
                editor.putInt("total_score", totalScore);
                editor.putInt("current_score", 10);
                editor.commit();
            }
        });
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("database.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
