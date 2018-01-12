package kamilzemczak.runny.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;

import kamilzemczak.runny.R;
import kamilzemczak.runny.model.Comment;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ContactViewHolder> {
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
        contactViewHolder.commentContent.setText(comment.getContents());
        contactViewHolder.surname.setText(comment.getAuthor().getSurname());
        contactViewHolder.name.setText(comment.getAuthor().getName());
        contactViewHolder.date.setText(calendar.getTime().toString());
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

        public ContactViewHolder(View v) {
            super(v);
            commentContent = (TextView) v.findViewById(R.id.commentLayout_tvContent);
            surname = (TextView) v.findViewById(R.id.commentLayout_tvSurname);
            name = (TextView) v.findViewById(R.id.commentLayout_tvName);
            date = (TextView) v.findViewById(R.id.commentLayout_tvDate);
        }
    }
}