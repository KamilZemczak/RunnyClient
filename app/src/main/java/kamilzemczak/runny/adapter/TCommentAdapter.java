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
import kamilzemczak.runny.model.TComment;
import kamilzemczak.runny.activity.activity_entry.LoginActivity;

public class TCommentAdapter extends RecyclerView.Adapter<TCommentAdapter.ContactViewHolder> {
    private LoginActivity loginActivity;

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
        DateFormat dateFormat = new SimpleDateFormat("E, HH:mm (dd/MM/yyyy)", new Locale("pl", "pl_PL"));
        String finalDate = dateFormat.format(tComment.getTime());
        contactViewHolder.commentConent.setText(tComment.getContents());
        contactViewHolder.surname.setText(tComment.getAuthor().getSurname());
        contactViewHolder.name.setText(tComment.getAuthor().getName());
        contactViewHolder.date.setText(finalDate);

        if(loginActivity.userCurrentId.equals(tComment.getAuthor().getId())) {
            contactViewHolder.updateButton.setVisibility(View.VISIBLE);
            contactViewHolder.deleteButton.setVisibility(View.VISIBLE);
        }
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
        protected ImageView updateButton;
        protected ImageView deleteButton;

        public ContactViewHolder(View v) {
            super(v);
            commentConent = (TextView) v.findViewById(R.id.tCommentLayout_tvCommentContent);
            surname = (TextView) v.findViewById(R.id.tCommentLayout_tvSurname);
            name = (TextView) v.findViewById(R.id.tCommentLayout_tvName);
            date = (TextView) v.findViewById(R.id.tCommentLayout_tvDate);
            updateButton = (ImageView) v.findViewById(R.id.tCommentLayout_bUpdate);
            deleteButton = (ImageView) v.findViewById(R.id.tCommentLayout_bDelete);
        }
    }
}