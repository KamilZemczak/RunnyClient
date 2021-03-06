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
import java.util.List;
import java.util.Locale;

import kamilzemczak.runny.R;
import kamilzemczak.runny.activity.activity_entry.LoginActivity;
import kamilzemczak.runny.model.Comment;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ContactViewHolder> {
    private LoginActivity loginActivity;

    private List<Comment> commentList;

    public CommentAdapter(Activity context, List<Comment> commentList) {
        this.commentList = commentList;
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        Calendar calendar = Calendar.getInstance();
        Comment comment = commentList.get(i);
        DateFormat dateFormat = new SimpleDateFormat("E, HH:mm (dd/MM/yyyy)", new Locale("pl", "pl_PL"));
        String finalDate = dateFormat.format(comment.getTime());
        contactViewHolder.commentContent.setText(comment.getContents());
        contactViewHolder.surname.setText(comment.getAuthor().getSurname());
        contactViewHolder.name.setText(comment.getAuthor().getName());
        contactViewHolder.date.setText(finalDate);

        if(loginActivity.userCurrentId.equals(comment.getAuthor().getId())) {
            contactViewHolder.updateButton.setVisibility(View.VISIBLE);
            contactViewHolder.deleteButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.form_comment, viewGroup, false);
        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        protected TextView commentContent;
        protected TextView name;
        protected TextView surname;
        protected TextView date;
        protected ImageView updateButton;
        protected ImageView deleteButton;

        public ContactViewHolder(View v) {
            super(v);
            commentContent = (TextView) v.findViewById(R.id.commentLayout_tvContent);
            surname = (TextView) v.findViewById(R.id.commentLayout_tvSurname);
            name = (TextView) v.findViewById(R.id.commentLayout_tvName);
            date = (TextView) v.findViewById(R.id.commentLayout_tvDate);
            updateButton = (ImageView) v.findViewById(R.id.commentLayout_bUpdate);
            deleteButton = (ImageView) v.findViewById(R.id.commentLayout_bDelete);
        }
    }
}