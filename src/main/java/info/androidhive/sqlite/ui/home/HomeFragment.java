package info.androidhive.sqlite.ui.home;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import info.androidhive.sqlite.HomeActivity;
import info.androidhive.sqlite.R;
import info.androidhive.sqlite.RideDetailsActivity;
import info.androidhive.sqlite.SplashActivity;
import info.androidhive.sqlite.database.model.Note;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);

        final EditText postalCode = root.findViewById(R.id.postalCode);
        final Button searchBtn = root.findViewById(R.id.search);

        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        searchBtn.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String postalCodeStr = postalCode.getText().toString();

                Boolean checkZip = checkZipCode(postalCodeStr);
                if(checkZip == true){
                    getLatLong(postalCodeStr.trim());
                } else{
                    Toast.makeText(getContext(), "Please enter valid Postal Code", Toast.LENGTH_LONG).show();

                }
//                if(postalCodeStr.trim().length() == 6){
//                    getLatLong(postalCodeStr.trim());
//                }else{
//                    Toast.makeText(getContext(), "Please enter valid Postal Code", Toast.LENGTH_LONG).show();
//
//                }

            }
        });

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    public void getLatLong(String postalCode){
        final Geocoder geocoder = new Geocoder(getContext());
//        final String zip = "201007";
        try {
            List<Address> addresses = geocoder.getFromLocationName(postalCode, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                // Use the address as needed
//                String message = String.format("Latitude: %f, Longitude: %f",
//                        address.getLatitude(), address.getLongitude());
//                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();

                Intent i = new Intent(getContext(), RideDetailsActivity.class);
                i.putExtra("DESTINATION", postalCode);
                i.putExtra("DESTINATIONLAT", address.getLatitude());
                i.putExtra("DESTINATIONLONG", address.getLongitude());


                startActivity(i);
            } else {
                // Display appropriate message when Geocoder services are not available
                Toast.makeText(getContext(), "Unable to find location", Toast.LENGTH_LONG).show();

            }
        } catch (IOException e) {
            // handle exception
        }
    }

    public Boolean checkZipCode(String zip){

        String regex = "^(?!.*[DFIOQU])[A-VXY][0-9][A-Z] ?[0-9][A-Z][0-9]$";

        boolean matches = zip.matches(regex);
        return matches;
    }


}