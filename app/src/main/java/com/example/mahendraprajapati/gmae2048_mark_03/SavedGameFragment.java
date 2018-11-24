package com.example.mahendraprajapati.gmae2048_mark_03;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mahendraprajapati.gmae2048_mark_03.database.MyDataBaseHelper;
import com.example.mahendraprajapati.gmae2048_mark_03.database.Game;

import java.util.List;

public class SavedGameFragment extends Fragment {
    private MyDataBaseHelper myDataBaseHelper;
    private SaveGameAdapter saveGameAdapter;
    private List<Game> gameList;
    private ListView listView;
    private Game recentRemoveGame;
    private boolean restoreOnBack = false;
    private int row;

    public static final String loadedGameTag = "LOADED_GAME_TAG";

    public SavedGameFragment() {}

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View root = layoutInflater.inflate(R.layout.fragment_saved_game, viewGroup, false);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myDataBaseHelper = new MyDataBaseHelper(getActivity());
        gameList = myDataBaseHelper.getAllSavedGames();
        saveGameAdapter = new SaveGameAdapter(getActivity(), gameList);

        listView = (ListView) view.findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Game game = gameList.get(position);
                Intent intent = new Intent(getActivity(), GameBoardActivity.class);
                intent.putExtra(loadedGameTag, game);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.left_in_animation, R.anim.left_out_animation);


            }
        });

        //animation for deleting the game by long press
        final Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.fad_out_animation);
        animation.setAnimationListener(new OnRemoveAnimationListener());

        //Long press initialize the animation, and passes removed element index to animation listener to remove item form list.
        //when the animation ends we update layout to reflect this deletion.
        listView.setOnItemLongClickListener(new OnItemLongClickRemove(animation));
    }

    private class OnItemLongClickRemove implements AdapterView.OnItemLongClickListener {
        private Animation itemRemove;

        public OnItemLongClickRemove(Animation animation) {
            itemRemove = animation;
        }

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            view.startAnimation(itemRemove);
            row = position;
            return true;
        }
    }

    //when user click back to undo rmoval of item this method will execute
    public void restoreLastItemRemoved() {
        gameList.add(recentRemoveGame);
        saveGameAdapter.notifyDataSetChanged();;
    }

    public boolean isRestoreLastItem() {
        return restoreOnBack;
    }

    private class OnRemoveAnimationListener implements Animation.AnimationListener {

        private static final int timeToRemove = 5000;

        @Override
        public void onAnimationStart(Animation animation) {
            //nothing
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            recentRemoveGame = gameList.get(row);
            gameList.remove(recentRemoveGame);
            saveGameAdapter.notifyDataSetChanged();

            Toast.makeText(getActivity(), "Save game removed. Press back to undo", Toast.LENGTH_SHORT).show();

            restoreOnBack = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    myDataBaseHelper.deleteSavedGame(recentRemoveGame);
                    restoreOnBack = false;
                }
            }, timeToRemove);

        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            //nothing
        }
    }


}
