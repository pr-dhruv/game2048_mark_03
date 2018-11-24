package com.example.mahendraprajapati.gmae2048_mark_03;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class SaveGameDialogBox extends DialogFragment {

    public interface SaveGameDialogBoxListener {
        void onSaveGameDialogBoxPositiveClicked();
        void onSaveGameDialogBoxNegativeClicked();
    }

    SaveGameDialogBoxListener saveGameDialogBoxListener;

    public void onAttch(Activity activity) {

        try {
            saveGameDialogBoxListener = (SaveGameDialogBoxListener) activity;
        }
        catch(ClassCastException exception) {
            throw new ClassCastException(activity.toString() + "must implement the \"SaveGameDialogBoxListener\"");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder myBuilder = new AlertDialog.Builder(getActivity());
        myBuilder.setTitle("Save Game")
                .setMessage("Do you wish to save game ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveGameDialogBoxListener.onSaveGameDialogBoxPositiveClicked();
                    }
                })
                .setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveGameDialogBoxListener.onSaveGameDialogBoxNegativeClicked();
                    }
                });

        return myBuilder.create();
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
    }

}
