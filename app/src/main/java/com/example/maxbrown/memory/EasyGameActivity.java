package com.example.maxbrown.memory;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.view.View.generateViewId;

public class EasyGameActivity extends AppCompatActivity implements OnClickListener{
    private GridLayout theGrid;
    TextView moveCounterDisplay;
    TextView matchesRemainingDisplay;
    TextView difficultyDisplay;

    private int numMoves;
    private int matchesRemaining;


    private CardButton firstChoice;
    private CardButton secondChoice;
    private boolean delayFactor;
    private boolean flashDelay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        String theDiff = bundle.getString("difficulty");
        setContentView(R.layout.activity_easy_game_);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        numMoves = 0;
        int difficulty = 1;
        int size = 0;

        delayFactor = false;
        flashDelay = false;


        theGrid = (GridLayout) findViewById(R.id.easy_game_grid);

        if (theDiff.equals("Easy")) {
            matchesRemaining = 3;
            size = 100;
            difficulty = 1;
            theGrid.setRowCount(2);
            theGrid.setColumnCount(3);
        }
        if (theDiff.equals("Medium")) {
            matchesRemaining = 6;
            size = 60;
            difficulty = 2;
            theGrid.setRowCount(3);
            theGrid.setColumnCount(4);
        }
        if (theDiff.equals("Hard")) {
            matchesRemaining = 12;
            size = 50;
            difficulty = 3;
            theGrid.setRowCount(4);
            theGrid.setColumnCount(6);
        }




        moveCounterDisplay = (TextView) findViewById(R.id.moves_display);
        moveCounterDisplay.setText("Moves: " + numMoves);

        matchesRemainingDisplay = (TextView) findViewById(R.id.matches_display);
        matchesRemainingDisplay.setText("Matches Remaining: " + matchesRemaining);

        difficultyDisplay = (TextView) findViewById(R.id.difficulty_display);
        difficultyDisplay.setText("Difficulty: " + theDiff);




        int numColumns = theGrid.getColumnCount();
        int numRows = theGrid.getRowCount();

        //the number of unique cards is half the total number cards
        int numCards = (numColumns * numRows);
        CardButton[] cardButtons = new CardButton[numCards];
        int[] cardPictures = {R.drawable.monkey , R.drawable.moose , R.drawable.racoon ,
                R.drawable.cat , R.drawable.penguin , R.drawable.fox , R.drawable.goat ,
                R.drawable.cow, R.drawable.giraffe , R.drawable.elephant , R.drawable.snail ,
                R.drawable.turtle};

        List<Integer> pictureList = getPictureList(cardPictures , difficulty);




        int index = 0;
        for(int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {

                CardButton theButton = new CardButton(this, pictureList.get(index), i , j , size);
                theButton.setId(generateViewId());
                theButton.setOnClickListener(this);
                theGrid.addView(theButton);
                cardButtons[index] = theButton;
                index++;
            }
        }


    }

    @Override
    public void onClick(View view) {
        if(delayFactor || flashDelay) {
            return;
        }

        final Handler handler = new Handler();

        CardButton button = (CardButton) view;
        if(button.isFlipped() || button.getIsMatched())
            return;

        if(firstChoice == null) {

            firstChoice = button;
            firstChoice.flip();

            return;

        }

        if (firstChoice.getCardPictureId() == button.getCardPictureId()) {
            secondChoice = button;
            secondChoice.flip();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    firstChoice.flashGreen();
                    secondChoice.flashGreen();


                    firstChoice.setMatched();
                    secondChoice.setMatched();

                    firstChoice.setEnabled(false);
                    secondChoice.setEnabled(false);

                    firstChoice = null;
                    secondChoice = null;


                }
            }, 500);


            matchesRemaining--;
            matchesRemainingDisplay.setText("Matches Remaining: " + matchesRemaining);

        }

        else {
            secondChoice = button;
            secondChoice.flip();
            delayFactor = true;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    firstChoice.flashRed();
                    secondChoice.flashRed();

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {


                            firstChoice.flip();
                            secondChoice.flip();
                            firstChoice = null;
                            secondChoice = null;
                            delayFactor = false;

                        }
                    }, 500);


                }
            }, 500);


        }



        if(matchesRemaining == 0) {
            int duration = Toast.LENGTH_SHORT;
            final Toast toast = Toast.makeText(this, "You Win! Returning to the Main Menu",
                    duration);


            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toast.show();

                }
            }, 1000);


            final Intent intent = new Intent(this, MainActivity.class);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(intent);

                }
            }, 3000);

        }



    }


    private static List<Integer> getPictureList(int[] cardPictures , int difficulty) {
        int numberOfPictures = 1;
        switch(difficulty) {
            case 1: numberOfPictures = 3;
                break;
            case 2: numberOfPictures = 6;
                break;
            case 3: numberOfPictures = 12;
                break;
        }
        List<Integer> pictureList = new ArrayList<Integer>();
        for (int i = 0; i < numberOfPictures; i++) {
            int randomIndex = (int) Math.floor((Math.random() * cardPictures.length));
            if(pictureList.contains(cardPictures[randomIndex]))
                i--;
            else{

                pictureList.add(cardPictures[randomIndex]);
                pictureList.add(cardPictures[randomIndex]);
            }
        }
        Collections.shuffle(pictureList);

        return pictureList;
    }
}
