package info.androidhive.sqlite;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import info.androidhive.sqlite.database.DatabaseHelper;
import info.androidhive.sqlite.database.model.Note;
import info.androidhive.sqlite.utils.GpsTracker;

public class RideDetailsActivity extends AppCompatActivity {

    EditText destination,distance,price;
            Spinner truckSize;
    String destinationStr="",distanceStr="",priceStr="",truckSizeStr="";
    Double amount = 0.0;

    Button confirmRide, map, cancel;
    private DatabaseHelper db;
    private List<Note> notesList = new ArrayList<>();
    private GpsTracker gpsTracker;

    private Double curLat,curLong, destinationLat,destinationLong;
     Double calculatedDistance = 0.0;
ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_details);

        getSupportActionBar().hide();

         back = (ImageView) findViewById(R.id.back);

         back.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                finish();

            }
        });

        Spinner dropdown = findViewById(R.id.truckSize);
        String[] items = new String[]{"Small", "Medium", "Large"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        String destinationPostalCode = getIntent().getStringExtra("DESTINATION");
         destinationLat = getIntent().getDoubleExtra("DESTINATIONLAT",0);
         destinationLong = getIntent().getDoubleExtra("DESTINATIONLONG",0);

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        getLocation();
//        calculatedDistance = distance(destinationLat,destinationLong,curLat,curLong);
        calculatedDistance = calculateDis(destinationLat,destinationLong,curLat,curLong);


        db = new DatabaseHelper(this);
        notesList.addAll(db.getAllNotes());

        destination = (EditText) findViewById(R.id.destination);
        distance = (EditText) findViewById(R.id.distance);
        price = (EditText) findViewById(R.id.price);
        truckSize = (Spinner) findViewById(R.id.truckSize);

        confirmRide = (Button) findViewById(R.id.confirm);
        cancel = (Button) findViewById(R.id.cancel);

        map = (Button) findViewById(R.id.map);

        destination.setText(destinationPostalCode.toString());

        double dis = calculatedDistance;

        DecimalFormat df = new DecimalFormat("#.##");
        dis = Double.valueOf(df.format(dis));
        distance.setText(String.valueOf(dis));

        calculatePrice();
        String temp = Double.toString(amount);
        price.setText("$"+temp);


        confirmRide.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                destinationStr = destination.getText().toString();
                distanceStr = distance.getText().toString();
                priceStr = price.getText().toString();

                if(destinationStr.trim().length()>0 && distanceStr.trim().length()>0 && priceStr.trim().length()>0 ){

                    createNote(destinationStr+"-"+distanceStr+"-"+priceStr+"-"+truckSizeStr);
                    Toast.makeText(getApplicationContext(),
                            "Ride details Saved Successfully", Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),
                            "Please fill all ride details", Toast.LENGTH_LONG).show();
                }

            }
        });


        cancel.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                finish();

            }
        });


        map.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                calculateDis(destinationLat,destinationLong,curLat,curLong);
                intent.putExtra("currentLat",curLat);
                intent.putExtra("currentLong",curLong);
                intent.putExtra("destinationLat",destinationLat);
                intent.putExtra("destinationLng",destinationLong);
                startActivity(intent);

            }
        });




        truckSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                calculatePrice();
                String temp = Double.toString(amount);
                price.setText("$"+temp);
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void createNote(String note) {
        // inserting note in db and getting
        // newly inserted note id
        long id = db.insertNote(note);

        // get the newly inserted note from db
        Note n = db.getNote(id);

        if (n != null) {
            // adding new note to array list at 0 position
            notesList.add(0, n);

//            // refreshing the list
//            mAdapter.notifyDataSetChanged();
//
//            toggleEmptyNotes();
        }
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


    public void getLocation(){
        gpsTracker = new GpsTracker(RideDetailsActivity.this);
        if(gpsTracker.canGetLocation()){
            curLat = gpsTracker.getLatitude();
            curLong = gpsTracker.getLongitude();

        }else{
            gpsTracker.showSettingsAlert();
        }
    }

    public double calculatePrice(){

        Double small = 100.0;
        Double medium = 250.0;
        Double big = 500.0;

        String text = truckSize.getSelectedItem().toString();
        if(text.equals("Small")){
            amount = (calculatedDistance * 1 ) + small;
            truckSizeStr = "Small";
        }else if(text.equals("Medium")){
            amount = (calculatedDistance * 1 ) + medium;
            truckSizeStr = "Medium";
        }else{
            amount = (calculatedDistance * 1 ) + big;
            truckSizeStr = "Big";

        }
        amount = (Double) Math.ceil(amount);
        return amount;

    }

    public Double calculateDis(Double lat1,Double lon1,Double lat2,Double lon2){
        Location loc1 = new Location("");
        loc1.setLatitude(lat1);
        loc1.setLongitude(lon1);

        Location loc2 = new Location("");
        loc2.setLatitude(lat2);
        loc2.setLongitude(lon2);

        float distanceInMeters = loc1.distanceTo(loc2);

         Double temp = Double.valueOf(Float.valueOf(distanceInMeters).toString()).doubleValue();

        Double distance = temp / 1000.0;
        distance = (Double) Math.ceil(distance);
        return distance;
    }
}