package kamilzemczak.runny.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import kamilzemczak.runny.R;
import kamilzemczak.runny.activity.activity_menu.WelcomeActivity;
import kamilzemczak.runny.model.Post;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ContactViewHolder> {
    WelcomeActivity welcomeActivity;
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
        List<String> commentsSize2 = welcomeActivity.commentsSize;
        Collections.reverse(commentsSize2);
        Calendar calendar = Calendar.getInstance();
        Post ci = contactList.get(i);
        //Set the text of the feed with your data
        contactViewHolder.feedText.setText(ci.getContents());
        contactViewHolder.surNameText.setText(ci.getAuthor().getSurname());
        contactViewHolder.nameText.setText(ci.getAuthor().getName());
        contactViewHolder.feedDate.setText(calendar.getTime().toString());
        if(commentsSize2.isEmpty()) {
            contactViewHolder.commentText.setText("Komentarze (0");
        } else {
            contactViewHolder.commentText.setText("Komentarze" + " " + "(" + commentsSize2.get(i) + ")");
        }

    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.post_layout, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        protected TextView feedText;
        protected TextView nameText;
        protected TextView surNameText;
        protected TextView feedDate;
        protected TextView commentText;

        public ContactViewHolder(View v) {
            super(v);
            feedText = (TextView) v.findViewById(R.id.tvPostContent);
            surNameText = (TextView) v.findViewById(R.id.tvPostSurname);
            nameText = (TextView) v.findViewById(R.id.tvPostName);
            feedDate = (TextView) v.findViewById(R.id.tvPostDate);
            commentText = (TextView) v.findViewById(R.id.tvComment);
        }
    }
}