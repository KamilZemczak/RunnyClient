package kamilzemczak.runny.activity.activity_user;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.NavigationView;
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
import kamilzemczak.runny.activity.activity_social.CommentActivity;
import kamilzemczak.runny.backgroundworker.CommentBackgroundWorker;
import kamilzemczak.runny.model.Objective;
import kamilzemczak.runny.backgroundworker.ObjectiveBackgroundWorker;
import kamilzemczak.runny.adapter.ObjectiveAdapter;
import kamilzemczak.runny.activity.activity_entry.LoginActivity;
import kamilzemczak.runny.activity.activity_menu.FriendsActivity;
import kamilzemczak.runny.activity.activity_menu.HistoryActivity;
import kamilzemczak.runny.activity.activity_menu.ObjectivesActivity;
import kamilzemczak.runny.activity.activity_menu.ProfileActivity;
import kamilzemczak.runny.activity.activity_menu.TrainingActivity;
import kamilzemczak.runny.activity.activity_menu.WelcomeActivity;

public class ViewUserObjectivesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private LoginActivity loginActivity;

    private TextView noObjectivesFind, objectiveId;
    private ListView allObjectives;
    private ImageView deleteButton;

    private List<Objective> objectives = new ArrayList<Objective>();

    public static Integer objectiveCurrentId;

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

        allObjectives = (ListView) findViewById(R.id.viewUserObjectivesActivity_lAllObjectives);
        noObjectivesFind = (TextView) findViewById(R.id.viewUserObjectivesActivity_tvNoObjectivesFind);
        updateObjectives();
        loadHistory();

        allObjectives.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                objectiveCurrentId = objectives.get(position).getId();
            }
        });

    }

    public void updateObjectives() {
        String username = loginActivity.userCurrentUsername;
        String type = "objective_update";
        ObjectiveBackgroundWorker objectiveBackgroundWorker = new ObjectiveBackgroundWorker(this);
        objectiveBackgroundWorker.execute(type, username);
    }

    private void loadHistory() {
        String type = "objectives_find";
        String username = loginActivity.userCurrentUsername;
        String result = null;
        ObjectiveBackgroundWorker objectiveBackgroundWorker = new ObjectiveBackgroundWorker(this);

        try {
            result = objectiveBackgroundWorker.execute(type, username).get();
            ObjectMapper objectMapper = new ObjectMapper();
            objectives = objectMapper.readValue(result, new TypeReference<List<Objective>>() {
            });

            ObjectiveAdapter customAdapter = new ObjectiveAdapter(this, R.layout.form_friends, objectives);
            allObjectives.setAdapter(customAdapter);

            if (objectives.isEmpty()) {
                noObjectivesFind.setText("Brak celów. Dodaj cel już dziś i ciesz się dodatkową motywacją!");
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

    private void openDeleteDialog() {
        LayoutInflater inflater = LayoutInflater.from(ViewUserObjectivesActivity.this);
        final View subView = inflater.inflate(R.layout.form_objective_delete_dialog, null);
        objectiveId = (EditText) subView.findViewById(R.id.objectivesLayout_tvId);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Usuń cel już teraz!");
        builder.setMessage("Czy jesteś pewien? To nieodwracalne.");
        builder.setView(subView);

        AlertDialog alertDialog = builder.create();

        builder.setPositiveButton("TAK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String type = "objective_delete";
                String sObjectiveCurrentId = String.valueOf(objectiveCurrentId);
                //String sObjectiveCurrentId2 = objectiveId.getText().toString();
                ObjectiveBackgroundWorker objectiveBackgroundWorker = new ObjectiveBackgroundWorker(ViewUserObjectivesActivity.this);
                objectiveBackgroundWorker.execute(type, sObjectiveCurrentId);
                finish();
                startActivity(getIntent());
                Toast.makeText(getBaseContext(), "Cel został usunięty.", Toast.LENGTH_LONG).show();
            }
        });

        builder.setNegativeButton("COFNIJ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    public void deleteObjective(View view) {
        openDeleteDialog();
    }

    public void goToAddObjectives(View view) {
        startActivity(new Intent(this, ObjectivesActivity.class));
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
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
