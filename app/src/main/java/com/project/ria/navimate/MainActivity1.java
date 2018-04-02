package com.project.ria.navimate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity1 extends AppCompatActivity {
    private DatabaseReference mDatabase;
    ListView listView;
    ArrayList<Contacts>list;
    private CustomList adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        listView=(ListView)findViewById(R.id.list) ;
        list=new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference();


        populateList();

    }

    private void populateList() {

        Query recentPostsQuery = mDatabase.child("User").child(Constants.phone).child("contacts")
                ;
        recentPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("TAG","DATA"+dataSnapshot);
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO
                    Log.w("TAG", "loadP"+postSnapshot);
                    Contacts c=new Contacts();
                    c.setName(postSnapshot.child("name").getValue().toString());
                    c.setPhone(postSnapshot.child("phone").getValue().toString());
                    list.add(c);
                }
                adapter=new CustomList(MainActivity1.this,list);
                listView.setAdapter(adapter);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
    }



}
