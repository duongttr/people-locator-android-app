package com.example.waud;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.waud.Services.LocationUpdatesBroadcastReceiver;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;

    private DatabaseReference ref;
    private final int INTERVAL_IN_MILLISECOND = 10000;
    private final int FASTEST_INTERVAL_IN_MILLISECOND = 5000;
    private final float SMALLEST_DISPLACEMENT = 0.0f;
    private final int PERMISSION_REQUEST_CODE = 183;

    private Map<String, User> listUser;
    private Map<String, Marker> listMaker;

    private final String TAG = "MapsActivity";

    private TextView tvSpeed;
    private String UID, followedUID;
    private List<String> listUIDs;

    private BottomSheetBehavior sheetBehavior;
    private ConstraintLayout sheetLayout;
    private LinearLayout bottomSheetTitleLayout;

    private TextView tvNickname, tvFullPosition, tviSpeed, tviLastUpdated;
    private FloatingActionButton fabDirection;
    private MaterialButton btnFollowUser;
    private PopupMenu menuUser;
    private SupportMapFragment mapFragment;

    private final float defaultZoom = 15.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        /* Declare BottomSheet View*/
        sheetLayout = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(sheetLayout);
        tvNickname = findViewById(R.id.textView_Nickname);
        tvFullPosition = findViewById(R.id.textView_FullPosition);
        tviSpeed = findViewById(R.id.textView_speedInfo);
        tviLastUpdated = findViewById(R.id.textView_LastUpdated);
        fabDirection = findViewById(R.id.fab_Direction);
        btnFollowUser = findViewById(R.id.button_FollowUser);

        bottomSheetTitleLayout = findViewById(R.id.linearLayout2);


        menuUser = new PopupMenu(this, btnFollowUser);

        menuUser.setOnMenuItemClickListener(item -> {
            followedUID = listUIDs.get(item.getItemId());
            User u = listUser.get(followedUID);
            if (u != null){
                SmoothMove(new LatLng(u.getLocation().getLatitude(), u.getLocation().getLongitude()),
                        u.getLocation().getBearing(), true);
                tvNickname.setText(u.getNickName());
                updateBottomSheet(u);

                Toast.makeText(MapsActivity.this, item.getTitle() + " is followed!", Toast.LENGTH_SHORT).show();
            }
            return false;
        });

        UID = getIntent().getStringExtra("UID");
        followedUID = UID;
        listUser = new HashMap<>();
        listMaker = new HashMap<>();
        listUIDs = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference();

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getLocationUpdates();
        readChanges();
    }

    private void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(INTERVAL_IN_MILLISECOND);
        locationRequest.setFastestInterval(FASTEST_INTERVAL_IN_MILLISECOND);
        locationRequest.setSmallestDisplacement(SMALLEST_DISPLACEMENT);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setMaxWaitTime(INTERVAL_IN_MILLISECOND * 5);
    }

    private void readChanges() {
        final int[] count = {0};
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    try {
                        for (DataSnapshot ds: snapshot.getChildren()){
                            User u = ds.getValue(User.class);
                            if(u != null){
                                if (!listUser.containsKey(ds.getKey())){
                                    Menu menu = menuUser.getMenu();
                                    String title = u.getNickName() + (Objects.equals(ds.getKey(), UID) ? " (you)" : "");
                                    menu.add(0, count[0], 0, title);
                                    listUIDs.add(ds.getKey());
                                    count[0]++;
                                }
                                listUser.put(ds.getKey(), u);
                                if(listMaker.containsKey(ds.getKey())){
                                    MyLocation myLoc = u.getLocation();
                                    LatLng coordinates = new LatLng(myLoc.getLatitude(), myLoc.getLongitude());
                                    Marker m = listMaker.get(ds.getKey());

                                    m.setPosition(coordinates);
                                    if(Objects.equals(ds.getKey(), followedUID)){
                                        tvNickname.setText(u.getNickName());
                                        tvSpeed.setText(String.format(Locale.US,"%.1f", MPStoKMPH(u.getLocation().getSpeed())));
                                        updateBottomSheet(u);
                                        SmoothMove(coordinates, myLoc.getBearing(), false);
                                    }

                                }else{
                                    addUserOnMap(u, ds.getKey());
                                }
                            }
                        }
                    }catch (Exception ex){
                        Log.e("Location Change Error", ex.getMessage());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void updateBottomSheet(User u) {
        LatLng coordinates = new LatLng(u.getLocation().getLatitude(), u.getLocation().getLongitude());
        new GetFullNameAdressTask().execute(coordinates);
        tviSpeed.setText(String.format(Locale.US, "%.1f km/h", MPStoKMPH(u.getLocation().getSpeed())));
        String convertedTime = ConvertTimeStampToFormmattedString(u.getLocation().getTime());
        tviLastUpdated.setText(convertedTime);
    }

    private String ConvertTimeStampToFormmattedString(long time) {
        Date d = new Date(time);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss aa dd/MM/yyyy");
        StringBuilder timeBuilder = new StringBuilder();
        timeBuilder.append(sdf.format(d));
        timeBuilder.append("\n(");
        timeBuilder.append(Utils.getRelationTime(time));
        timeBuilder.append(")");
        return timeBuilder.toString();
    }

    private void getLocationUpdates() {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                createLocationRequest();
                //createLocationCallback();
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, getPendingIntent());
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                                    Manifest.permission.CALL_PHONE},
                        PERMISSION_REQUEST_CODE);
            }
    }

    private PendingIntent getPendingIntent() {
        // Note: for apps targeting API level 25 ("Nougat") or lower, either
        // PendingIntent.getService() or PendingIntent.getBroadcast() may be used when requesting
        // location updates. For apps targeting API level O, only
        // PendingIntent.getBroadcast() should be used. This is due to the limits placed on services
        // started in the background in "O".

        // TODO(developer): uncomment to use PendingIntent.getService().
//        Intent intent = new Intent(this, LocationUpdatesIntentService.class);
//        intent.setAction(LocationUpdatesIntentService.ACTION_PROCESS_UPDATES);
//        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intent = new Intent(this, LocationUpdatesBroadcastReceiver.class);
        intent.setAction(LocationUpdatesBroadcastReceiver.ACTION_PROCESS_UPDATES);
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocationUpdates();
            } else {
                Toast.makeText(this, "Permission required", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Configure map
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(false);
        //mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);


        // Declare View
        tvSpeed = findViewById(R.id.textView_speed);

        // Add a marker in Sydney and move the camera
//        ref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                if(snapshot.exists()){
//                    try {
//                        for (DataSnapshot ds: snapshot.getChildren()){
//                            User u = ds.getValue(User.class);
//                            if(u != null){
//                                listUser.put(ds.getKey(), u);
//                                addUserOnMap(u, ds.getKey());
//                            }
//                        }
//                    }catch (Exception ex){
//                        Log.e(TAG, ex.getMessage());
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e(TAG, "LastKnownLocation: " + error.getMessage());
//            }
//        });

    }



    private void addUserOnMap(User u, String key) {

        //Add image as icon
        LatLng currentPosition = new LatLng(u.getLocation().getLatitude(), u.getLocation().getLongitude());
        if (key.equals(UID)){
            findViewById(R.id.fab_MyLocation).setVisibility(View.VISIBLE);
            tvNickname.setText(u.getNickName());
            updateBottomSheet(u);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, defaultZoom));
        }

        final Marker[] m = {mMap.addMarker(new MarkerOptions().position(currentPosition))};
        m[0].setSnippet(key);
        listMaker.put(key, m[0]);

        CustomTarget<Bitmap> target = new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                if (resource != null){
                    resource = designMarker(resource);
                    m[0].setIcon(BitmapDescriptorFactory.fromBitmap(resource));
                }
                listMaker.put(key, m[0]);
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {

            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        };

        Glide.with(this).asBitmap().load(u.getProfilePicture()).into(target);

    }


    private void saveLocationOnDatabase(Location location) {
        ref.child(UID).child("location").setValue(location);
        //ref.child(UID).child("lastUpdatedTime").setValue(location.getTime());
    }

    private void SmoothMove(LatLng pos, float bearing, boolean hasZoom) {
        CameraPosition.Builder positionBuilder = new CameraPosition.Builder();
        positionBuilder.target(pos);
        positionBuilder.bearing(bearing);
        if (hasZoom) positionBuilder.zoom(defaultZoom);
        else{
            positionBuilder.zoom(mMap.getCameraPosition().zoom);
        }
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(positionBuilder.build()));
    }

    public Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, (float) pixels, (float) pixels, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public Bitmap designMarker(Bitmap bmp){
        return getRoundedCornerBitmap(Bitmap.createScaledBitmap(bmp, 120, 120, false), 100);
    }

