package kamilzemczak.runny.activity.activity_user;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import kamilzemczak.runny.R;
import kamilzemczak.runny.activity.activity_social.TCommentActivity;
import kamilzemczak.runny.model.Training;
import kamilzemczak.runny.backgroundworker.TrainingBackgroundWorker;
import kamilzemczak.runny.adapter.TrainingAdapter;
import kamilzemczak.runny.helper.RecyclerItemClickListener;
import kamilzemczak.runny.activity.activity_entry.LoginActivity;
import kamilzemczak.runny.activity.activity_menu.FriendsActivity;
import kamilzemczak.runny.activity.activity_menu.HistoryActivity;
import kamilzemczak.runny.activity.activity_menu.ObjectivesActivity;
import kamilzemczak.runny.activity.activity_menu.ProfileActivity;
import kamilzemczak.runny.activity.activity_menu.TrainingActivity;
import kamilzemczak.runny.activity.activity_menu.WelcomeActivity;

public class ViewFriendsTrainingsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private LoginActivity loginActivity;

    private EditText distance, hours, mins, notes;
    private TextView noTrainings;
    private RecyclerView trainingContainer;
    private TrainingAdapter trainingAdapter;

    private List<Training> trainingHistory = new ArrayList<Training>();

    public static List<String> trainingsSize = new ArrayList<String>();

    public static Integer trainingCurrentId, trainingCurrentDistance, trainingCurrentDuration;
    public static String trainingCurrentNotes;

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

        noTrainings = (TextView) findViewById(R.id.viewFriendsTrainingsActivity_tvNoTrainingsFind);

        loadHistory();

        trainingContainer.addOnItemTouchListener(
                new RecyclerItemClickListener(ViewFriendsTrainingsActivity.this, trainingContainer, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        trainingCurrentId = trainingHistory.get(position).getId();
                        trainingCurrentDistance = trainingHistory.get(position).getDistance();
                        trainingCurrentDuration = trainingHistory.get(position).getDuration();
                        trainingCurrentNotes = trainingHistory.get(position).getNotes();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                })
        );
    }

    private void loadHistory() {
        String type = "trainings_find";
        String result = null;
        TrainingBackgroundWorker trainingBackgroundWorker = new TrainingBackgroundWorker(this);

        try {
            String username = loginActivity.userCurrentUsername;
            result = trainingBackgroundWorker.execute(type, username).get();
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

        setTrainingHistoryToAdapter();

        if (trainingHistory.isEmpty()) {
            noTrainings.setText("Brak dostępnych treningów");
        }
    }

    private void setTrainingHistoryToAdapter() {
        trainingContainer = (RecyclerView) findViewById(R.id.viewFriendsTrainingsActivity_rvTrainings);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        trainingContainer.setLayoutManager(llm);
        trainingAdapter = new TrainingAdapter(ViewFriendsTrainingsActivity.this, trainingHistory);
        trainingContainer.setAdapter(trainingAdapter);
    }

    private void loadCommentsSize() {
        String username = loginActivity.userCurrentUsername;
        String type = "trainings_comment_size";
        TrainingBackgroundWorker trainingBackgroundWorker = new TrainingBackgroundWorker(this);
        try {
            String result = trainingBackgroundWorker.execute(type, username).get();
            String resultToReplace = result.replace("[", "");
            String finalResult = resultToReplace.replace("]", "");
            trainingsSize = new ArrayList<String>(Arrays.asList(finalResult.split(",")));
            Collections.reverse(trainingsSize);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void openUpdateDialog() {
        int iHours = trainingCurrentDuration / 60;
        int iMinutes = trainingCurrentDuration % 60;
        LayoutInflater inflater = LayoutInflater.from(ViewFriendsTrainingsActivity.this);
        View subView = inflater.inflate(R.layout.form_training_update_dialog, null);
        distance = (EditText) subView.findViewById(R.id.formTrainingUpdate_etDistance);
        hours = (EditText) subView.findViewById(R.id.formTrainingUpdate_etHours);
        mins = (EditText) subView.findViewById(R.id.formTrainingUpdate_etMins);
        notes = (EditText) subView.findViewById(R.id.formTrainingUpdate_etNotes);

        distance.setText(String.valueOf(trainingCurrentDistance));
        hours.setText(String.valueOf(iHours));
        mins.setText(String.valueOf(iMinutes));
        notes.setText(String.valueOf(trainingCurrentNotes));

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edytuj trening już teraz!");
        builder.setView(subView);

        AlertDialog alertDialog = builder.create();

        builder.setPositiveButton("ZATWIERDŹ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String type = "training_update";
                String sTrainingCurrentId = String.valueOf(trainingCurrentId);
                String distanceToSend = distance.getText().toString();
                String sHours = hours.getText().toString();
                String sMins = mins.getText().toString();
                Integer iHours = null;
                if (!sHours.isEmpty()) {
                    iHours = Integer.valueOf(sHours);
                } else {
                    iHours = 0;
                }
                Integer iMins = null;
                if (!sMins.isEmpty()) {
                    iMins = Integer.valueOf(sMins);
                } else {
                    iMins = 0;
                }
                Integer duration = null;
                String notesToSend = null;

                duration = getDuration(iHours, iMins, duration);
                String duratioin = null;
                if (duration == null) {
                    duratioin = sMins;
                } else {
                    duratioin = String.valueOf(duration);
                }

                if (!notes.getText().toString().isEmpty()) {
                    notesToSend = notes.getText().toString();
                } else {
                    notesToSend = "Brak notatek nt. treningu.";
                }

                if (!validate(distanceToSend, iHours, iMins)) {
                    openUpdateDialog();
                    Toast.makeText(getBaseContext(), "Edycja treningu nieudane.", Toast.LENGTH_LONG).show();
                    setErrors(distanceToSend, iHours, iMins);
                } else {
                    TrainingBackgroundWorker trainingBackgroundWorker = new TrainingBackgroundWorker(ViewFriendsTrainingsActivity.this);
                    trainingBackgroundWorker.execute(type, loginActivity.userCurrentUsername, sTrainingCurrentId, distanceToSend, duratioin, notesToSend, sHours, sMins);
                    finish();
                    startActivity(getIntent());
                    Toast.makeText(getBaseContext(), "Edycja treningu udana!", Toast.LENGTH_LONG).show();
                }
            }

            private void setErrors(String distanceToSend, Integer iHours, Integer iMins) {
                if (distanceToSend.isEmpty() || Integer.parseInt(distanceToSend) == 0 || Integer.parseInt(distanceToSend) < 0) {
                    distance.setError("Minimalny dystans to 1km.");
                } else if (distanceToSend.isEmpty() || Integer.parseInt(distanceToSend) > 70) {
                    distance.setError("Maksymalny dystans to 70km.");
                }

                if (iHours > 10) {
                    hours.setError("Maksymalny czas biegu to 10h");
                } else if (iMins == 0 && iHours == 0) {
                    mins.setError("Nie wpisałeś czasu.");
                    hours.setError("Nie wpisałeś czasu.");
                }

                if (iMins > 60) {
                    mins.setError("Minut maksymalnie 60!");
                } else if (iMins == 0 && iHours == 0) {
                    mins.setError("Nie wpisałeś czasu.");
                    hours.setError("Nie wpisałeś czasu.");
                }
            }
        });

        builder.setNegativeButton("ANULUJ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void openDeleteDialog() {
        LayoutInflater inflater = LayoutInflater.from(ViewFriendsTrainingsActivity.this);
        View subView = inflater.inflate(R.layout.form_training_delete_dialog, null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Usuń swój już teraz!");
        builder.setMessage("Czy jesteś pewien? Zmiany są nieodwracalne.");
        builder.setView(subView);

        AlertDialog alertDialog = builder.create();

        builder.setPositiveButton("TAK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String sTrainingCurrentId = String.valueOf(trainingCurrentId);
                String type = "training_delete";
                TrainingBackgroundWorker trainingBackgroundWorker = new TrainingBackgroundWorker(ViewFriendsTrainingsActivity.this);
                trainingBackgroundWorker.execute(type, sTrainingCurrentId);
                finish();
                startActivity(getIntent());
                Toast.makeText(getBaseContext(), "Trening został usunięty.", Toast.LENGTH_LONG).show();
            }
        });

        builder.setNegativeButton("COFNIJ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private Integer getDuration(Integer iHours, Integer iMins, Integer duration) {
        if (iHours == 1) {
            duration = 60 + iMins;
        } else if (iHours == 2) {
            duration = 120 + iMins;
        } else if (iHours == 3) {
            duration = 180 + iMins;
        } else if (iHours == 4) {
            duration = 240 + iMins;
        } else if (iHours == 5) {
            duration = 300 + iMins;
        } else if (iHours == 6) {
            duration = 360 + iMins;
        } else if (iHours == 7) {
            duration = 420 + iMins;
        } else if (iHours == 8) {
            duration = 480 + iMins;
        } else if (iHours == 9) {
            duration = 540 + iMins;
        } else if (iHours == 10) {
            duration = 600 + iMins;
        }
        return duration;
    }

    /**
     * TODO
     *
     * @return
     */
    public boolean validate(String distanceToSend, Integer iHours, Integer iMins) {
        boolean valid = true;

        if (distanceToSend.isEmpty() || Integer.parseInt(distanceToSend) == 0 || Integer.parseInt(distanceToSend) < 0) {
            valid = false;
        } else if (distanceToSend.isEmpty() || Integer.parseInt(distanceToSend) > 70) {
            valid = false;
        } else {
            distance.setError(null);
        }

        if (iHours > 10) {
            valid = false;
        } else if (iMins == 0 && iHours == 0) {
            valid = false;
        } else {
            mins.setError(null);
            hours.setError(null);
        }

        if (iMins > 60) {
            valid = false;
        } else if (iMins == 0 && iHours == 0) {
            valid = false;
        } else {
            mins.setError(null);
        }
        return valid;
    }

    public void inProgress(View view) {
        Toast.makeText(getBaseContext(), "Funkcjonalność w budowie!", Toast.LENGTH_LONG).show();
    }

    public void updateTraining(View view) {
        openUpdateDialog();
    }

    public void deleteTraining(View view) {
        openDeleteDialog();
    }

    public void showPosts(View view) {
        startActivity(new Intent(this, WelcomeActivity.class));
    }

    public void showProfile(View view) {
        startActivity(new Intent(this, ProfileActivity.class));
    }

    public void logout(MenuItem menu) {
        startActivity(new Intent(this, LoginActivity.class));
        Toast.makeText(getBaseContext(), "Wylogowanie powiodło się!", Toast.LENGTH_LONG).show();
    }

    /**
     * TODO
     *
     * @param view TODO
     */
    public void openCommentsT(View view) {
        startActivity(new Intent(this, TCommentActivity.class));
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
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
