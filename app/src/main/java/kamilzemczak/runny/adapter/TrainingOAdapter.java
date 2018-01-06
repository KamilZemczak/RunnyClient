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
import kamilzemczak.runny.model.Training;

/**
 * Created by Kamil Zemczak on 05.01.2018.
 */

public class TrainingOAdapter extends RecyclerView.Adapter<TrainingOAdapter.ContactViewHolder> {

    private List<Training> trainingList;

    public TrainingOAdapter(Activity context, List<Training> trainingList) {
        this.trainingList = trainingList;
    }

    @Override
    public int getItemCount() {
        return trainingList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
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
        contactViewHolder.notesText.setText(ci.getNotes().toString());
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.training_own_layout, viewGroup, false);

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
        protected TextView notesText;

        public ContactViewHolder(View v) {
            super(v);
            feedText = (TextView) v.findViewById(R.id.tvTrainingContent);
            surNameText = (TextView) v.findViewById(R.id.tvTrainingSurname);
            nameText = (TextView) v.findViewById(R.id.tvTrainingName);
            feedDate = (TextView) v.findViewById(R.id.tvTrainingDate);
            distanceText = (TextView) v.findViewById(R.id.tvDistance);
            durationText = (TextView) v.findViewById(R.id.tvDurationO);
            caloriesText = (TextView) v.findViewById(R.id.tvCalories);
            notesText = (TextView) v.findViewById(R.id.tvNoteO);
        }
    }
}