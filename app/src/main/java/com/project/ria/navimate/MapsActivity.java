package com.project.ria.navimate;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private DatabaseReference mDatabase;

    private GoogleMap mMap;
    ArrayList<Contacts> list;
    String phone;
    Handler mHandler;
    Double lat,lng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        list=new ArrayList<>();

       // list.clear();
        getContacts();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        this.mHandler = new Handler();
        m_Runnable.run();
    }



    private final Runnable m_Runnable = new Runnable()
    {
        public void run()

        {
            if(mMap!=null)
                mMap.clear();

          //  Toast.makeText(getApplicationContext(),"jis",Toast.LENGTH_LONG).show();
            for(int jj=0;jj<list.size();jj++)
            {
                Query query  = mDatabase.child("Location").orderByChild("phone").equalTo(list.get(jj).getPhone());

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // dataSnapshot is the "notepad" node with all children with id
                            for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                                double lat = Double.parseDouble(postSnapshot.child("latitude").getValue().toString());
                                double lng = Double.parseDouble(postSnapshot.child("longitude").getValue().toString());
                                LatLng sydney = new LatLng(lat, lng);
                                mMap.addMarker(new MarkerOptions().position(sydney).title(postSnapshot.child("uname").getValue().toString()));
                                                       }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }


            MapsActivity.this.mHandler.postDelayed(m_Runnable,20000);
        }

    };
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
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
          }

        //   mDatabase.child("User").child(Constants.phone).child("").updateChildren(a);
    }

}

