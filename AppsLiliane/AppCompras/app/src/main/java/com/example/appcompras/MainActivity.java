package com.example.appcompras;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RadioGroup rgProdutos,rgDesconto;
    private CheckBox ckArroz,ckCarne,ckPao,ckLeite,ckOvos;
    private RadioButton zero,cinco,dez,quinze;
    private TextView txtValor;
    private Button btnTotal,btnPagar;
    private EditText edtValor;
    private float valor = 0;

    //Abreviação do Toast
    private void Toasty(String msg) {
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
    }

    //Somar os valores marcados
    private boolean somarValor(CheckBox[] checks, Map<CheckBox, Float> precos) {
        valor = 0;
        for (CheckBox x : checks)
            if (x.isChecked()) valor += precos.get(x);
        if (valor == 0) {
            Toasty("Escolha ao menos um produto!");
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find View by ID
        rgProdutos= findViewById(R.id.rgProdutos);
        rgDesconto= findViewById(R.id.rgDesconto);
        ckArroz   = findViewById(R.id.ckArroz);
        ckCarne   = findViewById(R.id.ckCarne);
        ckPao     = findViewById(R.id.ckPao);
        ckLeite   = findViewById(R.id.ckLeite);
        ckOvos    = findViewById(R.id.ckOvos);
        zero      = findViewById(R.id.rb0);
        cinco     = findViewById(R.id.rb5);
        dez       = findViewById(R.id.rb10);
        quinze    = findViewById(R.id.rb15);
        txtValor  = findViewById(R.id.txtValor);
        btnTotal  = findViewById(R.id.btnTotal);
        btnPagar  = findViewById(R.id.btnPagar);
        edtValor  = findViewById(R.id.edtValor);

        //Criar mapa de valores
        CheckBox[] checks = {ckArroz,ckCarne,ckPao,ckLeite,ckOvos};
        Map <CheckBox, Float> precos = new HashMap<CheckBox, Float>();
        precos.put(ckArroz, 3.5f);
        precos.put(ckCarne, 12.3f);
        precos.put(ckPao, 2.2f);
        precos.put(ckLeite, 5.5f);
        precos.put(ckOvos, 7.5f);

        //Gerar valor total (opcional)
        btnTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (somarValor(checks,precos)) return;
                String total = String.format("Valor: %.2f",valor);
                txtValor.setText(total);
            } //os valores são floats para melhor formatação
        });

        //Pagar
        btnPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Definir valor caso não clicar em Total
                if (somarValor(checks,precos)) return;
                float desc = 0;

                //Definir desconto
                switch (rgDesconto.getCheckedRadioButtonId()) {
                    case R.id.rb5 : desc = 5 ; break;
                    case R.id.rb10: desc = 10; break;
                    case R.id.rb15: desc = 15; break;
                    default: desc = 0;
                }
                float valDesc = ((1-(desc/100))*valor);

                //Coletar valor pago
                String unparsed = edtValor.getText().toString();
                if (unparsed.isEmpty()) {Toasty("Insira um valor!"); return;}
                float pago = Float.parseFloat(unparsed);
                if (pago < valDesc) {Toasty("Valor insuficiente!"); return;}

                //Popup final
                AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
                alerta.setIcon(R.mipmap.shoppingcircle);
                alerta.setTitle("Compra realizada com sucesso!");
                String result = String.format(
                        "Valor da compra: R$%.2f\n" +
                        "Desconto: %.0f%s\n" +
                        "Valor com desconto: R$%.2f\n" +
                        "Valor pago: R$%.2f\n" +
                        "Troco: R$%.2f",
                        valor,desc,"%",valDesc,pago,(pago-valDesc)
                        );
                alerta.setMessage(result);
                alerta.setNeutralButton("OK", null);
                alerta.show();
            }
        });
    }
}