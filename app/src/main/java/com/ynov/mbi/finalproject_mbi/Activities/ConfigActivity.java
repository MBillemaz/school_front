package com.ynov.mbi.finalproject_mbi.Activities;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.ynov.mbi.finalproject_mbi.Fragments.ActionBar;
import com.ynov.mbi.finalproject_mbi.R;
import com.ynov.mbi.finalproject_mbi.Services.Preferences;

public class ConfigActivity extends FragmentActivity implements ActionBar.OnFragmentInteractionListener {

    CheckBox publicImage;
    CheckBox privateImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        //Mise en place de l'action bar
        Fragment actionBar = new ActionBar();
        Bundle arguments = new Bundle();
        arguments.putString("title", "Configuration");
        arguments.putString("menu", "config");
        actionBar.setArguments(arguments);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, actionBar).commit();

        //On récupère les données stockées en cache et on remplit les checkboxs en conséquence
        publicImage = findViewById(R.id.public_toggle);
        privateImage = findViewById(R.id.private_toggle);

        final SharedPreferences prefs = Preferences.getPreferences();
        final boolean publicCache = prefs.getBoolean("public", true);
        boolean privateCache = prefs.getBoolean("private", true);

        publicImage.setChecked(publicCache);
        privateImage.setChecked(privateCache);

        //Lorsqu'on clique sur l'un des boutons, on vérifie que l'un des deux reste coché et on stocke dans le cache
        publicImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!publicImage.isChecked() && !privateImage.isChecked()){
                    privateImage.setChecked(true);
                }
                prefs.edit()
                        .putBoolean("public", publicImage.isChecked())
                        .putBoolean("private", privateImage.isChecked())
                        .apply();
            }
        });

        privateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!publicImage.isChecked() && !privateImage.isChecked()){
                    privateImage.setChecked(true);
                }
                prefs.edit()
                        .putBoolean("private", privateImage.isChecked())
                        .putBoolean("public", publicImage.isChecked())
                        .apply();
            }
        });



    }

    @Override
    public void onFragmentInteraction(String test) {

    }
}
