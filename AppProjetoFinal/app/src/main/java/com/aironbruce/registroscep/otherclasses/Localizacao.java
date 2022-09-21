package com.aironbruce.registroscep.otherclasses;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.aironbruce.registroscep.R;
import com.aironbruce.registroscep.database.LocalDatabaseManager;
import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Localizacao implements Parcelable {

    private final long ID;
    private final CEP cep;
    private final String data;

    private String nome, dataEdit, path = "";
    private double lat = 0, lng = 0;
    private int rot = 0;

    public Localizacao(CEP cep, Context context) {
        this.cep = cep;
        this.nome = cep.getCep();
        SimpleDateFormat sdf = new SimpleDateFormat(context.getString(R.string.date_format));
        this.data = sdf.format(new Date());
        this.dataEdit = data;
        LocalDatabaseManager LDM = new LocalDatabaseManager(context);
        this.ID = LDM.getLastID();
    }

    public Localizacao(CEP cep, String nome, String data, String dataEdit, long ID, LatLng latLng) {
        this.cep = cep;
        this.nome = nome;
        this.data = data;
        this.dataEdit = dataEdit;
        this.ID = ID;
        this.lat = latLng.latitude;
        this.lng = latLng.longitude;
    }

    protected Localizacao(Parcel in) {
        ID = in.readLong(); nome = in.readString(); data = in.readString(); dataEdit = in.readString();
        path = in.readString(); cep = (CEP) in.readSerializable();
        lat = in.readDouble(); lng = in.readDouble(); rot = in.readInt();
    }

    public static final Creator<Localizacao> CREATOR = new Creator<Localizacao>() {

        @Override
        public Localizacao createFromParcel(Parcel parcel) {return new Localizacao(parcel);}

        @Override
        public Localizacao[] newArray(int i) {return new Localizacao[i];}
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(ID); parcel.writeString(nome); parcel.writeString(data); parcel.writeString(dataEdit);
        parcel.writeString(path); parcel.writeSerializable(cep); parcel.writeDouble(lat);
        parcel.writeDouble(lng); parcel.writeInt(rot);
    }

    public String getNome() {return nome;}
    public void setNome(String nome) {this.nome = nome;}

    public long getID() {return ID;}

    public String getFotoPath() {return path;}
    public void setFotoPath(String path) {this.path = path;}

    public CEP getCep() {return cep;}

    public String getData() {return data;}
    public void setDataEdit(String dataEdit) {this.dataEdit = dataEdit;}
    public String getDataEdit() {return dataEdit;}

    public double getLatOrLng(boolean isLat) {if (isLat) return lat; else return lng;}
    public LatLng getLatlng() {return new LatLng(lat,lng);}
    public void setLatLng(LatLng latLng) {this.lat = latLng.latitude; this.lng = latLng.longitude;}

    public int getRotation() {return rot;}
    public int rotate() {
        rot += 90;
        if (rot >= 360) rot = 0;
        return rot;
    }
    public void setRotation(int rot) {this.rot = rot;}
}
