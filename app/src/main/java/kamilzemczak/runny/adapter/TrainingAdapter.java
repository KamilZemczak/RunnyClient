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
import kamilzemczak.runny.activity.activity_user.ViewFriendsTrainingsActivity;
import kamilzemczak.runny.model.Training;

public class TrainingAdapter extends RecyclerView.Adapter<TrainingAdapter.ContactViewHolder> {
    private ViewFriendsTrainingsActivity viewFriendsTrainingsActivity;
    private LoginActivity loginActivity;

    private List<Training> trainingList;

    public TrainingAdapter(Activity context, List<Training> trainingList) {
        this.trainingList = trainingList;
    }

    @Override
    public int getItemCount() {
        return trainingList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        List<String> commentsSize2 = viewFriendsTrainingsActivity.trainingsSize;

        Calendar calendar = Calendar.getInstance();
        Training training = trainingList.get(i);
        DateFormat dateFormat = new SimpleDateFormat("E, HH:mm (dd/MM/yyyy)", new Locale("pl", "pl_PL"));
        String finalDate = dateFormat.format(training.getTime());
        contactViewHolder.trainingContent.setText(training.getContents());
        contactViewHolder.surname.setText(training.getAuthor().getSurname());
        contactViewHolder.name.setText(training.getAuthor().getName());
        contactViewHolder.date.setText(finalDate);
        contactViewHolder.distance.setText(training.getDistance().toString() + " " + "KM");
        contactViewHolder.duration.setText("1G:20M");
        contactViewHolder.calories.setText(training.getCalories().toString() + " " + "KCAL");
        contactViewHolder.commentSize.setText("Komentarze" + " " + "(" + commentsSize2.get(i) + ")");


        if (loginActivity.userCurrentId.equals(training.getAuthor().getId())) {
            contactViewHolder.updateButton.setVisibility(View.VISIBLE);
            contactViewHolder.deleteButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.form_training_public, viewGroup, false);
        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        protected TextView trainingContent;
        protected TextView name;
        protected TextView surname;
        protected TextView date;
        protected TextView distance;
        protected TextView duration;
        protected TextView calories;
        protected TextView commentSize;
        protected ImageView updateButton;
        protected ImageView deleteButton;

        public ContactViewHolder(View v) {
            super(v);
            trainingContent = (TextView) v.findViewById(R.id.trainingPublicLayout_tvTrainingContent);
            surname = (TextView) v.findViewById(R.id.trainingPublicLayout_tvSurname);
            name = (TextView) v.findViewById(R.id.trainingPublicLayout_tvName);
            date = (TextView) v.findViewById(R.id.trainingPublicLayout_tvDate);
            distance = (TextView) v.findViewById(R.id.trainingPublicLayout_tvDistance);
            duration = (TextView) v.findViewById(R.id.trainingPublicLayout_tvDuration);
            calories = (TextView) v.findViewById(R.id.trainingPublicLayout_tvCalories);
            commentSize = (TextView) v.findViewById(R.id.trainingPublicLayout_tvComment);
            updateButton = (ImageView) v.findViewById(R.id.trainingPublicLayout_bUpdate);
            deleteButton = (ImageView) v.findViewById(R.id.trainingPublicLayout_bDelete);
        }
    }
}