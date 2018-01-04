package kamilzemczak.runny.activity.activity_user;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import kamilzemczak.runny.R;
import kamilzemczak.runny.activity.activity_entry.LoginActivity;
import kamilzemczak.runny.activity.activity_menu.WelcomeActivity;
import kamilzemczak.runny.adapter.CommentAdapter;
import kamilzemczak.runny.backgroundworker.CommentBackgroundWorker;
import kamilzemczak.runny.model.Comment;
import kamilzemczak.runny.model.Post;

public class CommentActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    LoginActivity loginActivity;
    WelcomeActivity welcomeActivity;
    DrawerLayout drawer;
    NavigationView navigationView;

    private TextView noComments;

    private EditText commentContent;
    private Calendar calendar;

    private Button postCommentButton;
    private RecyclerView commentContainer;
    private CommentAdapter commentAdapter;
    private List<Comment> commentHistory = new ArrayList<Comment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
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

        postCommentButton = (Button) findViewById(R.id.bPostComment);
        noComments = (TextView) findViewById(R.id.tvNoComments);
        calendar = Calendar.getInstance();

        loadHistory();

    }

    private void openDialog() {
        LayoutInflater inflater = LayoutInflater.from(CommentActivity.this);
        View subView = inflater.inflate(R.layout.comment_dialog_layout, null);
        commentContent = (EditText) subView.findViewById(R.id.dialogEditText);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Opublikuj komentarz juz teraz!");
        //builder.setMessage("Zobacza go wszyscy Twoi znajomi.");
        builder.setView(subView);

        AlertDialog alertDialog = builder.create();

        builder.setPositiveButton("OPUBLIKUJ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String commentText = commentContent.getText().toString();
                String type = "comment_send";
                CommentBackgroundWorker commentBackgroundWorker = new CommentBackgroundWorker(CommentActivity.this);
                Integer test = welcomeActivity.currentPostId;
                String test2 = String.valueOf(test);
                commentBackgroundWorker.execute(type, commentText, loginActivity.currentUsername, test2);
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
    public void openCommentDialog(View view) {
        openDialog();
    }


    private void loadHistory() {
        String type = "comments_find";
        String result = null;
        CommentBackgroundWorker commentBackgroundWorker = new CommentBackgroundWorker(this);

        try {
            Integer test = welcomeActivity.currentPostId;
            String test2 = String.valueOf(test);
            result = commentBackgroundWorker.execute(type, test2).get();
            ObjectMapper objectMapper = new ObjectMapper();
            commentHistory = objectMapper.readValue(result, new TypeReference<List<Comment>>() {
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

        //Set the layout and the RecyclerView
        commentContainer = (RecyclerView) findViewById(R.id.lComment);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        commentContainer.setLayoutManager(llm);
        commentAdapter = new CommentAdapter(CommentActivity.this, commentHistory);
        //Set the adapter for the recyclerlist
        commentContainer.setAdapter(commentAdapter);

        if(commentHistory.isEmpty()) {
            noComments.setText("Brak komentarzy." + "\n" +  "Twoj moze byc pierwszy!");
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
        getMenuInflater().inflate(R.menu.comment, menu);
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
