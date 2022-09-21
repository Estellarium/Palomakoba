package com.aironbruce.registroscep.running.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.aironbruce.registroscep.R;
import com.aironbruce.registroscep.database.LocalDatabaseManager;
import com.aironbruce.registroscep.otherclasses.Localizacao;
import com.aironbruce.registroscep.running.objects.QuickDialog;
import com.aironbruce.registroscep.running.objects.Permissoes;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditActivity extends AppCompatActivity {

    private static final String[] permissoes = new String[]
            {Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int REQUEST_CODE = 0, PICK_IMAGE = 100;

    private TextView txtImgPath;
    private EditText inNome;
    private ImageView imgLoc;
    private Button btnRot;

    private Localizacao local;

    private String curPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //Puxar os dados iniciais do local
        local = getIntent().getParcelableExtra("Local");

        inNome = findViewById(R.id.inputNome);
        imgLoc = findViewById(R.id.imgSelec);
        txtImgPath = findViewById(R.id.txtImgPath);

        curPath = local.getFotoPath();

        File img = new File(local.getFotoPath());
        if(img.exists()) {
            Bitmap bm = BitmapFactory.decodeFile(img.getAbsolutePath());
            imgLoc.setImageBitmap(bm);
            imgLoc.setRotation(local.getRotation());
        } else
            Log.w((local.getNome() + " - " + local.getID()),"Imagem não encontrada!");

        String truncatedPath = curPath.replace("/storage/emulated/0/", "");
        if (!truncatedPath.equals("")) txtImgPath.setText(truncatedPath);

        inNome.setText(local.getNome());

        //Abrir a galeria para escolher uma imagem
        Button btnPick = findViewById(R.id.btnPick);
        btnPick.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (Permissoes.validarPermissoes(permissoes, EditActivity.this, REQUEST_CODE))
                    galeria();
            }
        });

        //Girar a imagem
        btnRot = findViewById(R.id.btnRot);
        btnRot.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(img.exists())
                    imgLoc.setRotation(local.rotate());
            }
        });

        //Atualizar os dados do local
        Button btnEdit = findViewById(R.id.btnSave);
        btnEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                local.setNome(inNome.getText().toString());
                local.setFotoPath(curPath);
                SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.date_format));
                local.setDataEdit(sdf.format(new Date()));

                LocalDatabaseManager LDM = new LocalDatabaseManager(getApplicationContext());

                if (LDM.editar(local))
                     Toast.makeText(getApplicationContext(), R.string.edit_suc,
                            Toast.LENGTH_SHORT).show();
                else Toast.makeText(getApplicationContext(), R.string.edit_fail,
                            Toast.LENGTH_SHORT).show();

                new Thread(new Runnable() {
                    @Override
                    public void run() { //Abrir o local na LocalActivity
                        Intent intent = new Intent(EditActivity.this,
                                LocalActivity.class);
                        intent.putExtra("Local", local);
                        finish();
                        startActivity(intent);
                    }
                }).start();
            }
        });
    }

    //Deprecado, mas não sei outros métodos para coletar o resultado da galeria.
    private void galeria() {
        Intent galeria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(galeria, PICK_IMAGE);
    }

    //Dialog se as permissões forem negadas
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE)
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) galeria();
            else {
                AlertDialog.Builder builder = new
                        QuickDialog(EditActivity.this,
                        R.integer.GALLERY_PERMISSION_DENIED).build();
                builder.show();
            }
    }

    //Resultado da galeria
    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            Uri uri = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
            cursor.moveToFirst();
            curPath = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
            cursor.close();

            String truncatedPath = curPath.replace("/storage/emulated/0/", "");
            txtImgPath.setText(truncatedPath);
            Bitmap bitmap = BitmapFactory.decodeFile(curPath);
            imgLoc.setImageBitmap(bitmap);
            local.setRotation(0);
            imgLoc.setRotation(0);
            btnRot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imgLoc.setRotation(local.rotate());
                }
            });
        }
    }
}