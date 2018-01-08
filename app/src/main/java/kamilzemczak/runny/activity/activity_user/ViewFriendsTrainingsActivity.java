package kamilzemczak.runny.activity.activity_user;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
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
import kamilzemczak.runny.adapter.TrainingAdapter;
import kamilzemczak.runny.backgroundworker.TrainingBackgroundWorker;
import kamilzemczak.runny.helper.RecyclerItemClickListener;
import kamilzemczak.runny.model.Training;

public class ViewFriendsTrainingsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    LoginActivity loginActivity;
    private TextView noTrainings;

    private EditText trainingContent;
    private Calendar calendar;

    private RecyclerView trainingContainer;
    private TrainingAdapter trainingAdapter;
    private List<Training> trainingHistory = new ArrayList<Training>();

    public static List<String> trainingsSize = new ArrayList<String>();

    public static Integer currentTrainingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_friends_trainings);
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

        noTrainings = (TextView) findViewById(R.id.tvNoTrainingsV);

        loadHistory();

        trainingContainer.addOnItemTouchListener(
                new RecyclerItemClickListener(ViewFriendsTrainingsActivity.this, trainingContainer ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        currentTrainingId = trainingHistory.get(position).getId();
                    }

                    @Override public void onLongItemClick(View view, int position) {
                    }
                })
        );
    }

    private void loadHistory() {
        String type = "trainings_find";
        String result = null;
        TrainingBackgroundWorker trainingBackgroundWorker = new TrainingBackgroundWorker(this);

        try {
            String str_username = loginActivity.userCurrentUsername;
            result = trainingBackgroundWorker.execute(type, str_username).get();
            ObjectMapper objectMapper = new ObjectMapper();
            trainingHistory = objectMapper.readValue(result, new TypeReference<List<Training>>() {
            });
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
        loadCommentsSize();
        Collections.reverse(trainingHistory);

        //Set the layout and the RecyclerView
        trainingContainer = (RecyclerView) findViewById(R.id.lTrainingV);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        trainingContainer.setLayoutManager(llm);
        trainingAdapter = new TrainingAdapter(ViewFriendsTrainingsActivity.this, trainingHistory);
        //Set the adapter for the recyclerlist
        trainingContainer.setAdapter(trainingAdapter);

        if(trainingHistory.isEmpty()) {
            noTrainings.setText("Brak treningów.");
        }
    }

    /**
     * TODO
     *
     * @param view TODO
     */
    public void openCommentsT(View view) {
        startActivity(new Intent(this, TCommentActivity.class));
    }

    private void loadCommentsSize() {
        String str_username = loginActivity.userCurrentUsername;
        String type = "trainings_comment_size";
        TrainingBackgroundWorker trainingBackgroundWorker = new TrainingBackgroundWorker(this);
        try {
            String result = trainingBackgroundWorker.execute(type, str_username).get();
            String replace = result.replace("[","");
            String replace1 = replace.replace("]","");
            trainingsSize = new ArrayList<String>(Arrays.asList(replace1.split(",")));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
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
        getMenuInflater().inflate(R.menu.view_friends_trainings, menu);
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
