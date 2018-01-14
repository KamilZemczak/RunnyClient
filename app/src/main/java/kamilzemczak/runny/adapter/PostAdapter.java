package kamilzemczak.runny.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import kamilzemczak.runny.R;
import kamilzemczak.runny.activity.activity_entry.LoginActivity;
import kamilzemczak.runny.model.Post;
import kamilzemczak.runny.activity.activity_menu.WelcomeActivity;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ContactViewHolder> {
    private WelcomeActivity welcomeActivity;
    private LoginActivity loginActivity;
    private List<Post> contactList;

    public PostAdapter(Activity context, List<Post> contactList) {
        this.contactList = contactList;
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        List<String> commentsSize = welcomeActivity.commentsSize;
        Calendar calendar = Calendar.getInstance();
        Post post = contactList.get(i);
        DateFormat dateFormat = new SimpleDateFormat("E, HH:mm (dd/MM/yyyy)", new Locale("pl", "pl_PL"));
        String finalDate = dateFormat.format(post.getTime());
        contactViewHolder.postContent.setText(post.getContents());
        contactViewHolder.surname.setText(post.getAuthor().getSurname());
        contactViewHolder.name.setText(post.getAuthor().getName());
        contactViewHolder.date.setText(finalDate);
        contactViewHolder.commentSize.setText("Komentarze" + " " + "(" + commentsSize.get(i) + ")");

        if (loginActivity.userCurrentId.equals(post.getAuthor().getId())) {
            contactViewHolder.updateButton.setVisibility(View.VISIBLE);
            contactViewHolder.deleteButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.form_post, viewGroup, false);
        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        protected TextView postContent;
        protected TextView name;
        protected TextView surname;
        protected TextView date;
        protected TextView commentSize;
        protected ImageView updateButton;
        protected ImageView deleteButton;

        public ContactViewHolder(View v) {
            super(v);
            postContent = (TextView) v.findViewById(R.id.postLayout_tvPostContent);
            surname = (TextView) v.findViewById(R.id.postLayout_tvSurname);
            name = (TextView) v.findViewById(R.id.postLayout_tvName);
            date = (TextView) v.findViewById(R.id.postLayout_tvDate);
            commentSize = (TextView) v.findViewById(R.id.postLayout_tvComment);
            updateButton = (ImageView) v.findViewById(R.id.postLayout_bUpdate);
            deleteButton = (ImageView) v.findViewById(R.id.postLayout_bDelete);
        }
    }
}