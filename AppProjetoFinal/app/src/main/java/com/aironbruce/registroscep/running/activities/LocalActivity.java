package com.aironbruce.registroscep.running.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.aironbruce.registroscep.R;
import com.aironbruce.registroscep.databinding.ActivityLocalBinding;
import com.aironbruce.registroscep.otherclasses.CEP;
import com.aironbruce.registroscep.otherclasses.ImageViewDynamic;
import com.aironbruce.registroscep.otherclasses.Localizacao;
import com.aironbruce.registroscep.running.objects.IPassaCEP;
import com.aironbruce.registroscep.running.objects.MapsFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LocalActivity extends AppCompatActivity implements IPassaCEP {

    private TextView nomeLoc, cep;
    private ImageViewDynamic photoLoc;
    private Space spc;

    private Localizacao local;

    private MapsFragment mF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.aironbruce.registroscep.databinding.ActivityLocalBinding binding = ActivityLocalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Carregar os dados do local enviado por intent
        local = getIntent().getParcelableExtra("Local");

        nomeLoc = findViewById(R.id.txtLoc);
        photoLoc = findViewById(R.id.imgLoc);

        spc = findViewById(R.id.spc);

        cep = findViewById(R.id.txtCep);
        TextView cepLogr = findViewById(R.id.cepLogr);
        TextView cepComp = findViewById(R.id.cepComp);
        TextView cepBair = findViewById(R.id.cepBair);
        TextView cepLoca = findViewById(R.id.cepLoca);
        TextView cepUf = findViewById(R.id.cepUf);

        TextView data = findViewById(R.id.dataCriado);
        TextView dataEdit = findViewById(R.id.dataEditado);

        //Dados
        nomeLoc.setText(local.getNome());

        cep.setText(local.getCep().getCep() != null ?
                local.getCep().getCep() : getString(R.string.undefined));

        cepLogr.setText(local.getCep().getLogradouro() != null ?
                local.getCep().getLogradouro() : getString(R.string.undefined));

        cepComp.setText(local.getCep().getComplemento() != null ?
                local.getCep().getComplemento() : getString(R.string.undefined));

        cepBair.setText(local.getCep().getBairro() != null ?
                local.getCep().getBairro() : getString(R.string.undefined));

        cepLoca.setText(local.getCep().getLocalidade() != null ?
                local.getCep().getLocalidade() : getString(R.string.undefined_f));

        cepUf.setText(local.getCep().getUf() != null ?
                local.getCep().getUf() : getString(R.string.undefined));

        //Traduzir as datas
        SimpleDateFormat locFormat = new SimpleDateFormat(getString(R.string.date_format));
        SimpleDateFormat curFormat = new SimpleDateFormat(getString(R.string.cur_date_format));
        try {
            Date crDate = locFormat.parse(local.getData());
            Date edDate = locFormat.parse(local.getDataEdit());
            data.setText(curFormat.format(crDate));
            dataEdit.setText(curFormat.format(edDate));
        } catch (Exception e) {
            data.setText(local.getData());
            dataEdit.setText(local.getDataEdit());
        }

        //Puxar imagem (se existe)
        File img = new File(local.getFotoPath());
        if (img.exists()) {
            Bitmap bm = BitmapFactory.decodeFile(img.getAbsolutePath());
            BitmapDrawable bmd = new BitmapDrawable(getResources(), bm);
            bmd.getPaint().setFilterBitmap(false);
            photoLoc.setImageDrawable(bmd);
            photoLoc.setRotation(local.getRotation());
        } else Log.w((local.getNome() + " - " + local.getID()),"Imagem não encontrada!");

        //Definir a escala do espaço vazio, onde a imagem (usada como plano de fundo) fica visível
        spc.post(new Runnable() {
            @Override
            public void run() {
                int pHgt = photoLoc.getMeasuredWidth();
                int tHgt = nomeLoc.getHeight() + cep.getHeight();
                spc.setLayoutParams(new LinearLayout.LayoutParams(pHgt, pHgt - tHgt));
            }
        });

        //Definir local do mapa
        mF.sendLL(local.getLatlng());
        //Editar dados
        FloatingActionButton fab = findViewById(R.id.btnEdit);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditActivity.class);
                intent.putExtra("Local", local);
                finish();
                startActivity(intent);
            }
        });
    }

    @Override
    public void passa(CEP cep) {}

    @Override
    public void escutaMapa(MapsFragment mF) { this.mF = mF; }

    @Override
    public void sendLL(LatLng latLng) {}

}