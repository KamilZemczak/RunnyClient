package kamilzemczak.runny.activity.activity_menu;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import kamilzemczak.runny.R;
import kamilzemczak.runny.activity.activity_entry.LoginActivity;
import kamilzemczak.runny.activity.activity_user.EditProfileActivity;
import kamilzemczak.runny.backgroundworker.RegisterBackgroundWorker;
import kamilzemczak.runny.backgroundworker.TrainingBackgroundWorker;

public class TrainingActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView trainingCaloriesInfo;
    EditText distance, hours, mins, notes;
    Button addTrainingButton;
    LoginActivity loginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
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

        trainingCaloriesInfo = (TextView) findViewById(R.id.tvAddTreningCaloriesInfo);
        distance = (EditText) findViewById(R.id.etDistance);
        hours = (EditText) findViewById(R.id.etHours);
        mins = (EditText) findViewById(R.id.etMins);
        notes = (EditText) findViewById(R.id.etNotes);
        addTrainingButton = (Button) findViewById(R.id.bAddTraning);

        trainingCaloriesInfo.setText("Obliczymy średnią ilość kalorii spalonych przez Twój organizm w podanym przez Ciebie czasie. Pamiętaj, o aktualizowaniu na bieżąco swojej wagi w profilu, o warunkach otoczenia (takich jak wiatr i temperatura) oraz o cechach organizmu które mogą nieco zmienić rzeczywisty wynik.");


    }

    /**
     * Przesyła na serwer dane które użytkownik podał w formularzu rejestracyjnym
     *
     * @param view aktualny interfejs
     */
    public void addTraining(View view) {
        /*if(!validate()) {
            onRegisterFailed();
            return;
        }*/
        String str_username = loginActivity.currentUsername;
        String str_distance = distance.getText().toString();
        String str_hours = hours.getText().toString();
        Integer int_hours = Integer.valueOf(str_hours);
        String str_mins = mins.getText().toString();
        Integer int_mins = Integer.valueOf(str_mins);
        Integer int_duration = null;
        String str_notes = null;
        if (int_hours==1) {
            int_duration = 60 + int_mins;
        } else if (int_hours==2) {
            int_duration = 120 + int_mins;
        } else if (int_hours==3) {
            int_duration = 180 + int_mins;
        } else if (int_hours==4) {
            int_duration = 240 + int_mins;
        } else if (int_hours==5) {
            int_duration = 300 + int_mins;
        } else if (int_hours==6) {
            int_duration = 360 + int_mins;
        } else if (int_hours==7) {
            int_duration = 420 + int_mins;
        }
        String str_duration = String.valueOf(int_duration);
        if(!notes.getText().toString().isEmpty()) {
            str_notes = notes.getText().toString();
        } else {
            str_notes = "Brak notatek nt. treningu.";
        }

        String type = "training_add";
        TrainingBackgroundWorker trainingBackgroundWorker = new TrainingBackgroundWorker(this);
        trainingBackgroundWorker.execute(type, str_username, str_distance, str_duration, str_notes, str_hours, str_mins);
        onAddTrainingSuccess();
        finish();
        startActivity(getIntent());
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
        getMenuInflater().inflate(R.menu.training, menu);
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

    public void showEditProfile(View view) {
        startActivity(new Intent(this, EditProfileActivity.class));
    }

    /**
     * TODO
     */
    public void onAddTrainingSuccess() {
        Toast.makeText(getBaseContext(), "Dodanie treningu udane.", Toast.LENGTH_LONG).show();
        addTrainingButton.setEnabled(true);
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


}
