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
import kamilzemczak.runny.backgroundworker.ListBackgroundWorker;
import kamilzemczak.runny.model.User;

public class SearchFriendsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ListView allUsers;
    List<User> users = new ArrayList<User>();
    ArrayList<ArrayList<String>> usersList1234 = new ArrayList<ArrayList<String>>();

    public static String currentNameP;
    public static String currentSurnameP;
    public static String currentUsernameP;
    public static String currentEmailP;
    public static String currentGenderP;
    public static Integer currentAgeP;
    public static Integer currentWeightP;
    public static Integer currentHeightP;
    public static String currentCityP;
    public static String currentAboutP;

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

        allUsers = (ListView) findViewById(R.id.lAllUsers);

        String type = "users_find";
        String result = null;
        ListBackgroundWorker listBackgroundWorker = new ListBackgroundWorker(this);

        try {
            result = listBackgroundWorker.execute(type).get();
            ObjectMapper objectMapper = new ObjectMapper();
            users = objectMapper.readValue(result, new TypeReference<List<User>>() {
            });
            for (User user : users) {
                ArrayList<String> usersList123 = new ArrayList<>();
                usersList123.add(user.getUsername());
                if (user.getName().isEmpty()) {
                    usersList123.add("Imie");
                } else {
                    usersList123.add(user.getName());
                }
                if (user.getSurname().isEmpty()) {
                    usersList123.add("Nazwisko");
                } else {
                    usersList123.add(user.getSurname());
                }
                usersList1234.add(usersList123);
            }
            ArrayAdapter adapter = new ArrayAdapter<>(SearchFriendsActivity.this, android.R.layout.simple_list_item_1, usersList1234);
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
                //dupa = position;
                Intent appInfo = new Intent(SearchFriendsActivity.this, ViewUserProfileActivity.class);
                startActivity(appInfo);

                currentUsernameP = users.get(position).getUsername();
                currentNameP = users.get(position).getName();
                currentSurnameP = users.get(position).getSurname();
                currentEmailP = users.get(position).getEmail();
                currentAgeP = users.get(position).getAge();
                currentGenderP = users.get(position).getGender();
                if(users.get(position).getWeight()!=null) {
                    currentWeightP = users.get(position).getWeight();
                }
                if(users.get(position).getHeight()!=null) {
                    currentHeightP = users.get(position).getHeight();
                }
                if(users.get(position).getCity()!=null) {
                    currentCityP = users.get(position).getCity();
                }
                if(users.get(position).getAbout()!=null) {
                    currentAboutP = users.get(position).getAbout();
                }

                /*JSONArray array = new JSONArray();
                for (Object dupes : allUsersSet) {
                    try {
                        array.put(new JSONObject()
                                .put("name", dupes));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                try {
                    JSONObject rec = array.getJSONObject(dupa);

                    String statistics = rec.getString("name");
                    String dupa = null;
                  a  dupa = "a";
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/


               // String test = null;


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
        getMenuInflater().inflate(R.menu.friend, menu);
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
            //Intent h = new Intent(WelcomeActivity.this,ProfileActivity.class);
            //startActivity(h);
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(this, ViewProfileActivity.class));
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
}
