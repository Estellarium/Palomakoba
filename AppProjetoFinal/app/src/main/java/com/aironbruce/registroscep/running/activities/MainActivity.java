package com.aironbruce.registroscep.running.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aironbruce.registroscep.R;
import com.aironbruce.registroscep.database.DbHelper;
import com.aironbruce.registroscep.database.LocalAdapter;
import com.aironbruce.registroscep.database.LocalDatabaseManager;
import com.aironbruce.registroscep.databinding.ActivityMainBinding;
import com.aironbruce.registroscep.otherclasses.Localizacao;
import com.aironbruce.registroscep.running.objects.MyBroadcastReceiver;
import com.aironbruce.registroscep.running.objects.QuickDialog;
import com.aironbruce.registroscep.running.objects.RecyclerItemClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("CommentedOutCode")
public class MainActivity extends AppCompatActivity {

  //private static final String [] permissoes = new String[]
          //{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

    private List<Localizacao> locais;
    private Localizacao locSel;

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private Sensor accelerometer;
    private SensorManager sensorManager;
    private SensorEventListener sensorEventListener;
    private float x = 0, y = 0, z = 0;

    private int THEME;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.aironbruce.registroscep.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Carregar tema
        SharedPreferences pref = this.getPreferences(Context.MODE_PRIVATE);
        THEME = pref.getInt("tema",AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        AppCompatDelegate.setDefaultNightMode(THEME);

        //Botão de ajuda
        Button btnHelp = findViewById(R.id.btnHelp);
        btnHelp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new
                        QuickDialog(MainActivity.this,
                        R.integer.HELP).build();
                builder.show();
            }
        });

        //Botão de seleção de tema
        Button btnTheme = findViewById(R.id.btnTheme);
        btnTheme.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                openPopUp(view);
            }
        });

        locais = new ArrayList<>();

        //Botão de adicionar novo local
        FloatingActionButton fab = findViewById(R.id.btnNew);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {//Originalmente, iria pedir permissão para localização,
                //mas não foi necessário.

                if (locais.size() >= 9999) {
                    AlertDialog.Builder builder = new
                        QuickDialog(MainActivity.this,
                        R.integer.TOO_MANY_ITEMS).build();
                    builder.show();
                } else {//if (Permissoes.validarPermissoes(permissoes, this, 1)) {
                    Intent intent = new Intent(getApplicationContext(), InputActivity.class);
                    startActivity(intent);
                    /*
                } else {
                    AlertDialog.Builder builder = new
                        QuickDialog(MainActivity.this,
                        R.integer.LOCATION_PERMISSION_DENIED).build();
                    builder.show();
                    */
                }
            }
        });

        //Métodos do RecyclerView
        recyclerView = findViewById(R.id.recView);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(),
                        recyclerView, new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {

                locSel = locais.get(position);

                new Thread(new Runnable() {//Carregar os locais por uma thread
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this, LocalActivity.class);
                        intent.putExtra("Local", locSel);
                        startActivity(intent);
                    }
                }).start();
            }

            @Override
            public void onLongItemClick(View view, int position) {

                locSel = locais.get(position);

                //Apagar local
                AlertDialog.Builder builder = new
                        QuickDialog(MainActivity.this,
                        R.integer.CONFIRM_DELETE_LOCATION,
                        false).build();
                builder.setMessage(getString(R.string.local_deletion, locSel.getNome()));
                builder.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        LocalDatabaseManager LDM = new LocalDatabaseManager(getApplicationContext());
                        if (LDM.apagar(locSel)){
                            carregarLocais();
                            Toast.makeText(getApplicationContext(), R.string.delete,
                                    Toast.LENGTH_SHORT).show();
                        }
                        else Toast.makeText(getApplicationContext(), R.string.del_fail,
                                    Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton(R.string.nao, null);
                builder.show();
            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {}

        }));

        //Apagar todos os locais
        Button btnEliminate = findViewById(R.id.btnEliminate);
        btnEliminate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (locais.size() < 1) {
                    Toast.makeText(getApplicationContext(), R.string.no_data,
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                AlertDialog.Builder builder = new
                        QuickDialog(MainActivity.this,
                        R.integer.CONFIRM_DELETE_DATABASE,
                        false).build();
                builder.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DbHelper deletar = new DbHelper(getApplicationContext());
                        if (deletar.reset()) {
                        Toast.makeText(getApplicationContext(), R.string.delete,
                                Toast.LENGTH_SHORT).show();
                        recreate();
                        } else Toast.makeText(getApplicationContext(), R.string.del_fail,
                                Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton(R.string.nao, null);
                builder.show();

            }
        });

        //Sensor acelerômetro
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (accelerometer != null) {

            sensorEventListener = new SensorEventListener() {

                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {

                    x = sensorEvent.values[0];
                    y = sensorEvent.values[1];
                    z = sensorEvent.values[2];

                    if (x > 9 || x < -9 || z < -8) {
                        Toast.makeText(getApplicationContext(), R.string.goodbye,
                                Toast.LENGTH_SHORT).show();
                        finish(); System.exit(0);
                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {}

            };
        } else Log.w("Dispositivo " + android.os.Build.MODEL, "Não há acelerômetro");

        IntentFilter filtro = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        getApplicationContext().registerReceiver(new MyBroadcastReceiver(), filtro);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
    }

    //Janela de seleção de tema
    public void openPopUp(View view) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.pick_theme, null);

        int bounds = LinearLayout.LayoutParams.MATCH_PARENT;
        final PopupWindow popupWindow = new PopupWindow(popupView, bounds, bounds, true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        RadioGroup rG = popupView.findViewById(R.id.radioGp);
        switch(THEME) {
            case AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM: rG.check(R.id.themeAuto); break;
            case AppCompatDelegate.MODE_NIGHT_NO: rG.check(R.id.themeLight); break;
            case AppCompatDelegate.MODE_NIGHT_YES: rG.check(R.id.themeNight); break;
        }

        popupView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {popupWindow.dismiss();}
        });

        SharedPreferences pref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        rG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                int selecTheme;
                switch (rG.getCheckedRadioButtonId()) {
                    case R.id.themeLight: selecTheme = AppCompatDelegate.MODE_NIGHT_NO; break;
                    case R.id.themeNight: selecTheme = AppCompatDelegate.MODE_NIGHT_YES; break;
                    default: selecTheme = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM; break;
                }
                editor.putInt("tema", selecTheme); editor.apply();
                AppCompatDelegate.setDefaultNightMode(selecTheme);
            }
        });

        Button btnPickTheme = popupView.findViewById(R.id.btnPickTheme);
        btnPickTheme.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                THEME = AppCompatDelegate.getDefaultNightMode();
                editor.putInt("tema", THEME); editor.apply();
                popupWindow.dismiss();
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                editor.putInt("tema", THEME); editor.apply();
                AppCompatDelegate.setDefaultNightMode(THEME);
            }
        });
    }

    //Carregar locais do SQL no RecyclerView
    public void carregarLocais() {

        LocalDatabaseManager LDM = new LocalDatabaseManager(getApplicationContext());
        locais = LDM.listar();
        LocalAdapter adapter = new LocalAdapter(locais);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {carregarLocais();}
                });
            }
        }).start();
        super.onStart();
    }
}