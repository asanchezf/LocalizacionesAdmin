package com.antonioejemplo.localizacionesadmin;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import adaptadores.AdaptadorUsuarios;
import adaptadores.AdaptadorUltimasPosiciones;
import mapas.MapaUltimasPosiciones;
import modelos.UltimasPosiciones;
import volley.AppController;
import volley.MyJSonRequestImmediate;


/**
 * Fragmento principal que contiene el RecyclerView con los usuarios
 */

//implements OnMapReadyCallback, LocationListener
public class FragmentUltmasPosiciones extends Fragment implements OnMapReadyCallback {
    private static final String LOGTAG = "OBTENER MARCADORES";
    /*
        AdaptadorUsuarios del recycler view
         */
    private AdaptadorUltimasPosiciones adapter;
    /*
    Instancia global del recycler view
     */
    private RecyclerView lista;
    /*
    instancia global del administrador del recycler View
     */
    private RecyclerView.LayoutManager lManager;
    private RequestQueue requestQueue;//Cola de peticiones de Volley. se encarga de gestionar automáticamente el envió de las peticiones, la administración de los hilos, la creación de la caché y la publicación de resultados en la UI.
    private JsonObjectRequest myjsonObjectRequest;
    private List<UltimasPosiciones> listdatos;//Se le enviará al AdaptadorUsuarios
    private UltimasPosiciones ultimasPosiciones;
    private static String LOGCAT;
    //Variable que le pasamos a la llamada del adaptador. Necesita un listener
    private AdaptadorUsuarios.OnItemClickListener listener;

    //Variables para mostrar mapa en fragment
    private GoogleMap map;
    private LatLng localizacion;
    MapFragment mapFragment;
    private static View v;


    //=============================
    public FragmentUltmasPosiciones() {//Es obligatorio poner el constructor por defecto para evitar errores

    }


    public static FragmentUltmasPosiciones newInstance() {//Constructor stático para poder instanciar desde otra clase....
        return new FragmentUltmasPosiciones();
    }



/*    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initilizeMap();

    }*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //super.onCreateView(inflater, container, savedInstanceState);


        Context contexto = getActivity();

       /* View v=null;
        if (v == null) {

            v = inflater.inflate(R.layout.fragment_ultimas_posiciones, container, false);
        }*/


        //En KIT-KAT da error. Se debe controlar...

            if (v != null) {
                ViewGroup parent = (ViewGroup) v.getParent();
                if (parent != null) parent.removeView(v);
            }
            try {
                v = inflater.inflate(R.layout.fragment_ultimas_posiciones, container, false);
            } catch (InflateException e) {
            }

            //e.getMessage("Error creando el mapa");
            //Toast.makeText(getActivity(),"Se ha producido un error al crear el mapa",Toast.LENGTH_SHORT).show();



        initilizeMap();

        /*MapFragment mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.mapcompartido);
        mapFragment.getMapAsync(this);*/


//POnemos el escuchador del mapa
        /*MapFragment MapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapcompartido);
        MapFragment.getMapAsync(this);*/


        //GoogleMap gooleMap;
       /* Fragment map;
        map = ((SupportMapFragment) getChildFragmentManager().findFragmentById( R.id.map)).getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

            }
        });*/


        /*GoogleMap gooleMap;
        ((HomeFragment) getFragmentManager().findFragmentById(R.id.map)).getMapAsync(new OnMapReadyCallback()
        { @Override public void onMapReady(GoogleMap map)
        {
            GoogleMap googleMap = map;
        }});*/

        //mapa = ((SupportMapFragment) getChildFragmentManager() .findFragmentById(R.id.fragment).getMap();

        /*SupportMapFragment myMapFragment;
        FragmentManager fm;
        fm = getChildFragmentManager();
        myMapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map);*/


        lista = (RecyclerView) v.findViewById(R.id.reciclador);

        //lista.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(contexto);
        //lista.setLayoutManager(lManager);
        lista.setLayoutManager(
                new LinearLayoutManager(contexto, LinearLayoutManager.VERTICAL, false));

        requestQueue = Volley.newRequestQueue(contexto);
        traerUltimasPosiciones();

