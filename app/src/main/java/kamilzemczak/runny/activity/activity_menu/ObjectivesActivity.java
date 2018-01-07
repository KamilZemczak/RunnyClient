package kamilzemczak.runny.activity.activity_menu;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import kamilzemczak.runny.R;
import kamilzemczak.runny.activity.activity_entry.LoginActivity;
import kamilzemczak.runny.activity.activity_user.ViewUserObjectivesActivity;
import kamilzemczak.runny.backgroundworker.ObjectiveBackgroundWorker;

/**
 * TODO
 */
public class ObjectivesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    LoginActivity loginActivity;

    Spinner typeSpinner, objectiveSpinner;
    TextView typeSelected;
    TextView objectiveSelected;

Button saveObjectiveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_objectives);
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

        typeSpinner = (Spinner) findViewById(R.id.static_spinner);
        objectiveSpinner = (Spinner) findViewById(R.id.sObjective);
        saveObjectiveButton = (Button) findViewById(R.id.bSaveObjective);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.typeso_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);


        ArrayAdapter<CharSequence> objectiveAdapter = ArrayAdapter.createFromResource(this, R.array.objectives_array_distance, android.R.layout.simple_spinner_item);
        objectiveAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        objectiveSpinner.setAdapter( objectiveAdapter );

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeSelected = (TextView) view;
               // Toast.makeText(ObjectivesActivity.this, "Wybrałes" + " " + typeSelected.getText(), Toast.LENGTH_SHORT).show();
                String test = typeSelected.getText().toString();
                if(typeSelected!=null) {
                    if(test.equals("Dystans")) {
                        ArrayAdapter<CharSequence> objectiveAdapter2 = ArrayAdapter.createFromResource(ObjectivesActivity.this, R.array.objectives_array_distance, android.R.layout.simple_spinner_item);
                        objectiveAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        objectiveSpinner.setAdapter( objectiveAdapter2 );
                    }else if(test.equals("Kalorie")) {
                        ArrayAdapter<CharSequence> objectiveAdapter22 = ArrayAdapter.createFromResource(ObjectivesActivity.this, R.array.objectives_array_calories, android.R.layout.simple_spinner_item);
                        objectiveAdapter22.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        objectiveSpinner.setAdapter( objectiveAdapter22 );
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        objectiveSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                objectiveSelected = (TextView) view;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
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
        getMenuInflater().inflate(R.menu.objectives, menu);
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

    public void saveObjective(View view) {
        String str_username = loginActivity.currentUsername;
        String typeo = typeSelected.getText().toString();
        String objective = objectiveSelected.getText().toString();
        String type = "objective_add";
        ObjectiveBackgroundWorker objectiveBackgroundWorker = new ObjectiveBackgroundWorker(this);
        objectiveBackgroundWorker.execute(type, str_username, typeo, objective);
        onSaveSucces();
    }

    /**
     * TODO
     */
    public void onSaveSucces() {
        Toast.makeText(getBaseContext(), "Dodanie celu udane.", Toast.LENGTH_LONG).show();
        saveObjectiveButton.setEnabled(true);
    }

    public void showObjectiveHistory(View view) {
        startActivity(new Intent(this, ViewUserObjectivesActivity.class));

    }
}
