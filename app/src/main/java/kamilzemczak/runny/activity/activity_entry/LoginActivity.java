/**
 * ***********************************************************
 * Autorskie Prawa Majątkowe Kamil Zemczak
 * <p>
 * Copyright 2017 Kamil Zemczak
 * ************************************************************
 * Utworzono 26-10-2017, Kamil Zemczak
 * ************************************************************
 */
package kamilzemczak.runny.activity.activity_entry;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.app.AppCompatActivity;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import kamilzemczak.runny.R;
import kamilzemczak.runny.model.User;
import kamilzemczak.runny.backgroundworker.UserBackgroundWorker;
import kamilzemczak.runny.backgroundworker.LoginBackgroundWorker;
import kamilzemczak.runny.activity.activity_menu.WelcomeActivity;

/**
 * Klasa odpowiedzialna za logowanie użytkownika do portalu
 */
public class LoginActivity extends AppCompatActivity {

    private EditText username, password;
    Button loginButton;

    public static String userCurrentName, userCurrentSurname, userCurrentUsername, userCurrentEmail, userCurrentGender, userCurrentCity, userCurrentAbout;
    public static Integer userCurrentId, userCurrentAge, userCurrentWeight, userCurrentHeight;

    /**
     * TODO
     *
     * @param savedInstanceState TODO
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.registerActivity_etName);
        password = (EditText) findViewById(R.id.registerActivity_etPassword);
        loginButton = (Button) findViewById(R.id.loginActivity_bLogin);
    }

    /**
     * Przesyła na serwer dane które użytkownik podał w formularzu logowanie
     *
     * @param view aktualny interfejs
     */
    public void login(View view) {
        if (!validate()) {
            loginFailed();
            return;
        }

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
            loginFailed();
            username.setError("Niewłaściwy login lub hasło.");
            password.setError("Niewłaściwy login lub hasło.");
            return;
        } else if (result.equals("Zalogowanie udane.")) {
            getUserDetails();
            loginSuccess();
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
        String username = this.username.getText().toString();
        String type = "user_details";
        UserBackgroundWorker userBackgroundWorker = new UserBackgroundWorker(this);
        try {
            User currentUser;
            ObjectMapper mapper = new ObjectMapper();
            String userJson = userBackgroundWorker.execute(type, username).get();
            currentUser = mapper.readValue(userJson, User.class);
            prepareUserData(currentUser);
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
     *
     * @param currentUser
     */
    private void prepareUserData(User currentUser) {
        userCurrentId = currentUser.getId();
        userCurrentName = currentUser.getName();
        userCurrentSurname = currentUser.getSurname();
        userCurrentUsername = currentUser.getUsername();
        userCurrentEmail = currentUser.getEmail();
        userCurrentAge = currentUser.getAge();
        userCurrentGender = currentUser.getGender();
        if (currentUser.getWeight() != null) {
            userCurrentWeight = currentUser.getWeight();
        }
        if (currentUser.getHeight() != null) {
            userCurrentHeight = currentUser.getHeight();
        }
        if (currentUser.getCity() != null) {
            userCurrentCity = currentUser.getCity();
        }
        if (currentUser.getAbout() != null) {
            userCurrentAbout = currentUser.getAbout();
        }
    }

    /**
     * TODO
     *
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
    public void loginFailed() {
        Toast.makeText(getBaseContext(), "Logowanie nieudane.", Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);
    }

    /**
     * TODO
     */
    public void loginSuccess() {
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
