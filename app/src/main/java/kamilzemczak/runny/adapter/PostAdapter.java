package kamilzemczak.runny.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import kamilzemczak.runny.R;
import kamilzemczak.runny.model.Post;
import kamilzemczak.runny.activity.activity_menu.WelcomeActivity;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ContactViewHolder> {
    private WelcomeActivity welcomeActivity;
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
        Collections.reverse(commentsSize);
        Calendar calendar = Calendar.getInstance();
        Post post = contactList.get(i);
        contactViewHolder.postContent.setText(post.getContents());
        contactViewHolder.surname.setText(post.getAuthor().getSurname());
        contactViewHolder.name.setText(post.getAuthor().getName());
        contactViewHolder.date.setText(calendar.getTime().toString());
        if (commentsSize.isEmpty()) {
            contactViewHolder.commentSize.setText("Komentarze (0");
        } else {
            contactViewHolder.commentSize.setText("Komentarze" + " " + "(" + commentsSize.get(i) + ")");
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

        public ContactViewHolder(View v) {
            super(v);
            postContent = (TextView) v.findViewById(R.id.postLayout_tvPostContent);
            surname = (TextView) v.findViewById(R.id.postLayout_tvSurname);
            name = (TextView) v.findViewById(R.id.postLayout_tvName);
            date = (TextView) v.findViewById(R.id.postLayout_tvDate);
            commentSize = (TextView) v.findViewById(R.id.postLayout_tvComment);
        }
    }
}