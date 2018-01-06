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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
import kamilzemczak.runny.activity.activity_menu.SettingsActivity;
import kamilzemczak.runny.activity.activity_menu.TrainingActivity;
import kamilzemczak.runny.activity.activity_menu.ProfileActivity;
import kamilzemczak.runny.activity.activity_menu.WelcomeActivity;
import kamilzemczak.runny.adapter.SearchFriendsAdapter;
import kamilzemczak.runny.adapter.SearchUserFriendsAdapter;
import kamilzemczak.runny.backgroundworker.FriendBackgroundWorker;
import kamilzemczak.runny.model.User;

/**
 * TODO
 */
public class SearchUserFriendsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    LoginActivity loginActivity;
    ListView allFriends;

    public static String currentNameF, currentSurnameF, currentUsernameF, currentEmailF, currentGenderF, currentCityF, currentAboutF;
    public static Integer currentIdF, currentAgeF, currentWeightF, currentHeightF;

    List<User> friends = new ArrayList<User>();
    ArrayList<ArrayList<String>> friendsAfterProcessing = new ArrayList<ArrayList<String>>();

    public static List<ArrayList<String>> friendsAfterProcessingToSend = new ArrayList<ArrayList<String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_friend_list);
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

        allFriends = (ListView) findViewById(R.id.lAllFriends);

        String type = "friends_find";
        String str_username = loginActivity.currentUsername;
        String result = null;
        FriendBackgroundWorker friendBackgroundWorker = new FriendBackgroundWorker(this);

        try {
            result = friendBackgroundWorker.execute(type, str_username).get();
            ObjectMapper objectMapper = new ObjectMapper();
            friends = objectMapper.readValue(result, new TypeReference<List<User>>() {
            });

            for(User friend : friends) {
                ArrayList<String> friendsToProcessing = new ArrayList<>();
                if (friend.getName().isEmpty() && friend.getSurname().isEmpty()) {
                    friendsToProcessing.add("Imie Nazwisko");
                } else {
                    friendsToProcessing.add(friend.getName()+" "+friend.getSurname());
                }
                friendsToProcessing.add(friend.getUsername());
                if(friend.getCity()!=null) {
                    if (friend.getCity().isEmpty()) {
                        friendsToProcessing.add("Sosnowiec");
                    } else {
                        friendsToProcessing.add(friend.getCity());
                    }
                } else {
                    friendsToProcessing.add("Sosnowiec");
                }
                friendsAfterProcessing.add(friendsToProcessing);
            }


            friendsAfterProcessingToSend = friendsAfterProcessing;

            SearchUserFriendsAdapter customAdapter = new SearchUserFriendsAdapter(this, R.layout.friends_item_layout, friendsAfterProcessing);

            allFriends.setAdapter(customAdapter);


           // ArrayAdapter adapter = new ArrayAdapter<>(SearchUserFriendsActivity.this, android.R.layout.simple_list_item_1, friendsAfterProcessing);
            //this.allFriends.setAdapter(adapter);
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

        allFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent appInfo = new Intent(SearchUserFriendsActivity.this, ViewFriendProfileActivity.class);
                startActivity(appInfo);

                currentIdF = friends.get(position).getId();
                currentUsernameF = friends.get(position).getUsername();
                currentNameF = friends.get(position).getName();
                currentSurnameF = friends.get(position).getSurname();
                currentEmailF = friends.get(position).getEmail();
                currentAgeF = friends.get(position).getAge();
                currentGenderF = friends.get(position).getGender();
                if (friends.get(position).getWeight() != null) {
                    currentWeightF = friends.get(position).getWeight();
                }
                if (friends.get(position).getHeight() != null) {
                    currentHeightF = friends.get(position).getHeight();
                }
                if (friends.get(position).getCity() != null) {
                    currentCityF = friends.get(position).getCity();
                }
                if (friends.get(position).getAbout() != null) {
                    currentAboutF = friends.get(position).getAbout();
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
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
