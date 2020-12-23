package com.dcoleman.mad_christmas_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class GameOver extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
    }

    public void doRetry(View view) {
        Intent Retry = new Intent(view.getContext(),MainActivity.class);

        MainActivity.roundValue = 1;
        MainActivity.scoreValue = 0;
        MainActivity.sequenceCount = 4;

        startActivity(Retry);
        finish();
    }

    public void doScore(View view) {
        Intent highScoreActivity = new Intent(view.getContext(), HighScore.class);

        highScoreActivity.putExtra("score", MainActivity.scoreValue);

        startActivity(highScoreActivity);
        finish();
    }
}