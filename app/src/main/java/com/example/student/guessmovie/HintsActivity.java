package com.example.student.guessmovie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class HintsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hints);

        TextView hint = (TextView) findViewById(R.id.hintsActivityTextView);
        try {
            JSONObject obj = new JSONObject(getIntent().getStringExtra("movie"));
            int hints_taken = getIntent().getIntExtra("hints_taken", 0);

            if (hints_taken == 1) {
                hint.setText(obj.getString("hint1"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

//    public String loadJSONFromAsset() {
//        String json = null;
//        try {
//            InputStream is = getAssets().open("database.json");
//            int size = is.available();
//            byte[] buffer = new byte[size];
//            is.read(buffer);
//            is.close();
//            json = new String(buffer, "UTF-8");
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            return null;
//        }
//        return json;
//    }
}
