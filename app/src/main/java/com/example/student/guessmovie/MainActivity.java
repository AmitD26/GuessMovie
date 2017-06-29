package com.example.student.guessmovie;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SharedPreferences scoresSharedPreferences = getSharedPreferences("scores", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = scoresSharedPreferences.edit();
        editor.putInt("total_score", 0);
        editor.putInt("current_score", 10);
        editor.commit();

        Button hintsButton = (Button) findViewById(R.id.hintsButton);
        Button submitButton = (Button) findViewById(R.id.submitButton);
        final TextView hintsTextView = (TextView) findViewById(R.id.hintsTextView);

        hintsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int totalScore = scoresSharedPreferences.getInt("total_score", 0);
                Intent intent = new Intent(MainActivity.this, HintsActivity.class);
                startActivity(intent);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
