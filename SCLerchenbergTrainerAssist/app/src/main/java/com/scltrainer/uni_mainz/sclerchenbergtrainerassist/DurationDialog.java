package com.scltrainer.uni_mainz.sclerchenbergtrainerassist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
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
import android.widget.Toast;

/**
 * Created by lars on 16.03.17.
 */

public class DurationDialog extends DialogFragment {

    private DialogFragment thisdialog = this;

    public void addListener(DurationDialogListener list){
        dialogListener = list;
    }
    public interface DurationDialogListener{
        public void onDialogPoistiveClick(int duration);
        public void onDialogNegativeClick(int duration);

    }
    private DurationDialogListener dialogListener;

    private EditText durationEditText;

    public void setDuration(int duration) {
        this.duration = duration;
    }

    private int duration = 0;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.duration_dialog, null);
        durationEditText = (EditText) dialogView.findViewById(R.id.duration);

        durationEditText.setHint(""+duration);



        builder.setMessage(R.string.duratioDialog)
                .setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (durationEditText == null || durationEditText.getText().toString().equals("")){
                            Toast.makeText(getActivity().getBaseContext(),"Geben Sie bitte eine Zeit für die Übung ein.", Toast.LENGTH_SHORT).show();
                            DurationDialog d = new DurationDialog();
                            d.addListener(dialogListener);
                            d.show(getFragmentManager(),"Duration Dialog");
                        }else {
                            String dur = durationEditText.getText().toString();
                            dialogListener.onDialogPoistiveClick(Integer.parseInt(dur));
                        }
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


    public EditText getDurationEditText() {
        return durationEditText;
    }



}