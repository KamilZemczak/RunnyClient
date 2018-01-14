package kamilzemczak.runny.activity.activity_social;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import kamilzemczak.runny.R;
import kamilzemczak.runny.activity.activity_user.ViewFriendsTrainingsActivity;
import kamilzemczak.runny.helper.RecyclerItemClickListener;
import kamilzemczak.runny.model.TComment;
import kamilzemczak.runny.adapter.TCommentAdapter;
import kamilzemczak.runny.backgroundworker.TCommentBackgroundWorker;
import kamilzemczak.runny.activity.activity_entry.LoginActivity;
import kamilzemczak.runny.activity.activity_menu.FriendsActivity;
import kamilzemczak.runny.activity.activity_menu.HistoryActivity;
import kamilzemczak.runny.activity.activity_menu.ObjectivesActivity;
import kamilzemczak.runny.activity.activity_menu.ProfileActivity;
import kamilzemczak.runny.activity.activity_menu.TrainingActivity;
import kamilzemczak.runny.activity.activity_menu.WelcomeActivity;

public class TCommentActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private LoginActivity loginActivity;
    private WelcomeActivity welcomeActivity;
    private ViewFriendsTrainingsActivity viewFriendsTrainingsActivity;

    private TextView noCommentsFind;
    private EditText commentContent;
    private Button postCommentButton;
    private Calendar calendar;
    private RecyclerView commentContainer;
    private TCommentAdapter tCommentAdapter;

    private List<TComment> commentHistory = new ArrayList<TComment>();

    public static String tCommentCurrentContent;
    public static Integer tCommentCurrentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcomment);
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

        postCommentButton = (Button) findViewById(R.id.tCommentActivity_bPostComment);
        noCommentsFind = (TextView) findViewById(R.id.tCommentActivity_tvNoCommentsFind);
        calendar = Calendar.getInstance();

        loadHistory();

        commentContainer.addOnItemTouchListener(
                new RecyclerItemClickListener(TCommentActivity.this, commentContainer, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        tCommentCurrentId = commentHistory.get(position).getId();
                        tCommentCurrentContent = commentHistory.get(position).getContents();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                })
        );
    }

    private void openDialog() {
        LayoutInflater inflater = LayoutInflater.from(TCommentActivity.this);
        View subView = inflater.inflate(R.layout.form_tcomment_dialog, null);
        commentContent = (EditText) subView.findViewById(R.id.dialogEditTextT);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Opublikuj komentarz już teraz!");
        builder.setView(subView);

        AlertDialog alertDialog = builder.create();

        builder.setPositiveButton("OPUBLIKUJ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String commentText = commentContent.getText().toString();
                String type = "tcomment_send";
                TCommentBackgroundWorker tcommentBackgroundWorker = new TCommentBackgroundWorker(TCommentActivity.this);
                Integer test = ViewFriendsTrainingsActivity.trainingCurrentId;
                String test2 = String.valueOf(test);
                tcommentBackgroundWorker.execute(type, commentText, loginActivity.userCurrentUsername, test2);
                finish();
                startActivity(getIntent());

            }
        });

        builder.setNegativeButton("ANULUJ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void openUpdateDialog() {
        LayoutInflater inflater = LayoutInflater.from(TCommentActivity.this);
        View subView = inflater.inflate(R.layout.form_tcomment_update_dialog, null);
        commentContent = (EditText) subView.findViewById(R.id.dialogEditText);
        commentContent.setText(tCommentCurrentContent);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edytuj komentarz już teraz!");
        builder.setView(subView);

        AlertDialog alertDialog = builder.create();

        builder.setPositiveButton("ZATWIERDŹ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String type = "tcomment_update";
                String sTCommentCurrentId = String.valueOf(tCommentCurrentId);
                String commentText = commentContent.getText().toString();
                if (!validate(commentText)) {
                    openUpdateDialog();
                    Toast.makeText(getBaseContext(), "Edycja komentarzu nieudana.", Toast.LENGTH_LONG).show();
                    commentContent.setError("Komentarz musi zawierać minimum dwa znaki.");
                    commentContent.setText(commentText);
                } else {
                    TCommentBackgroundWorker tCommentBackgroundWorker = new TCommentBackgroundWorker(TCommentActivity.this);
                    tCommentBackgroundWorker.execute(type, sTCommentCurrentId, commentText);
                    finish();
                    startActivity(getIntent());
                    Toast.makeText(getBaseContext(), "Edycja komentarza udana!", Toast.LENGTH_LONG).show();
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
        LayoutInflater inflater = LayoutInflater.from(TCommentActivity.this);
        View subView = inflater.inflate(R.layout.form_tcomment_delete_dialog, null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Usuń komentarz już teraz!");
        builder.setMessage("Czy jesteś pewien?");
        builder.setView(subView);

        AlertDialog alertDialog = builder.create();

        builder.setPositiveButton("TAK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String type = "tcomment_delete";
                String sTCommentCurrentId = String.valueOf(tCommentCurrentId);
                TCommentBackgroundWorker tCommentBackgroundWorker = new TCommentBackgroundWorker(TCommentActivity.this);
                tCommentBackgroundWorker.execute(type, sTCommentCurrentId);
                finish();
                startActivity(getIntent());
                Toast.makeText(getBaseContext(), "Komentarz został usunięty.", Toast.LENGTH_LONG).show();
            }
        });

        builder.setNegativeButton("COFNIJ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void loadHistory() {
        String type = "tcomments_find";
        String result = null;
        TCommentBackgroundWorker tCommentBackgroundWorker = new TCommentBackgroundWorker(this);

        try {
            Integer iCurrentTrainingId = viewFriendsTrainingsActivity.trainingCurrentId;
            String sCurrentTrainingId = String.valueOf(iCurrentTrainingId);
            result = tCommentBackgroundWorker.execute(type, sCurrentTrainingId).get();
            ObjectMapper objectMapper = new ObjectMapper();
            commentHistory = objectMapper.readValue(result, new TypeReference<List<TComment>>() {
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

        Collections.reverse(commentHistory);

        setCommentHistoryToAdapter();

        if (commentHistory.isEmpty()) {
            noCommentsFind.setText("Brak komentarzy." + "\n" + "Twój może byc pierwszy!");
        }
    }

    private void setCommentHistoryToAdapter() {
        commentContainer = (RecyclerView) findViewById(R.id.tCommentActivity_rvComments);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        commentContainer.setLayoutManager(llm);
        tCommentAdapter = new TCommentAdapter(TCommentActivity.this, commentHistory);
        commentContainer.setAdapter(tCommentAdapter);
    }

    /**
     * TODO
     *
     * @return
     */
    public boolean validate(String commentText) {
        boolean valid = true;
        if (commentText.isEmpty() || commentText.length() < 2) {
            valid = false;
        } else {
            commentContent.setError(null);
        }
        return valid;
    }

    /**
     * TODO
     *
     * @param view TODO FIXME
     */
    public void openPostDialog(View view) {
        openDialog();
    }

    /**
     * TODO FIXME
     *
     * @param view TODO
     */
    public void openCommentDialogT(View view) {
        openDialog();
    }

    public void updateTComment(View view) {
        openUpdateDialog();
    }

    public void deleteTComment(View view) {
        openDeleteDialog();
    }

    public void showTrainings(View view) {
        startActivity(new Intent(this, ViewFriendsTrainingsActivity.class));
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
        getMenuInflater().inflate(R.menu.tcomment, menu);
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
