package kamilzemczak.runny.activity.activity_user;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import kamilzemczak.runny.R;
import kamilzemczak.runny.activity.activity_entry.LoginActivity;
import kamilzemczak.runny.activity.activity_menu.FriendsActivity;
import kamilzemczak.runny.activity.activity_menu.HistoryActivity;
import kamilzemczak.runny.activity.activity_menu.ObjectivesActivity;
import kamilzemczak.runny.activity.activity_menu.SettingsActivity;
import kamilzemczak.runny.activity.activity_menu.TrainingActivity;
import kamilzemczak.runny.activity.activity_menu.ProfileActivity;
import kamilzemczak.runny.activity.activity_menu.WelcomeActivity;
import kamilzemczak.runny.backgroundworker.UniqueBackgroundWorker;
import kamilzemczak.runny.backgroundworker.UserBackgroundWorker;
import kamilzemczak.runny.model.User;

/**
 * TODO
 */
public class EditProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    LoginActivity loginActivity;
    EditText username, email, age, gender, weight, height, city, about;
    TextView user;
    //String str_username, str_email, str_age, str_gender, str_weight, str_height, str_city, str_about;
    Integer int_id = loginActivity.currentId;
    Button updateButton;

    String currentUsername = loginActivity.currentUsername;
    String currentEmail = loginActivity.currentEmail;

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

        user = (TextView) findViewById(R.id.tvUserShow);
        username = (EditText) findViewById(R.id.etUsernameE);
        email = (EditText) findViewById(R.id.etEmailE);
        age = (EditText) findViewById(R.id.etAgeE);
        gender = (EditText) findViewById(R.id.etGenderE);
        weight = (EditText) findViewById(R.id.etWeightE);
        height = (EditText) findViewById(R.id.etHeightE);
        city = (EditText) findViewById(R.id.etCityE);
        about = (EditText) findViewById(R.id.etAboutE);

        updateButton = (Button) findViewById(R.id.bEditProfile);

        user.setText(loginActivity.currentName + " " + loginActivity.currentSurname);

        username.setText(loginActivity.currentUsername, TextView.BufferType.EDITABLE);
        email.setText(loginActivity.currentEmail, TextView.BufferType.EDITABLE);
        age.setText(Integer.toString(loginActivity.currentAge), TextView.BufferType.EDITABLE);
        if (loginActivity.currentGender != null && loginActivity.currentGender.equals("M")) {
            gender.setText("Mężczyzna", TextView.BufferType.EDITABLE);
        }
        if (loginActivity.currentGender != null && loginActivity.currentGender.equals("F")) {
            gender.setText("Kobieta", TextView.BufferType.EDITABLE);
        }
        if ((loginActivity.currentWeight) != null) {
            weight.setText(Integer.toString(LoginActivity.currentWeight), TextView.BufferType.EDITABLE);
        }
        if ((loginActivity.currentHeight) != null) {
            height.setText(Integer.toString(loginActivity.currentHeight), TextView.BufferType.EDITABLE);
        }
        if (loginActivity.currentCity != null) {
            city.setText(loginActivity.currentCity, TextView.BufferType.EDITABLE);
        }
        if (loginActivity.currentAbout != null) {
            about.setText(loginActivity.currentAbout, TextView.BufferType.EDITABLE);
        }

        gender.setKeyListener(null);
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
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * TODO
     *
     * @param view TODO
     */
    public void updateUser(View view) {
        if (!validate()) {
            onUpdateFailed();
            return;
        }

        String str_id = Integer.toString(int_id);
        String str_username = username.getText().toString();
        String str_email = email.getText().toString();
        String str_age = age.getText().toString();
        String str_weight = weight.getText().toString();
        String str_height = height.getText().toString();
        String str_city = city.getText().toString();
        String str_about = about.getText().toString();
        String type = "user_update";
        UserBackgroundWorker userBackgroundWorker = new UserBackgroundWorker(this);
        userBackgroundWorker.execute(type, str_id, str_username, str_email, str_age, str_weight, str_height, str_city, str_about);
        onUpdateSuccess();
        getUserDetails();
        showProfile(view);
    }

    /**
     * TODO
     *
     * @return
     */
    public boolean validate() {
        boolean valid = true;

        String str_username = username.getText().toString();
        String str_email = email.getText().toString();
        String str_age = age.getText().toString();
        String str_weight = null;
        String str_height = null;
        String str_city = null;
        String str_about = null;
        Integer int_age = null;
        Integer int_weight = null;
        Integer int_height = null;
        Integer int_about = null;

        if (weight.getText() != null) {
            str_weight = weight.getText().toString();
        }

        if (height.getText() != null) {
            str_height = height.getText().toString();
        }

        if (city.getText() != null) {
            str_city = city.getText().toString();
        }

        if (about.getText() != null) {
            str_about = about.getText().toString();
            int_about = str_about.length();
        }

        if (!str_age.isEmpty()) {
            int_age = Integer.parseInt(str_age);
        }

        if (!str_weight.isEmpty()) {
            int_weight = Integer.parseInt(str_weight);
        }

        if (!str_height.isEmpty()) {
            int_height = Integer.parseInt(str_height);
        }

        if (str_username.isEmpty() || str_username.length() < 4 || str_username.length() > 26) {
            username.setError("Nazwa użytkownika musi zawierać minimum cztery znaki.");
            valid = false;
        } else if (!isUniqueUser(str_username) && !sameUsername(str_username)) {
            username.setError("Nazwa użytkownika jest już używana.");
            valid = false;
        } else {
            username.setError(null);
        }

        if (str_email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(str_email).matches()) {
            email.setError("Niepoprawny format email.");
            valid = false;
        } else if (!isUniqueEmail(str_email) && !sameEmail(str_email)) {
            email.setError("Email jest już używany.");
            valid = false;
        } else {
            email.setError(null);
        }

        if (str_age.isEmpty() || str_age.length() < 1 || str_age.length() > 2) {
            age.setError("Niepoprawny format wieku.");
            valid = false;
        } else if (int_age != null && int_age < 18) {
            age.setError("Rejestracja w serwisie możliwa od 18 roku życia.");
            valid = false;
        } else {
            age.setError(null);
        }

        if (!str_weight.isEmpty() && (str_weight.length() < 1 || str_weight.length() > 3)) {
            weight.setError("Niepoprawny format wagi.");
            valid = false;
        } else if (int_weight != null && int_weight < 30) {
            weight.setError("Najniższa waga dostępna w serwisie to 30kg.");
            valid = false;
        } else if (int_weight != null && int_weight > 200) {
            weight.setError("Najwyższa waga dostępna w serwisie to 200kg.");
            valid = false;
        } else {
            weight.setError(null);
        }

        if (!str_height.isEmpty() && (str_height.length() < 2 || str_height.length() > 3)) {
            height.setError("Niepoprawny format wzrostu.");
            valid = false;
        } else if (int_height != null && int_height < 80) {
            height.setError("Najniższy wzrost dostępny w serwisie to 80cm.");
            valid = false;
        } else if (int_height != null && int_height > 250) {
            height.setError("Najwyższy wzrost dostępny w serwisie to 250cm.");
            valid = false;
        } else {
            height.setError(null);
        }

        if (!str_city.isEmpty() && (str_city.length() < 3 || str_city.length() > 24)) {
            city.setError("Niepoprawny format miasta.");
            valid = false;
        } else {
            city.setError(null);
        }

        if (!str_about.isEmpty() && int_about < 20) {
            about.setError("Minimum 20 znaków o sobie.");
            valid = false;
        } else if (!str_about.isEmpty() && int_about > 256) {
            about.setError("Maksymalne 255 znaków o sobie.");
            valid = false;
        } else {
            about.setError(null);
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
     * @param str_username TODO
     * @return TODO
     */
    private boolean sameUsername(String str_username) {
        Boolean result = false;
        if (str_username.equals(currentUsername)) {
            result = true;
        }
        return result;
    }

    /**
     * TODO
     *
     * @param str_email TODO
     * @return TODO
     */
    private boolean sameEmail(String str_email) {
        Boolean result = false;
        if (str_email.equals(currentEmail)) {
            result = true;
        }
        return result;
    }

    /**
     * TODO
     */
    public void onUpdateFailed() {
        Toast.makeText(getBaseContext(), "Zaktualizowanie profilu nieudane.", Toast.LENGTH_LONG).show();
        updateButton.setEnabled(true);
    }

    /**
     * TODO
     */
    public void onUpdateSuccess() {
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

    /**
     * TODO
     */
    public void getUserDetails() {
        String str_username = username.getText().toString();
        String type = "user_details";
        UserBackgroundWorker userBackgroundWorker = new UserBackgroundWorker(this);
        try {
            ObjectMapper mapper = new ObjectMapper();
            User currentUser;
            String userJson = userBackgroundWorker.execute(type, str_username).get();
            currentUser = mapper.readValue(userJson, User.class);
            loginActivity.currentId = currentUser.getId();
            loginActivity.currentName = currentUser.getName();
            loginActivity.currentSurname = currentUser.getSurname();
            loginActivity.currentUsername = currentUser.getUsername();
            loginActivity.currentEmail = currentUser.getEmail();
            loginActivity.currentAge = currentUser.getAge();
            loginActivity.currentGender = currentUser.getGender();
            if (currentUser.getWeight() != null) {
                loginActivity.currentWeight = currentUser.getWeight();
            }
            if (currentUser.getHeight() != null) {
                loginActivity.currentHeight = currentUser.getHeight();
            }
            if (currentUser.getCity() != null) {
                loginActivity.currentCity = currentUser.getCity();
            }
            if (currentUser.getAbout() != null) {
                loginActivity.currentAbout = currentUser.getAbout();
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
}
