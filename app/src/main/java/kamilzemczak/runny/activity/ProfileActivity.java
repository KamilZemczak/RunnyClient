package kamilzemczak.runny.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import kamilzemczak.runny.R;
import kamilzemczak.runny.backgroundworker.RegisterBackgroundWorker;
import kamilzemczak.runny.backgroundworker.UpdateBackgroundWorker;

public class ProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    LoginActivity loginActivity;
    TextView user;
    EditText username, email, age, gender, weight, height, city, about;
    String str_username, str_email, str_age, str_gender, str_weight, str_height, str_city, str_about;
    Integer int_id = loginActivity.currentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
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

        user.setText(loginActivity.currentName + " " + loginActivity.currentSurname);

        username.setText(loginActivity.currentUsername, TextView.BufferType.EDITABLE);
        email.setText(loginActivity.currentEmail, TextView.BufferType.EDITABLE);
        age.setText(Integer.toString(loginActivity.currentAge), TextView.BufferType.EDITABLE);
        if (loginActivity.currentGender != null && loginActivity.currentGender.equals("M")) {
            gender.setText("Mężczyzna", TextView.BufferType.EDITABLE);
        }
        if (loginActivity.currentGender != null && loginActivity.currentGender.equals("K")) {
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
        getMenuInflater().inflate(R.menu.profile, menu);
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
            startActivity(new Intent(this, FriendActivity.class));
        } else if (id == R.id.nav_history) {
            startActivity(new Intent(this, HistoryActivity.class));
        } else if (id == R.id.nav_decision) {
            startActivity(new Intent(this, DecisionActivity.class));
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onUpdate(View view) {
        String str_id = Integer.toString(int_id);
        String str_username = username.getText().toString();
        String str_email = email.getText().toString();
        String str_age = age.getText().toString();
        String str_weight = weight.getText().toString();
        String str_height = height.getText().toString();
        String str_city = city.getText().toString();
        String str_about = about.getText().toString();
        String type = "update_user";
        UpdateBackgroundWorker updateBackgroundWorker = new UpdateBackgroundWorker(this);
        updateBackgroundWorker.execute(type, str_id, str_username, str_email, str_age, str_weight, str_height, str_city, str_about);
        
    }
}
