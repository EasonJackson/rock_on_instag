/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import static android.content.Context.INPUT_METHOD_SERVICE;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,
                                                                View.OnKeyListener{

    boolean signUpMode = true;
    TextView switchSignUpText;
    Button signUpButton;
    EditText username;
    EditText password;
    ConstraintLayout backgroundConstraintLayout;
    ImageView logoImageView;

    public void showUserList() {
        Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.switchSignUpText) {

            if (signUpMode) {
                signUpMode = false;
                signUpButton.setText("Sign In");
                switchSignUpText.setText("Or SignUp");
            } else {
                signUpMode = true;
                signUpButton.setText("Sign Up");
                switchSignUpText.setText("Or Login");
            }
            Log.i("AppInfo", "Change signup mode");
        } else if (view.getId() == R.id.background || view.getId() == R.id.logoImageView) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (i == keyEvent.KEYCODE_ENTER && keyEvent.getAction() == keyEvent.ACTION_DOWN) {
            signButtonOnClick(view);
        }
        return false;
    }

    protected void signButtonOnClick(View view) {
        if (signUpMode) {
            signUp();
        } else {
            signIn();
        }
    }

    protected void signUp() {
        if (username == null || username.getText().toString().equals("") ||
                password == null || password.getText().toString().equals("")) {
            Toast.makeText(this, "A username and password are needed.", Toast.LENGTH_SHORT).show();
            return;
        }

        ParseUser user = new ParseUser();
        user.setUsername(username.getText().toString());
        user.setPassword(password.getText().toString());

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.i("Sign Up", "Successful");
                    showUserList();
                } else {
                    Toast.makeText(MainActivity.this, "Sign up failure: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.i("Sign Up", "Failed");
                }
            }
        });
    }

    protected void signIn() {
        if (username == null || username.getText().toString().equals("") ||
                password == null || password.getText().toString().equals("")) {
            Toast.makeText(this, "A username and password are needed.", Toast.LENGTH_SHORT).show();
            return;
        }

        ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    Log.i("Login", "Successful");
                    showUserList();
                } else {
                    Toast.makeText(MainActivity.this, "Login failure: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.i("Login", "Failed");
                }
            }
        });

    }


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    switchSignUpText = (TextView) findViewById(R.id.switchSignUpText);
    switchSignUpText.setOnClickListener(this);

    signUpButton = (Button) findViewById(R.id.signUpButton);

    backgroundConstraintLayout = (ConstraintLayout) findViewById(R.id.background);
    backgroundConstraintLayout.setOnClickListener(this);

    logoImageView = (ImageView) findViewById(R.id.logoImageView);
    logoImageView.setOnClickListener(this);

    username = (EditText) findViewById(R.id.nameText);

    password = (EditText) findViewById(R.id.passwordText);
    password.setOnKeyListener(this);

//    if (ParseUser.getCurrentUser() != null) {
//        showUserList();
//    }
    
    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }
}