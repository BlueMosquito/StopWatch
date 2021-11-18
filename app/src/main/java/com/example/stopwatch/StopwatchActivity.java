package com.example.stopwatch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

public class StopwatchActivity extends AppCompatActivity {

    private int seconds = 0;
    private boolean running;

    //Det kollar om aktivitetet kördes innan onStop metoden.
    private boolean wasRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);

        /*Med hjälp av detta if sats så försöker man ta fram det gamla data om aktivitetet kör
        onDestroy metod och sedan onCreate igen, då vill man ha det gamla datan och inte att
        allt börjar om.
         */
        if(savedInstanceState != null){
            seconds = savedInstanceState.getInt("seconds");
            running = savedInstanceState.getBoolean("running");

            /*Här försöker man se om aktivitetet kördes innan onStop metoden, om så, då kommer
            det köras igen och innehåller det gamla datan eftersom man sparade det data med
            Bundle i onSaveInstanceState metoden.
            */
            wasRunning = savedInstanceState.getBoolean("wasRunning");
        }
        runTimer();
    }

    public void onClickStart(View view){
        running = true;
    }

    public void onClickStop(View view){
        running = false;
    }

    public void onClickReset(View view){
        running = false;
        seconds = 0;
    }

    public void runTimer(){
        final TextView timeView = (TextView) findViewById(R.id.time_view);
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = seconds/3600;
                int minutes = (seconds%3600)/60;
                int secs = seconds%60;
                String time = String.format(Locale.getDefault(),
                        "%d %02d %02d", hours, minutes, secs);
                timeView.setText(time);
                if(running){
                    seconds++;
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("seconds", seconds);
        savedInstanceState.putBoolean("running", running);
        savedInstanceState.putBoolean("wasRunning", wasRunning);
    }

    //Metoden körs när aktivitetet inte är synligt längre.
    @Override
    public void onPause(){
        super.onPause();

        //Kollar om aktivitetet kördes innan onStop metoden kallades.
        wasRunning = running;
        running = false;
    }

    //Vi kollar om programmet kördes innan, om så då börjar vi köra det igen
    @Override
    public void onResume(){
        super.onResume();
        if(wasRunning){
            running = true;
        }
    }

}