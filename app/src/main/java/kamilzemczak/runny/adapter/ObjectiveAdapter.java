package kamilzemczak.runny.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import kamilzemczak.runny.R;
import kamilzemczak.runny.model.Objective;

/**
 * Created by Kamil Zemczak on 07.01.2018.
 */

public class ObjectiveAdapter extends ArrayAdapter<Objective> {

    public ObjectiveAdapter (Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ObjectiveAdapter (Context context, int resource, List<Objective> objectiveList) {
        super(context, resource, objectiveList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.objectives_check_item_layout, null);
        }


        Objective objective = getItem(position);

        if (objective != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.tvTypeO);
            TextView tt2 = (TextView) v.findViewById(R.id.tvObjectiveO);
            TextView tt3 = (TextView) v.findViewById(R.id.tvDateO);
            ImageView im4 = (ImageView) v.findViewById(R.id.bCheckC);
            TextView tt4 = (TextView) v.findViewById(R.id.tvGratsO);

            if (tt1 != null) {
                tt1.setText("Rodzaj:" + " " + objective.getType());
            }

            if (tt2 != null) {
                tt2.setText("Cel:" + " " + objective.getObjective());
            }

            if (tt3 != null) {
                tt3.setText("Data dodania celu: 07/01/2018");
            }

            if(objective.getExecuted().equals("Y")) {
                im4.setImageResource(R.drawable.check);
                tt4.setText("CEL OSIAGNIETY :)");
            } else {
                im4.setImageResource(R.drawable.cross3);

            }



        }

        return v;
    }


}