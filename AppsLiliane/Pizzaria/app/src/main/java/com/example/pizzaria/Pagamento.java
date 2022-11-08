package com.example.pizzaria;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class Pagamento extends AppCompatActivity {

    Button btvoltar;
    double total;
    TextView txtpagar;
    RadioGroup grupo;
    RadioButton rbpix,rbdinheiro, rbcartao;
    String saida, txtpizza;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagamento);
        btvoltar = findViewById(R.id.btVoltar);
        btvoltar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {finish();}
        });
        txtpagar = findViewById(R.id.edtpagar);
        Bundle params = getIntent().getExtras();
        if (params != null){
            total = params.getDouble("total");
            txtpagar.setText(String.format("Total a Pagar $%5.2f",total));
            txtpizza = params.getString("pizzas");
        }
        grupo = findViewById(R.id.grupo);
        rbdinheiro = findViewById(R.id.rbDindin);
        rbpix = findViewById(R.id.rbPix);
        rbcartao = findViewById(R.id.rbCartao);
        grupo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup grupo, int i){
                    saida = "";
                    if (rbdinheiro.isChecked()) saida = "Pagamento em Dinheiro";
                    else if (rbpix.isChecked()) saida = "Pagamento via Pix";
                    else saida = "Pagamento através de Cartão";
                    saida += ("\n" + txtpizza);

                    AlertDialog.Builder alerta = new AlertDialog.Builder(Pagamento.this);
                    alerta.setIcon(R.mipmap.minipizza);
                    alerta.setTitle("Forma de Pagamento");
                    String textao = String.format("%s\nPreço R$%5.2f", saida, total);
                    alerta.setMessage(textao);
                    alerta.setNeutralButton("OK", null);
                    alerta.show();
                }
        });
    }
}