//    int backButtonCount = 0;
//    @Override
//    public void onBackPressed() {
//        //super.onBackPressed();
//        if(backButtonCount >= 1)
//        {
//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.addCategory(Intent.CATEGORY_HOME);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//        }
//        else
//        {
//            Toast.makeText(this, getString(R.string.close_app_notif), Toast.LENGTH_SHORT).show();
//            backButtonCount++;
//        }
//    }

    int backButtonCount = 0;
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(backButtonCount >= 1){
            finish();
        }else{
            Toast.makeText(this, getString(R.string.close_app_notif), Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }


//    @SuppressLint("DefaultLocale")
//    @Override
//    public boolean onMarkerClick(Marker marker) {
//        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//        String id = marker.getSnippet();
//        UserIdClicked = id;
//        User u = listUser.get(id);
//        tvNickname.setText(u.getNickName());
//
//        LatLng ll = new LatLng(u.getLocation().getLatitude(), u.getLocation().getLongitude());
//
//        tviSpeed.setText(String.format("%.2f m/s", u.getLocation().getSpeed()));
//
//
//        tviLastUpdated.setText(ConvertTimeStampToFormmattedString(u.getLocation().getTime()));
//        new GetFullNameAdressTask().execute(ll);
//        //SmoothMove(ShiftCamera(ll), u.getLocation().getBearing());
//        //tviAddressName.setText(getFullNameAddress(u.getLocation().getLatitude(), u.getLocation().getLongitude()));
//        return false;
//    }

    public void OnClickBottomSheetControl(View view) {
        if (sheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }else if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    public void showFollowMenu(View view) {
        menuUser.show();
    }

    public void findDirectionClicked(View view) {
        User u = listUser.get(followedUID);
        Uri gmmIntentUri = Uri.parse(String.format(Locale.US,"google.navigation:q=%f,%f", u.getLocation().getLatitude(),
                                                                                u.getLocation().getLongitude()));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    public void moveToMyLocationClicked(View view) {
        User u = listUser.get(UID);
        SmoothMove(new LatLng(u.getLocation().getLatitude(), u.getLocation().getLongitude()), u.getLocation().getBearing(), true);
    }

    public void makeAPhoneCallClicked(View view) {
        User u = listUser.get(followedUID);
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", u.getPhoneNumber(), null));
        startActivity(intent);
    }

    public void messageClicked(View view) {
        User u = listUser.get(followedUID);

        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.me/" + u.getFbUID()));

        try {
            startActivity(i);
        }catch (Exception ex){
            Toast.makeText(this, "Facebook Messenger is not installed, INSTALL IT NOW!", Toast.LENGTH_SHORT).show();
        }

    }

    public void emergencyCallClicked(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Are you in danger?")
                .setMessage("I will make a phone call to 113 after clicking Yes.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:113"));
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    public class GetFullNameAdressTask extends AsyncTask<LatLng, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(LatLng... latLngs) {
            LatLng ll = latLngs[0];
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(ll.latitude, ll.longitude, 1);
                return addresses.get(0).getAddressLine(0);
            }catch (Exception ex){
                Log.e(TAG, "getFullNameAddress error: " + ex.getMessage());
            }

            return "Unknown place";
        }


        @Override
        protected void onPostExecute(String s) {
            tvFullPosition.setText(s);
            bottomSheetTitleLayout.getViewTreeObserver().addOnGlobalLayoutListener(() -> sheetBehavior.setPeekHeight(bottomSheetTitleLayout.getHeight()));
            if (fabDirection.getVisibility() == View.INVISIBLE) fabDirection.setVisibility(View.VISIBLE);
        }

    }

    private boolean isLocationServiceRunning(){
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        if (activityManager != null){
            for (ActivityManager.RunningServiceInfo service:
                activityManager.getRunningServices(Integer.MAX_VALUE)) {
                if (LocationServices.class.getName().equals(service.service.getClassName())){
                    if (service.foreground) return true;
                }
            }
            return false;
        }
        return false;
    }

//    private void startLocationService(){
//        if (!isLocationServiceRunning()){
//            Intent intent = new Intent(getApplicationContext(), LocationTrackingService.class);
//            intent.setAction(Constants.ACTION_START_LOCATION_SERVICE);
//            startService(intent);
//            Toast.makeText(this, "Service started", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void stopLocationService(){
//        if (isLocationServiceRunning()){
//            Intent intent = new Intent(getApplicationContext(), LocationTrackingService.class);
//            intent.setAction(Constants.ACTION_STOP_LOCATION_SERVICE);
//            startService(intent);
//            Toast.makeText(this, "Service stopped", Toast.LENGTH_SHORT).show();
//        }
//    }

    private float MPStoKMPH(float speed){
        return speed*3600/1000;
    }
}