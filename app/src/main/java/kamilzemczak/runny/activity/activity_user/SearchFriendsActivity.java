package kamilzemczak.runny.activity.activity_user;

import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import kamilzemczak.runny.R;
import kamilzemczak.runny.activity.activity_menu.FriendsActivity;
import kamilzemczak.runny.activity.activity_menu.HistoryActivity;
import kamilzemczak.runny.activity.activity_menu.ObjectivesActivity;
import kamilzemczak.runny.activity.activity_menu.SettingsActivity;
import kamilzemczak.runny.activity.activity_menu.TrainingActivity;
import kamilzemczak.runny.activity.activity_menu.WelcomeActivity;
import kamilzemczak.runny.backgroundworker.UserBackgroundWorker;
import kamilzemczak.runny.model.User;

/**
 * TODO
 */
public class SearchFriendsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ListView allUsers;
    List<User> users = new ArrayList<User>();
    List<ArrayList<String>> usersAfterProcessing = new ArrayList<ArrayList<String>>();

    public static String currentNameP, currentSurnameP, currentUsernameP, currentEmailP, currentGenderP, currentCityP, currentAboutP;
    public static Integer currentAgeP, currentWeightP, currentHeightP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user_friends);
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

        allUsers = (ListView) findViewById(R.id.lAllUsers);

        String type = "users_find";
        String result = null;
        UserBackgroundWorker userBackgroundWorker = new UserBackgroundWorker(this);

        try {
            result = userBackgroundWorker.execute(type).get();
            ObjectMapper objectMapper = new ObjectMapper();
            users = objectMapper.readValue(result, new TypeReference<List<User>>() {
            });
            for (User user : users) {
                ArrayList<String> usersToProcessing = new ArrayList<>();
                usersToProcessing.add(user.getUsername());
                if (user.getName().isEmpty() && user.getSurname().isEmpty()) {
                    usersToProcessing.add("Imie Nazwisko");
                } else {
                    usersToProcessing.add(user.getName() + " " + user.getSurname());
                }
                usersAfterProcessing.add(usersToProcessing);
            }
            ArrayAdapter adapter = new ArrayAdapter<>(SearchFriendsActivity.this, android.R.layout.simple_list_item_1, usersAfterProcessing);
            this.allUsers.setAdapter(adapter);
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

        allUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent appInfo = new Intent(SearchFriendsActivity.this, ViewUserProfileActivity.class);
                startActivity(appInfo);

                currentUsernameP = users.get(position).getUsername();
                currentNameP = users.get(position).getName();
                currentSurnameP = users.get(position).getSurname();
                currentEmailP = users.get(position).getEmail();
                currentAgeP = users.get(position).getAge();
                currentGenderP = users.get(position).getGender();
                if (users.get(position).getWeight() != null) {
                    currentWeightP = users.get(position).getWeight();
                }
                if (users.get(position).getHeight() != null) {
                    currentHeightP = users.get(position).getHeight();
                }
                if (users.get(position).getCity() != null) {
                    currentCityP = users.get(position).getCity();
                }
                if (users.get(position).getAbout() != null) {
                    currentAboutP = users.get(position).getAbout();
                }
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
        getMenuInflater().inflate(R.menu.friends, menu);
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
            startActivity(new Intent(this, ViewOwnProfileActivity.class));
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
