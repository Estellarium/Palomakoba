package com.example.pizzaria;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class MainActivity extends AppCompatActivity {
    Button btlimpar, btpagar;
    double total;
    String pedido = "";
    CheckBox ckcalabresa, ckpalmito, ckmargarita, ck4queijos, ckmodacasa;
    LinearLayout opcs;
    Button btCal;

    private Bundle btClick(String tipo, float preco, float coef) {
        Bundle pparams = new Bundle();
        pparams.putString("tipo", tipo);
        pparams.putFloat("preco", preco);
        pparams.putFloat("coef", coef);
        return pparams;
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (MY_CHILD_ACTIVITY) : {
                if (resultCode == Activity.RESULT_OK) {
                    pedido += data.getStringExtra("Pizza");
                }
                break;
            }
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btlimpar = findViewById(R.id.btnlimpar);
        btpagar = findViewById(R.id.btnpagar);
        ckcalabresa = findViewById(R.id.ckcalabresa);
        ckpalmito = findViewById(R.id.ckpalmito);
        ckmargarita = findViewById(R.id.ckmargarita);
        ck4queijos = findViewById(R.id.ck4queijos);
        ckmodacasa = findViewById(R.id.ckmodacasa);
        //opcs = findViewById(R.id.opcoesPizza);
            //btCal = findViewById(R.id.btCalabresa);


        /*btCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), MontarPizza.class);
                Bundle pparams = btClick("Calabresa",10,1);
                intent.putExtras(pparams);
                startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE);
            }
        });*/


        btlimpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                total = 0;
                ckcalabresa.setChecked(false);
                ckpalmito.setChecked(false);
                ckmargarita.setChecked(false);
                ck4queijos.setChecked(false);
                ckmodacasa.setChecked(false);
            }
        });

        btpagar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                total = 0;
                if (ckcalabresa.isChecked()) {total += 70; pedido += "Calabresa\n";}
                if (ckpalmito.isChecked()) {total += 70; pedido += "Palmito\n";}
                if (ckmargarita.isChecked()) {total += 70; pedido += "Margarita\n";}
                if (ck4queijos.isChecked()) {total += 85; pedido += "Quatro Queijos\n";}
                if (ckmodacasa.isChecked()) {total += 85; pedido += "Moda da Casa\n";}
                String msg = String.format("Total Pedido= R$%5.2f", total);
                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
                Intent it = new Intent(getBaseContext(), Pagamento.class);
                Bundle params = new Bundle();
                params.putDouble("total", total);
                params.putString("pizzas", pedido);
                it.putExtras(params);
                startActivity(it);
            }
        });

        /*
        * btx.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            * Intent it = new Intent(getBaseContext(), Pagamento.class);
              Bundle params = new Bundle();
              params.putString("Pizza", btx.getText());
              it.putExtras(params);
              startActivity(it);
            }
        * */
    }
}
