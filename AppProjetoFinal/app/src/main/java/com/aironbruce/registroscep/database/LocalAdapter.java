package com.aironbruce.registroscep.database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aironbruce.registroscep.R;
import com.aironbruce.registroscep.otherclasses.Localizacao;

import java.io.File;
import java.util.List;

public class LocalAdapter extends RecyclerView.Adapter<LocalAdapter.MyViewHolder> {

    private final List<Localizacao> locais;

    public LocalAdapter(List<Localizacao> lista) {this.locais = lista;}

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        final ImageView picLocal;
        final TextView txtNome, txtInfo, txtCEP, txtID;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            picLocal = itemView.findViewById(R.id.picLocal);
            txtNome = itemView.findViewById(R.id.txtNome);
            txtInfo = itemView.findViewById(R.id.txtInfo);
            txtCEP = itemView.findViewById(R.id.txtCEP);
            txtID = itemView.findViewById(R.id.txtID);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.local_layout, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull LocalAdapter.MyViewHolder holder, int position) {

        Localizacao local = this.locais.get(position);
        if (local != null) {

            File img = new File(local.getFotoPath());
            if(img.exists()) {
                Bitmap bm = BitmapFactory.decodeFile(img.getAbsolutePath());
                holder.picLocal.setImageBitmap(bm);
                holder.picLocal.setRotation(local.getRotation());
            } else Log.w((local.getNome() + " - " + local.getID()),"Imagem não encontrada!");

            holder.txtNome.setText(local.getNome());
            holder.txtInfo.setText(local.getCep().toString(0, 1, 0, 0, 1, 1));
            holder.txtCEP.setText(local.getCep().getCep());
            holder.txtID.setText(String.valueOf(position + 1));
            //holder.txtID.setText(String.valueOf(local.getID()));
        }
    }

    @Override
    public int getItemCount() {
        return locais == null ? 0 : locais.size();
    }
}
