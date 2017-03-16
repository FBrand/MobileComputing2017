package com.scltrainer.uni_mainz.sclerchenbergtrainerassist;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Lukas on 15.03.2017.
 */

public class EinstellungenFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_einstellungen, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Button acceptButton = (Button) (getActivity().findViewById(R.id.acceptButton));
        acceptButton.setEnabled(false);

        final SharedPreferences shared = getActivity().getPreferences(Context.MODE_PRIVATE);
        String username = shared.getString("USERNAME", "");

        final EditText usernameText = (EditText) (getActivity().findViewById(R.id.usernameText));
        usernameText.setText(username);

        usernameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String text = usernameText.getText().toString();
                String nutzername = shared.getString("USERNAME", "");
                if((anzahlRichtigerZeichen(text) == 0) || text.equals(nutzername)){
                    acceptButton.setEnabled(false);
                } else {
                    acceptButton.setEnabled(true);
                }
            }
        });

        acceptButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                String text = trimmeNutzernamen(usernameText.getText().toString());
                usernameText.setText(text);
                acceptButton.setEnabled(false);
                SharedPreferences.Editor editor = shared.edit();
                editor.putString("USERNAME", text);
                editor.commit();
            }
        });

        final EditText emailText = (EditText) (getActivity().findViewById(R.id.emailText));
        emailText.setEnabled(false);

        final Button emailButton = (Button) (getActivity().findViewById(R.id.email_button));

        if(!shared.getString("USEREMAIL", "").equals("")) {
            emailButton.setEnabled(false);
            String userEmail = shared.getString("USEREMAIL", "");
            emailText.setText(userEmail);
        }
        emailButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(shared.getString("USEREMAIL", "").equals("")) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.GET_ACCOUNTS}, 0);

                    getFragmentManager().popBackStack();
                    doTransaction(R.id.menu_frame, new MenuFragment(), "menu");
                }
            }
        });
    }

    private String trimmeNutzernamen(String string) {
        char[] array1 = string.toCharArray();
        while (array1[0] == ' '){
            int laenge = array1.length;
            char[] array2 = new char[laenge-1];
            for(int i = 1; i < laenge; i++) {
                array2[i-1] = array1[i];
            }
            array1 = array2;
        }
        while (array1[array1.length-1] == ' '){
            int laenge = array1.length;
            char[] array2 = new char[laenge-1];
            for(int i = 0; i < laenge-1; i++){
                array2[i] = array1[i];
            }
            array1 = array2;
        }
        return new String(array1);
    }

    private int anzahlRichtigerZeichen(String string) {
        int laenge = string.length();
        int result = laenge;
        for(int i = 0; i < laenge; i++){
            if(string.charAt(i) == ' '){
                result -= 1;
            }
        }
        return result;
    }

    private void doTransaction(int frameID, Fragment fragment, String tag){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(frameID, fragment, tag);
        ft.addToBackStack(tag);
        ft.commit();
    }
}
