package kamilzemczak.runny.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kamilzemczak.runny.R;
import kamilzemczak.runny.activity.activity_user.SearchUserFriendsActivity;

public class SearchUserFriendsAdapter extends ArrayAdapter<ArrayList<String>> {

    private SearchUserFriendsActivity searchUserFriendsActivity;

    public SearchUserFriendsAdapter(Context context, int resource, List<ArrayList<String>> usersList) {
        super(context, resource, usersList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            view = vi.inflate(R.layout.form_friends, null);
        }


        List<String> usersList = searchUserFriendsActivity.friendsAfterProcessingToSend.get(position);

        if (usersList != null) {
            TextView name = (TextView) view.findViewById(R.id.friendsLayout_tvName);
            TextView username = (TextView) view.findViewById(R.id.friendLayout_tvUsername);
            TextView location = (TextView) view.findViewById(R.id.friendsLayout_tvLocaton);

            if (name != null) {
                name.setText(usersList.get(0));
            }

            if (username != null) {
                username.setText(usersList.get(1));
            }

            if (location != null) {
                location.setText(usersList.get(2));
            }
        }
        return view;
    }

}