package com.aironbruce.registroscep.running.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.aironbruce.registroscep.R;
import com.aironbruce.registroscep.database.LocalDatabaseManager;
import com.aironbruce.registroscep.otherclasses.CEP;
import com.aironbruce.registroscep.otherclasses.CEPService;
import com.aironbruce.registroscep.otherclasses.Localizacao;
import com.aironbruce.registroscep.running.objects.QuickDialog;
import com.aironbruce.registroscep.running.objects.IPassaCEP;
import com.aironbruce.registroscep.running.objects.MapsFragment;
import com.google.android.gms.maps.model.LatLng;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InputActivity extends AppCompatActivity implements IPassaCEP {

    private static final String TAG = InputActivity.class.getSimpleName();

    private Button btnAdd;
    private TextView textCEP;
    private EditText inputCEP;

    private CEP cepSelect;
    private Localizacao local;
    private Retrofit retrofit;

    private LatLng LL = new LatLng(0,0);

    private MapsFragment mF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        btnAdd = findViewById(R.id.btnAdd);
        textCEP = findViewById(R.id.txtCEPResult);
        inputCEP = findViewById(R.id.inputNome);

        String urlCep = "https://viacep.com.br/ws/";
        retrofit =  new Retrofit.Builder().baseUrl(urlCep)
                .addConverterFactory(GsonConverterFactory.create()).build();

        //Buscar CEP em uma thread
        Button btnCEP = findViewById(R.id.btnCep);
        btnCEP.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick (View view){

                textCEP.setText(R.string.searching);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        boolean conectado = false;
                        try {conectado = Runtime.getRuntime().exec(
                                "ping -c 1 google.com").waitFor() == 0;
                        } catch (Exception e) {
                            Log.e(TAG,"Não conectado à internet", e);
                        }

                        if (!conectado) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textCEP.setText(R.string.no_connection);
                                    AlertDialog.Builder builder = new
                                            QuickDialog(InputActivity.this,
                                            R.integer.NO_CONNECTION).build();
                                    builder.show();
                                }
                            });
                        }
                        else pesquisarCep();
                    }
                }).start();
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.mapFragment, MapsFragment.class, null)
                    .commit();
        }

        //Salvar novo local no banco de dados
        btnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                LocalDatabaseManager LDM = new LocalDatabaseManager(getApplicationContext());
                local = new Localizacao(cepSelect, getApplicationContext());
                local.setLatLng(LL);

                if (LDM.salvar(local)) {
                    local = new Localizacao(cepSelect, getApplicationContext());
                    local.setLatLng(LL);
                    Toast.makeText(getApplicationContext(), R.string.create_suc,
                            Toast.LENGTH_SHORT).show();

                    //Abrir o local em uma EditActivity, para opcionalmente trocar o nome e imagem
                    Intent intent = new Intent(InputActivity.this, EditActivity.class);
                    intent.putExtra("Local", local);
                    finish();
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.create_fail,
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    private void pesquisarCep(){

        CEPService cepService = retrofit.create(CEPService.class);
        String sCEP = inputCEP.getText().toString();
        Call<CEP> call = cepService.recuperarCEP(sCEP);
        call.enqueue(new Callback<CEP>() {

            @Override
            public void onResponse(@NonNull Call<CEP> call, @NonNull Response<CEP> response) {

                if(response.isSuccessful()){
                    CEP cep = response.body();
                    if (cep != null) {

                        cepSelect = cep;
                        textCEP.setText(cep.toString());
                        mF.passa(cep);
                        btnAdd.setVisibility(View.VISIBLE);
                    } else textCEP.setText(R.string.cep_not_found);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CEP> call, @NonNull Throwable t) {
                textCEP.setText(R.string.cep_not_found);
            }
        });

    }
    @Override
    public void passa(CEP cep) {}

    @Override
    public void escutaMapa(MapsFragment mF) {this.mF = mF;}

    @Override
    public void sendLL(LatLng latLng) {this.LL = latLng;}
}