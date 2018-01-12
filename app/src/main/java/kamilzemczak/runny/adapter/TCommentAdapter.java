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
import kamilzemczak.runny.model.TComment;

public class TCommentAdapter extends RecyclerView.Adapter<TCommentAdapter.ContactViewHolder> {
    private List<TComment> tCommentList;

    public TCommentAdapter(Activity context, List<TComment> tCommentList) {
        this.tCommentList = tCommentList;
    }

    @Override
    public int getItemCount() {
        return tCommentList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        Calendar calendar = Calendar.getInstance();
        TComment tComment = tCommentList.get(i);
        contactViewHolder.commentConent.setText(tComment.getContents());
        contactViewHolder.surname.setText(tComment.getAuthor().getSurname());
        contactViewHolder.name.setText(tComment.getAuthor().getName());
        contactViewHolder.date.setText(calendar.getTime().toString());
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.form_tcomment, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        protected TextView commentConent;
        protected TextView name;
        protected TextView surname;
        protected TextView date;

        public ContactViewHolder(View v) {
            super(v);
            commentConent = (TextView) v.findViewById(R.id.tCommentLayout_tvCommentContent);
            surname = (TextView) v.findViewById(R.id.tCommentLayout_tvSurname);
            name = (TextView) v.findViewById(R.id.tCommentLayout_tvName);
            date = (TextView) v.findViewById(R.id.tCommentLayout_tvDate);
        }
    }
}