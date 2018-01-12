package kamilzemczak.runny.activity.activity_social;

import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.text.TextUtils;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.NavigationView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import kamilzemczak.runny.R;
import kamilzemczak.runny.activity.activity_user.SearchUserFriendsActivity;
import kamilzemczak.runny.activity.activity_user.ViewFriendProfileActivity;
import kamilzemczak.runny.model.Message;
import kamilzemczak.runny.adapter.MessageAdapter;
import kamilzemczak.runny.backgroundworker.MessageBackgroundWorker;
import kamilzemczak.runny.activity.activity_entry.LoginActivity;
import kamilzemczak.runny.activity.activity_menu.FriendsActivity;
import kamilzemczak.runny.activity.activity_menu.HistoryActivity;
import kamilzemczak.runny.activity.activity_menu.ObjectivesActivity;
import kamilzemczak.runny.activity.activity_menu.ProfileActivity;
import kamilzemczak.runny.activity.activity_menu.TrainingActivity;
import kamilzemczak.runny.activity.activity_menu.WelcomeActivity;

public class MessageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private LoginActivity loginActivity;
    private SearchUserFriendsActivity searchUserFriendsActivity;

    private EditText message;
    private ListView messagesContainer;
    private ImageButton sendButton;
    private MessageAdapter adapter;

    private List<Message> chatHistory = new ArrayList<Message>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
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

        initControls();
    }

    private void initControls() {
        messagesContainer = (ListView) findViewById(R.id.messageActivity_lAllMessages);
        message = (EditText) findViewById(R.id.messageActivity_etMessage);
        sendButton = (ImageButton) findViewById(R.id.messageActivity_bSendMessage);

        TextView authorUsername = (TextView) findViewById(R.id.messageActivity_tvAuthorUsername);
        TextView recipientUsername = (TextView) findViewById(R.id.messageActivity_tvRecipientUsername);
        RelativeLayout container = (RelativeLayout) findViewById(R.id.messageActivity_container);
        authorUsername.setText(loginActivity.userCurrentUsername);
        recipientUsername.setText(searchUserFriendsActivity.friendCurrentUsername);

        loadHistory();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = message.getText().toString();
                if (TextUtils.isEmpty(messageText)) {
                    return;
                }

                String type = "message_send";
                MessageBackgroundWorker messageBackgroundWorker = new MessageBackgroundWorker(MessageActivity.this);
                messageBackgroundWorker.execute(type, messageText, loginActivity.userCurrentUsername, searchUserFriendsActivity.friendCurrentUsername);

                Message chatMessage = addStaticMessage(messageText);
                displayMessage(chatMessage);
            }

            @NonNull
            private Message addStaticMessage(String messageText) {
                Message chatMessage = new Message();
                chatMessage.setId(122);
                chatMessage.setContents(messageText);
                String dateTime = DateFormat.getDateTimeInstance().format(new Date()); //FIXME
                chatMessage.setDateTime(DateFormat.getDateTimeInstance().format(new Date()));
                chatMessage.setMe(true);
                message.setText("");
                return chatMessage;
            }
        });
    }


    public void displayMessage(Message message) {
        adapter.add(message);
        adapter.notifyDataSetChanged();
        scroll();
    }

    private void scroll() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }

    private void loadHistory() {
        String type = "messages_find";
        String result = null;
        MessageBackgroundWorker messageBackgroundWorker = new MessageBackgroundWorker(this);

        try {
            String author_username = loginActivity.userCurrentUsername;
            String recipient_username = searchUserFriendsActivity.friendCurrentUsername;
            result = messageBackgroundWorker.execute(type, author_username, recipient_username).get();
            ObjectMapper objectMapper = new ObjectMapper();
            chatHistory = objectMapper.readValue(result, new TypeReference<List<Message>>() {
            });
            processMessageList();
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

        adapter = new MessageAdapter(MessageActivity.this, new ArrayList<Message>());
        messagesContainer.setAdapter(adapter);

        for (int i = 0; i < chatHistory.size(); i++) {
            Message message = chatHistory.get(i);
            displayMessage(message);
        }

    }

    private void processMessageList() {
        for (Message message : chatHistory) {
            if (message.getAuthor().getId().equals(loginActivity.userCurrentId)) {
                message.setMe(true);
            } else {
                message.setMe(false);
            }
            DateFormat date = new SimpleDateFormat("E, HH:mm:ss (dd/MM/yyyy)");
            String finalDate = date.format(message.getTime());
            message.setDateTime(finalDate);
        }
    }

    /**
     * TODO
     *
     * @param view TODO
     */
    public void showAuthorProfile(View view) {
        startActivity(new Intent(this, ProfileActivity.class));
    }

    /**
     * TODO
     *
     * @param view TODO
     */
    public void showRecipientProfile(View view) {
        startActivity(new Intent(this, ViewFriendProfileActivity.class));
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
        getMenuInflater().inflate(R.menu.message, menu);
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
