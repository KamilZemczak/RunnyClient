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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.support.v7.app.AppCompatActivity;

import java.util.concurrent.ExecutionException;

import kamilzemczak.runny.R;
import kamilzemczak.runny.backgroundworker.UniqueBackgroundWorker;
import kamilzemczak.runny.backgroundworker.RegisterBackgroundWorker;

/**
 * Klasa odpowiedzialna za rejestrację użytkownika do serwisu
 */
public class RegisterActivity extends AppCompatActivity {

    private EditText name, surname, username, email, age, password, passwordConfirm;
    private TextView chooseGender;
    private Button registerButton;
    private RadioGroup genderRadioGroup;
    private RadioButton genderRadioButton;

    private String gender;

    /**
     * TODO
     *
     * @param savedInstanceState TODO
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_register);

        name = (EditText) findViewById(R.id.registerActivity_etName);
        surname = (EditText) findViewById(R.id.registerActivity_etSurname);
        username = (EditText) findViewById(R.id.registerActivity_etUsername);
        email = (EditText) findViewById(R.id.registerActivity_etEmail);
        age = (EditText) findViewById(R.id.registerActivity_etAge);
        password = (EditText) findViewById(R.id.registerActivity_etPassword);
        passwordConfirm = (EditText) findViewById(R.id.registerActivity_etPasswordConfirm);
        genderRadioGroup = (RadioGroup) findViewById(R.id.registerActivity_rgGender);
        registerButton = (Button) findViewById(R.id.registerActivity_bRegister);
        chooseGender = (TextView) findViewById(R.id.registerActivity_tvGender);
    }

    /**
     * Przesyła na serwer dane które użytkownik podał w formularzu rejestracyjnym
     *
     * @param view aktualny interfejs
     */
    public void register(View view) {
        if (!validate()) {
            registerFailed();
            return;
        }
        String name = this.name.getText().toString();
        String surname = this.surname.getText().toString();
        String username = this.username.getText().toString();
        String email = this.email.getText().toString();
        String age = this.age.getText().toString();
        String password = this.password.getText().toString();
        String passwordConfirm = this.passwordConfirm.getText().toString();
        String type = "register";
        RegisterBackgroundWorker registerBackgroundWorker = new RegisterBackgroundWorker(this);
        registerBackgroundWorker.execute(type, name, surname, username, email, age, password, passwordConfirm, gender);
        registerSuccess();
        openLoginPage(view);
    }

    /**
     * Otwiera aktywność logowanie
     *
     * @param view aktualny interfejs
     */
    public void openLogin(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    /**
     * Weryfikuje jaką płeć wybrał użytkownik
     *
     * @param view aktualny interfejs
     */
    public void radioButtonClick(View view) {
        int radioButtonId = genderRadioGroup.getCheckedRadioButtonId();
        genderRadioButton = (RadioButton) findViewById(radioButtonId);
        String genderCheck = genderRadioButton.getText().toString();
        if (genderCheck.equals("Mężczyzna") && genderCheck != null) {
            gender = "M";
        } else if (genderCheck.equals("Kobieta") && genderCheck != null) {
            gender = "F";
        } else {
            return;
        }
    }

    /**
     * TODO
     *
     * @return TODO
     */
    public boolean validate() {
        boolean valid = true;

        String name = this.name.getText().toString();
        String surname = this.surname.getText().toString();
        String username = this.username.getText().toString();
        String email = this.email.getText().toString();
        String age = this.age.getText().toString();
        String password = this.password.getText().toString();
        String passwordConfirm = this.passwordConfirm.getText().toString();
        Integer int_age = null;

        if (!age.isEmpty()) {
            int_age = Integer.parseInt(age);
        }

        valid = validName(valid, name);
        valid = validSurname(valid, surname);
        valid = validUsername(valid, username);
        valid = validEmail(valid, email);
        valid = validAge(valid, age, int_age);
        valid = validPassword(valid, password, passwordConfirm);
        valid = validGender(valid);

        return valid;
    }

    /**
     * TODO
     *
     * @param valid
     * @return
     */
    private boolean validGender(boolean valid) {
        if (genderRadioGroup.getCheckedRadioButtonId() <= 0) {
            //chooseGender.requestFocus(); //TODO: rozwiązać problem prioretyetów i znikania ostrzeżenia.
            chooseGender.setError("Wybierz płeć.");
            valid = false;
        } else {
            chooseGender.setError(null);
        }
        return valid;
    }

    /**
     * TODO
     *
     * @param valid
     * @param password
     * @param passwordConfirm
     * @return
     */
    private boolean validPassword(boolean valid, String password, String passwordConfirm) {
        if (password.isEmpty() || password.length() < 8 || password.length() > 26) {
            this.password.setError("Hasło musi zawierać co najmniej 8 znaków.");
            valid = false;
        } else if (!passwordConfirm.equals(password)) {
            this.password.setError("Wybrane hasła się od siebie różnią.");
            valid = false;
        } else {
            this.password.setError(null);
        }
        return valid;
    }

    /**
     * TODO
     *
     * @param valid
     * @param age
     * @param int_age
     * @return
     */
    private boolean validAge(boolean valid, String age, Integer int_age) {
        if (age.isEmpty() || age.length() < 1 || age.length() > 2) {
            this.age.setError("Niepoprawny format wieku.");
            valid = false;
        } else if (int_age != null && int_age < 18) {
            this.age.setError("Rejestracja w serwisie możliwa od 18 roku życia.");
            valid = false;
        } else {
            this.age.setError(null);
        }
        return valid;
    }

    /**
     * TODO
     *
     * @param valid
     * @param email
     * @return
     */
    private boolean validEmail(boolean valid, String email) {
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            this.email.setError("Niepoprawny format email.");
            valid = false;
        } else if (!isUniqueEmail(email)) {
            this.email.setError("Email jest już używany.");
            valid = false;
        } else {
            this.email.setError(null);
        }
        return valid;
    }

