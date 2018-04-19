package com.project.ria.navimate;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    private DatabaseReference mDatabase;
    EditText name,phonenum,pass,uname;User note = new User();
    HashMap<String,ArrayList<Contacts>> a;
    Button b;
    ArrayList<Contacts>a1;
    ArrayList<Contacts>contactsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        name=(EditText) findViewById(R.id.name);
        pass=(EditText) findViewById(R.id.pass);
        phonenum=(EditText) findViewById(R.id.phone);
        uname=(EditText) findViewById(R.id.uname);
        b=(Button)findViewById(R.id.button) ;
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                register();
            }
        });

    }

    private void register() {
        getContacts();

        note.setId(phonenum.getText().toString());
        note.setName(name.getText().toString());
        note.setPhone(phonenum.getText().toString());
        note.setUname(uname.getText().toString());
        note.setPassword(pass.getText().toString());


        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();


    }
    private void getContacts() {
        a1=new ArrayList<Contacts>();
        a1.clear();
        a=new HashMap<>();
        contactsList=new ArrayList<>();
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
                noten=new Contacts();
                noten.setPhone(num);
                noten.setName(name);
                contactsList.add(noten);
                a.put("contact",contactsList);

                Query query = mDatabase.child("User").orderByChild("phone").equalTo(num);

                final String finalNum = num;
                final Contacts finalNote = noten;
                final Contacts finalNote1 = noten;
                query.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot.exists()) {
                            a1.add(finalNote);
                            Log.i("jisjoe","inside="+a1.toString());
                            note.setContacts(a1);
                            Log.i("jisjoe","=="+a1.toString());
                            mDatabase.child("User").child(note.getId()).setValue(note);

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

            } while (people.moveToNext());
            Log.i("jisjoe","over="+a1.toString());
        }

        //   mDatabase.child("User").child(Constants.phone).child("").updateChildren(a);
    }
}
