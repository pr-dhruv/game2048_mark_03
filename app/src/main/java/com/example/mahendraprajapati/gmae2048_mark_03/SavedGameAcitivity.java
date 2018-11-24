package com.example.mahendraprajapati.gmae2048_mark_03;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SavedGameAcitivity extends FragmentActivity {
    private SavedGameFragment savedGameFragment;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        setContentView(R.layout.acitivity_saved_game);
        savedGameFragment = new SavedGameFragment();

        if(saveInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, savedGameFragment).commit();
        }
    }

    public static class PlaceholderFragment extends Fragment {
        public PlaceholderFragment () {}

        @Override
        public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
            return layoutInflater.inflate(R.layout.fragment_saved_game, viewGroup, false);
        }

    }

    @Override
    public void onBackPressed() {
        if(savedGameFragment.isRestoreLastItem()) {
            super.onBackPressed();
            overridePendingTransition(R.anim.left_in_animation, R.anim.right_out_animation);
            return;
        }

        savedGameFragment.restoreLastItemRemoved();

    }


}
