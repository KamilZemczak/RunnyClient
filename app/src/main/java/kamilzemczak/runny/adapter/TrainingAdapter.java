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
import kamilzemczak.runny.activity.activity_user.ViewFriendsTrainingsActivity;
import kamilzemczak.runny.model.Training;

/**
 * Created by Kamil Zemczak on 05.01.2018.
 */

public class TrainingAdapter extends RecyclerView.Adapter<TrainingAdapter.ContactViewHolder> {
    ViewFriendsTrainingsActivity viewFriendsTrainingsActivity;
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
        Collections.reverse(commentsSize2);

        Calendar calendar = Calendar.getInstance();
        Training ci = trainingList.get(i);
        //Set the text of the feed with your data
        contactViewHolder.feedText.setText(ci.getContents());
        contactViewHolder.surNameText.setText(ci.getAuthor().getSurname());
        contactViewHolder.nameText.setText(ci.getAuthor().getName());
        contactViewHolder.feedDate.setText(calendar.getTime().toString());
        contactViewHolder.distanceText.setText(ci.getDistance().toString() + " " + "KM");
        contactViewHolder.durationText.setText("1G:20M");
        contactViewHolder.caloriesText.setText(ci.getCalories().toString() + " " + "KCAL");
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
                inflate(R.layout.training_public_layout, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        protected TextView feedText;
        protected TextView nameText;
        protected TextView surNameText;
        protected TextView feedDate;
        protected TextView distanceText;
        protected TextView durationText;
        protected TextView caloriesText;
        protected TextView commentText;

        public ContactViewHolder(View v) {
            super(v);
            feedText = (TextView) v.findViewById(R.id.tvTrainingContent);
            surNameText = (TextView) v.findViewById(R.id.tvTrainingSurname);
            nameText = (TextView) v.findViewById(R.id.tvTrainingName);
            feedDate = (TextView) v.findViewById(R.id.tvTrainingDate);
            distanceText = (TextView) v.findViewById(R.id.tvDistance);
            durationText = (TextView) v.findViewById(R.id.tvDuration);
            caloriesText = (TextView) v.findViewById(R.id.tvCalories);
            commentText = (TextView) v.findViewById(R.id.tvCommentT);
        }
    }
}