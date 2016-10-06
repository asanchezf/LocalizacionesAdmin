package com.antonioejemplo.localizacionesadmin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public class MapFragmentPrueba extends android.app.Fragment {
    private GoogleMap map;
    private static final LatLng HAMBURG = new LatLng(53.558, 9.927);
    private static final LatLng KIEL = new LatLng(53.551, 9.993);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        // Get a handle to the Map
       /* Fragment map = ((SupportMapFragment) getFragmentManager().findFragmentById( R.id.map)).getMap();
        Marker hamburg = map.addMarker(new MarkerOptions().position(HAMBURG) .title("Hamburg"));
        Marker kiel = map.addMarker(new MarkerOptions() .position(KIEL) .title("Kiel") .snippet("Kiel is cool") .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));
        // Move the camera instantly to hamburg with a zoom of 15.
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(HAMBURG, 15));
        // Zoom in, animating the camera.
        map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null); */
        return rootView;
    }

    public void getMapAsync(FragmentUltmasPosiciones fragmentUltmasPosiciones) {
    }
}

