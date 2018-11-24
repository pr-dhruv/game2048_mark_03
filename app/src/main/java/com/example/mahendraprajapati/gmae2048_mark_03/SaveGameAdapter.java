package com.example.mahendraprajapati.gmae2048_mark_03;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ImageView;

import com.example.mahendraprajapati.gmae2048_mark_03.database.Game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class SaveGameAdapter extends BaseAdapter {

    Context context;
    List<Game> gameList;
    private static LayoutInflater layoutInflater = null;

    public SaveGameAdapter(Context context, List<Game> gameList) {
        this.context = context;
        this.gameList = gameList;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return gameList.size();
    }

    @Override
    public Object getItem(int position) {
        return gameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolderItem {
        TextView playerName;
        TextView score;
        ImageView screenShot;
    }

    public View getView(int position, View view, ViewGroup parent) {
        ViewHolderItem viewHolderItem;
        if(view == null) {
            view = layoutInflater.inflate(R.layout.row, null);

            viewHolderItem = new ViewHolderItem();
            viewHolderItem.playerName = view.findViewById(R.id.edittext_player_name);
            viewHolderItem.score = view.findViewById(R.id.score);
            viewHolderItem.screenShot = view.findViewById(R.id.edittext_player_name);

            view.setTag(viewHolderItem);

        }
        else {
            viewHolderItem = (ViewHolderItem) view.getTag();
        }

        viewHolderItem.playerName.setText(gameList.get(position).getPlayerName());
        viewHolderItem.score.setText(String.valueOf(gameList.get(position).getScore()));

        File file = new File(gameList.get(position).getUriToImage());
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
        }
        catch (FileNotFoundException e) {
            Log.e("Image Load",e.getMessage());
            e.printStackTrace();
        }

        viewHolderItem.screenShot.setImageDrawable(new BitmapDrawable(context.getResources(), bitmap));
        return view;
    }

}
