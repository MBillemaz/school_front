package com.ynov.mbi.finalproject_mbi.Adapters;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ynov.mbi.finalproject_mbi.Activities.MapsActivity;
import com.ynov.mbi.finalproject_mbi.Controllers.RequestInterface;
import com.ynov.mbi.finalproject_mbi.Interfaces.EcoleService;
import com.ynov.mbi.finalproject_mbi.Models.Ecole;
import com.ynov.mbi.finalproject_mbi.R;
import com.ynov.mbi.finalproject_mbi.Services.Localisation;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EcoleAdapter extends ArrayAdapter<Ecole> {


    public EcoleAdapter(@NonNull Context context, int resource, @NonNull List<Ecole> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Ecole ecole = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_ecole_object, parent, false);
        }

        final EcoleService ecoleService = RequestInterface.getInterface(EcoleService.class);

        //On récupère tous les champs et on les remplis
        TextView name = convertView.findViewById(R.id.nom_ecole);
        TextView address = convertView.findViewById(R.id.adresse_ecole);
        TextView nombre = convertView.findViewById(R.id.nb_eleve);
        TextView distance = convertView.findViewById(R.id.distance_ecole);
        name.setText(ecole != null ? ecole.getName() : "");
        address.setText(ecole != null ? ecole.getAddress() : "");
        nombre.setText(ecole.getNb_student() + " élèves");

        //On récupère la localisation actuelle (si la localisation est activée) pour calculer la distance. Si elle
        // est désactivée, on affiche "distance indéfinie"
        Location location = Localisation.getLocation((Activity) getContext(), false);

        if(location != null){
            Location schoolLoc = new Location("");
            schoolLoc.setLatitude(ecole.getLatitude());
            schoolLoc.setLongitude(ecole.getLongitude());
            String distanceString = String.format("%.0f", location.distanceTo(schoolLoc) / 1000);

            distance.setText(distanceString + " km");
        }
        else {
            distance.setText("Distance indisponible");
        }



        //Lors du clic sur le bouton map d'une école, on lance la map avec la latitude et longitude à afficher
        ImageView state = convertView.findViewById(R.id.ecoleState);
        ImageButton map_button = convertView.findViewById(R.id.map_button);
        map_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MapsActivity.class);
                intent.putExtra("latitude", ecole.getLatitude());
                intent.putExtra("longitude", ecole.getLongitude());
                getContext().startActivity(intent);
            }
        });

        //Lors du clic sur le bouton suppression, on lance une alertDialog pour confirmer la suppression
        ImageButton delete_button = convertView.findViewById(R.id.delete_button);
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext()).setTitle("Suppression")
                        .setMessage("Etes vous sûre de vouloir supprimer l’école " + ecole.getName())
                        .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ecoleService.removeEcole(ecole.getId()).enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        notifyDataSetChanged();
                                        Toast.makeText(getContext(), "Ecole supprimée", Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton("Non", null)
                        .show();

            }
        });
        //On modifie la couleur en fonction du nombre d'éleves
        map_button.setImageResource(R.drawable.map_icon);
        delete_button.setImageResource(R.drawable.delete);
        if(ecole.getNb_student() < 50){
            state.setImageResource(R.drawable.ko_icon);
            convertView.setBackgroundColor(convertView.getResources().getColor(R.color.red));
        }
        else {
            state.setImageResource(R.drawable.ok_icon);
            if(ecole.getNb_student() >= 50 && ecole.getNb_student() < 200){
                convertView.setBackgroundColor(convertView.getResources().getColor(R.color.orange));
            }
            else {
                convertView.setBackgroundColor(convertView.getResources().getColor(R.color.green));
            }
        }


        return convertView;
    }
}
