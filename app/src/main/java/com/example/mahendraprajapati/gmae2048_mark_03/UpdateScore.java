package com.example.mahendraprajapati.gmae2048_mark_03;

import android.widget.TextView;

import android.os.Handler;

public class UpdateScore implements Runnable {

    private TextView scoreTextView;
    private long startTimeInMillis;
    private long currentTimeInMillis;
    private Handler handler;
    private boolean runningState = false;

    public UpdateScore(TextView scoreTextView, Handler handler) {
        this.scoreTextView = scoreTextView;
        this.handler = handler;
        startTimeInMillis = System.currentTimeMillis();
    }

    public UpdateScore(TextView scoreTextView, Handler handler, long elapsedTimesInMillis) {
        this(scoreTextView, handler);
        startTimeInMillis = System.currentTimeMillis() - elapsedTimesInMillis;
    }

    @Override
    public void run() {
        currentTimeInMillis = System.currentTimeMillis() - startTimeInMillis;

        int milliSeconds = (int) currentTimeInMillis % 1000;
        int seconds = (int) (currentTimeInMillis / 1000) % 60;
        int minutes = (int) ((currentTimeInMillis / (1000 * 60))  % 60);

        String timStamp = String.format("%02d:%02d:%03d", milliSeconds, seconds, minutes);

        scoreTextView.setText(timStamp);
        if(runningState)
            handler.postDelayed(this, 30);

    }

    public void stopRunning() {
        runningState = false;
    }

    public long getCurrentTimeInMillis() {
        return currentTimeInMillis;
    }

}
