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
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by lars on 16.03.17.
 */

public class FilterDialog extends DialogFragment {

    private String[] filter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.filter_dialog, null);
        Spinner age = (Spinner) dialogView.findViewById(R.id.age);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.altersgruppen_array, android.R.layout.simple_spinner_item);
        age.setAdapter(adapter);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        //final TextView tv = (TextView) this.getActivity().findViewById(R.id.np);
        NumberPicker technic = (NumberPicker) dialogView.findViewById(R.id.technik_picker);
        NumberPicker tactic = (NumberPicker) dialogView.findViewById(R.id.taktik_picker);
        NumberPicker physis = (NumberPicker) dialogView.findViewById(R.id.physis_picker);

        //Set TextView text color
        //tv.setTextColor(Color.parseColor("#ffd32b3b"));

        //Populate NumberPicker values from minimum and maximum value range
        //Set the minimum value of NumberPicker
        technic.setMinValue(1);
        //Specify the maximum value/number of NumberPicker
        technic.setMaxValue(7);
        tactic.setMinValue(1);
        tactic.setMaxValue(7);
        physis.setMinValue(1);
        physis.setMaxValue(7);


        //Gets whether the selector wheel wraps when reaching the min/max value.
        technic.setWrapSelectorWheel(true);
        tactic.setWrapSelectorWheel(true);
        //Set a value change listener for NumberPicker
        technic.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                //Display the newly selected number from picker
                System.out.println("Selected Number : " + newVal);
            }
        });


        builder.setMessage(R.string.filterDialog)
                .setPositiveButton(R.string.filtern, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        builder.setView(dialogView);
        // Create the AlertDialog object and return it
        return builder.create();
    }
    public Dialog onCreateDialog(Bundle savedInstanceState, String[] filter){
        this.filter = filter;
        return onCreateDialog(savedInstanceState);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        Spinner spinner = (Spinner) view.getRootView().findViewById(R.id.keyword);

        boolean checked = ((RadioButton) view).isChecked();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.default_array, android.R.layout.simple_spinner_item);

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_fitness:
                if (checked)
                    adapter = ArrayAdapter.createFromResource(view.getContext(),
                            R.array.fitness_array, android.R.layout.simple_spinner_item);
                break;
            case R.id.radio_taktik:
                if (checked)
                    adapter = ArrayAdapter.createFromResource(view.getContext(),
                            R.array.taktik_array, android.R.layout.simple_spinner_item);
                break;
            case R.id.radio_technik:
                if (checked)
                    adapter = ArrayAdapter.createFromResource(view.getContext(),
                            R.array.technik_array, android.R.layout.simple_spinner_item);
                break;
            case R.id.radio_torwart:
                if (checked)
                    adapter = ArrayAdapter.createFromResource(view.getContext(),
                            R.array.torwart_array, android.R.layout.simple_spinner_item);
                break;
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.performClick();
    }

}