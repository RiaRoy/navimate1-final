package com.project.ria.navimate;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

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
                list.add(new Contacts("Other Contacts","",""));
               getContacts();
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
    }

    private void getContacts() {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection    = new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER};

        Cursor people = getContentResolver().query(uri, projection, null, null, null);

        int indexName = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int indexNumber = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        Contacts noten;
        if(people.moveToFirst()) {
            do {
                String num="";
                final String name   = people.getString(indexName);
                String number = people.getString(indexNumber);
                if( number.contains("+")){
                    num=number.substring(3);


                }
                else{
                    num=number;
                }
                String numm1=num.trim().replace("\\s+","").replace("#","").replaceAll("[^a-zA-Z0-9]","");
                if(!TextUtils.isEmpty(numm1)) {
                    //a1.add(num.trim().replace("\\s+", "").replace("#", "").replaceAll("[^a-zA-Z0-9]", ""));
                    num=numm1;
                }

                  Contacts c=new Contacts();
                            c.setName(name);
                            c.setPhone(num);
                            list.add(c);




            } while (people.moveToNext());
            adapter=new CustomList(MainActivity1.this,list);
            listView.setAdapter(adapter);
        }

        //   mDatabase.child("User").child(Constants.phone).child("").updateChildren(a);
    }

}
