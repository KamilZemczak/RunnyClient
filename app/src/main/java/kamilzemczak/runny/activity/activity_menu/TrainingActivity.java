package kamilzemczak.runny.activity.activity_menu;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.NavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import kamilzemczak.runny.R;
import kamilzemczak.runny.backgroundworker.TrainingBackgroundWorker;
import kamilzemczak.runny.activity.activity_entry.LoginActivity;
import kamilzemczak.runny.activity.activity_user.EditProfileActivity;

public class TrainingActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private LoginActivity loginActivity;

    private TextView trainingCaloriesInfo, trainingNotesInfo;
    private EditText distance, hours, mins, notes;
    private Button addTrainingButton;

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

        trainingCaloriesInfo = (TextView) findViewById(R.id.trainingActivity_tvTrainingCaloriesInfo);
        trainingNotesInfo = (TextView) findViewById(R.id.trainingActivity_tvNotesInfo);
        distance = (EditText) findViewById(R.id.trainingActivity_etDistance);
        hours = (EditText) findViewById(R.id.trainingActivity_etHours);
        mins = (EditText) findViewById(R.id.trainingActivity_etMins);
        notes = (EditText) findViewById(R.id.trainingActivity_etNotes);
        addTrainingButton = (Button) findViewById(R.id.trainingActivity_bAddTraning);

        trainingCaloriesInfo.setText("Obliczymy średnią ilość kalorii spalonych przez Twój organizm w podanym przez Ciebie czasie. Pamiętaj, o aktualizowaniu na bieżąco swojej wagi w profilu, o warunkach otoczenia (takich jak wiatr i temperatura) oraz o cechach organizmu które mogą nieco zmienić rzeczywisty wynik.");
        trainingNotesInfo.setText("*to pole nie jest wymagane. Możesz dodać osobiste notatki z treningu, które pomoga Ci osiągać swoje cele. Notatki nie będą widoczne dla innych użytkowników.");
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
        String username = loginActivity.userCurrentUsername;
        String distance = this.distance.getText().toString();
        String sHours = hours.getText().toString();
        Integer iHours = Integer.valueOf(sHours);
        String sMins = this.mins.getText().toString();
        Integer iMins = Integer.valueOf(sMins);
        Integer duration = null;
        String notes = null;

        duration = getDuration(iHours, iMins, duration);

        String duratioin = String.valueOf(duration);
        if(!this.notes.getText().toString().isEmpty()) {
            notes = this.notes.getText().toString();
        } else {
            notes = "Brak notatek nt. treningu.";
        }

        String type = "training_add";
        TrainingBackgroundWorker trainingBackgroundWorker = new TrainingBackgroundWorker(this);
        trainingBackgroundWorker.execute(type, username, distance, duratioin, notes, sHours, sMins);
        addTrainingSuccess();
        finish();
        startActivity(getIntent());
    }

    private Integer getDuration(Integer iHours, Integer iMins, Integer duration) {
        if (iHours==1) {
            duration = 60 + iMins;
        } else if (iHours==2) {
            duration = 120 + iMins;
        } else if (iHours==3) {
            duration = 180 + iMins;
        } else if (iHours==4) {
            duration = 240 + iMins;
        } else if (iHours==5) {
            duration = 300 + iMins;
        } else if (iHours==6) {
            duration = 360 + iMins;
        } else if (iHours==7) {
            duration = 420 + iMins;
        } else if (iHours==8) {
            duration = 480 + iMins;
        } else if (iHours==9) {
            duration = 540 + iMins;
        } else if (iHours==10) {
            duration = 600 + iMins;
        }
        return duration;
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
    public void addTrainingSuccess() {
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
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
