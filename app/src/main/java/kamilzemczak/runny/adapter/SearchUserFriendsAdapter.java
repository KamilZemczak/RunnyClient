package kamilzemczak.runny.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kamilzemczak.runny.R;
import kamilzemczak.runny.activity.activity_user.SearchFriendsActivity;
import kamilzemczak.runny.activity.activity_user.SearchUserFriendsActivity;

/**
 * Created by Kamil Zemczak on 06.01.2018.
 */

public class SearchUserFriendsAdapter extends ArrayAdapter<ArrayList<String>> {

    SearchUserFriendsActivity searchUserFriendsActivity;

    public SearchUserFriendsAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public SearchUserFriendsAdapter(Context context, int resource, List<ArrayList<String>> usersList) {
        super(context, resource, usersList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.friends_item_layout, null);
        }


        List<String> usersList = searchUserFriendsActivity.friendsAfterProcessingToSend.get(position);

        if (usersList != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.tvNameF);
            TextView tt2 = (TextView) v.findViewById(R.id.tvUsernameF);
            TextView tt3 = (TextView) v.findViewById(R.id.tvLocatonF);

            if (tt1 != null) {
                tt1.setText(usersList.get(0));
            }

            if (tt2 != null) {
                tt2.setText(usersList.get(1));
            }

            if (tt3 != null) {
                tt3.setText(usersList.get(2));
            }
        }

        return v;
    }

}