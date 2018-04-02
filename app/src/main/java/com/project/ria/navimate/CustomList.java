package com.project.ria.navimate;

/**
 * Created by skynet on 3/3/18.
 */


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class CustomList extends BaseAdapter
{
    Activity context;
    ArrayList<Contacts>d;
    private DatabaseReference mDatabase;

    public CustomList(Activity context, ArrayList<Contacts>u) {
        super();
        this.context = context;
        this.d=u;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return d.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    private class ViewHolder {
        TextView txtViewTitle;
        TextView txtViewDescription;
    }

    public View getView(final int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        final ViewHolder holder;
        LayoutInflater inflater =  context.getLayoutInflater();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.txtViewTitle = (TextView) convertView.findViewById(R.id.name);
            holder.txtViewDescription = (TextView) convertView.findViewById(R.id.phone);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query query = mDatabase.child("Location").orderByChild("phone").equalTo(d.get(position).getPhone());
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0

                    holder.txtViewTitle.setText(d.get(position).getName());
                    holder.txtViewDescription.setText(d.get(position).getPhone());
                    holder.txtViewTitle.setTextColor(Color.GREEN);
                    holder.txtViewDescription.setTextColor(Color.GREEN);
                }
            else {

                    holder.txtViewTitle.setText(d.get(position).getName());
                    holder.txtViewDescription.setText(d.get(position).getPhone());
                }
            }

                                        @Override
                                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                        }

                                        @Override
                                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                                        }

                                        @Override
                                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

            }
                                        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("TAG","CLICK");
                checkUser(d.get(position).getPhone(),view.getContext());

            }
        });


        return convertView;
    }


    private void checkUser(String ph, final Context c1) {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query query = mDatabase.child("Location").orderByChild("phone").equalTo(ph);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0

                    Intent intent=new Intent(c1,Dashboard.class);
                    intent.putExtra("lat",dataSnapshot.getValue(Location.class).getLatitude());
                    intent.putExtra("lon",dataSnapshot.getValue(Location.class).getLongitude());
                    intent.putExtra("phone",dataSnapshot.getValue(Location.class).getPhone());
                    intent.putExtra("name",dataSnapshot.getValue(Location.class).getUname());
                    c1.startActivity(intent);


                }


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}