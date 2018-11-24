package com.example.mahendraprajapati.gmae2048_mark_03;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.example.mahendraprajapati.gmae2048_mark_03.FragmentCallBack.Direction;
import com.example.mahendraprajapati.gmae2048_mark_03.SaveGameDetails.SaveGameDetailsDialogListener;
import com.example.mahendraprajapati.gmae2048_mark_03.SaveGameDialogBox.SaveGameDialogBoxListener;
import com.example.mahendraprajapati.gmae2048_mark_03.database.Game;
import com.example.mahendraprajapati.gmae2048_mark_03.database.MyDataBaseHelper;
import com.example.mahendraprajapati.gmae2048_mark_03.dialogs.GameOverDialog.GameOverDialogListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GameBoardActivity extends AppCompatActivity implements SaveGameDetailsDialogListener, SaveGameDialogBoxListener, GameOverDialogListener {

    //for passing the swapping events to GameBoardFragment
    private FragmentCallBack fragmentCallBack;

    //Touch Events
    private int touchEventX;
    private int touchEventY;
    private int touchEventLength;

    //game from the database
    private Game saveOrLoadedGame;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        GameBoardFragment gameBoardFragment = new GameBoardFragment();
        fragmentCallBack = gameBoardFragment;

        Bundle extra = getIntent().getExtras();
        if(extra != null) {
            saveOrLoadedGame = extra.getParcelable(SavedGameFragment.loadedGameTag);
            gameBoardFragment.setArguments(extra);
        }

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, gameBoardFragment).commit();
        }

        touchEventLength = GridDesign.setScreenWidthAndGetMinimalTouchEventLength(this);

    }

    @Override
    public void onBackPressed() {
        fragmentCallBack.onBackButtonPressed();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        int myAction = motionEvent.getActionMasked();

        switch (myAction) {
            case MotionEvent.ACTION_DOWN:
                touchEventX = (int) motionEvent.getX();
                touchEventY = (int) motionEvent.getY();
                return true;

            case MotionEvent.ACTION_UP:
                int tempX = (int) (touchEventX - motionEvent.getX());
                int tempY = (int) (touchEventY - motionEvent.getY());

                int x = Math.abs(tempX);
                int y = Math.abs(tempY);



                if( x > y) {
                    if(x > touchEventLength) {
                        if(x > 0) {
                            //check this line
                            Log.d("X", x+"");
                            Log.d("Y", y+"");
                            Log.d("touch event length", touchEventLength + "");
                            Log.d("Direction", "Left");
                            fragmentCallBack.onSwap(Direction.LEFT);
                        }else{
                            Log.d("X", x+"");
                            Log.d("Y", y+"");
                            Log.d("touch event length", touchEventLength + "");
                            Log.d("Direction", "Right");
                            fragmentCallBack.onSwap(Direction.RIGHT);
                        }
                    }
                } else if(x < y) {
                    if( y > touchEventLength) {
                        if(y > 0) {
                            Log.d("X", x+"");
                            Log.d("Y", y+"");
                            Log.d("touch event length", touchEventLength + "");
                            Log.d("Direction", "Up");
                            fragmentCallBack.onSwap(Direction.UP);
                        }else {
                            Log.d("X", x+"");
                            Log.d("Y", y+"");
                            Log.d("touch event length", touchEventLength + "");
                            Log.d("Direction", "Down");
                            fragmentCallBack.onSwap(Direction.DOWN);
                        }
                    }
                }

                return true;

            default:
                return super.onTouchEvent(motionEvent);
        }

    }

    @Override
    public void onSaveGameDialogBoxPositiveClicked() {
        if(saveOrLoadedGame != null) {

            MyDataBaseHelper dbManager = new MyDataBaseHelper(getApplicationContext());

            dbManager.deleteSavedGame(saveOrLoadedGame);

            Game myGame = ((GameBoardFragment)fragmentCallBack).getCurrentGame();
            myGame.setPlayerName(saveOrLoadedGame.getPlayerName());
            dbManager.addSavedGame(myGame);

            finish();
            overridePendingTransition(R.anim.left_in_animation, R.anim.left_out_animation);

        }else {
            SaveGameDetails saveGameDetailsDialogBox = new SaveGameDetails();
            saveGameDetailsDialogBox.show(getSupportFragmentManager(),"SAVE GAME DETAIL");
        }
    }

    @Override
    public void onSaveGameDialogBoxNegativeClicked() {
        finish();
        overridePendingTransition(R.anim.left_in_animation, R.anim.right_out_animation);
    }

    @Override
    public void onSaveGameDetailsDialogPositiveClick(String userInput) {
        Game myGame = ((GameBoardFragment)fragmentCallBack).getCurrentGame();

        if(userInput.equals("")) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);
            String  date = simpleDateFormat.format(new Date());
            String defaultName = String.format("%s%s", date, myGame.getScore());
        } else
            myGame.setPlayerName(userInput);

        myGame.setID(myGame.getPlayerName().hashCode());

        MyDataBaseHelper dbManager = new MyDataBaseHelper(this);
        dbManager.addSavedGame(myGame);
        overridePendingTransition(R.anim.left_in_animation, R.anim.left_out_animation);

    }

    @Override
    public void onGameOverDismissClick(boolean wasHighScore) {
        if(wasHighScore)
            ((GameBoardFragment)fragmentCallBack).AddCurrentGameToHighScores();
        finish();
        overridePendingTransition(R.anim.left_in_animation, R.anim.right_out_animation);
    }
}
