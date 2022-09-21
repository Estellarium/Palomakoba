package com.aironbruce.registroscep.running.objects;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.aironbruce.registroscep.R;
import com.aironbruce.registroscep.otherclasses.CEP;
import com.aironbruce.registroscep.running.activities.InputActivity;
import com.aironbruce.registroscep.running.activities.LocalActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsFragment extends Fragment implements IPassaCEP {

    private static final String TAG = MapsFragment.class.getSimpleName();

    private GoogleMap mMap;
    private FragmentActivity thisAct;

    private CEP cep;
    private LatLng ll;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        thisAct = getActivity();
        try { //Enviar esta atividade como objeto para a atividade onde está
            if (getActivity() instanceof InputActivity)
                ((InputActivity) thisAct).escutaMapa(this);
            else if (getActivity() instanceof LocalActivity)
                ((LocalActivity) thisAct).escutaMapa(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) mapFragment.getMapAsync(callback);
        onCreate(savedInstanceState);
    }

    private final OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            mMap = googleMap;

            try {//Puxar o tema do JSON (diurno, noturno)
                boolean leitura;
                int modoNoturno = thisAct.getResources().getConfiguration().uiMode &
                                Configuration.UI_MODE_NIGHT_MASK;

                switch (modoNoturno) {

                    case Configuration.UI_MODE_NIGHT_YES:
                        leitura = mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(thisAct, R.raw.map_night));
                        if (!leitura) Log.e(TAG, "Não foi possível ler o JSON.");
                        break;

                    case Configuration.UI_MODE_NIGHT_NO:
                        leitura = mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(thisAct, R.raw.map_day));
                        if (!leitura) Log.e(TAG, "Não foi possível ler o JSON.");
                        break;
                }

            } catch (Resources.NotFoundException e) {
                Log.e(TAG, "Estilo não encontrado.", e);
            }
            //Desativar toque, posição default
            mMap.getUiSettings().setAllGesturesEnabled(false);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-15.5954,-56.0926), 3.5f));
            atualizarLocal();
        }
    };

    //Definir o local baseado no CEP pesquisado
    public void atualizarLocal() {

        if (cep != null) {
            String localidade = cep.toString();
            LatLng x;

            try {
                Geocoder geocoder = new Geocoder(thisAct, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocationName(localidade, 1);
                if (addresses == null || addresses.size() < 1) {
                    Log.e(TAG, "Geocoder não encontrou");
                    return;
                }

                Address local = addresses.get(0);
                x = new LatLng(local.getLatitude(), local.getLongitude());

                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(x));

                //Animação de mapa (se for na pesquisa)
                if (thisAct instanceof InputActivity) {
                     mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(x, 15));
                     ((InputActivity) thisAct).sendLL(x);}
                else mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(x, 15));

            } catch (IOException e) {
                Log.e(TAG, "Falha na geolocalização.", e);
            }

        } else if (ll != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 15));
            mMap.addMarker(new MarkerOptions().position(ll));
        }
    }

    @Override
    public void passa(CEP cep) {
        this.cep = cep;
        atualizarLocal();
    }

    @Override
    public void escutaMapa(MapsFragment mF) {}

    @Override
    public void sendLL(LatLng latLng) {this.ll = latLng;}

}


