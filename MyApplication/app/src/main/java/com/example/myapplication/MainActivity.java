package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.myapplication.BotaoJogo;

public class MainActivity extends AppCompatActivity {

    BotaoJogo[] btn = new BotaoJogo[9];
    int[] btnIDs = {R.id.bt1, R.id.bt2, R.id.bt3, R.id.bt4, R.id.bt5, R.id.bt6, R.id.bt7, R.id.bt8, R.id.bt9};
    int taps = 0;
    TextView toques;

    private void buttonSetup() {

        for (int i = 0; i < btn.length; i++)
            btn[i] = findViewById(btnIDs[i]);

        btn[0].addNeigh(new BotaoJogo[]{btn[1],btn[3],null,null});
        btn[1].addNeigh(new BotaoJogo[]{btn[0],btn[2],btn[4],null});
        btn[2].addNeigh(new BotaoJogo[]{btn[1],btn[5],null,null});
        btn[3].addNeigh(new BotaoJogo[]{btn[0],btn[4],btn[6],null});
        btn[4].addNeigh(new BotaoJogo[]{btn[1],btn[3],btn[5],btn[7]});
        btn[5].addNeigh(new BotaoJogo[]{btn[2],btn[4],btn[8],null});
        btn[6].addNeigh(new BotaoJogo[]{btn[3],btn[7],null,null});
        btn[7].addNeigh(new BotaoJogo[]{btn[4],btn[6],btn[8],null});
        btn[8].addNeigh(new BotaoJogo[]{btn[5],btn[7],null,null});

        for (BotaoJogo bt : btn) {
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bt.trocaCor(true);
                    checaFull();
                }
            });
            bt.randomize();
        }
    }

    public void checaFull() {
        taps++;

        toques.setText(String.format("Toques: %d",taps));
        for (BotaoJogo bt : btn) {
            if (!bt.getOn()) return;
        }
        AlertDialog.Builder win = new AlertDialog.Builder(MainActivity.this);
        win.setTitle(R.string.app_name);
        win.setMessage("Você ganhou! Clique para jogar de novo.\nToques utilizados: "+taps);
        win.setPositiveButton("Jogar",new DialogInterface.OnClickListener(){
            @Override public void onClick(DialogInterface dIn, int which) {
                buttonSetup(); taps = 0; toques.setText(String.format("Toques: %d",taps));
                }});
        win.show();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toques = findViewById(R.id.textToques);

        buttonSetup();

    }
}
