package com.example.mahendraprajapati.gmae2048_mark_03;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class SaveGameDetails extends DialogFragment {

    public interface SaveGameDetailsDialogListener {
        void onSaveGameDetailsDialogPositiveClick(String playerName);
    }

    private SaveGameDetailsDialogListener saveGameDetailsDialogListener;
    private EditText playerName;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(getContext());
        try {
            saveGameDetailsDialogListener = (SaveGameDetailsDialogListener) activity;
        }
        catch(ClassCastException e) {
            throw  new ClassCastException(activity.toString());
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder((getActivity()));
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.dialog_fragment_highscore, null);
        playerName = view.findViewById(R.id.edittext_player_name);
        builder.setView(view)
                .setTitle("Save")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userName = playerName.getText().toString();
                        saveGameDetailsDialogListener.onSaveGameDetailsDialogPositiveClick(userName);
                    }
                });
        return builder.create();
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
    }

}
