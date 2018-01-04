package kamilzemczak.runny.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import kamilzemczak.runny.R;
import kamilzemczak.runny.model.Comment;

/**
 * Created by Kamil Zemczak on 03.01.2018.
 */

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
        Comment ci = commentList.get(i);
        //Set the text of the feed with your data
        contactViewHolder.feedText2.setText(ci.getContents());
        contactViewHolder.surNameText2.setText(ci.getAuthor().getSurname());
        contactViewHolder.nameText2.setText(ci.getAuthor().getName());
        contactViewHolder.feedDate2.setText(calendar.getTime().toString());
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.comment_layout, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        protected TextView feedText2;
        protected TextView nameText2;
        protected TextView surNameText2;
        protected TextView feedDate2;

        public ContactViewHolder(View v) {
            super(v);
            feedText2 = (TextView) v.findViewById(R.id.tvPostContentC);
            surNameText2 = (TextView) v.findViewById(R.id.tvPostSurnameC);
            nameText2 = (TextView) v.findViewById(R.id.tvPostNameC);
            feedDate2 = (TextView) v.findViewById(R.id.tvPostDateC);
        }
    }
}