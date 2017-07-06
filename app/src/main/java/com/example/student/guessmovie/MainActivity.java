package com.example.student.guessmovie;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    Iterator<String> keys;
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
        final TextView totalScoreTextView = (TextView) findViewById(R.id.totalScoreDisplay);
        final TextView currentScoreTextView = (TextView) findViewById(R.id.currentScoreDisplay);
        final EditText movieNameEditText = (EditText) findViewById(R.id.movieNameEditText);

        final Integer total_score = scoresSharedPreferences.getInt("total_score", 0);
        totalScoreTextView.setText(total_score.toString());
        Integer current_score = scoresSharedPreferences.getInt("current_score", 0);
        currentScoreTextView.setText(current_score.toString());

        JSONObject currentMovie = null;
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            keys = obj.keys();
            String currentKey = keys.next();
            currentMovie = obj.getJSONObject(currentKey);
            editor.putString("currentKey", currentKey);
            editor.commit();
            hintsTextView.setText(currentMovie.getString("description"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String mov = currentMovie.toString();


        hintsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer currentScore = scoresSharedPreferences.getInt("current_score", 0);
                currentScore -= 2;
                int hints_taken = scoresSharedPreferences.getInt("hints_taken", 0);
                hints_taken++;
                editor.putInt("hints_taken", hints_taken);
                editor.putInt("current_score", currentScore);
                editor.commit();
                currentScoreTextView.setText(currentScore.toString());
                Intent intent = new Intent(MainActivity.this, HintsActivity.class);
                intent.putExtra("hints_taken",hints_taken);
                intent.putExtra("movie", scoresSharedPreferences.getString("currentKey", ""));
                startActivity(intent);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer currentScore = scoresSharedPreferences.getInt("current_score", 0);
                Integer totalScore = scoresSharedPreferences.getInt("total_score", 0);
                String enteredMovieName = movieNameEditText.getText().toString();
                if (enteredMovieName.equals(scoresSharedPreferences.getString("currentKey", ""))) {
                    totalScore += currentScore;
                    currentScore = 10;
                    editor.putInt("total_score", totalScore);
                    editor.putInt("current_score", currentScore);
                    editor.commit();
                    totalScoreTextView.setText(totalScore.toString());
                    currentScoreTextView.setText(currentScore.toString());
                    if (keys.hasNext()) {
                        String currentKey = keys.next();
                        editor.putString("currentKey", currentKey);
                        editor.commit();
                        try {
                            JSONObject db = new JSONObject(loadJSONFromAsset());
                            hintsTextView.setText(db.getJSONObject(scoresSharedPreferences.getString("currentKey", "")).getString("description"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        editor.putInt("hints_taken", 0);
                        editor.commit();
                    }
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Game complete. Thank you for playing.");
                        builder.show();
                    }
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Incorrect. Try once more!");
                    builder.show();
                }
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
