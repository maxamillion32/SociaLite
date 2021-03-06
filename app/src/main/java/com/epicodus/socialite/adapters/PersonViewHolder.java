package com.epicodus.socialite.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.epicodus.socialite.Constants;
import com.epicodus.socialite.R;
import com.epicodus.socialite.models.Person;
import com.epicodus.socialite.ui.ConfirmActivity;
import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;


public class PersonViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.nameTextView) TextView mNameTextView;
    @Bind(R.id.nameTextView2) CheckBox mNameCheckBox;

    private Context mContext;
    private ArrayList<Person> mPersons = new ArrayList<>();
    private String name;
    private String event;
    private String rsvp;
    private String phone;
    private String pushId;

    public PersonViewHolder(final View itemView, ArrayList<Person> persons) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mContext = itemView.getContext();
        mPersons = persons;


        mNameCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                int itemPosition = getLayoutPosition();
                name = mPersons.get(itemPosition).getName();
                event = mPersons.get(itemPosition).getEvent();
                phone = mPersons.get(itemPosition).getPhone();
                pushId = mPersons.get(itemPosition).getPushId();
                if (isChecked) {
                    rsvp = "yes";
                    updatersvp(rsvp);
                } else {
                    rsvp = "no";
                    updatersvp(rsvp);
                }
            }
        });


        if(mContext.getClass().getSimpleName().equals(ConfirmActivity.class.getSimpleName())) {
            itemView.setClickable(false);
            mNameCheckBox.setVisibility(View.INVISIBLE);
        }

    }

    public void bindPerson(Person person) {
        if(person.getrsvp().equals("yes")) {
            mNameCheckBox.setChecked(true);
            mNameTextView.setText(person.getName());
            mNameCheckBox.setText(person.getName());
        } else {
            mNameCheckBox.setChecked(false);
            mNameTextView.setText(person.getName());
            mNameCheckBox.setText(person.getName());
        }

    }

    public void updatersvp(String rsvp) {
        Firebase firebaseRef = new Firebase(Constants.FIREBASE_URL);
        Firebase inviteeListRef = firebaseRef.child(event);
        Firebase inviteeRef = inviteeListRef.child(pushId);
        Map<String,Object> inviteeMap = new HashMap<String,Object>();
        inviteeMap.put("rsvp", rsvp);
        inviteeMap.put("event", event);
        inviteeMap.put("name", name);
        inviteeMap.put("phone", phone);
        inviteeMap.put("pushId", pushId);
        inviteeRef.updateChildren(inviteeMap);
    }
}