package com.antonioejemplo.localizacionesadmin;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapaUltimasPosiciones extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener {
    private GoogleMap map;
    private double longitud;
    private double latitud;
    private String nombreusuario;
    private String telefonousuario;
    private LatLng localizacion;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_ultimas_posiciones);

        //POnemos el escuchador del mapa
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map_ultimas);

        mapFragment.getMapAsync(this);
        context=getBaseContext();

        Bundle bundle = getIntent().getExtras();
        longitud=bundle.getDouble("Longitud");
        latitud=bundle.getDouble("Latitud");
        nombreusuario=bundle.getString("Nombre");
        telefonousuario=bundle.getString("Telefono");
        localizacion=new LatLng(latitud,longitud);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setZoomControlsEnabled(true);//Botones de zomm
        map.getUiSettings().setMapToolbarEnabled(false);//Deshabilitamos los iconos con accesos a googlemaps porque la app no necesita ubicación.
        map.setOnMarkerDragListener(this);//Se implementan los tres métodos de la interfaz...

        /*Marker posicion = map.addMarker(new MarkerOptions() .position(localizacion) .title("localizacion") .snippet("Kiel is cool") .icon(BitmapDescriptorFactory.fromResource(R.drawable.brujula_opt)));
        // Move the camera instantly to hamburg with a zoom of 15.
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(localizacion, 15));
        // Zoom in, animating the camera.
        map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);*/


        pintarMarcador();



    }

    private void pintarMarcador() {
        map.addMarker(new MarkerOptions()
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.icono_ubicacion))//Icono por defecto
                //.anchor(0.0f, 1.0f)
                .title(nombreusuario)
                .draggable(true)//Para poder arrastrar el marcador
                //.snippet(calle + " " + numero + ">" + fechaHora + ">" + velocidad)
                .position(localizacion));

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(localizacion, 60));
        map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

    }

    @Override
    public void onMarkerDragStart(Marker marker) {//METODO DE LA INTERFAZ OnMarkerDragListener
        String telf_wsp = telefonousuario.toString();
        String telf_wsp2 = telf_wsp.substring(telf_wsp.length() - 9, telf_wsp.length());//Sacamos los nueve últimos caracteres para extraer el teléfono...
        Toast.makeText(context, "Preparando envío de wsp. al Telefono:  " + telf_wsp2, Toast.LENGTH_LONG).show();


        Uri uri = Uri.parse("smsto:" + telf_wsp2);
        Intent i = new Intent(Intent.ACTION_SENDTO, uri);

        //i.putExtra("sms_body", smsText);
        i.setPackage("com.whatsapp");
        startActivity(i);
    }

    @Override
    public void onMarkerDrag(Marker marker) {//METODO DE LA INTERFAZ OnMarkerDragListener

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {//METODO DE LA INTERFAZ OnMarkerDragListener
        //Borramos el marcador y lo volvemos a pintar para que la ubicación siga siendo la ubicación inicial.
        marker.remove();
        pintarMarcador();
    }


}
