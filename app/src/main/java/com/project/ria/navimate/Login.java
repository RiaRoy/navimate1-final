package com.project.ria.navimate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class Login extends AppCompatActivity {
    EditText phone;
    Button b,r;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

       // mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        phone=(EditText) findViewById(R.id.phone);


        b=(Button)findViewById(R.id.button) ;
        r=(Button)findViewById(R.id.register) ;
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            checkUser();

            }
        });r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

           startActivity(new Intent(getApplicationContext(),Register.class));

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
      //  FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }



    private void checkUser() {


        Query query = mDatabase.child("User").orderByChild("phone").equalTo(phone.getText().toString());
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0

                        Intent intent=new Intent(getApplicationContext(),Dashboard.class);
                        Constants.id=dataSnapshot.getValue(User.class).getId();
                        Constants.username=dataSnapshot.getValue(User.class).getUname();
                        Constants.phone=dataSnapshot.getValue(User.class).getPhone();
                        startActivity(intent);


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
