package kamilzemczak.runny.activity.activity_menu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import kamilzemczak.runny.R;
import kamilzemczak.runny.activity.activity_entry.LoginActivity;
import kamilzemczak.runny.activity.activity_user.CommentActivity;
import kamilzemczak.runny.activity.activity_user.ViewFriendsTrainingsActivity;
import kamilzemczak.runny.adapter.PostAdapter;
import kamilzemczak.runny.backgroundworker.PostBackgroundWorker;
import kamilzemczak.runny.helper.RecyclerItemClickListener;
import kamilzemczak.runny.model.Post;


import android.support.v7.widget.RecyclerView;

import android.widget.Button;
import android.widget.EditText;


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


public class WelcomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    LoginActivity loginActivity;
    TextView welcome, info;
    DrawerLayout drawer;
    NavigationView navigationView;

    public static Integer currentPostId;

    private Button postButton;

    private TextView noPosts;

    private ImageView commentButton;
    private List<Post> list;
    private RecyclerView postContainer;
    private PostAdapter postAdapter;
    private EditText postContent;
    private Calendar calendar;
    private PostAdapter adapter;
    private List<Post> postHistory = new ArrayList<Post>();
    public static List<String> commentsSize = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
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

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        welcome = (TextView) findViewById(R.id.tvWelcome);
        info = (TextView) findViewById(R.id.tvInfo);
        postButton = (Button) findViewById(R.id.bPost);
        noPosts = (TextView) findViewById(R.id.tvNoPosts);


        welcome.setText("Witaj w Ready4RUN" + "\n" + loginActivity.currentName + " " + loginActivity.currentSurname + ".");
        info.setText("Mozesz Tutaj opublikowac post lub podejrzec posty swoich znajomych, do dziela!");
        loadHistory();

        calendar = Calendar.getInstance();

        postContainer.addOnItemTouchListener(
                new RecyclerItemClickListener(WelcomeActivity.this, postContainer ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        currentPostId = postHistory.get(position).getId();
                    }

                    @Override public void onLongItemClick(View view, int position) {
                    }
                })
        );
    }



    private void openDialog() {
        LayoutInflater inflater = LayoutInflater.from(WelcomeActivity.this);
        View subView = inflater.inflate(R.layout.post_dialog_layout, null);
        postContent = (EditText) subView.findViewById(R.id.dialogEditText);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Opublikuj post juz teraz!");
        builder.setMessage("Zobacza go wszyscy Twoi znajomi.");
        builder.setView(subView);

        AlertDialog alertDialog = builder.create();

        builder.setPositiveButton("OPUBLIKUJ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String postText = postContent.getText().toString();
                String type = "post_send";
                PostBackgroundWorker postBackgroundWorker = new PostBackgroundWorker(WelcomeActivity.this);
                postBackgroundWorker.execute(type, postText, loginActivity.currentUsername);
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


    /**
     * TODO
     *
     * @param view TODO
     */
    public void openPostDialog(View view) {
        openDialog();
    }

    /**
     * TODO
     *
     * @param view TODO
     */
     public void openComments(View view) {
        startActivity(new Intent(this, CommentActivity.class));
    }

    public void showFriendsTraining(View view) {
        startActivity(new Intent(this, ViewFriendsTrainingsActivity.class));
    }

    private void loadHistory() {
        String type = "posts_find";
        String result = null;
        PostBackgroundWorker postBackgroundWorker = new PostBackgroundWorker(this);

        try {
            String str_author_username = loginActivity.currentUsername;
            result = postBackgroundWorker.execute(type, str_author_username).get();
            ObjectMapper objectMapper = new ObjectMapper();
            postHistory = objectMapper.readValue(result, new TypeReference<List<Post>>() {
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
        Collections.reverse(postHistory);
        //Set the layout and the RecyclerView
        postContainer = (RecyclerView) findViewById(R.id.lPost);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        postContainer.setLayoutManager(llm);
        postAdapter = new PostAdapter(WelcomeActivity.this, postHistory);
        //Set the adapter for the recyclerlist
        postContainer.setAdapter(postAdapter);

        if(postHistory.isEmpty()) {
            noPosts.setText("Brak postow." + "\n" + "Twoj moze byc pierwszy!");
        }
    }

    private void loadCommentsSize() {
        String str_username = loginActivity.currentUsername;
        String type = "posts_comment_size";
        PostBackgroundWorker postBackgroundWorker = new PostBackgroundWorker(this);
        try {
            String result = postBackgroundWorker.execute(type, str_username).get();
            String replace = result.replace("[","");
            String replace1 = replace.replace("]","");
            commentsSize = new ArrayList<String>(Arrays.asList(replace1.split(",")));
            //String test = "test";
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
        getMenuInflater().inflate(R.menu.welcome, menu);
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
