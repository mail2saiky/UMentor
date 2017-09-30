package parimi.com.umentor.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import parimi.com.umentor.R;
import parimi.com.umentor.models.User;
import parimi.com.umentor.views.activity.MainActivity;
import parimi.com.umentor.views.fragment.ProfileFragment;
import parimi.com.umentor.views.fragment.SendMessageFragment;

/**
 * Created by nandpa on 9/27/17.
 */

public class MentorListAdapter extends BaseAdapter {

    List<User> mentors = new ArrayList<>();
    Context context;
    public MentorListAdapter(List<User> mentors, Context context) {
        this.mentors = mentors;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mentors.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View listView;
        final ImageView sendMessageImage;
        final TextView experienceText;
        final TextView nameText;
        if (convertView == null) {
            listView = inflater.inflate(R.layout.mentors_list_item, null);
            nameText = (TextView) listView.findViewById(R.id.name);
            experienceText = (TextView) listView.findViewById(R.id.experience);
            sendMessageImage = (ImageView) listView.findViewById(R.id.message_img);
            nameText.setText(mentors.get(i).getName().toString());
            experienceText.setText(String.valueOf(mentors.get(i).getExperience()));


            sendMessageImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment fragment = new SendMessageFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user", mentors.get(i));
                    fragment.setArguments(bundle);
                    ((MainActivity)context).insertFragment(fragment);
                }
            });

        } else {
            listView = (View) convertView;
        }

        listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new ProfileFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", mentors.get(i));
                fragment.setArguments(bundle);
                ((MainActivity)context).insertFragment(fragment);
            }
        });


        return listView;

    }

    public void setMentors(List<User> mentors) {
        this.mentors = mentors;
        notifyDataSetChanged();
    }
}
