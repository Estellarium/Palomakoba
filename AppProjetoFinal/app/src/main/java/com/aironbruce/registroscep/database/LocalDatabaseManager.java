package com.aironbruce.registroscep.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.aironbruce.registroscep.otherclasses.CEP;
import com.aironbruce.registroscep.otherclasses.Localizacao;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class LocalDatabaseManager implements ILDM {

    private final SQLiteDatabase leitura, escrita;
    private static final String TAG = LocalDatabaseManager.class.getSimpleName();

    public LocalDatabaseManager(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        leitura = dbHelper.getReadableDatabase();
        escrita = dbHelper.getWritableDatabase();
    }

    @Override
    public boolean salvar(Localizacao local) {

        ContentValues cv = new ContentValues();
        cv.put("nome", local.getNome());
        cv.put("data", local.getData());
        cv.put("edit", local.getDataEdit());
        cv.put("cep", local.getCep().getCep());
        cv.put("logradouro", local.getCep().getLogradouro());
        cv.put("complemento", local.getCep().getComplemento());
        cv.put("bairro", local.getCep().getBairro());
        cv.put("localidade", local.getCep().getLocalidade());
        cv.put("uf", local.getCep().getUf());
        cv.put("lat", local.getLatOrLng(true));
        cv.put("lng", local.getLatOrLng(false));
        cv.put("path", local.getFotoPath());
        cv.put("rot", local.getRotation());

        try {this.escrita.insert(DbHelper.TABELA_LOCAIS, null, cv);}
        catch (Exception e) {
            Log.e(TAG,"Não foi possível salvar", e);
            return false;
        }
        return true;
    }

    @SuppressLint("Range")
    public long getLastID() {

        String sql = "SELECT * FROM " + DbHelper.TABELA_LOCAIS + ";";
        Cursor cursor = leitura.rawQuery(sql, null);

        cursor.moveToLast();

        long locID = 0;
        try {locID = cursor.getLong(cursor.getColumnIndex("ID"));}
        catch (Exception ignored) {}

        cursor.close();

        return locID;
    }

    @Override
    public boolean editar(Localizacao local) {

        ContentValues cv = new ContentValues();
        cv.put("nome", local.getNome());
        cv.put("edit", local.getDataEdit());
        cv.put("path", local.getFotoPath());
        cv.put("rot", local.getRotation());

        String[] args = {String.valueOf(local.getID())};

        try {this.escrita.update(DbHelper.TABELA_LOCAIS, cv, "id=?", args);}
        catch (Exception e) {
            Log.e(TAG,"Não foi possível editar", e);
            return false;
        }
        return true;
    }

    @Override
    public boolean apagar(Localizacao local) {
        String[] args = {String.valueOf(local.getID())};

        try {this.escrita.delete(DbHelper.TABELA_LOCAIS, "id=?", args);}
        catch (Exception e) {
            Log.e(TAG,"Não foi possível apagar", e);
            return false;
        }
        return true;
    }

    @SuppressLint("Range")
    @Override
    public Localizacao carregar(long id) {

        String sql = "SELECT * FROM " + DbHelper.TABELA_LOCAIS + ";";
        Cursor cursor = leitura.rawQuery(sql, null);

        while (cursor.moveToNext()) {

            long locID = cursor.getLong(cursor.getColumnIndex("ID"));
            if (locID == id) {

                String locNome = cursor.getString(cursor.getColumnIndex("nome"));
                String locData = cursor.getString(cursor.getColumnIndex("data"));
                String locDataEdit = cursor.getString(cursor.getColumnIndex("edit"));
                String locCep = cursor.getString(cursor.getColumnIndex("cep"));
                String locLogr = cursor.getString(cursor.getColumnIndex("logradouro"));
                String locCompl = cursor.getString(cursor.getColumnIndex("complemento"));
                String locBair = cursor.getString(cursor.getColumnIndex("bairro"));
                String locLoc = cursor.getString(cursor.getColumnIndex("localidade"));
                String locUf = cursor.getString(cursor.getColumnIndex("uf"));
                double locLat = cursor.getDouble(cursor.getColumnIndex("lat"));
                double locLng = cursor.getDouble(cursor.getColumnIndex("lng"));
                String locPath = cursor.getString(cursor.getColumnIndex("path"));
                int locRot = cursor.getInt(cursor.getColumnIndex("rot"));

                Localizacao retLoc = new Localizacao(new CEP(locCep, locLogr, locCompl, locBair, locLoc, locUf),
                        locNome, locData, locDataEdit, locID, new LatLng(locLat, locLng));
                retLoc.setRotation(locRot);
                retLoc.setFotoPath(locPath);

                return retLoc;
            }
        }
        cursor.close();

        return null;
    }


    @SuppressLint("Range")
    @Override
    public List<Localizacao> listar() {

        List<Localizacao> locais = new ArrayList<>();
        String sql = "SELECT * FROM " + DbHelper.TABELA_LOCAIS + ";";
        Cursor cursor = leitura.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            long ThisId = cursor.getLong(cursor.getColumnIndex("ID"));
            locais.add(carregar(ThisId));
        }
        cursor.close();

        return locais;
    }
}
