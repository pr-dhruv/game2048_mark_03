package com.example.mahendraprajapati.gmae2048_mark_03;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();

    }

    // A simple View
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {}

        @Override
        public View onCreateView(LayoutInflater infalter, ViewGroup container, Bundle saveInstanceState) {
            return infalter.inflate(R.layout.fragment_main, container, false);
        }

    }

    public void newGame(View view) {
        Intent gameStartIntent = new Intent(MainActivity.this, GameBoardActivity.class);
        startActivity(gameStartIntent);
        overridePendingTransition(R.anim.right_in_animation, R.anim.right_out_animation);
    }

    public void resumeGame(View view) {
        Intent previousGame = new Intent(MainActivity.this, SavedGameAcitivity.class);
        previousGame.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(previousGame);
        overridePendingTransition(R.anim.left_in_animation, R.anim.left_out_animation);
    }

    public void scoreBoard(View view) {
        /*Intent scoreBoard = new Intent(MainActivity.this, GameBoardActivity.class);
        startActivity(scoreBoard);
        overridePendingTransition(R.anim.left_in_animation, R.anim.left_out_animation);
        */
    }

}
