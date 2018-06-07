package com.ynov.mbi.finalproject_mbi.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.Preference;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ynov.mbi.finalproject_mbi.Controllers.RequestInterface;
import com.ynov.mbi.finalproject_mbi.Fragments.ActionBar;
import com.ynov.mbi.finalproject_mbi.Interfaces.EcoleService;
import com.ynov.mbi.finalproject_mbi.Models.Ecole;
import com.ynov.mbi.finalproject_mbi.Models.JsonResponseEcole;
import com.ynov.mbi.finalproject_mbi.R;
import com.ynov.mbi.finalproject_mbi.Services.Localisation;
import com.ynov.mbi.finalproject_mbi.Services.Preferences;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ActionBar.OnFragmentInteractionListener, LocationListener {


    private EcoleService ecoleService;
    private GoogleMap mMap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Mise en place de la map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ecoleService = RequestInterface.getInterface(EcoleService.class);

        // Mise en place de l'action bar
        Fragment actionBar = new ActionBar();
        Bundle arguments = new Bundle();
        arguments.putString("title", "Carte");
        arguments.putString("menu", "map");
        actionBar.setArguments(arguments);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, actionBar).commit();

        //Mise en place de la barre de recherche. Lors de la séléction, on centre la carte dessus
        PlaceAutocompleteFragment placeAutoComplete = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete);
        placeAutoComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                // Showing the current location in Google Map
                mMap.moveCamera(CameraUpdateFactory.newLatLng( place.getLatLng()));

                // Zoom in the Google Map
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                Log.d("Maps", "Place selected: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                Log.d("Maps", "An error occurred: " + status);
            }
        });


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
        //On ajoute le bouton de centrage sur l'utilisateur
        mMap.getUiSettings().setMyLocationButtonEnabled(true);


        //On vérifie que nous avons la permission (obligatoire pour utiliser les fonctions en dessous)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, 177);


        }

        Location location;
        //On récupere la liste des écoles
        populateListEcole();

        Intent intent = getIntent();
        double lat = intent.getDoubleExtra("latitude", 0);
        double longitude = intent.getDoubleExtra("longitude", 0);

        //Si des coordonnées sont données en extra, on centre sur cette position et on demande à la carte de ne pas mettre a jour la position
        if (lat != 0 && longitude != 0){
            // Showing the current location in Google Map
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, longitude)));

            // Zoom in the Google Map
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            location = Localisation.getLocation(this, false);
        }
        else {

            //Sinon, on centre sur nous et on update
            location = Localisation.getLocation(this, true);

        }

        mMap.setMyLocationEnabled(true);


    }

    public void populateListEcole() {
        ecoleService.getEcole(Preferences.getSchoolType()).enqueue(new Callback<JsonResponseEcole>() {

            @Override
            public void onResponse(Call<JsonResponseEcole> call, Response<JsonResponseEcole> response) {
                if (response.code() == 200) {
                    final List<Ecole> listEcole = response.body().getSchools();
                    //Pour chaque école, on récupère la couleur demandée et on crée un marqueur
                    for(Ecole school : listEcole){
                        BitmapDescriptor color;
                        if(school.getNb_student() < 50){
                            color = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
                        }
                        else if(school.getNb_student() >= 50 && school.getNb_student() < 200){
                            color = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
                        }
                        else {
                            color = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                        }
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(school.getLatitude(), school.getLongitude()))
                                .title(school.getName())
                                .icon(color)

                        );
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonResponseEcole> call, Throwable t) {

            }
        });
    }


    public void onLocationChanged(Location location) {

        //Lorsque la position change, on meet a jour et on centre la caméra

        // Getting latitude of the current location
        double latitude = location.getLatitude();

        // Getting longitude of the current location
        double longitude = location.getLongitude();

        // Creating a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);

        // Showing the current location in Google Map
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in the Google Map
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText( getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT ).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText( getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT ).show();
    }


    @Override
    public void onFragmentInteraction(String test) {

    }
}
