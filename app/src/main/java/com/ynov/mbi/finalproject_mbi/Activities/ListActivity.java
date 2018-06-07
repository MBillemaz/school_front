package com.ynov.mbi.finalproject_mbi.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ynov.mbi.finalproject_mbi.Adapters.EcoleAdapter;
import com.ynov.mbi.finalproject_mbi.Controllers.RequestInterface;
import com.ynov.mbi.finalproject_mbi.Fragments.ActionBar;
import com.ynov.mbi.finalproject_mbi.Interfaces.EcoleService;
import com.ynov.mbi.finalproject_mbi.Models.Ecole;
import com.ynov.mbi.finalproject_mbi.Models.JsonResponseEcole;
import com.ynov.mbi.finalproject_mbi.R;
import com.ynov.mbi.finalproject_mbi.Services.Preferences;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListActivity extends FragmentActivity implements ActionBar.OnFragmentInteractionListener, LocationListener{

    EcoleService ecoleService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ecoleService = RequestInterface.getInterface(EcoleService.class);
        Fragment actionBar = new ActionBar();
        Bundle arguments = new Bundle();
        arguments.putString("title", "Liste des écoles");
        arguments.putString("menu", "ecoleList");
        actionBar.setArguments(arguments);
        populateListView();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, actionBar).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateListView();
    }

    private void populateListView(){

        //Lors du démarrage ou redémarrage de l'activité, on fait une demande de la liste des écoles pour les afficher
        ecoleService.getEcole(Preferences.getSchoolType()).enqueue(new Callback<JsonResponseEcole>() {
            @Override
            public void onResponse(Call<JsonResponseEcole> call, Response<JsonResponseEcole> response) {
                if(response.code() == 200){
                    final List<Ecole> listEcole = response.body().getSchools();

                    ListView listView = findViewById(R.id.listView);
                    EcoleAdapter adapter = new EcoleAdapter(ListActivity.this, R.layout.list_ecole_object, listEcole);
                    listView.setAdapter(adapter);


                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //Lors du clique sur une école, on la récupère et on lance l'activité de détail
                            Ecole school = listEcole.get(position);
                            Intent i = new Intent(view.getContext(), DetailActivity.class);
                            i.putExtra("school", school);
                            view.getContext().startActivity(i);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<JsonResponseEcole> call, Throwable t) {
                Log.e("a", t.toString());
                Toast.makeText(ListActivity.this, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }



    @Override
    public void onFragmentInteraction(String test) {

    }

    @Override
    public void onLocationChanged(Location location) {

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
}
