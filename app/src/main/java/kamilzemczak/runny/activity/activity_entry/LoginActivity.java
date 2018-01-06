/**
 * ***********************************************************
 * Autorskie Prawa Majątkowe Kamil Zemczak
 *
 * Copyright 2017 Kamil Zemczak
 * ************************************************************
 * Utworzono 26-10-2017, Kamil Zemczak
 * ************************************************************
 */
package kamilzemczak.runny.activity.activity_entry;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.concurrent.ExecutionException;


import kamilzemczak.runny.activity.activity_menu.WelcomeActivity;
import kamilzemczak.runny.backgroundworker.LoginBackgroundWorker;
import kamilzemczak.runny.backgroundworker.UserBackgroundWorker;
import kamilzemczak.runny.model.User;
import kamilzemczak.runny.R;

/**
 * Klasa odpowiedzialna za logowanie użytkownika do portalu
 */
public class LoginActivity extends AppCompatActivity {

    private EditText username, password;

    public static String currentName, currentSurname, currentUsername, currentEmail, currentGender, currentCity, currentAbout;
    public static Integer currentId, currentAge, currentWeight, currentHeight;
    
    Button loginButton;

    /**
     * TODO
     *
     * @param savedInstanceState TODO
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.etName);
        password = (EditText) findViewById(R.id.etPassword);
        loginButton = (Button) findViewById(R.id.bLogin);
    }

    /**
     * Przesyła na serwer dane które użytkownik podał w formularzu logowanie
     *
     * @param view aktualny interfejs
     */
    public void onLogin(View view) {
        if (!validate()) {
            onLoginFailed();
            return;
        }

        //SearchFriendsAdapter adapter = new SearchFriendsAdapter();
        String str_username = username.getText().toString();
        String str_password = password.getText().toString();
        String type = "login";
        String result = null;
        LoginBackgroundWorker loginBackgroundWorker = new LoginBackgroundWorker(this);

        try {
            result = loginBackgroundWorker.execute(type, str_username, str_password).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (result.equals("Wrong.")) {
            onLoginFailed();
            username.setError("Niewłaściwy login lub hasło.");
            password.setError("Niewłaściwy login lub hasło.");
            return;
        } else if (result.equals("Zalogowanie udane.")) {
            getUserDetails();
            onLoginSuccess();
            startActivity(new Intent(this, WelcomeActivity.class));
        }
    }

    /**
     * Otwiera aktywność rejestracja
     *
     * @param view aktualny interfejs
     */
    public void openRegister(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    /**
     * TODO
     */
    public void getUserDetails() {
        String str_username = username.getText().toString();
        String type = "user_details";
        UserBackgroundWorker userBackgroundWorker = new UserBackgroundWorker(this);
        try {
            User currentUser;
            ObjectMapper mapper = new ObjectMapper();
            String userJson = userBackgroundWorker.execute(type, str_username).get();
            currentUser = mapper.readValue(userJson, User.class);
            currentId = currentUser.getId();
            currentName = currentUser.getName();
            currentSurname = currentUser.getSurname();
            currentUsername = currentUser.getUsername();
            currentEmail = currentUser.getEmail();
            currentAge = currentUser.getAge();
            currentGender = currentUser.getGender();
            if (currentUser.getWeight() != null) {
                currentWeight = currentUser.getWeight();
            }
            if (currentUser.getHeight() != null) {
                currentHeight = currentUser.getHeight();
            }
            if (currentUser.getCity() != null) {
                currentCity = currentUser.getCity();
            }
            if (currentUser.getAbout() != null) {
                currentAbout = currentUser.getAbout();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * TODO
     * @return
     */
    public boolean validate() {
        boolean valid = true;

        String str_username = username.getText().toString();
        String str_password = password.getText().toString();

        if (str_username.isEmpty()) {
            username.setError("Nie wpisano nazwy użytkownika.");
            valid = false;
        } else {
            username.setError(null);
        }

        if (str_password.isEmpty()) {
            password.setError("Nie wpisano hasła.");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }

    /**
     * TODO
     */
    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Logowanie nieudane.", Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);
    }

    /**
     * TODO
     */
    public void onLoginSuccess() {
        Toast.makeText(getBaseContext(), "Logowanie udane.", Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);
    }
}

/*
TODO:
1. Automatyczne przekierowanie do logowania po założeniu konta (do obmyślenia).
2. Możliwość edycji hasła (nowy widok).
3. Móżliwość edycji zdjęcia.
 */
