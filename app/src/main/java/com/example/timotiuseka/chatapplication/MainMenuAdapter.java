package com.example.timotiuseka.chatapplication;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Timotius Eka on 1/13/2017.
 */

public class MainMenuAdapter extends ArrayAdapter<SignedInUser> {
    public MainMenuAdapter(Context context, ArrayList<SignedInUser> userList) {
        super(context, 0, userList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        SignedInUser signedInUser = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.last_login, parent, false);
        }
        // Lookup view for data population
        TextView tvUser = (TextView) convertView.findViewById(R.id.user_email);
        TextView tvDate= (TextView) convertView.findViewById(R.id.user_last_login);
        TextView tvText= (TextView) convertView.findViewById(R.id.user_message);
        // Populate the data into the template view using the data object
        tvUser.setText(signedInUser.getUserEmail());
        tvDate.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                signedInUser.getUserLastLogInTime()));
        tvText.setText("");
        // Return the completed view to render on screen
        return convertView;
    }
}
