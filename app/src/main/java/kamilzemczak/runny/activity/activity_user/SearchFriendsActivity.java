package kamilzemczak.runny.activity.activity_user;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
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
import kamilzemczak.runny.model.User;
import kamilzemczak.runny.backgroundworker.UniqueBackgroundWorker;
import kamilzemczak.runny.backgroundworker.UserBackgroundWorker;
import kamilzemczak.runny.adapter.SearchFriendsAdapter;
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
public class SearchFriendsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private LoginActivity loginActivity;

    private ListView allUsers;

    private List<User> users = new ArrayList<User>();
    private List<ArrayList<String>> usersAfterProcessing = new ArrayList<ArrayList<String>>();

    public static List<ArrayList<String>> usersAfterProcessingToSend = new ArrayList<ArrayList<String>>();

    public static String userCurrentWatchedName, userCurrentWatchedSurname, userCurrentWatchedUsername, userCurrentWatchedEmail, userCurrentWatchedGender, userCurrentWatchedCity, userCurrentWatchedAbout;
    public static Integer userCurrentWatchedId, userCurrentWatchedAge, userCurrentWatchedWeight, userCurrentWatchedHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friends);
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

        allUsers = (ListView) findViewById(R.id.searchFriendsActivity_lAllUsers);

        loadHistory();

        allUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String username = loginActivity.userCurrentUsername;
                getCurrentWatchesUserDetails(position);

                if (isFriend(username, userCurrentWatchedUsername)) {
                    Intent appInfo = new Intent(SearchFriendsActivity.this, ViewFriendProfileActivity.class);
                    startActivity(appInfo);
                } else {
                    Intent appInfo = new Intent(SearchFriendsActivity.this, ViewUserProfileActivity.class);
                    startActivity(appInfo);
                }
            }

            private void getCurrentWatchesUserDetails(int position) {
                userCurrentWatchedId = users.get(position).getId();
                userCurrentWatchedUsername = users.get(position).getUsername();
                userCurrentWatchedName = users.get(position).getName();
                userCurrentWatchedSurname = users.get(position).getSurname();
                userCurrentWatchedEmail = users.get(position).getEmail();
                userCurrentWatchedAge = users.get(position).getAge();
                userCurrentWatchedGender = users.get(position).getGender();
                if (users.get(position).getWeight() != null) {
                    userCurrentWatchedWeight = users.get(position).getWeight();
                }
                if (users.get(position).getHeight() != null) {
                    userCurrentWatchedHeight = users.get(position).getHeight();
                }
                if (users.get(position).getCity() != null) {
                    userCurrentWatchedCity = users.get(position).getCity();
                }
                if (users.get(position).getAbout() != null) {
                    userCurrentWatchedAbout = users.get(position).getAbout();
                }
            }
        });
    }

    private void loadHistory() {
        String type = "users_find";
        String username = loginActivity.userCurrentUsername;
        String result = null;
        UserBackgroundWorker userBackgroundWorker = new UserBackgroundWorker(this);

        try {
            result = userBackgroundWorker.execute(type, username).get();
            ObjectMapper objectMapper = new ObjectMapper();
            users = objectMapper.readValue(result, new TypeReference<List<User>>() {
            });
            processUsersList();
            usersAfterProcessingToSend = usersAfterProcessing;
            SearchFriendsAdapter adapter = new SearchFriendsAdapter(this, R.layout.form_friends, usersAfterProcessing);
            allUsers.setAdapter(adapter);
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

    private void processUsersList() {
        for (User user : users) {
            ArrayList<String> usersToProcessing = new ArrayList<>();
            if (user.getName().isEmpty() && user.getSurname().isEmpty()) {
                usersToProcessing.add("Imie Nazwisko");
            } else {
                usersToProcessing.add(user.getName() + " " + user.getSurname());
            }
            usersToProcessing.add(user.getUsername());
            if (user.getCity() != null) {
                if (user.getCity().isEmpty()) {
                    usersToProcessing.add("Brak lokalizacji.");
                } else {
                    usersToProcessing.add(user.getCity());
                }
            } else {
                usersToProcessing.add("Brak lokalizacji.");
            }
            usersAfterProcessing.add(usersToProcessing);
        }
    }

    /**
     * TODO
     *
     * @param str_username TODO
     * @return TODO
     */
    private boolean isFriend(String str_username, String currentUsernameP) {
        String type = "is_friend";
        Boolean result = true;
        UniqueBackgroundWorker uniqueBackgroundWorker = new UniqueBackgroundWorker(this);
        try {
            result = uniqueBackgroundWorker.execute(type, str_username, currentUsernameP).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return result;
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
        getMenuInflater().inflate(R.menu.search_friends, menu);
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
