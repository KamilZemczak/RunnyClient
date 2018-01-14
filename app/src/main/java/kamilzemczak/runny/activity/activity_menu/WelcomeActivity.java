package kamilzemczak.runny.activity.activity_menu;

import android.os.Bundle;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.support.design.widget.NavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

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
import kamilzemczak.runny.backgroundworker.RegisterBackgroundWorker;
import kamilzemczak.runny.model.Post;
import kamilzemczak.runny.backgroundworker.PostBackgroundWorker;
import kamilzemczak.runny.adapter.PostAdapter;
import kamilzemczak.runny.helper.RecyclerItemClickListener;
import kamilzemczak.runny.activity.activity_entry.LoginActivity;
import kamilzemczak.runny.activity.activity_social.CommentActivity;
import kamilzemczak.runny.activity.activity_user.ViewFriendsTrainingsActivity;

public class WelcomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private LoginActivity loginActivity;

    private TextView welcome, info, noPosts;
    private EditText postContent;
    private Button postButton;
    private ImageView editImage;
    private RecyclerView postContainer;
    private PostAdapter postAdapter;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Calendar calendar;

    private List<Post> postHistory = new ArrayList<Post>();
    public static List<String> commentsSize = new ArrayList<String>();

    public static String postCurrentContent;
    public static Integer postCurrentId;


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

        welcome = (TextView) findViewById(R.id.welcomeActivity_tvWelcomeInfo);
        info = (TextView) findViewById(R.id.welcomeActivity_tvInfo);
        noPosts = (TextView) findViewById(R.id.welcomeActivity_tvNoPostsFind);
        postButton = (Button) findViewById(R.id.welcomeActivity_bAddPost);
        calendar = Calendar.getInstance();

        welcome.setText("Witaj w Ready4RUN" + "\n" + loginActivity.userCurrentName + " " + loginActivity.userCurrentSurname + ".");
        info.setText("W tym miejscu możesz opublikować post lub po prostu przeglądać posty swoich znajomych. Do dzieła!");

        loadHistory();

        postContainer.addOnItemTouchListener(
                new RecyclerItemClickListener(WelcomeActivity.this, postContainer, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        postCurrentId = postHistory.get(position).getId();
                        postCurrentContent = postHistory.get(position).getContents();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                })
        );
    }

    private void openDialog() {
        LayoutInflater inflater = LayoutInflater.from(WelcomeActivity.this);
        View subView = inflater.inflate(R.layout.form_post_dialog, null);
        postContent = (EditText) subView.findViewById(R.id.dialogEditText);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Opublikuj post już teraz!");
        builder.setMessage("Zobaczą go wszyscy Twoi znajomi.");
        builder.setView(subView);

        AlertDialog alertDialog = builder.create();

        builder.setPositiveButton("OPUBLIKUJ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String type = "post_send";
                String postText = postContent.getText().toString();
                if (!validate(postText)) {
                    openDialog();
                    Toast.makeText(getBaseContext(), "Dodawanie postu nieudane.", Toast.LENGTH_LONG).show();
                    postContent.setError("Post musi zawierać minimum trzy znaki.");
                    postContent.setText(postText);
                } else {
                    PostBackgroundWorker postBackgroundWorker = new PostBackgroundWorker(WelcomeActivity.this);
                    postBackgroundWorker.execute(type, postText, loginActivity.userCurrentUsername);
                    finish();
                    startActivity(getIntent());
                    Toast.makeText(getBaseContext(), "Dodanie postu udane!.", Toast.LENGTH_LONG).show();
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

    private void openUpdateDialog() {
        LayoutInflater inflater = LayoutInflater.from(WelcomeActivity.this);
        View subView = inflater.inflate(R.layout.form_post_update_dialog, null);
        postContent = (EditText) subView.findViewById(R.id.dialogEditText);
        postContent.setText(postCurrentContent);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edytuj post już teraz!");
        builder.setView(subView);

        AlertDialog alertDialog = builder.create();

        builder.setPositiveButton("ZATWIERDŹ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String type = "post_update";
                String sPostCurrentId = String.valueOf(postCurrentId);
                String postText = postContent.getText().toString();
                if (!validate(postText)) {
                    openUpdateDialog();
                    Toast.makeText(getBaseContext(), "Edycja postu nieudana.", Toast.LENGTH_LONG).show();
                    postContent.setError("Post musi zawierać minimum trzy znaki.");
                    postContent.setText(postText);
                } else {
                    PostBackgroundWorker postBackgroundWorker = new PostBackgroundWorker(WelcomeActivity.this);
                    postBackgroundWorker.execute(type, sPostCurrentId, postText);
                    finish();
                    startActivity(getIntent());
                    Toast.makeText(getBaseContext(), "Edycja postu udana!", Toast.LENGTH_LONG).show();
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
        LayoutInflater inflater = LayoutInflater.from(WelcomeActivity.this);
        View subView = inflater.inflate(R.layout.form_post_delete_dialog, null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Usuń post już teraz!");
        builder.setMessage("Czy jesteś pewien?");
        builder.setView(subView);

        AlertDialog alertDialog = builder.create();

        builder.setPositiveButton("TAK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String type = "post_delete";
                String sPostCurrentId = String.valueOf(postCurrentId);
                PostBackgroundWorker postBackgroundWorker = new PostBackgroundWorker(WelcomeActivity.this);
                postBackgroundWorker.execute(type, sPostCurrentId);
                finish();
                startActivity(getIntent());
                Toast.makeText(getBaseContext(), "Post został usunięty.", Toast.LENGTH_LONG).show();
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
        String type = "posts_find";
        String result = null;
        PostBackgroundWorker postBackgroundWorker = new PostBackgroundWorker(this);
        try {
            String authorUsername = loginActivity.userCurrentUsername;
            result = postBackgroundWorker.execute(type, authorUsername).get();
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
        setPostHistoryToAdapter();

        if (postHistory.isEmpty()) {
            noPosts.setText("Brak postów." + "\n" + "Twój może być pierwszy!");
        }
    }

    private void setPostHistoryToAdapter() {
        Collections.reverse(postHistory);
        postContainer = (RecyclerView) findViewById(R.id.welcomeActivity_rvPosts);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        postContainer.setLayoutManager(llm);
        postAdapter = new PostAdapter(WelcomeActivity.this, postHistory);
        postContainer.setAdapter(postAdapter);
    }

    private void loadCommentsSize() {
        String username = loginActivity.userCurrentUsername;
        String type = "posts_comment_size";
        PostBackgroundWorker postBackgroundWorker = new PostBackgroundWorker(this);
        try {
            String result = postBackgroundWorker.execute(type, username).get();
            String resultToReplace = result.replace("[", "");
            String finalResult = resultToReplace.replace("]", "");
            commentsSize = new ArrayList<String>(Arrays.asList(finalResult.split(",")));
            Collections.reverse(commentsSize);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * TODO
     *
     * @return
     */
    public boolean validate(String postText) {
        boolean valid = true;
        if (postText.isEmpty() || postText.length() < 3) {
            valid = false;
        } else {
            postContent.setError(null);
        }
        return valid;
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

    public void inProgress(View view) {
        Toast.makeText(getBaseContext(), "Funkcjonalność w budowie!", Toast.LENGTH_LONG).show();
    }

    public void updatePost(View view) {
        openUpdateDialog();
    }

    public void deletePost(View view) {
        openDeleteDialog();
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
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
