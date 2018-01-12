package kamilzemczak.runny.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import kamilzemczak.runny.R;
import kamilzemczak.runny.model.Objective;

public class ObjectiveAdapter extends ArrayAdapter<Objective> {

    public ObjectiveAdapter (Context context, int resource, List<Objective> objectiveList) {
        super(context, resource, objectiveList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            view = vi.inflate(R.layout.form_objectives, null);
        }

        Objective objective = getItem(position);

        if (objective != null) {
            TextView type = (TextView) view.findViewById(R.id.objectivesLayout_tvType);
            TextView objectiveo = (TextView) view.findViewById(R.id.objectivesLayout_tvObjective);
            TextView date = (TextView) view.findViewById(R.id.objectivesLayout_tvDate);
            ImageView image = (ImageView) view.findViewById(R.id.objectivesLayout_image);
            TextView info = (TextView) view.findViewById(R.id.objectivesLayout_tvInfo);

            if (type != null) {
                type.setText("Rodzaj:" + " " + objective.getType());
            }

            if (objectiveo != null) {
                objectiveo.setText("Cel:" + " " + objective.getObjective());
            }

            if (date != null) {
                date.setText("Data dodania celu: 07/01/2018");
            }

            if(objective.getExecuted().equals("Y")) {
                image.setImageResource(R.drawable.check);
                info.setText("CEL OSIAGNIETY :)");
            } else {
                image.setImageResource(R.drawable.cross);

            }
        }
        return view;
    }


}