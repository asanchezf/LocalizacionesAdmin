package com.antonioejemplo.localizacionesadmin;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

/**
 * Created by Susana on 05/10/2016.
 */

public class FragmentUltimasMapa extends Fragment implements OnMapReadyCallback{
private GoogleMap map;

    public FragmentUltimasMapa() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_map, container,false);

        // Registrar escucha onMapReadyCallback
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map_ultimas);
        mapFragment.getMapAsync(this);


        return v;




    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Implementamos OnMapReadyCallback
        map=googleMap;

    }
}