        //return super.onCreateView(inflater, container, savedInstanceState);
        return v;
    }


  /*  @Override
    public void onResume() {
        super.onResume();
        initilizeMap();

    }*/

    private void initilizeMap() {

        if (map == null) {
            //map = ((MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.mapcompartido)).getMap();

               /* MapFragment mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.mapcompartido);
                mapFragment.getMapAsync(this);*/

            //ES OBLIGATORIO CONTROLAR LA VERSIÓN PORQUE EN KIT-KAT DABA ERROR AL CARGAR EL MAPA
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.mapcompartido);
                mapFragment.getMapAsync(this);

            } else {
                mapFragment = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapcompartido));
                mapFragment.getMapAsync(this);
            }


        }

    }

    private void traerUltimasPosiciones() {

        String tag_json_obj_actual = "json_obj_req_actual";
        //http://petty.hol.es/obtener_localizaciones.php
        //String patronUrl = "http://petty.hol.es/obtener_localizaciones.php";
        String patronUrl = "http://antonymail62.000webhostapp.com/obtener_localizaciones.php";

        String uri = String.format(patronUrl);

        listdatos = new ArrayList<UltimasPosiciones>();

        Log.v(LOGTAG, "Ha llegado a immediateRequestTiempoActual. Uri: " + uri);



        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Obteniedo posiciones espera por favor...");
        pDialog.show();

        myjsonObjectRequest = new MyJSonRequestImmediate(
                Request.Method.GET,
                uri,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response2) {

                        //String id = "";
                        pDialog.dismiss();
                        int id;
                        String poblacion = "";
                        String calle = "";
                        String numero = "";
                        String longitud = "";
                        String latitud = "";
                        String velocidad = "";
                        String fecha = "";
                        String nombre = "";
                        String telefono = "";
                        double sulatitud = 0;//Para mostrar los marcadores en el mapa pequeño
                        double sulongitud = 0;//Para mostrar los marcadores en el mapa pequeño
                        int suVelocidad = 0;//Para convertir la velocidad a Km/h
                        /*  {"Id":"1016","Poblacion":"","Calle":"Lugar Illa de Ons","Numero":" 25","Longitud":"-8.93295",
                "Latitud":"42.3758","Velocidad":"1.2638043165207","FechaHora":"06-08-2016 12:48:33",
                "Telefono":"659355808","Email":"antoniom.sanchezf@gmail.com","Username":"Antonio"}*/

                        try {

                            //for (int i = 0; i < response2.length(); i++) {
                            //JSONObject json_estado = response2.getJSONObject("estado");
                            String resultJSON = response2.getString("estado");

                            JSONArray json_array = response2.getJSONArray("alumnos");
                            for (int z = 0; z < json_array.length(); z++) {
                                //OJO: se ha cambiado a int. Antes era un String
                                id = json_array.getJSONObject(z).getInt("Id");
                                nombre = json_array.getJSONObject(z).getString("Username");
                                fecha = json_array.getJSONObject(z).getString("FechaHora");
                                velocidad = json_array.getJSONObject(z).getString("Velocidad");
                                latitud = json_array.getJSONObject(z).getString("Latitud");
                                longitud = json_array.getJSONObject(z).getString("Longitud");
                                calle = json_array.getJSONObject(z).getString("Calle");
                                poblacion = json_array.getJSONObject(z).getString("Poblacion");
                                numero = json_array.getJSONObject(z).getString("Numero");
                                telefono = json_array.getJSONObject(z).getString("Telefono");

                                //Hacemos una coversión de la velocidad de string a double y llamamos a conversionVelocidad
                                //para que pase el valor de millas/hora a Km/hora

                                //suVelocidad= Double.parseDouble(String.valueOf(velocidad));
                                //suVelocidad = (int) conversionVelocidad((int) json_array.getJSONObject(z).getDouble("Velocidad"));

                                //Hacemos casting a int para que no traiga tantos decimales
                                suVelocidad = (int) conversionVelocidad(json_array.getJSONObject(z).getDouble("Velocidad"));

                                ultimasPosiciones = new UltimasPosiciones();
                                ultimasPosiciones.setId(id);
                                ultimasPosiciones.setUsername(nombre);
                                ultimasPosiciones.setCalle(calle);
                                ultimasPosiciones.setFechaHora(fecha);
                                ultimasPosiciones.setLatitud(latitud);
                                ultimasPosiciones.setLongitud(longitud);
                                ultimasPosiciones.setNumero(numero);
                                ultimasPosiciones.setPoblacion(poblacion);
                                //ultimasPosiciones.setVelocidad(velocidad);
                                ultimasPosiciones.setVelocidad(String.valueOf(suVelocidad));
                                ultimasPosiciones.setTelefono(telefono);

                                listdatos.add(ultimasPosiciones);
                                Log.d(LOGTAG, "Tamaño listadatos: " + listdatos.size());

                                sulatitud = Double.parseDouble(latitud);
                                sulongitud = Double.parseDouble(longitud);

                                localizacion = new LatLng(sulatitud, sulongitud);

                                //Dibuja los marcadores según las posiciones recogidas
                                pintarMarcadores(localizacion, nombre);


                            }


                            // }new AdaptadorUsuarios.OnItemClickListener()

                            //Al adaptador le pasamos la lista, el listener y el contexto
                            //Le pasamos new AdaptadorUsuarios.OnItemClickListener() para inicializar el listener

                            adapter = new AdaptadorUltimasPosiciones(listdatos, new AdaptadorUltimasPosiciones.OnItemClickListener() {


                                @Override
                                public void onClick(RecyclerView.ViewHolder holder, int idPromocion, View v) {
                                    //idPromocion es el id de cada uno de los registros devueltos
                                    if (v.getId() == R.id.imagenUsuario_ult) {

                                        Toast.makeText(getActivity(), "Has pulsado en la imagen", Toast.LENGTH_LONG).show();


                                    } else if (v.getId() == R.id.txtNombre_ult) {

                                        Toast.makeText(getActivity(), "Has pulsado en el nombre", Toast.LENGTH_LONG).show();

                                    } else if (v.getId() == R.id.txtubicacion) {

                                        //Toast.makeText(getActivity(), "Has pulsado en  contactar", Toast.LENGTH_LONG).show();
                                        verMapaULtimasPosiciones(idPromocion);
                                    } else {


                                        verMapaULtimasPosiciones(idPromocion);
                                    }

                                }
                            }, getActivity());


                            lista.setAdapter(adapter);
                            /*adapter=new AdaptadorUsuarios(listdatos,listener,getContext());
                            lista.setAdapter(adapter);*/


                            //map.moveCamera(CameraUpdateFactory.newLatLngZoom(localizacion, 10));


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(LOGTAG, "Error Respuesta en JSON: ");
                            pDialog.dismiss();
                            Toast.makeText(getActivity(), "Se ha producido un error conectando con el servidor.", Toast.LENGTH_LONG).show();

                        }

                        //priority = Request.Priority.IMMEDIATE;

                    }//fin onresponse

                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(LOGTAG, "Error Respuesta en JSON: " + error.getMessage());
                        pDialog.dismiss();
                        Toast.makeText(getActivity(), "Se ha producido un error conectando al Servidor", Toast.LENGTH_SHORT).show();


                    }
                }
        );

        // Añadir petición a la cola
        AppController.getInstance().addToRequestQueue(myjsonObjectRequest, tag_json_obj_actual);

    }

    private double conversionVelocidad(double speed) {
        //Convierte la velocidad de Millas/h a KM/h

        double speedConvertida = (speed / 1000) * 3600;

        return speedConvertida;
    }

    private void verMapaULtimasPosiciones(int idPromocion) {


        double dlatitud = 0;
        double dlongitud = 0;
        int id;
        String nombreusuariomapa = null;
        String telefonousuariomapa = null;


        id = idPromocion;

        Iterator<UltimasPosiciones> it = listdatos.iterator();


        while (it.hasNext()) {

            // Log.d(LOGCAT, "Posiciones del arraylis:  "+it.next());

            ultimasPosiciones = (UltimasPosiciones) it.next();
            //Toast.makeText(getActivity(),"Id de las posiciones  "+ultimasPosiciones.getId(),Toast.LENGTH_SHORT).show();
            //Toast.makeText(getActivity(),"Id de las posiciones  "+ultimasPosiciones.getId(),Toast.LENGTH_SHORT).show();
            if (ultimasPosiciones.getId() == (idPromocion)) {

                dlatitud = Double.parseDouble(ultimasPosiciones.getLatitud());
                dlongitud = Double.parseDouble(ultimasPosiciones.getLongitud());
                nombreusuariomapa = ultimasPosiciones.getUsername();
                telefonousuariomapa = ultimasPosiciones.getTelefono();
                Toast.makeText(getActivity(), "Abriendo mapa de ubicación.", Toast.LENGTH_SHORT).show();
                break;
            }


        }
        //listdatos.size();

        //dlatitud= Double.parseDouble(ultimasPosiciones.getLatitud());
        //dlongitud= Double.parseDouble(ultimasPosiciones.getLongitud());


        Intent intent = new Intent(getActivity(), MapaUltimasPosiciones.class);
        intent.putExtra("Longitud", dlongitud);
        intent.putExtra("Latitud", dlatitud);
        intent.putExtra("Nombre", nombreusuariomapa);
        intent.putExtra("Telefono", telefonousuariomapa);
        startActivity(intent);

    }

    private void pintarMarcadores(LatLng localizacion, String nombre) {
        //Dibuja los marcadores según las posiciones recogidas
        MarkerOptions markerOptions =
                new MarkerOptions()
                        //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder_green))
                        .anchor(0.0f, 1.0f)
                        .title(nombre)
                        //.snippet("Día: " + fechaHora + " - Teléf: " + telefonomarcador)
                        .draggable(true)
                        .position(localizacion)
                        .flat(true);

        Marker marker = map.addMarker(markerOptions);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(localizacion, 10));
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        map.getUiSettings().setZoomControlsEnabled(true);//Botones de zomm
        map.getUiSettings().setMapToolbarEnabled(false);//Deshabilitamos los iconos con accesos a googlemaps porque la app no necesita ubicación.

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        //mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapcompartido);

        //SE elimina porque da error... em Api 19...S4mini
      /*  if (mapFragment != null) {
            getFragmentManager().beginTransaction().remove(mapFragment).commit();
        }*/


    }


}
