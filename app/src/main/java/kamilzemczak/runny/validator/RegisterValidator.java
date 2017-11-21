package kamilzemczak.runny.validator;

import kamilzemczak.runny.activity.RegisterActivity;
import kamilzemczak.runny.backgroundworker.RegisterBackgroundWorker;

/**
 * Created by Kamil Zemczak on 02.11.2017.
 */

public class RegisterValidator {
    RegisterActivity registerActivity = new RegisterActivity();
   // public boolean validate() {
       /* boolean valid = true;


        String str_name = registerActivity.getName().getText().toString();
        String str_surname =  registerActivity.getSurname().getText().toString();
        String str_username =  registerActivity.getUsername().getText().toString();
        String str_email = registerActivity.getEmail().getText().toString();
        String str_age = registerActivity.getAge().getText().toString();
        String str_password = registerActivity.getPassword().getText().toString();
        String str_passwordConfirm = registerActivity.getPasswordConfirm().getText().toString();

        if (str_name.isEmpty() || str_name.length() < 3 || str_name.length() > 26) {
            registerActivity.getName().setError("Imię musi zawierać minimum trzy znaki.");
            valid = false;
        } else if (str_name.isEmpty() && Character.isUpperCase(str_name.charAt(0))) {
            registerActivity.getName().setError("Imię musi zaczynać się od dużej litery."); //TODO: nie działa
            valid = false;
        } else {
            registerActivity.getName().setError(null);
        }

        if (str_surname.isEmpty() || str_surname.length() < 3 || str_surname.length() > 26) {
            surname.setError("Nazwisko musi zawierać minimum trzy znaki.");
            valid = false;
        } else if (str_surname.isEmpty() && Character.isUpperCase(str_name.charAt(0))) {
            surname.setError("Nazwisko musi zaczynać się od dużej litery"); //TODO: nie działa.
        } else {
            surname.setError(null);
        }

        if (str_username.isEmpty() || str_username.length() < 4 || str_username.length() > 26 || !isUnique(str_username)) { //TODO: zastanowić się jak wyrzucić wyjątek, że taka nazwa użytkownika istnieje.
            username.setError("Nazwa użytkownika musi zawierać minimum cztery znaki.");
            valid = false;
        } else {
            username.setError(null);
        }
        if (str_email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(str_email).matches()) { //TODO: zastanowić się jak wyrzucić wyjątek, że email jest już taki zarejestrowany.
            registerActivity.getEmail().setError("Niepoprawny format email.");
            valid = false;
        } else {
            registerActivity.getEmail().setError(null);
        }

        if (str_age.isEmpty() || str_age.length() < 1 || str_age.length() > 2) {  //TODO: zastanowić się czy to ma sens TextUtils.isDigitsOnly(str_age)
            age.setError("Niepoprawny format wieku.");
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

        return valid;
    }*/

}
