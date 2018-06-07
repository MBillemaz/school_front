package com.ynov.mbi.finalproject_mbi.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


import com.ynov.mbi.finalproject_mbi.R;


public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        findViewById(R.id.btn_map).setOnClickListener(genericOnClickListener);
        findViewById(R.id.btn_list).setOnClickListener(genericOnClickListener);
        findViewById(R.id.btn_config).setOnClickListener(genericOnClickListener);
        findViewById(R.id.btn_add).setOnClickListener(genericOnClickListener);

    }


    final View.OnClickListener genericOnClickListener = new View.OnClickListener() {
        public void onClick(final View v) {
            Intent i = null;
            //On séléctionne l'activité en fonction du bouton selectionné
            switch (v.getId()) {
                case R.id.btn_map:
                    i = new Intent(MenuActivity.this, MapsActivity.class);
                    break;
                case R.id.btn_add:
                    i = new Intent(MenuActivity.this, CreationActivity.class);
                    break;
                case R.id.btn_config:
                    i = new Intent(MenuActivity.this, ConfigActivity.class);
                    break;
                case R.id.btn_list:
                    i = new Intent(MenuActivity.this, ListActivity.class);
                    break;
                case R.id.back_button:
                    finish();
                    break;
                default:
                    break;
            }

            MenuActivity.this.startActivity(i);
        }
    };

}
