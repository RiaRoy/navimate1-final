package com.project.ria.navimate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Dashboard extends AppCompatActivity {
Button tracking,contacts,find;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        tracking=(Button)findViewById(R.id.track);
        contacts=(Button)findViewById(R.id.contacts);
        find=(Button)findViewById(R.id.find);

        tracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LocationActivity.class));
            }
        }); contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity1.class));
            }
        });find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),FriendsLocationActivity.class));
            }
        });


    }
}
