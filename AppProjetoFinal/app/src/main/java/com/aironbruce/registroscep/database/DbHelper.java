package com.aironbruce.registroscep.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    private static final String TAG = DbHelper.class.getSimpleName();
    static final int VERSION = 5;
    static final String NOME_DB = "DB_LOCAIS";
    static final String TABELA_LOCAIS = "localizacoes";

    public DbHelper(@Nullable Context context) {
        super(context, NOME_DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String sql = "CREATE TABLE IF NOT EXISTS " + TABELA_LOCAIS +
                "(" +
                "ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT," +
                "data TEXT NOT NULL, " +
                "edit TEXT NOT NULL, "+
                "cep TEXT," +
                "logradouro TEXT," +
                "complemento TEXT," +
                "bairro TEXT," +
                "localidade TEXT," +
                "uf TEXT," +
                "path TEXT," +
                "lat INTEGER NOT NULL, " +
                "lng INTEGER NOT NULL, " +
                "rot INTEGER NOT NULL" +
                ");";
        try{
            sqLiteDatabase.execSQL(sql);
            Log.i(TAG, "Sucesso ao criar a tabela!");
        } catch (Exception e) {
            Log.e(TAG, "Erro ao criar a tabela" + e);//.getMessage()
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        String sql = "DROP TABLE IF EXISTS " + TABELA_LOCAIS + ";";
        try{
            sqLiteDatabase.execSQL(sql);
            onCreate(sqLiteDatabase);
            Log.i(TAG, "Sucesso ao atualizar app");
        } catch (Exception e) {
            Log.e(TAG, "Erro ao atualizar app" + e);
        }
    }

    public boolean reset() {

        String sql = "DROP TABLE IF EXISTS " + TABELA_LOCAIS + ";";
        try{
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL(sql);
            onCreate(db);
            Log.i(TAG, "Sucesso ao excluir tabela");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Erro ao excluir tabela" + e.getMessage());
            return false;
        }
    }
}
