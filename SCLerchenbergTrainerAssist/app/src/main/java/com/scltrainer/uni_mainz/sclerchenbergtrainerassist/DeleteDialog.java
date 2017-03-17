package com.scltrainer.uni_mainz.sclerchenbergtrainerassist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by lars on 16.03.17.
 */

public class DeleteDialog extends DialogFragment {

    private Integer duration;

    public void addListener(DeleteDialogListener list){
        dialogListener = list;
    }
    public interface DeleteDialogListener{
        public void onDialogPoistiveClick(int duration);
        public void onDialogNegativeClick(int duration);

    }
    private DeleteDialogListener dialogListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.delete_dialog, null);


        builder.setMessage(R.string.deleteDialog)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialogListener.onDialogPoistiveClick(0);//TODO Übergabe der ID
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialogListener.onDialogNegativeClick(0);
                    }
                });
        builder.setView(dialogView);
        // Create the AlertDialog object and return it
        return builder.create();
    }



}