    /**
     * TODO
     *
     * @param valid
     * @param username
     * @return
     */
    private boolean validUsername(boolean valid, String username) {
        if (username.isEmpty() || username.length() < 4 || username.length() > 26) {
            this.username.setError("Nazwa użytkownika musi zawierać minimum cztery znaki.");
            valid = false;
        } else if (!isUniqueUser(username)) {
            this.username.setError("Nazwa użytkownika jest już używana.");
            valid = false;
        } else {
            this.username.setError(null);
        }
        return valid;
    }

    /**
     * TODO
     *
     * @param valid
     * @param surname
     * @return
     */
    private boolean validSurname(boolean valid, String surname) {
        if (surname.isEmpty() || surname.length() < 3 || surname.length() > 26) {
            this.surname.setError("Nazwisko musi zawierać minimum trzy znaki.");
            valid = false;
        } else if (!Character.isUpperCase(surname.charAt(0))) {
            this.surname.setError("Nazwisko musi zaczynać się od dużej litery");
            valid = false;
        } else {
            this.surname.setError(null);
        }
        return valid;
    }

    /**
     * TODO
     *
     * @param valid
     * @param name
     * @return
     */
    private boolean validName(boolean valid, String name) {
        if (name.isEmpty() || name.length() < 3 || name.length() > 26) {
            this.name.setError("Imię musi zawierać minimum trzy znaki.");
            valid = false;
        } else if (!Character.isUpperCase(name.charAt(0))) {
            this.name.setError("Imię musi zaczynać się od dużej litery.");
            valid = false;
        } else {
            this.name.setError(null);
        }
        return valid;
    }

    /**
     * TODO
     *
     * @param str_username TODO
     * @return TODO
     */
    private boolean isUniqueUser(String str_username) {
        String type = "unique_user";
        Boolean result = true;
        UniqueBackgroundWorker uniqueBackgroundWorker = new UniqueBackgroundWorker(this);
        try {
            result = uniqueBackgroundWorker.execute(type, str_username).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * TODO
     *
     * @param str_email TODO
     * @return TODO
     */
    private boolean isUniqueEmail(String str_email) {
        String type = "unique_email";
        Boolean result = true;
        UniqueBackgroundWorker uniqueBackgroundWorker = new UniqueBackgroundWorker(this);
        try {
            result = uniqueBackgroundWorker.execute(type, str_email).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * TODO
     *
     */
    public void registerFailed() {
        Toast.makeText(getBaseContext(), "Rejestracja nieudana.", Toast.LENGTH_LONG).show();
        registerButton.setEnabled(true);
    }

    /**
     * TODO
     */
    public void registerSuccess() {
        Toast.makeText(getBaseContext(), "Rejestracja udana.", Toast.LENGTH_LONG).show();
        registerButton.setEnabled(true);
    }

    /**
     * TODO
     */
    public void openLoginPage(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }
}
