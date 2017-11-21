/**
 * ***********************************************************
 * Autorskie Prawa Majątkowe Kamil Zemczak
 *
 * Copyright 2017 Kamil Zemczak
 * ************************************************************
 * Utworzono 26-10-2017, Kamil Zemczak
 * ************************************************************
 */
package kamilzemczak.runny.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import kamilzemczak.runny.R;
import kamilzemczak.runny.backgroundworker.RegisterBackgroundWorker;
import kamilzemczak.runny.backgroundworker.UniqueBackgroundWorker;

/**
 * Klasa odpowiedzialna za rejestrację użytkownika do serwisu
 */
public class RegisterActivity extends AppCompatActivity {

    EditText name, surname, username, email, age, password, passwordConfirm;
    String str_name, str_surname, str_username, str_email, str_age, str_password, str_passwordConfirm, gender;
    RadioGroup genderRgroup;
    RadioButton genderRbutton;
    //RadioButton maleRbutton, femaleRButton;
    Button registerButton;
    TextView chooseGender;


    /**
     * TODO
     *
     * @param savedInstanceState TODO
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = (EditText)findViewById(R.id.etName);
        surname = (EditText)findViewById(R.id.etSurname);
        username = (EditText)findViewById(R.id.etUsername);
        email = (EditText)findViewById(R.id.etEmail);
        age = (EditText)findViewById(R.id.etAge);
        password = (EditText)findViewById(R.id.etPassword);
        passwordConfirm = (EditText)findViewById(R.id.etPasswordConfirm);
        genderRgroup = (RadioGroup)findViewById(R.id.rgGender);
        registerButton = (Button)findViewById(R.id.bRegister);
        //maleRbutton = (RadioButton)findViewById(R.id.rbMale);
        //femaleRButton = (RadioButton)findViewById(R.id.rbFemale);
        chooseGender = (TextView)findViewById(R.id.tvGender);
    }

    /**
     * Przesyła na serwer dane które użytkownik podał w formularzu rejestracyjnym
     *
     * @param view aktualny interfejs
     */
    public void onRegister(View view) {
        if(!validate()) {
            onRegisterFailed();
            return;
        }

        String str_name = name.getText().toString();
        String str_surname = surname.getText().toString();
        String str_username = username.getText().toString();
        String str_email = email.getText().toString();
        String str_age = age.getText().toString();
        String str_password = password.getText().toString();
        String str_passwordConfirm = passwordConfirm.getText().toString();
        String type = "register";
        RegisterBackgroundWorker registerBackgroundWorker = new RegisterBackgroundWorker(this);
        registerBackgroundWorker.execute(type, str_name, str_surname, str_username, str_email, str_age, str_password, str_passwordConfirm, gender);
        onRegisterSuccess();
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
    public void rbClick(View view) {
        int radioButtonId = genderRgroup.getCheckedRadioButtonId();
        genderRbutton = (RadioButton)findViewById(radioButtonId);
        String genderCheck = genderRbutton.getText().toString();
        if(genderCheck.equals("Mężczyzna") && genderCheck!=null) {
            gender = "M";
        } else if(genderCheck.equals("Kobieta") && genderCheck!=null) {
            gender = "F";
        } else {
            return;
        }
    }


    public boolean validate() {
        boolean valid = true;

        String str_name = name.getText().toString();
        String str_surname = surname.getText().toString();
        String str_username = username.getText().toString();
        String str_email = email.getText().toString();
        String str_age = age.getText().toString();
        String str_password = password.getText().toString();
        String str_passwordConfirm = passwordConfirm.getText().toString();
        Integer int_age = null;
        if(!str_age.isEmpty()) {
           int_age = Integer.parseInt(str_age);
        }

        if (str_name.isEmpty() || str_name.length() < 3 || str_name.length() > 26) {
            name.setError("Imię musi zawierać minimum trzy znaki.");
            valid = false;
        } else if (!Character.isUpperCase(str_name.charAt(0))) {
            name.setError("Imię musi zaczynać się od dużej litery.");
            valid = false;
        } else {
            name.setError(null);
        }

        if (str_surname.isEmpty() || str_surname.length() < 3 || str_surname.length() > 26) {
            surname.setError("Nazwisko musi zawierać minimum trzy znaki.");
            valid = false;
        } else if (!Character.isUpperCase(str_surname.charAt(0))) {
            surname.setError("Nazwisko musi zaczynać się od dużej litery");
            valid = false;
        } else {
            surname.setError(null);
        }

        if (str_username.isEmpty() || str_username.length() < 4 || str_username.length() > 26) {
            username.setError("Nazwa użytkownika musi zawierać minimum cztery znaki.");
            valid = false;
        } else if(!isUniqueUser(str_username)) {
            username.setError("Nazwa użytkownika jest już używana.");
            valid = false;
        } else {
            username.setError(null);
        }

        if (str_email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(str_email).matches()) {
            email.setError("Niepoprawny format email.");
            valid = false;
        } else if (!isUniqueEmail(str_email)) {
            email.setError("Email jest już używany.");
            valid = false;
        } else {
            email.setError(null);
        }

        if (str_age.isEmpty() || str_age.length() < 1 || str_age.length() > 2) {
            age.setError("Niepoprawny format wieku.");
            valid = false;
        } else if (int_age!=null && int_age < 18) {
            age.setError("Rejestracja w serwisie możliwa od 18 roku życia.");
            valid = false;
        } else {
            age.setError(null);
        }

        if (str_password.isEmpty() || str_password.length() < 8 || str_password.length() > 26) {
            password.setError("Hasło musi zawierać co najmniej 8 znaków.");
            valid = false;
        } else if(!str_passwordConfirm.equals(str_password)) {
            password.setError("Wybrane hasła się od siebie różnią.");
            valid = false;
        } else {
            password.setError(null);
        }

        if(genderRgroup.getCheckedRadioButtonId()<=0) {
            //chooseGender.requestFocus(); //TODO: rozwiązać problem prioretyetów i znikania ostrzeżenia.
            chooseGender.setError("Wybierz płeć.");
            valid = false;
        } else {
            chooseGender.setError(null);
        }

        return valid;
    }

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

    public void onRegisterFailed() {
        Toast.makeText(getBaseContext(), "Rejestracja nieudana.", Toast.LENGTH_LONG).show();
        registerButton.setEnabled(true);
    }

    public void onRegisterSuccess() {
        Toast.makeText(getBaseContext(), "Rejestracja udana.", Toast.LENGTH_LONG).show();
        registerButton.setEnabled(true);
    }

}
