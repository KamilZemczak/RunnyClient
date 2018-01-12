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
import kamilzemczak.runny.model.Training;

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
        Training training = trainingList.get(i);
        contactViewHolder.trainingContent.setText(training.getContents());
        contactViewHolder.surname.setText(training.getAuthor().getSurname());
        contactViewHolder.text.setText(training.getAuthor().getName());
        contactViewHolder.date.setText(calendar.getTime().toString());
        contactViewHolder.distance.setText(training.getDistance().toString() + " " + "KM");
        contactViewHolder.duration.setText("1G:20M");
        contactViewHolder.calories.setText(training.getCalories().toString() + " " + "KCAL");
        contactViewHolder.notes.setText(training.getNotes().toString());
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.form_training_own, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        protected TextView trainingContent;
        protected TextView text;
        protected TextView surname;
        protected TextView date;
        protected TextView distance;
        protected TextView duration;
        protected TextView calories;
        protected TextView notes;

        public ContactViewHolder(View v) {
            super(v);
            trainingContent = (TextView) v.findViewById(R.id.trainingOwnLayout_tvTrainingContent);
            surname = (TextView) v.findViewById(R.id.trainingOwnLayout_tvSurname);
            text = (TextView) v.findViewById(R.id.trainingOwnLayout_tvName);
            date = (TextView) v.findViewById(R.id.trainingOwnLayout_tvDate);
            distance = (TextView) v.findViewById(R.id.trainingOwnLayout_tvDistance);
            duration = (TextView) v.findViewById(R.id.trainingOwnLayout_tvDuration);
            calories = (TextView) v.findViewById(R.id.trainingOwnLayout_tvCalories);
            notes = (TextView) v.findViewById(R.id.trainingOwnLayout_tvNote);
        }
    }
}