package com.ynov.mbi.finalproject_mbi.Activities;



import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;

import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.JsonObject;
import com.ynov.mbi.finalproject_mbi.Controllers.RequestInterface;
import com.ynov.mbi.finalproject_mbi.Interfaces.EcoleService;

import com.ynov.mbi.finalproject_mbi.Models.Credentials;
import com.ynov.mbi.finalproject_mbi.R;
import com.ynov.mbi.finalproject_mbi.Services.Preferences;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    TextView login;
    TextView password;
    CheckBox remember;
    Button valider;
    Button reset;

    EcoleService ecoleService;

    @Override
    protected void onResume() {
        super.onResume();
        Button valid = findViewById(R.id.btnValid);
        valid.setEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ecoleService = RequestInterface.getInterface(EcoleService.class);

        login = findViewById(R.id.inputLogin);
        password = findViewById(R.id.inputPassword);
        remember = findViewById(R.id.remember);

        //Pour chaque bouton, on lui définit une fonction à executer lors du clic
        valider = findViewById(R.id.btnValid);
        valider.setOnClickListener(genericOnClickListener);

        reset = findViewById(R.id.btnReset);
        reset.setOnClickListener(genericOnClickListener);

        Preferences.setPreferences(PreferenceManager.getDefaultSharedPreferences(MainActivity.this));

        setDefaultValue();

        //On regarde si nous avons le droit d'utiliser la position. Sinon, nous la demandons
        int permissionFine = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        int permissionCoarse = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (permissionFine != PackageManager.PERMISSION_GRANTED || permissionCoarse != PackageManager.PERMISSION_GRANTED) {
            makeRequest();
        }


    }

    protected void makeRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
    }

    //On regarde si les identifiants de l'utilisateur sont stockés en cache. Si oui, on les mets en tant que valeur par défaut des deux inputs et on coche la checkbox avant de supprimer le cache.
    // (Il sera recrée si l'utilisateur laisse la checkbox cochée
    protected void setDefaultValue(){
        SharedPreferences prefs = Preferences.getPreferences();
        String cacheLogin = prefs.getString("login", null);
        String cachePass = prefs.getString("password", null);

        //Crée un cache par défaut des écoles à afficher
        boolean cachePrivate = prefs.getBoolean("private", true);
        boolean cachePublic = prefs.getBoolean("public", true);
        prefs.edit()
                .putBoolean("private", cachePrivate)
                .putBoolean("public", cachePublic)
                .apply();


        if(cacheLogin != null && cachePass != null){
            login.setText(cacheLogin);
            password.setText(cachePass);
            remember.setChecked(true);
            prefs.edit().remove("login").remove("password").apply();
        }
    }


    //lors du clic sur un bouton, on vérifie l'id du bouton et on agit en conséquence (Si bouton vider, on vide les champs, sinon on lance le test d'authentification)
    final View.OnClickListener genericOnClickListener = new View.OnClickListener() {
        public void onClick(final View v) {
            CharSequence errorMessage = getText(R.string.empty_champ);
            switch (v.getId()) {
                case R.id.btnValid:
                    if (TextUtils.isEmpty(login.getText())) {
                        login.setError(errorMessage);
                    }
                    if (TextUtils.isEmpty(password.getText())) {
                        password.setError(errorMessage);
                    }
                    if (!TextUtils.isEmpty(password.getText()) && !TextUtils.isEmpty(login.getText())) {

                        valider.setEnabled(false);
                        Credentials cred = new Credentials(login.getText().toString(), password.getText().toString());

                        ecoleService.authenticate(cred).enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                                //On stocke l'auth_token dans la classe Credentials, puis on lance l'activité menu
                                if (response.code() == 200) {
                                    String auth_token = response.body().get("auth_token").getAsString();
                                    Credentials.setAuth_token(auth_token);
                                    CheckBox remember = MainActivity.this.findViewById(R.id.remember);
                                    if (remember.isChecked()) {
                                        SharedPreferences prefs = Preferences.getPreferences();
                                        prefs.edit()
                                                .putString("login", login.getText().toString())
                                                .putString("password", password.getText().toString())
                                                .apply();
                                    }
                                    Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                                    MainActivity.this.startActivity(intent);
                                }
                                valider.setEnabled(true);
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                valider.setEnabled(true);
                                Toast.makeText(MainActivity.this, t.toString(), Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                    break;
                case R.id.btnReset:
                    login.setText(null);
                    password.setText(null);
                    remember.setChecked(false);
                    break;
            }
        }
    };


}