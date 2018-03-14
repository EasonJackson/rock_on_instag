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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    boolean signUpMode = true;
    TextView switchSignUpText;
    Button signUpButton;

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
        }
    }

    protected void signButtonOnClick() {
        if (signUpMode) {
            signUp();
        } else {
            signIn();
        }
    }

    protected void signUp() {
        EditText username = (EditText) findViewById(R.id.nameText);
        EditText password = (EditText) findViewById(R.id.passwordText);

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
                } else {
                    Toast.makeText(MainActivity.this, "Sign up failure: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.i("Sign Up", "Failed");
                }
            }
        });
    }

    protected void signIn() {
        EditText username = (EditText) findViewById(R.id.nameText);
        EditText password = (EditText) findViewById(R.id.passwordText);

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
    
    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }


}