package kamilzemczak.runny.activity.activity_user;

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
import android.widget.ListView;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import kamilzemczak.runny.R;
import kamilzemczak.runny.activity.activity_entry.LoginActivity;
import kamilzemczak.runny.activity.activity_menu.FriendsActivity;
import kamilzemczak.runny.activity.activity_menu.HistoryActivity;
import kamilzemczak.runny.activity.activity_menu.ObjectivesActivity;
import kamilzemczak.runny.activity.activity_menu.ProfileActivity;
import kamilzemczak.runny.activity.activity_menu.SettingsActivity;
import kamilzemczak.runny.activity.activity_menu.TrainingActivity;
import kamilzemczak.runny.activity.activity_menu.WelcomeActivity;
import kamilzemczak.runny.adapter.ObjectiveAdapter;
import kamilzemczak.runny.backgroundworker.ObjectiveBackgroundWorker;
import kamilzemczak.runny.model.Objective;
import kamilzemczak.runny.model.User;

public class ViewUserObjectivesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    LoginActivity loginActivity;
    TextView noObjectives;

    ListView allObjectives;
    List<Objective> objectives = new ArrayList<Objective>();
   // List<String> objectivesAfterProcessing = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_objectives);
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

        allObjectives = (ListView) findViewById(R.id.lAllObjectives);
        noObjectives = (TextView) findViewById(R.id.tvNoObjectives);

        loadHistory();
    }

    private void loadHistory() {
        String type = "objectives_find";
        String str_username = loginActivity.currentUsername;
        String result = null;
        ObjectiveBackgroundWorker objectiveBackgroundWorker = new ObjectiveBackgroundWorker(this);

        try {
            result = objectiveBackgroundWorker.execute(type, str_username).get();
            ObjectMapper objectMapper = new ObjectMapper();
            objectives = objectMapper.readValue(result, new TypeReference<List<Objective>>() {
            });

           /* for (User user : users) {
                ArrayList<String> usersToProcessing = new ArrayList<>();
                if (user.getName().isEmpty() && user.getSurname().isEmpty()) {
                    usersToProcessing.add("Imie Nazwisko");
                } else {
                    usersToProcessing.add(user.getName() + " " + user.getSurname());
                }
                usersToProcessing.add(user.getUsername());
                if(user.getCity()!=null) {
                    if (user.getCity().isEmpty()) {
                        usersToProcessing.add("Sosnowiec");
                    } else {
                        usersToProcessing.add(user.getCity());
                    }
                } else {
                    usersToProcessing.add("Sosnowiec");
                }

                //
                usersAfterProcessing.add(usersToProcessing);
            }

            usersAfterProcessingToSend = usersAfterProcessing;*/

            ObjectiveAdapter customAdapter = new ObjectiveAdapter(this, R.layout.friends_item_layout, objectives);

            allObjectives.setAdapter(customAdapter);

            if(objectives.isEmpty()) {
                noObjectives.setText("Brak celow.");
            }

        } catch (
                InterruptedException e) {
            e.printStackTrace();
        } catch (
                ExecutionException e) {
            e.printStackTrace();
        } catch (
                JsonParseException e) {
            e.printStackTrace();
        } catch (
                JsonMappingException e) {
            e.printStackTrace();
        } catch (
                IOException e) {
            e.printStackTrace();
        }


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
        getMenuInflater().inflate(R.menu.view_user_objectives, menu);
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
}
