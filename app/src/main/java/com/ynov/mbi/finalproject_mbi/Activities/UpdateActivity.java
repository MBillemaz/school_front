package com.ynov.mbi.finalproject_mbi.Activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ynov.mbi.finalproject_mbi.Controllers.RequestInterface;
import com.ynov.mbi.finalproject_mbi.Fragments.ActionBar;
import com.ynov.mbi.finalproject_mbi.Interfaces.EcoleService;
import com.ynov.mbi.finalproject_mbi.Models.Ecole;
import com.ynov.mbi.finalproject_mbi.Models.JsonResponseEcole;
import com.ynov.mbi.finalproject_mbi.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateActivity extends AppCompatActivity implements ActionBar.OnFragmentInteractionListener, AdapterView.OnItemSelectedListener {

    private EcoleService ecoleService;
    private Ecole school;

    ArrayAdapter<CharSequence> adapter;

    private EditText editName;
    private EditText editaddress;
    private EditText editNbStud;
    private EditText editLat;
    private EditText editLong;
    private EditText editMail;
    private EditText editPostal;
    private EditText editHoraires;
    private EditText editTel;
    private EditText editCommune;
    private Spinner editType;
    private ArrayList<EditText> listEdit = new ArrayList<EditText>();

    private Button buttonValid;
    private Button buttonCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation);

        ecoleService = RequestInterface.getInterface(EcoleService.class);

        //Mise en place du spinner de type
        editType = findViewById(R.id.inputType);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editType.setAdapter(adapter);
        editType.setOnItemSelectedListener(this);

        editName = findViewById(R.id.inputName);
        listEdit.add(editName);
        editaddress = findViewById(R.id.inputAddress);
        listEdit.add(editaddress);
        editNbStud = findViewById(R.id.inputNbStud);
        listEdit.add(editNbStud);
        editLat = findViewById(R.id.inputLat);
        listEdit.add(editLat);
        editLong = findViewById(R.id.inputLong);
        listEdit.add(editLong);
        editMail = findViewById(R.id.inputMail);
        listEdit.add(editMail);
        editPostal = findViewById(R.id.inputCode);
        listEdit.add(editPostal);
        editHoraires = findViewById(R.id.inputTimes);
        listEdit.add(editHoraires);
        editTel = findViewById(R.id.inputPhone);
        listEdit.add(editTel);
        editCommune = findViewById(R.id.inputCity);
        listEdit.add(editCommune);

        buttonValid = findViewById(R.id.btnValid);
        buttonCancel = findViewById(R.id.btnCancel);


        //On récupère l'école donnée en extra et on préremplis les champs avec
        Intent i = getIntent();
        school = (Ecole) i.getSerializableExtra("school");

        populateDatas(school, adapter);

        //Mise en place de l'actionbar
        Fragment actionBar = new ActionBar();
        Bundle arguments = new Bundle();
        arguments.putString("title", "Modification " + school.getName());
        arguments.putString("menu", "ecoleUpdate");
        actionBar.setArguments(arguments);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, actionBar).commit();

        buttonValid.setOnClickListener(genericOnClickListener);
        buttonCancel.setOnClickListener(genericOnClickListener);

    }

    private void populateDatas(Ecole school, ArrayAdapter<CharSequence> adapter) {

        editName.setText(school.getName());
        editaddress.setText(school.getAddress());
        editNbStud.setText(Integer.toString(school.getNb_student()));
        editLat.setText(Double.toString(school.getLatitude()));
        editLong.setText(Double.toString(school.getLongitude()));
        editMail.setText(school.getMail());
        editPostal.setText(school.getPostal_code());
        editHoraires.setText(school.getHoraire());
        editTel.setText(school.getPhone_number());
        editCommune.setText(school.getCommune());
        editType.setSelection(adapter.getPosition(school.getStatus()));

    }

    final View.OnClickListener genericOnClickListener = new View.OnClickListener() {
        public void onClick(final View v) {
            switch (v.getId()) {
                case R.id.btnValid:
                    //On récupère l'id de l'école sélectionnée, et on envoie tous les champs au back pour la mise à jour
                    boolean continu = true;
                    CharSequence errorMessage = getText(R.string.empty_champ);
                    for(EditText edit : listEdit){
                        Log.e("text", edit.getText().toString());
                        if(edit.getText().toString().equals("")){
                            continu = false;
                            edit.setError(errorMessage);
                        }
                    }
                    //On ne continue que si il n'y a pas d'erreur
                    if(continu){
                        final Ecole newSchool = new Ecole(
                                school.getId(),
                                editName.getText().toString(),
                                editaddress.getText().toString(),
                                Integer.parseInt(editNbStud.getText().toString()),
                                editType.getSelectedItem().toString(),
                                Double.parseDouble(editLat.getText().toString()),
                                Double.parseDouble(editLong.getText().toString()),
                                editMail.getText().toString(),
                                editPostal.getText().toString(),
                                editHoraires.getText().toString(),
                                editTel.getText().toString(),
                                editCommune.getText().toString());
                        buttonValid.setEnabled(false);
                        ecoleService.updateEcole(newSchool.getId(), newSchool).enqueue(new Callback<JsonResponseEcole>() {
                            @Override
                            public void onResponse(Call<JsonResponseEcole> call, Response<JsonResponseEcole> response) {

                                if (response.code() == 200) {
                                    //On renvoie la nouvelle école et on quitte l'application
                                    Toast.makeText(UpdateActivity.this, "Ecole modifiée", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent();
                                    i.putExtra("school", newSchool);
                                    setResult(1, i);
                                    finish();
                                } else {
                                    Toast.makeText(UpdateActivity.this, "Erreur : " + response.body().getError(), Toast.LENGTH_LONG).show();
                                }
                                buttonValid.setEnabled(true);
                            }

                            @Override
                            public void onFailure(Call<JsonResponseEcole> call, Throwable t) {
                                Toast.makeText(UpdateActivity.this, "Erreur, impossible de contacter le back", Toast.LENGTH_LONG).show();
                                buttonValid.setEnabled(true);
                            }
                        });
                    }

                    break;
            }
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onFragmentInteraction(String test) {

    }
}
