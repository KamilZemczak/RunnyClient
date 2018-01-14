package kamilzemczak.runny.activity.activity_user;

import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

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
import kamilzemczak.runny.backgroundworker.FriendBackgroundWorker;
import kamilzemczak.runny.adapter.SearchUserFriendsAdapter;
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
public class SearchUserFriendsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private LoginActivity loginActivity;

    private ListView allFriends;
    private TextView noFriendsFind;

    private List<User> friends = new ArrayList<User>();
    private ArrayList<ArrayList<String>> friendsAfterProcessing = new ArrayList<ArrayList<String>>();

    public static List<ArrayList<String>> friendsAfterProcessingToSend = new ArrayList<ArrayList<String>>();

    public static String friendCurrentName, friendCurrentSurname, friendCurrentUsername, friendCurrentEmail, friendCurrentGender, friendCurrentCity, friendCurrentAbout;
    public static Integer friendCurrentId, friendCurrentAge, friendCurrentWeight, friendCurrentHeight;

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

        allFriends = (ListView) findViewById(R.id.searchUserFriendsActivity_lAllFriends);
        noFriendsFind = (TextView) findViewById(R.id.searchUserFriendsActivity_tvNoFriendsFind);

        loadHistory();

        allFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent appInfo = new Intent(SearchUserFriendsActivity.this, ViewFriendProfileActivity.class);
                startActivity(appInfo);
                getFriendDetails(position);
            }

            private void getFriendDetails(int position) {
                friendCurrentId = friends.get(position).getId();
                friendCurrentUsername = friends.get(position).getUsername();
                friendCurrentName = friends.get(position).getName();
                friendCurrentSurname = friends.get(position).getSurname();
                friendCurrentEmail = friends.get(position).getEmail();
                friendCurrentAge = friends.get(position).getAge();
                friendCurrentGender = friends.get(position).getGender();
                if (friends.get(position).getWeight() != null) {
                    friendCurrentWeight = friends.get(position).getWeight();
                }
                if (friends.get(position).getHeight() != null) {
                    friendCurrentHeight = friends.get(position).getHeight();
                }
                if (friends.get(position).getCity() != null) {
                    friendCurrentCity = friends.get(position).getCity();
                }
                if (friends.get(position).getAbout() != null) {
                    friendCurrentAbout = friends.get(position).getAbout();
                }
            }
        });
    }

    private void loadHistory() {
        String type = "friends_find";
        String username = loginActivity.userCurrentUsername;
        String result = null;
        FriendBackgroundWorker friendBackgroundWorker = new FriendBackgroundWorker(this);

        try {
            result = friendBackgroundWorker.execute(type, username).get();
            ObjectMapper objectMapper = new ObjectMapper();
            friends = objectMapper.readValue(result, new TypeReference<List<User>>() {
            });

            processFriendsList();
            friendsAfterProcessingToSend = friendsAfterProcessing;
            SearchUserFriendsAdapter adapter = new SearchUserFriendsAdapter(this, R.layout.form_friends, friendsAfterProcessing);
            allFriends.setAdapter(adapter);
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

        if(friendsAfterProcessing.isEmpty()) {
            noFriendsFind.setText("Brak znajomych na Twojej liście. Zacznij szukać, dodaj i nawiąż nową znajomość!");
        }
    }

    private void processFriendsList() {
        for (User friend : friends) {
            ArrayList<String> friendsToProcessing = new ArrayList<>();
            if (friend.getName().isEmpty() && friend.getSurname().isEmpty()) {
                friendsToProcessing.add("Imie Nazwisko");
            } else {
                friendsToProcessing.add(friend.getName() + " " + friend.getSurname());
            }
            friendsToProcessing.add(friend.getUsername());
            if (friend.getCity() != null) {
                if (friend.getCity().isEmpty()) {
                    friendsToProcessing.add("Brak lokalizacji.");
                } else {
                    friendsToProcessing.add(friend.getCity());
                }
            } else {
                friendsToProcessing.add("Brak lokalizacji.");
            }
            friendsAfterProcessing.add(friendsToProcessing);
        }
    }

    public void goToSearchFriends(View view) {
        startActivity(new Intent(this, SearchFriendsActivity.class));
    }

    public void showProfile(View view) {
        startActivity(new Intent(this, ProfileActivity.class));
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
        getMenuInflater().inflate(R.menu.search_user_friends, menu);
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
