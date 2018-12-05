package com.example.ranu.gajabgame;

import android.support.v7.app.ActionBar;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;


public class MainActivity extends AppCompatActivity {

    // 1 = yellow, 2 = red, 3=blue

    int activePlayer;

    int previousCounter = -1;
    int previousCounterReplacementsLeft = -1;
    int maxPreviousCounterReplacements = 1;

    int yellowScoreValue = 0;
    int redScoreValue = 0;
    int blueScoreValue = 0;

    boolean gameIsActive = true;

    // 0 means unplayed

    int[] gameState = {0, 0, 0, 0, 0, 0, 0, 0, 0};

    int[][] winningPositions = {{0,1,2}, {3,4,5}, {6,7,8}, {0,3,6}, {1,4,7}, {2,5,8}, {0,4,8}, {2,4,6}};

    int[] positionsReplacements = new int[9];

    ActionBar actionBar;
    Toolbar actionBarToolbar;

    public void dropIn(View view) {

        RelativeLayout parent = (RelativeLayout) view;

        ImageView counter = (ImageView) parent.getChildAt(0);

        int tappedCounterIndex = Integer.parseInt(counter.getTag().toString());

        if (gameIsActive) {

            //checking for previous counter replacements count
            if(!(positionsReplacements[tappedCounterIndex] == 0 && gameState[tappedCounterIndex] != 0)) {
                if (previousCounter == tappedCounterIndex) {
                    if (previousCounterReplacementsLeft == 0) {
                        return;
                    } else {
                        previousCounterReplacementsLeft--;
                    }
                } else if (gameState[tappedCounterIndex] != 0) {
                    previousCounterReplacementsLeft = maxPreviousCounterReplacements - 1;
                    previousCounter = tappedCounterIndex;
                } else {
                    previousCounterReplacementsLeft = maxPreviousCounterReplacements;
                    previousCounter = tappedCounterIndex;
                }
            }
            //checking for position replacements
            if(positionsReplacements[tappedCounterIndex] == 0 && gameState[tappedCounterIndex] != 0){
                return;
            }
            else if(gameState[tappedCounterIndex] != 0) {
                positionsReplacements[tappedCounterIndex]--;
            }


            gameState[tappedCounterIndex] = activePlayer;


            //animating the counter in position and changing turn animation
            counter.setTranslationY(-1000f);

            if (activePlayer == 1) {
                counter.setImageResource(R.drawable.yellow);
                activePlayer = 2;

                TextView yellowScore = (TextView) findViewById(R.id.yellowScore);
                TextView redScore = (TextView) findViewById(R.id.redScore);

                yellowScore.setTypeface(null, Typeface.NORMAL);
                yellowScore.setPaintFlags(0);
                redScore.setTypeface(null, Typeface.BOLD);
                redScore.setPaintFlags(redScore.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

                actionBar.setBackgroundDrawable(new ColorDrawable(Color.RED));
                actionBarToolbar.setTitleTextColor(Color.WHITE);

            } else if(activePlayer == 2) {
                counter.setImageResource(R.drawable.red);
                activePlayer = 3;

                TextView blueScore = (TextView) findViewById(R.id.blueScore);
                TextView redScore = (TextView) findViewById(R.id.redScore);

                redScore.setTypeface(null, Typeface.NORMAL);
                redScore.setPaintFlags(0);
                blueScore.setTypeface(null, Typeface.BOLD);
                blueScore.setPaintFlags(blueScore.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

                actionBar.setBackgroundDrawable(new ColorDrawable(Color.BLUE));
            } else {
                counter.setImageResource(R.drawable.blue);
                activePlayer = 1;

                TextView blueScore = (TextView) findViewById(R.id.blueScore);
                TextView yellowScore = (TextView) findViewById(R.id.yellowScore);

                blueScore.setTypeface(null, Typeface.NORMAL);
                blueScore.setPaintFlags(0);
                yellowScore.setTypeface(null, Typeface.BOLD);
                yellowScore.setPaintFlags(yellowScore.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

                actionBar.setBackgroundDrawable(new ColorDrawable(Color.YELLOW));
                actionBarToolbar.setTitleTextColor(Color.BLACK);
            }

            counter.animate().translationYBy(1000f).rotation(180).setDuration(300);


            //changing replacements left count in GUI
            TextView replacementsText = (TextView) parent.getChildAt(1);
            replacementsText.setText(Integer.toString(positionsReplacements[tappedCounterIndex]));

            //freezing animation if replacements = 0
            if(positionsReplacements[tappedCounterIndex] == 0){
                ImageView freezer = ((ImageView)parent.getChildAt(2));
                freezer.setAlpha(0.1f);
                freezer.bringToFront();
            }

            //checking if game won
            for (int[] winningPosition : winningPositions) {

                if (gameState[winningPosition[0]] == gameState[winningPosition[1]] &&
                        gameState[winningPosition[1]] == gameState[winningPosition[2]] &&
                        gameState[winningPosition[0]] != 0) {

                    // Someone has won!

                    gameIsActive = false;

                    String winner;

                    if (gameState[winningPosition[0]] == 1) {

                        winner = "Yellow";
                        TextView yellowScore = (TextView) findViewById(R.id.yellowScore);
                        yellowScoreValue++;
                        yellowScore.setText("Yellow: " + yellowScoreValue);
                    } else if(gameState[winningPosition[0]] == 2){
                        winner = "Red";
                        TextView redScore = (TextView) findViewById(R.id.redScore);
                        redScoreValue++;
                        redScore.setText("Red: " + redScoreValue);
                    } else {
                        winner = "Blue";
                        TextView blueScore = (TextView) findViewById(R.id.blueScore);
                        blueScoreValue++;
                        blueScore.setText("Blue: " + blueScoreValue);
                    }

                    TextView winnerMessage = (TextView) findViewById(R.id.winnerMessage);

                    winnerMessage.setText(winner + " has won!");

                    LinearLayout layout = (LinearLayout) findViewById(R.id.playAgainLayout);

                    layout.setVisibility(View.VISIBLE);

                }
            }


            //checking for game is over

            boolean gameIsOver = true;


            for(int i=0; i<gameState.length; i++){
                if(positionsReplacements[i] !=0 || gameState[i] != 0){
                    gameIsOver = false;
                    break;
                }
            }

            if (gameIsOver) {
                gameIsActive = false;

                TextView winnerMessage = (TextView) findViewById(R.id.winnerMessage);

                winnerMessage.setText("It's a draw");

                LinearLayout layout = (LinearLayout)findViewById(R.id.playAgainLayout);

                layout.setVisibility(View.VISIBLE);

            }

        }

    }

    public void playAgain(View view) {

        previousCounterReplacementsLeft = -1;
        previousCounter = -1;

        gameIsActive = true;

        initializeReplacements();

        LinearLayout layout = (LinearLayout)findViewById(R.id.playAgainLayout);

        layout.setVisibility(View.INVISIBLE);


        for (int i = 0; i < gameState.length; i++) {

            gameState[i] = 0;

        }

        GridLayout gridLayout = (GridLayout)findViewById(R.id.gridLayout);

        for (int i = 0; i< gridLayout.getChildCount(); i++) {

            ((ImageView) ((RelativeLayout) gridLayout.getChildAt(i)).getChildAt(0)).setImageResource(0);
            ((ImageView) ((RelativeLayout) gridLayout.getChildAt(i)).getChildAt(2)).setAlpha(0f);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar = getSupportActionBar();
        actionBarToolbar = (Toolbar) findViewById(R.id.action_bar);

        initializeReplacements();

    }



    public void initializeReplacements() {
        Random r = new Random();

        for (int i = 0; i < positionsReplacements.length; i++) {
            positionsReplacements[i] = r.nextInt(11); //0-10
        }

        //changing all replacements in GUI
        GridLayout gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            ((TextView) ((RelativeLayout) gridLayout.getChildAt(i)).getChildAt(1)).setText(Integer.toString(positionsReplacements[i]));
        }

        //randomize active player
        activePlayer = r.nextInt(3) + 1;

        TextView yellowScore = (TextView) findViewById(R.id.yellowScore);
        TextView redScore = (TextView) findViewById(R.id.redScore);
        TextView blueScore = (TextView) findViewById(R.id.blueScore);

        yellowScore.setTypeface(null, Typeface.NORMAL);
        yellowScore.setPaintFlags(0);

        redScore.setTypeface(null, Typeface.NORMAL);
        redScore.setPaintFlags(0);

        blueScore.setTypeface(null, Typeface.NORMAL);
        blueScore.setPaintFlags(0);

        switch (activePlayer){
            case 1:
                actionBar.setBackgroundDrawable(new ColorDrawable(Color.YELLOW));
                actionBarToolbar.setTitleTextColor(Color.BLACK);

                yellowScore.setTypeface(null, Typeface.BOLD);
                yellowScore.setPaintFlags(yellowScore.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
                break;
            case 2:
                actionBar.setBackgroundDrawable(new ColorDrawable(Color.RED));
                actionBarToolbar.setTitleTextColor(Color.WHITE);

                redScore.setTypeface(null, Typeface.BOLD);
                redScore.setPaintFlags(redScore.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
                break;
            case 3:
                actionBar.setBackgroundDrawable(new ColorDrawable(Color.BLUE));
                actionBarToolbar.setTitleTextColor(Color.WHITE);

                blueScore.setTypeface(null, Typeface.BOLD);
                blueScore.setPaintFlags(blueScore.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
                break;
        }
    }
}
