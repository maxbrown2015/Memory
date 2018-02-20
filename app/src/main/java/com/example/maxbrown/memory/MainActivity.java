package com.example.maxbrown.memory;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    private Button startButton;
    private Spinner difficultySpinner;
    private String difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        startButton = (Button) findViewById(R.id.start_game_button);



        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                difficultySpinner = (Spinner) findViewById(R.id.difficulty_select_spinner);
                difficulty = (String) difficultySpinner.getSelectedItem();

                Intent intent = new Intent(MainActivity.this, EasyGameActivity.class);
                intent.putExtra("difficulty" , difficulty);
                startActivity(intent);

            }
        });

    }
}
