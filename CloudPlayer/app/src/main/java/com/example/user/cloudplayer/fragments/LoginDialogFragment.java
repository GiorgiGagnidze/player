package com.example.user.cloudplayer.fragments;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.cloudplayer.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.regex.Pattern;


public class LoginDialogFragment extends DialogFragment {
    private EditText editText;
    private EditText username;
    private boolean checker;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog d = new Dialog(getActivity());
        d.setCanceledOnTouchOutside(false);
        d.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK)
                    return true;
                return false;
            }
        });
        d.setContentView(R.layout.login_dialog);
        editText = (EditText)d.findViewById(R.id.edit_email);
        username = (EditText)d.findViewById(R.id.edit_username);
        checker = true;
        if (savedInstanceState != null) {
            editText.setText(savedInstanceState.getString(getResources().getString(R.string.key_email)));
            username.setText(savedInstanceState.getString(getResources().getString(R.string.key_username)));
            checker = savedInstanceState.getBoolean(getResources().getString(R.string.key_for_bool));
        }
        Button button = (Button)d.findViewById(R.id.sign_up);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checker) {
                    checker = false;
                    String email = editText.getText().toString();
                    String password = username.getText().toString();
                    if (password.equals("")) {
                        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.user_name_alert),
                                Toast.LENGTH_LONG).show();
                        checker = true;
                        return;
                    }
                    if (email.equals("")) {
                        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.email_alert),
                                Toast.LENGTH_LONG).show();
                        checker = true;
                    } else {
                        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
                        int counter = 0;
                        Account[] accounts = AccountManager.get(getActivity().getApplicationContext()).getAccounts();
                        for (Account account : accounts) {
                            if (emailPattern.matcher(account.name).matches() &&
                                    email.equals(account.name)) {
                                handleEmail(email, d, password);
                                break;
                            }
                            counter++;
                        }
                        if (counter == accounts.length) {
                            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.email_incorrect),
                                    Toast.LENGTH_LONG).show();
                            checker = true;
                        }
                    }
                }
            }
        });
        return d;
    }

    private void handleEmail(final String email,final Dialog d,final String
            password){
        ParseUser.logInInBackground(email, password, new LogInCallback() {
            @Override
            public void done(final ParseUser parseUser, com.parse.ParseException e) {
                final SharedPreferences prefs = getActivity().getSharedPreferences(
                        getResources().getString(R.string.key_app), Context.MODE_PRIVATE);
                if (parseUser != null) {
                    checker = true;
                    Log.i("ki", "daloginda");
                    prefs.edit().putString(getResources().getString(R.string.key_account), email).apply();
                    d.dismiss();
                } else {
                    Log.i("ver", "daloginda");
                    ParseUser user = new ParseUser();
                    user.setUsername(email);
                    user.setPassword(password);
                    user.put(getResources().getString(R.string.name_col),password);
                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                checker = true;
                                Log.i("ki", "daregistrirda");
                                prefs.edit().putString(getResources().getString(R.string.key_account)
                                        , email).apply();
                                d.dismiss();
                            } else {
                                checker = true;
                                Log.i("ver", "daregistrirda");
                                Toast.makeText(getActivity(),
                                        getActivity().getResources().getString(R.string.login_alert),
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(getResources().getString(R.string.key_email),editText.getText().toString());
        outState.putString(getResources().getString(R.string.key_username),username.getText().toString());
        outState.putBoolean(getResources().getString(R.string.key_for_bool),checker);
    }
}
