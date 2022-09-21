package com.aironbruce.registroscep.running.objects;

import com.aironbruce.registroscep.otherclasses.CEP;
import com.google.android.gms.maps.model.LatLng;

public interface IPassaCEP {
    void passa(CEP cep);
    void escutaMapa(MapsFragment mF);
    void sendLL(LatLng latLng);
}
