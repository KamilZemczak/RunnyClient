package kamilzemczak.runny.activity.activity_user;

import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.NavigationView;

import java.util.concurrent.ExecutionException;

import kamilzemczak.runny.R;
import kamilzemczak.runny.activity.activity_social.MessageActivity;
import kamilzemczak.runny.backgroundworker.FriendBackgroundWorker;
import kamilzemczak.runny.activity.activity_entry.LoginActivity;
import kamilzemczak.runny.activity.activity_menu.FriendsActivity;
import kamilzemczak.runny.activity.activity_menu.HistoryActivity;
import kamilzemczak.runny.activity.activity_menu.ObjectivesActivity;
import kamilzemczak.runny.activity.activity_menu.TrainingActivity;
import kamilzemczak.runny.activity.activity_menu.ProfileActivity;
import kamilzemczak.runny.activity.activity_menu.WelcomeActivity;

public class ViewFriendProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private LoginActivity loginActivity;
    private SearchUserFriendsActivity searchUserFriendsActivity;
    private SearchFriendsActivity searchFriendsActivity;

    private TextView user, usernameAge, location, about;
    private Button deleteFriendButton, sendMessageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_friend_profile);
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

        user = (TextView) findViewById(R.id.viewFriendProfileActivity_tvUser);
        usernameAge = (TextView) findViewById(R.id.viewFriendProfileActivity_tvAge);
        location = (TextView) findViewById(R.id.viewFriendProfileActivity_tvLocation);
        about = (TextView) findViewById(R.id.viewFriendProfileActivity_tvAboutInfo);
        sendMessageButton = (Button) findViewById(R.id.viewFriendProfileActivity_bSendMessage);
        deleteFriendButton = (Button) findViewById(R.id.viewFriendProfileActivity_bDeleteFriend);

        setCurrentUserInfo();
    }

    private void setCurrentUserInfo() {
        if ((searchUserFriendsActivity.friendCurrentUsername != null && (searchUserFriendsActivity.friendCurrentUsername != searchFriendsActivity.userCurrentWatchedUsername))) {
            user.setText(searchUserFriendsActivity.friendCurrentName + " " + searchUserFriendsActivity.friendCurrentSurname);
            usernameAge.setText(searchUserFriendsActivity.friendCurrentUsername + "," + " " + searchUserFriendsActivity.friendCurrentAge + " " + "lat.");
            if (searchUserFriendsActivity.friendCurrentCity != null) {
                location.setText(searchUserFriendsActivity.friendCurrentCity);
            } else {
                location.setText("Nie ustawiono lokalizacji.");
            }

            if (searchUserFriendsActivity.friendCurrentAbout != null) {
                about.setText(searchUserFriendsActivity.friendCurrentAbout);
            } else {
                about.setText("Nie ustawiono żadnych informacji o sobie.");
            }
        } else {
            user.setText(searchFriendsActivity.userCurrentWatchedName + " " + searchFriendsActivity.userCurrentWatchedSurname);
            usernameAge.setText(searchFriendsActivity.userCurrentWatchedUsername + "," + " " + searchFriendsActivity.userCurrentWatchedAge + " " + "lat.");
            if (searchFriendsActivity.userCurrentWatchedCity != null) {
                location.setText(searchFriendsActivity.userCurrentWatchedCity);
            } else {
                location.setText("Nie ustawiono lokalizacji.");
            }

            if (searchFriendsActivity.userCurrentWatchedAbout != null) {
                about.setText(searchFriendsActivity.userCurrentWatchedAbout);
            } else {
                about.setText("Nie ustawiono żadnych informacji o sobie.");
            }
        }
    }

    /**
     * TODO
     *
     * @param view TODO
     */
    public void deleteFriend(View view) {
        String username = loginActivity.userCurrentUsername;
        String friendUsername = searchUserFriendsActivity.friendCurrentUsername;
        String type = "friend_delete";
        String result = null;
        FriendBackgroundWorker friendBackgroundWorker = new FriendBackgroundWorker(this);
        try {
            result = friendBackgroundWorker.execute(type, username, friendUsername).get();
            addDeleteSuccess();
            openFriendMenu(view);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * TODO
     */
    public void addDeleteSuccess() {
        Toast.makeText(getBaseContext(), "Usunięto użytkownika ze znajomych.", Toast.LENGTH_LONG).show();
        deleteFriendButton.setEnabled(false);
        sendMessageButton.setEnabled(false);
        deleteFriendButton.setText("UŻYTKOWNIK ZOSTAŁ USUNIĘTY ZE ZNAJOMYCH.");
    }

    /**
     * TODO
     */
    public void openFriendMenu(View view) {
        startActivity(new Intent(this, SearchUserFriendsActivity.class));
    }

    /**
     * TODO
     */
    public void openMessageView(View view) {
        startActivity(new Intent(this, MessageActivity.class));
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
        getMenuInflater().inflate(R.menu.view_friend_profile, menu);
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
