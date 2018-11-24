package com.example.mahendraprajapati.gmae2048_mark_03;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mahendraprajapati.gmae2048_mark_03.database.MyDataBaseHelper;
import com.example.mahendraprajapati.gmae2048_mark_03.database.GameScore;
import com.example.mahendraprajapati.gmae2048_mark_03.database.Game;
import com.example.mahendraprajapati.gmae2048_mark_03.dialogs.GameOverDialog;

public class GameBoardFragment extends Fragment implements FragmentCallBack {

    private GameGrid mGrid;
    private TextView mTextScore;
    private TextView mTextTime;
    private Handler mHandler = new Handler();
    private UpdateScore mRunnable;

    private Game mSavedGame;

    //	private String mSavedGameState;
    //	private String mSavedTime;
    //	private String mSavedScore;
    //REMOVE
    private int counter = 0;
    private boolean mFakeLoss;
    // TODO three hours of sleep, two hot meals, one shower

    public GameBoardFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game, container,
                false);

        if (getArguments() != null) {
            //		    mSavedGameState = getArguments().getString(SavedGamesFragment.SAVED_GAME_INTENT_TAG);
            //		    mSavedScore = getArguments().getString(SavedGamesFragment.SCORE_INTENT_TAG);
            //		    mSavedTime = getArguments().getString(SavedGamesFragment.ELAPSED_TIME_INTENT_TAG);
            mSavedGame = getArguments().getParcelable(SavedGameFragment.loadedGameTag);
        }

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTextScore = (TextView) getView().findViewById(R.id.score_value);
        mTextTime = (TextView) getView().findViewById(R.id.time_value);


        GridDesign.adjustGrid(view);
        mGrid = new GameGrid();

        if (/*mSavedGameState != null*/mSavedGame != null) {
            //			mGrid.loadState(mSavedGameState);
            //			mRunnable = new UpdateScoreRunnable(mTextTime, mHandler, Long.valueOf(mSavedTime));
            //			mTextScore.setText(mSavedScore);
            //			mGrid.setScore(Integer.valueOf(mSavedScore));

            mGrid.loadGameState(mSavedGame.getGameState());
            mRunnable = new UpdateScore(mTextTime, mHandler, Long.valueOf(mSavedGame.getTime()));
            mTextScore.setText(mSavedGame.getScore() + "");
            mGrid.setScore(mSavedGame.getScore());
        } else {
            mRunnable = new UpdateScore(mTextTime, mHandler);
            mGrid.addRandomTile();
            mGrid.addRandomTile();
        }


        mHandler.post(mRunnable);
        refreshGameBoard();
    }

    private void animateView() {
        updateScore();
        refreshGameBoard();
    }

    private void updateScore() {
        mTextScore.setText(String.valueOf(mGrid.getScore()));
    }

    public Game getCurrentGame() {
        // Load bitmap from view and save it to internal storage
        RelativeLayout grid = (RelativeLayout) getView().findViewById(R.id.grid);
        Bitmap b = GridDesign.loadBitmapFromView(grid);
        String uri = GridDesign.saveBitmap(b, getActivity());

        Game game = new Game();
        game.setGameState(mGrid.getGameState());
        game.setScore(mGrid.getScore());
        game.setTime(String.valueOf(mRunnable.getCurrentTimeInMillis()));
        game.setUriToImage(uri);

        return game;
    }

    private void refreshGameBoard() {
        int newElement = mGrid.addRandomTile();

        counter++;
        if(counter > 3) {
            mFakeLoss = true;
        }

        if (newElement != -1 /*&& !mFakeLoss*/) {
            RelativeLayout parent = (RelativeLayout) getView().findViewById(R.id.grid);
            for (int i = 0; i < parent.getChildCount(); i++) {
                // retrieve relative layout that represents each grid element and holds textview
                // FrameLayout is placeholder for empty grid
                RelativeLayout element = (RelativeLayout) ((FrameLayout) parent.getChildAt(i)).getChildAt(0);
                // update its background and displayed value
                GridDesign.updateElement(element, mGrid.getTileAt(i), getActivity());

                if (i == newElement) {
                    Animation fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.adding_new_tile);
                    //fadeIn.setInterpolator(new AccelerateInterpolator());
                    element.startAnimation(fadeIn);
                }
            }
        } else if (mGrid.gameLostCondition() /*|| mFakeLoss*/) {
            /* Game is over, lets check if user has earned highscore */
            boolean hasUserEarnedHighscore = hasUserEarnedHighScore();

            /* Show game over dialog */
            GameOverDialog dialog = new GameOverDialog();
            Bundle bundle = new Bundle();
            /* Notify user if he has earned higshcore */
            bundle.putBoolean(GameOverDialog.BUNDLE_HIGHSCORE_TAG, hasUserEarnedHighscore);
            bundle.putInt(GameOverDialog.BUNDLE_SCORE_TAG, mGrid.getScore());
            dialog.setArguments(bundle);
            dialog.show(getFragmentManager(), "SAVEGAME?");
        }
    }

    private boolean hasUserEarnedHighScore() {
        MyDataBaseHelper dbHelper = new MyDataBaseHelper(getActivity());
        GameScore bestGame = dbHelper.getHighestScore();

        /* there are no scores in database, any score user currently reached is the best */
        if (bestGame == null) return true;
        /* There are other scores but current is the best */
        if (mGrid.getScore() > bestGame.getScore()) return true;
        /* There are other but current isn't best*/
        return false;
    }

    public void AddCurrentGameToHighScores() {
//		DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
//		GameScore bestGame = dbHelper.getHighestScore();
//		dbHelper.addHighScore(score)
    }

    @Override
    public void onSwap(Direction direction) {
        switch(direction) {
            case UP:
                mGrid.moveTopSide();
                break;
            case DOWN:
                mGrid.moveDownSide();
                break;
            case LEFT:
                mGrid.moveLeftSide();
                break;
            case RIGHT:
                mGrid.moveRightSide();
                break;
        }

        animateView();
    }

    @Override
    public void onBackButtonPressed() {
        if (!mGrid.gameLostCondition()) {
            mRunnable.stopRunning();
            SaveGameDialogBox dialog = new SaveGameDialogBox();
            dialog.show(getFragmentManager(), "SAVEGAME?");
        }
    }


}
