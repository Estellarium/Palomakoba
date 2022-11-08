package com.example.pizzaria;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class MontarPizza extends AppCompatActivity {

    /*Button plus, minus, add;
    RadioGroup tamanho;
    RadioButton tP, tM, tG;
    TextView tipo, qtd, valor;
    int qtdPizzas;
    float preco, coef;
    String tipoPizza; char tam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_montar_pizza);

        tipo = findViewById(R.id.tipoPizza);
        qtd = findViewById(R.id.qtd);
        valor = findViewById(R.id.valor);
        plus = findViewById(R.id.btInc);
        minus = findViewById(R.id.btDec);
        add = findViewById(R.id.btAdd);
        tamanho = findViewById(R.id.tamPizza);
        tP = findViewById(R.id.pizzaP);
        tM = findViewById(R.id.pizzaM);
        tG = findViewById(R.id.pizzaG);

        Bundle params = getIntent().getExtras();
        if (params != null){
            tipoPizza = params.getString("tipo");
            preco = params.getFloat("precoBasico");
            coef = params.getFloat("coefTamanho");
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int opcTam = tamanho.getCheckedRadioButtonId();
                switch (opcTam) {
                    case R.id.pizzaP: tam = 'P'; break;
                    case R.id.pizzaM: tam = 'M'; break;
                    case R.id.pizzaG: tam = 'G'; break;
                    default: tam = 'P'; break;
                }
                Pizza pizza = new Pizza(tipoPizza, preco, coef, tam);
                Intent result = new Intent();
                result.putExtra("Pizza", Pizza.toString());
                setResult(Activity.RESULT_OK, result);
                finish();
            }
        });


    }*/
}