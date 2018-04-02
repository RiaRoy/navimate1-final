package com.project.ria.navimate;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class Register extends AppCompatActivity {
    private DatabaseReference mDatabase;
    EditText name,phonenum,pass,uname;
    HashMap<String,ArrayList<Object>> a;
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
        User note = new User();
        note.setId(phonenum.getText().toString());
        note.setName(name.getText().toString());
        note.setPhone(phonenum.getText().toString());
        note.setUname(uname.getText().toString());
        note.setPassword(pass.getText().toString());
        note.setContacts(a1);
        mDatabase.child("User").child(note.getId()).setValue(note);


        startActivity(new Intent(getApplicationContext(),Login.class));



    }
    private void getContacts() {
        a1=new ArrayList<>();
         a=new HashMap<>();
         contactsList=new ArrayList<>();
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection    = new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER};

        Cursor people = getContentResolver().query(uri, projection, null, null, null);

        int indexName = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int indexNumber = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        Contacts note;
        if(people.moveToFirst()) {
            do {
                String num="";
                String name   = people.getString(indexName);
                String number = people.getString(indexNumber);
                if( number.contains("+")){
                    num=number.substring(3);


                }
                else{
                    num=number;
                }
                note=new Contacts();
                note.setPhone(num);
                note.setName(name);
                contactsList.add(note);
                String numm1=num.trim().replace("\\s+","").replace("#","").replaceAll("[^a-zA-Z0-9]","");
             if(!TextUtils.isEmpty(numm1)) {
                 //a1.add(num.trim().replace("\\s+", "").replace("#", "").replaceAll("[^a-zA-Z0-9]", ""));
                    note.setPhone(numm1);
                 a1.add(note);
             }

            } while (people.moveToNext());

        }

       // mDatabase.child("User").child(Constants.phone).child("").updateChildren(a);
    }
}
