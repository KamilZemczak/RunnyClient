package kamilzemczak.runny.activity.activity_user;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import kamilzemczak.runny.R;
import kamilzemczak.runny.backgroundworker.PostBackgroundWorker;
import kamilzemczak.runny.model.User;
import kamilzemczak.runny.backgroundworker.UniqueBackgroundWorker;
import kamilzemczak.runny.backgroundworker.UserBackgroundWorker;
import kamilzemczak.runny.activity.activity_entry.LoginActivity;
import kamilzemczak.runny.activity.activity_menu.FriendsActivity;
import kamilzemczak.runny.activity.activity_menu.HistoryActivity;
import kamilzemczak.runny.activity.activity_menu.ObjectivesActivity;
import kamilzemczak.runny.activity.activity_menu.TrainingActivity;
import kamilzemczak.runny.activity.activity_menu.ProfileActivity;
import kamilzemczak.runny.activity.activity_menu.WelcomeActivity;

/**
 * TODO
 */
public class EditProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private LoginActivity loginActivity;

    private EditText username, email, age, gender, weight, height, city, about, password, passwordConfirm;
    private TextView user;
    private Button updateButton;

    private String currentUsername = loginActivity.userCurrentUsername;
    private String currentEmail = loginActivity.userCurrentEmail;
    private Integer userCurrentId = loginActivity.userCurrentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        user = (TextView) findViewById(R.id.editProfileActivity_tvUser);
        username = (EditText) findViewById(R.id.editProfileActivity_etUsername);
        email = (EditText) findViewById(R.id.editProfileActivity_etEmail);
        age = (EditText) findViewById(R.id.editProfileActivity_etAge);
        gender = (EditText) findViewById(R.id.editProfileActivity_etGender);
        weight = (EditText) findViewById(R.id.editProfileActivity_etWeight);
        height = (EditText) findViewById(R.id.editProfileActivity_etHeight);
        city = (EditText) findViewById(R.id.editProfileActivity_etCity);
        about = (EditText) findViewById(R.id.editProfileActivity_etAbout);
        updateButton = (Button) findViewById(R.id.editProfileActivity_bEditProfile);

        setCurrentUserDetails();
        gender.setKeyListener(null);
    }

    private void setCurrentUserDetails() {
        user.setText(loginActivity.userCurrentName + " " + loginActivity.userCurrentSurname);
        username.setText(loginActivity.userCurrentUsername, TextView.BufferType.EDITABLE);
        email.setText(loginActivity.userCurrentEmail, TextView.BufferType.EDITABLE);
        age.setText(Integer.toString(loginActivity.userCurrentAge), TextView.BufferType.EDITABLE);
        if (loginActivity.userCurrentGender != null && loginActivity.userCurrentGender.equals("M")) {
            gender.setText("Mężczyzna", TextView.BufferType.EDITABLE);
        }
        if (loginActivity.userCurrentGender != null && loginActivity.userCurrentGender.equals("F")) {
            gender.setText("Kobieta", TextView.BufferType.EDITABLE);
        }
        if ((loginActivity.userCurrentWeight) != null) {
            weight.setText(Integer.toString(LoginActivity.userCurrentWeight), TextView.BufferType.EDITABLE);
        }
        if ((loginActivity.userCurrentHeight) != null) {
            height.setText(Integer.toString(loginActivity.userCurrentHeight), TextView.BufferType.EDITABLE);
        }
        if (loginActivity.userCurrentCity != null) {
            city.setText(loginActivity.userCurrentCity, TextView.BufferType.EDITABLE);
        }
        if (loginActivity.userCurrentAbout != null) {
            about.setText(loginActivity.userCurrentAbout, TextView.BufferType.EDITABLE);
        }
    }

    /**
     * TODO
     *
     * @param view TODO
     */
    public void updateUser(View view) {
        if (!validate()) {
            updateProfileFailed();
            return;
        }
        String type = "user_update";
        String id = Integer.toString(userCurrentId);
        String username = this.username.getText().toString();
        String email = this.email.getText().toString();
        String age = this.age.getText().toString();
        String weight = this.weight.getText().toString();
        String height = this.height.getText().toString();
        String city = this.city.getText().toString();
        String about = this.about.getText().toString();
        UserBackgroundWorker userBackgroundWorker = new UserBackgroundWorker(this);
        userBackgroundWorker.execute(type, id, username, email, age, weight, height, city, about);
        updateProfileSuccess();
        getUserDetails();
        showProfile(view);
    }

    /**
     * TODO
     */
    public void getUserDetails() {
        String type = "user_details";
        String username = this.username.getText().toString();
        UserBackgroundWorker userBackgroundWorker = new UserBackgroundWorker(this);
        try {
            ObjectMapper mapper = new ObjectMapper();
            User currentUser;
            String userJson = userBackgroundWorker.execute(type, username).get();
            currentUser = mapper.readValue(userJson, User.class);
            setCurrentUserValues(currentUser);
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

    private void setCurrentUserValues(User currentUser) {
        loginActivity.userCurrentId = currentUser.getId();
        loginActivity.userCurrentName = currentUser.getName();
        loginActivity.userCurrentSurname = currentUser.getSurname();
        loginActivity.userCurrentUsername = currentUser.getUsername();
        loginActivity.userCurrentEmail = currentUser.getEmail();
        loginActivity.userCurrentAge = currentUser.getAge();
        loginActivity.userCurrentGender = currentUser.getGender();
        if (currentUser.getWeight() != null) {
            loginActivity.userCurrentWeight = currentUser.getWeight();
        }
        if (currentUser.getHeight() != null) {
            loginActivity.userCurrentHeight = currentUser.getHeight();
        }
        if (currentUser.getCity() != null) {
            loginActivity.userCurrentCity = currentUser.getCity();
        }
        if (currentUser.getAbout() != null) {
            loginActivity.userCurrentAbout = currentUser.getAbout();
        }
    }

    private void openUpdateDialog() {
        LayoutInflater inflater = LayoutInflater.from(EditProfileActivity.this);
        View subView = inflater.inflate(R.layout.form_password_update_dialog, null);
        password = (EditText) subView.findViewById(R.id.formPasswordUpdate_etPassword);
        passwordConfirm = (EditText) subView.findViewById(R.id.formPasswordUpdate_etPasswordConfirm);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edytuj hasło już teraz!");
        builder.setView(subView);

        AlertDialog alertDialog = builder.create();

        builder.setPositiveButton("ZATWIERDŹ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String type = "user_password_update";
                String sPassword = password.getText().toString();
                String sPasswordConfirm = passwordConfirm.getText().toString();
                if (!validPassword(sPassword, sPasswordConfirm)) {
                    openUpdateDialog();
                    Toast.makeText(getBaseContext(), "Edycja hasła nieudana.", Toast.LENGTH_LONG).show();
                    setErrors(sPassword, sPasswordConfirm);
                } else {
                    UserBackgroundWorker userBackgroundWorker = new UserBackgroundWorker(EditProfileActivity.this);
                    userBackgroundWorker.execute(type, loginActivity.userCurrentUsername, sPassword, sPasswordConfirm);
                    finish();
                    startActivity(getIntent());
                    Toast.makeText(getBaseContext(), "Edycja hasła udana!", Toast.LENGTH_LONG).show();
                }
            }

            private void setErrors(String sPassword, String sPasswordConfirm) {
                if (sPassword.isEmpty() || sPassword.length() < 8 || sPassword.length() > 26) {
                    password.setError("Hasło musi zawierać co najmniej 8 znaków.");
                } else if (!sPasswordConfirm.equals(sPassword)) {
                    password.setError("Wybrane hasła się od siebie różnią.");
                }
            }
        });

        builder.setNegativeButton("ANULUJ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    /**
     * TODO
     *
     * @return
     */
    public boolean validate() {
        boolean valid = true;

        String username = this.username.getText().toString();
        String email = this.email.getText().toString();
        String age = this.age.getText().toString();
        String weight = null;
        String height = null;
        String city = null;
        String about = null;
        Integer iAge = null;
        Integer iWeight = null;
        Integer iHeight = null;
        Integer iAbout = null;

        if (this.weight.getText() != null) {
            weight = this.weight.getText().toString();
        }

        if (this.height.getText() != null) {
            height = this.height.getText().toString();
        }

        if (this.city.getText() != null) {
            city = this.city.getText().toString();
        }

        if (this.about.getText() != null) {
            about = this.about.getText().toString();
            iAbout = about.length();
        }

        if (!age.isEmpty()) {
            iAge = Integer.parseInt(age);
        }

        if (!weight.isEmpty()) {
            iWeight = Integer.parseInt(weight);
        }

        if (!height.isEmpty()) {
            iHeight = Integer.parseInt(height);
        }

        valid = validUsername(valid, username);
        valid = validEmail(valid, email);
        valid = validAge(valid, age, iAge);
        valid = validWeight(valid, weight, iWeight);
        valid = validHeight(valid, height, iHeight);
        valid = validCity(valid, city);
        valid = validAbout(valid, about, iAbout);

        return valid;
    }

    private boolean validAbout(boolean valid, String about, Integer iAbout) {
        if (!about.isEmpty() && iAbout < 20) {
            this.about.setError("Minimum 20 znaków o sobie.");
            valid = false;
        } else if (!about.isEmpty() && iAbout > 256) {
            this.about.setError("Maksymalne 255 znaków o sobie.");
            valid = false;
        } else {
            this.about.setError(null);
        }
        return valid;
    }

    private boolean validCity(boolean valid, String city) {
        if (!city.isEmpty() && (city.length() < 3 || city.length() > 24)) {
            this.city.setError("Niepoprawny format miasta.");
            valid = false;
        } else {
            this.city.setError(null);
        }
        return valid;
    }

    private boolean validHeight(boolean valid, String height, Integer iHeight) {
        if (!height.isEmpty() && (height.length() < 2 || height.length() > 3)) {
            this.height.setError("Niepoprawny format wzrostu.");
            valid = false;
        } else if (iHeight != null && iHeight < 80) {
            this.height.setError("Najniższy wzrost dostępny w serwisie to 80cm.");
            valid = false;
        } else if (iHeight != null && iHeight > 250) {
            this.height.setError("Najwyższy wzrost dostępny w serwisie to 250cm.");
            valid = false;
        } else {
            this.height.setError(null);
        }
        return valid;
    }

    private boolean validWeight(boolean valid, String weight, Integer iWeight) {
        if (!weight.isEmpty() && (weight.length() < 1 || weight.length() > 3)) {
            this.weight.setError("Niepoprawny format wagi.");
            valid = false;
        } else if (iWeight != null && iWeight < 30) {
            this.weight.setError("Najniższa waga dostępna w serwisie to 30kg.");
            valid = false;
        } else if (iWeight != null && iWeight > 200) {
            this.weight.setError("Najwyższa waga dostępna w serwisie to 200kg.");
            valid = false;
        } else {
            this.weight.setError(null);
        }
        return valid;
    }

    private boolean validAge(boolean valid, String age, Integer iAge) {
        if (age.isEmpty() || age.length() < 1 || age.length() > 2) {
            this.age.setError("Niepoprawny format wieku.");
            valid = false;
        } else if (iAge != null && iAge < 18) {
            this.age.setError("Rejestracja w serwisie możliwa od 18 roku życia.");
            valid = false;
        } else {
            this.age.setError(null);
        }
        return valid;
    }

    private boolean validEmail(boolean valid, String email) {
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            this.email.setError("Niepoprawny format email.");
            valid = false;
        } else if (!isUniqueEmail(email) && !sameEmail(email)) {
            this.email.setError("Email jest już używany.");
            valid = false;
        } else {
            this.email.setError(null);
        }
        return valid;
    }

    private boolean validUsername(boolean valid, String username) {
        if (username.isEmpty() || username.length() < 4 || username.length() > 26) {
            this.username.setError("Nazwa użytkownika musi zawierać minimum cztery znaki.");
            valid = false;
        } else if (!isUniqueUser(username) && !sameUsername(username)) {
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
     * @param username TODO
     * @return TODO
     */
    private boolean sameUsername(String username) {
        Boolean result = false;
        if (username.equals(currentUsername)) {
            result = true;
        }
        return result;
    }

    /**
     * TODO
     *
     * @param email TODO
     * @return TODO
     */
    private boolean sameEmail(String email) {
        Boolean result = false;
        if (email.equals(currentEmail)) {
            result = true;
        }
        return result;
    }

    private boolean validPassword(String password, String passwordConfirm) {
        boolean valid = true;
        if (password.isEmpty() || password.length() < 8 || password.length() > 26) {
            valid = false;
        } else if (!passwordConfirm.equals(password)) {
            valid = false;
        } else {
            this.password.setError(null);
        }
        return valid;
    }

    /**
     * TODO
     */
    public void updateProfileFailed() {
        Toast.makeText(getBaseContext(), "Zaktualizowanie profilu nieudane.", Toast.LENGTH_LONG).show();
        updateButton.setEnabled(true);
    }

    /**
     * TODO
     */
    public void updateProfileSuccess() {
        Toast.makeText(getBaseContext(), "Zaktualizowanie profilu udane.", Toast.LENGTH_LONG).show();
        updateButton.setEnabled(true);
    }

    /**
     * TODO
     *
     * @param view TODO
     */
    public void showProfile(View view) {
        startActivity(new Intent(this, ProfileActivity.class));
    }

    public void changePassword(View view) {
        openUpdateDialog();
    }

    public void logout(MenuItem menu) {
        startActivity(new Intent(this, LoginActivity.class));
        Toast.makeText(getBaseContext(), "Wylogowanie powiodło się!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(this, WelcomeActivity.class));
            // Handle the camera action
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
        } else if (id == R.id.nav_training) {
            startActivity(new Intent(this, TrainingActivity.class));
        } else if (id == R.id.nav_friend) {
            startActivity(new Intent(this, FriendsActivity.class));
        } else if (id == R.id.nav_history) {
            startActivity(new Intent(this, HistoryActivity.class));
        } else if (id == R.id.nav_decision) {
            startActivity(new Intent(this, ObjectivesActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
