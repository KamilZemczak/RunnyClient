package kamilzemczak.runny.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import kamilzemczak.runny.R;
import kamilzemczak.runny.model.Objective;

public class ObjectiveAdapter extends ArrayAdapter<Objective> {

    public ObjectiveAdapter(Context context, int resource, List<Objective> objectiveList) {
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
            TextView id = (TextView) view.findViewById(R.id.objectivesLayout_tvId);
            TextView failInfo = (TextView) view.findViewById(R.id.objectivesLayout_tvFailInfo);
            TextView goodInfo = (TextView) view.findViewById(R.id.objectivesLayout_tvGoodInfo);

            if (type != null) {
                type.setText("Rodzaj:" + " " + objective.getType());
            }

            if (objectiveo != null) {
                objectiveo.setText("Cel:" + " " + objective.getObjective());
            }

            if (date != null) {
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String finalDate = dateFormat.format(objective.getTime());
                date.setText("Data dodania celu:" + " " + finalDate);
            }

            if (objective.getExecuted().equals("Y")) {
                image.setImageResource(R.drawable.check);
                goodInfo.setText("CEL OSIĄGNIĘTY!");
                goodInfo.setVisibility(View.VISIBLE);
                failInfo.setVisibility(View.INVISIBLE);
            }

            if(objective.getExecuted().equals("N")) {
                image.setImageResource(R.drawable.cross);
                goodInfo.setVisibility(View.INVISIBLE);
                failInfo.setVisibility(View.VISIBLE);
                failInfo.setText("CEL NIEOSIĄGNIĘTY");
            }

            if(id!=null) {
                id.setText(objective.getId().toString());
            }
        }
        return view;
    }


}