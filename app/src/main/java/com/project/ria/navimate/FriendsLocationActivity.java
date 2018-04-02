package com.project.ria.navimate;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendsLocationActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    private Location mLastKnownLocation;
    private static final String TAG ="" ;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    boolean mLocationPermissionGranted=true;
    private DatabaseReference mDatabase;
    ArrayList<MapLocation>locArrayList;
    private Location locationV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        locArrayList=new ArrayList<>();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

    }


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

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
        populateList();




    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {

            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            locationV=mLastKnownLocation;
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), 18));
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            LatLng mDefaultLocation  = new LatLng(-33.852, 151.211);
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, 18));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    @Override
    public void onMapClick(final LatLng latlng) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
locationV=location;
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(latLng).title("Current Location"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                        // Zoom in, animating the camera.
                        mMap.animateCamera(CameraUpdateFactory.zoomIn());
                        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                        if (location != null) {
                            // Logic to handle location object
                        }
                    }
                });
    }

    private void populateList() {
        final Query recentPostsQuery = mDatabase.child("User").child(Constants.phone).child("contacts")
                ;

        recentPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("TAG","DATA"+dataSnapshot);
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO
                    Log.w("TAG", "loadP"+postSnapshot);
                    Log.w("TAG", "p"+postSnapshot.getValue().toString().length());


                    Query recentPostsQuery1= mDatabase.child("Location").child(postSnapshot.getValue().toString());

                    recentPostsQuery1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.i("TAG","DATA"+dataSnapshot);
                            if(dataSnapshot.exists()){
                            if(dataSnapshot!=null) {

                                double rad = distFrom(locationV.getLatitude(), locationV.getLongitude(), Double.parseDouble(dataSnapshot.getValue(com.project.ria.navimate.Location.class).getLatitude()), Double.parseDouble(dataSnapshot.getValue(com.project.ria.navimate.Location.class).getLongitude()));

                                Log.i("TAG","hel"+rad);

                                if (rad >= 0&& rad < 1.5) {

                                    Log.i("TAG", "inside rad");
                                    com.project.ria.navimate.MapLocation d = new com.project.ria.navimate.MapLocation();
                                    d.setId(dataSnapshot.getValue(com.project.ria.navimate.Location.class).getId());

                                    Log.i("TAG", "latitudev" + dataSnapshot.getValue(com.project.ria.navimate.Location.class).getLatitude());
                                    d.setLatitude(dataSnapshot.getValue(com.project.ria.navimate.Location.class).getLatitude());
                                    d.setLongitude(dataSnapshot.getValue(com.project.ria.navimate.Location.class).getLongitude());
                                    d.setPhone(dataSnapshot.getValue(com.project.ria.navimate.Location.class).getPhone());
                                    d.setUname(dataSnapshot.getValue(com.project.ria.navimate.Location.class).getUname());
                                    MarkerOptions markerOptions = new MarkerOptions();

                                    // Setting position for the marker

                                    markerOptions.position(new LatLng(Double.parseDouble(d.getLatitude()), Double.parseDouble(d.getLongitude())));
                                    Log.i("TAG", "array lat" + d.getLatitude());
                                    // Setting custom icon for the marker
                                    // Setting title for the infowindow
                                    markerOptions.title(d.getUname());
                                    markerOptions.snippet(d.getPhone());

                                    // Adding the marker to the map
                                    mMap.addMarker(markerOptions);
                                    //locArrayList.add(d);


                                }

                            }

                            }
                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Getting Post failed, log a message
                            Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
                            // ...
                        }
                    });
                }}
               // Log.i("TAG","array rad"+locArrayList.size());





            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
    }

    public double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371.0;//use 6371000 for getting distance in meter
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double ccc = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * ccc;
        Log.d("TAG","dist"+dist);
        return dist;
    }

}
