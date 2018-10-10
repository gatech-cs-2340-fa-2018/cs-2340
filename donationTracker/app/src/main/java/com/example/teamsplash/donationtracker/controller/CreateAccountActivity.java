package com.example.teamsplash.donationtracker.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.teamsplash.donationtracker.R;
import com.example.teamsplash.donationtracker.model.User;
import com.example.teamsplash.donationtracker.model.Users;
import com.example.teamsplash.donationtracker.model.UserType;


public class CreateAccountActivity extends AppCompatActivity {

    private EditText firstNameField;
    private EditText lastNameField;
    private Spinner userTypeField;
    private EditText emailAddressField;
    private EditText passwordField;
    private EditText confirmPasswordField;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);
        firstNameField = findViewById(R.id.first_name_field);
        lastNameField = findViewById(R.id.last_name_field);
        userTypeField = findViewById(R.id.user_type_field);
        ArrayAdapter<UserType> userTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, UserType.values());
        userTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userTypeField.setAdapter(userTypeAdapter);
        emailAddressField = findViewById(R.id.email_address_field);
        passwordField = findViewById(R.id.password_field);
        confirmPasswordField = findViewById(R.id.confirm_password_field);
        findViewById(R.id.create_account_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateAccount();
            }
        });
        findViewById(R.id.cancel_account_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelPressed();
            }
        });
    }

    /**
     * Button handler for cancel
     */
    public void onCancelPressed() {
        startActivity(new Intent(com.example.teamsplash.donationtracker.controller.CreateAccountActivity.this, LoginActivity.class));
        finish();
    }

    /**
     * Tries to create a new account when the "Create account" button is clicked.
     */
    private void onCreateAccount() {
        Users users = Users.getInstance();
        String firstname = firstNameField.getText().toString();
        String lastname = lastNameField.getText().toString();
        String email = emailAddressField.getText().toString();;

        String password = passwordField.getText().toString();
        String confirmPass = confirmPasswordField.getText().toString();

        if (!isPasswordValid()) {
            // Warn user that passwords don't match
        }

        if (!isEmailValid(email)) {
            // Warn user that email is not valid
        }


        UserType usertype = (UserType) userTypeField.getSelectedItem();
        User newUser = new User(firstname, lastname, email, password, usertype);
        if (users.contains(newUser)) {
            // Warn user that account exists
            return;
        } else {
            users.add(newUser);
            users.setCurrentUser(newUser);
            Intent accountCreated = new Intent(com.example.teamsplash.donationtracker.controller.CreateAccountActivity.this, LoginActivity.class);
            startActivity(accountCreated);
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        emailAddressField.setError(null);
        passwordField.setError(null);

        // Store values at the time of the login attempt.
        String email = emailAddressField.getText().toString();
        String password = passwordField.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid()) {
            passwordField.setError(getString(R.string.error_invalid_password));
            focusView = passwordField;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            emailAddressField.setError(getString(R.string.error_field_required));
            focusView = emailAddressField;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailAddressField.setError(getString(R.string.error_invalid_email));
            focusView = emailAddressField;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
        //TODO: figure out of we need this?
        // Show a progress spinner, and kick off a background task to
        // perform the user login attempt.
        // showProgress(true);
    }

    // Checks for an @ symbol and a period
    private boolean isEmailValid(String email) {
        return email.contains("@") && email.contains(".");
    }

    // Confirms that the entries in ""enter password" and "confirm password" are equivalent
    private boolean isPasswordValid() {
        return passwordField.getText().equals(confirmPasswordField.getText());
    }


}