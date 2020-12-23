package com.dcoleman.mad_christmas_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class PlayScreen_Accelerometer extends AppCompatActivity implements SensorEventListener
{

    int sequenceCount = 4, n = 0;
    int[] gameSequence = new int[120];

    // experimental values for hi and lo magnitude limits
    private final double PValue = 5;
    private final double NValue = -5;
    boolean highLimit = false;      // detect high limit

    private SensorManager mSensorManager;
    private Sensor mSensor;

    TextView score ,round;
    int scoreValue, roundValue ;

    Button btnNorth, btnSouth, btnEast, btnWest;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_screen__accelerometer);

        // we are going to use the sensor service
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        score = findViewById(R.id.tvScoreValue);
        round = findViewById(R.id.tvRoundinfo);

        btnNorth = findViewById(R.id.btnYellow);
        btnSouth = findViewById(R.id.btnRed);
        btnEast = findViewById(R.id.btnGreen);
        btnWest = findViewById(R.id.btnBlue);

        scoreValue = getIntent().getIntExtra("score", -1);
        roundValue = getIntent().getIntExtra("round", -1);

        sequenceCount = getIntent().getIntExtra("sequenceCount", -1);
        gameSequence = getIntent().getIntArrayExtra("sequenceArray");

        view = new View(this);
        //round.setText(String.valueOf(roundValue));
        round.setText(String.valueOf(MainActivity.roundValue));
    }

    /*
     * When the app is brought to the foreground - using app on screen
     */
    protected void onResume()
    {
        super.onResume();
        // turn on the sensor
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    /*
     * App running but not on screen - in the background
     */
    protected void onPause()
    {
        super.onPause();
        mSensorManager.unregisterListener(this);    // turn off listener to save power
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        if(z >= PValue && !highLimit)
        {
            highLimit = true;
            btnNorth.setPressed(true);
            doNorth(view);
        }
        else if( z < PValue)
        {
            btnNorth.setPressed(false);
        }

        if(z <= NValue && !highLimit)
        {
            highLimit = true;
            btnSouth.setPressed(true);
            doSouth(view);
        }
        else if(z > NValue)
        {
            btnSouth.setPressed(false);
        }

        if(y >= PValue && !highLimit)
        {
            highLimit = true;
            btnEast.setPressed(true);
            doEast(view);
        }
        else if(y < PValue)
        {
            btnEast.setPressed(false);
        }

        if(y <= NValue && !highLimit)
        {
            highLimit = true;
            btnWest.setPressed(true);
            doWest(view);
        }
        else if(y > NValue)
        {
            btnWest.setPressed(false);
        }

        if(z < PValue && z > NValue && y < PValue && y > NValue)
        {
            highLimit = false;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
        // not used
    }

    public void doSouth(View view)
    {
        //value is 2
        HandleInput(2);
    }

    public void doNorth(View view)
    {
        //value is 3
        HandleInput(3);
    }

    public void doEast(View view)
    {
        //value is 4
        HandleInput(4);
    }

    public void doWest(View view)
    {
        //value is 1
        HandleInput(1);
    }

    public void HandleInput(int value)
    {
        //test to see if correct value is outputted

        if(n+1 < sequenceCount)
        {
            if(gameSequence[n] == value)
            {
                scoreValue++;
                score.setText(String.valueOf(scoreValue));
                n++;
            }
            else
            {
                Intent gameOverActivity = new Intent(view.getContext(), GameOver.class);

                gameOverActivity.putExtra("score", scoreValue);
                gameOverActivity.putExtra("round", roundValue);

                startActivity(gameOverActivity);
            }
        }
        else if(n+1 >= sequenceCount)
        {
            if(gameSequence[n] == value)
            {
                scoreValue++;
                score.setText(String.valueOf(scoreValue));
                roundValue++;

                Intent mainActivity = new Intent(view.getContext(), MainActivity.class);

                MainActivity.scoreValue = scoreValue;
                MainActivity.roundValue = roundValue;
                MainActivity.sequenceCount = sequenceCount + 2;

                startActivity(mainActivity);
                finish();
            }
            else
            {
                Intent gameOverActivity = new Intent(view.getContext(), GameOver.class);

                gameOverActivity.putExtra("score", scoreValue);
                gameOverActivity.putExtra("round", roundValue);

                startActivity(gameOverActivity);
            }
        }
    }
}