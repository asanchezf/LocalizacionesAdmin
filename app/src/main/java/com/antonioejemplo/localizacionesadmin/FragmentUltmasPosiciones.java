package com.antonioejemplo.localizacionesadmin;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import modelos.UltimasPosiciones;


/**
 * Fragmento principal que contiene el RecyclerView con los usuarios
 */

public class FragmentUltmasPosiciones extends Fragment{
    private static final String LOGTAG = "OBTENER MARCADORES";
    /*
        Adaptador del recycler view
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
    private List<UltimasPosiciones> listdatos;//Se le enviará al Adaptador
    private UltimasPosiciones ultimasPosiciones;

    //Variable que le pasamos a la llamada del adaptador. Necesita un listener
    private Adaptador.OnItemClickListener listener;
    //private Context contexto;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Context contexto = getActivity();

       // listener= OnItemClickListener;
        View v = inflater.inflate(R.layout.fragment_ultimas_posiciones, container, false);

        lista = (RecyclerView) v.findViewById(R.id.reciclador);

        //lista.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(contexto);
        //lista.setLayoutManager(lManager);
        lista.setLayoutManager(
                new LinearLayoutManager(contexto, LinearLayoutManager.VERTICAL, false));

        requestQueue = Volley.newRequestQueue(contexto);
        traerUltimasPosiciones();

       //La llamada al adaptador llega vacía. Hay que llamarle desde el método traerUsuarios();
        /*adapter=new Adaptador(listdatos,listener,getContext());
        lista.setAdapter(adapter);*/
        //adapter=new Adaptador(listdatos,this,this);

        //return super.onCreateView(inflater, container, savedInstanceState);
        return v;
    }

    private void traerUltimasPosiciones() {

        String tag_json_obj_actual = "json_obj_req_actual";
        //http://petty.hol.es/obtener_localizaciones.php
        String patronUrl = "http://petty.hol.es/obtener_localizaciones.php";
        String uri = String.format(patronUrl);

        listdatos= new ArrayList<UltimasPosiciones>();

        Log.v(LOGTAG, "Ha llegado a immediateRequestTiempoActual. Uri: " + uri);

        myjsonObjectRequest = new MyJSonRequestImmediate(
                Request.Method.GET,
                uri,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response2) {

                        //String id = "";
                        int id;
                        String poblacion = "";
                        String calle = "";
                        String numero = "";
                        String longitud = "";
                        String latitud = "";
                        String velocidad = "";
                        String fecha = "";
                        String nombre="";

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
                                    id= json_array.getJSONObject(z).getInt("Id");
                                    nombre = json_array.getJSONObject(z).getString("Username");
                                    fecha = json_array.getJSONObject(z).getString("FechaHora");
                                    velocidad = json_array.getJSONObject(z).getString("Velocidad");
                                    latitud = json_array.getJSONObject(z).getString("Latitud");
                                    longitud = json_array.getJSONObject(z).getString("Longitud");
                                    calle = json_array.getJSONObject(z).getString("Calle");
                                    poblacion = json_array.getJSONObject(z).getString("Poblacion");
                                    numero = json_array.getJSONObject(z).getString("Numero");


                                    ultimasPosiciones=new UltimasPosiciones();
                                    ultimasPosiciones.setId(id);
                                    ultimasPosiciones.setUsername(nombre);
                                    ultimasPosiciones.setCalle(calle);
                                    ultimasPosiciones.setFechaHora(fecha);
                                    ultimasPosiciones.setLatitud(latitud);
                                    ultimasPosiciones.setLongitud(longitud);
                                    ultimasPosiciones.setNumero(numero);
                                    ultimasPosiciones.setPoblacion(poblacion);
                                    ultimasPosiciones.setVelocidad(velocidad);

                                    listdatos.add(ultimasPosiciones);
                                    Log.d(LOGTAG, "Tamaño listadatos: "+listdatos.size());

                                }

                           // }new Adaptador.OnItemClickListener()

                        //Al adaptador le pasamos la lista, el listener y el contexto
                        //Le pasamos new Adaptador.OnItemClickListener() para inicializar el listener
                        adapter=new AdaptadorUltimasPosiciones(listdatos, new AdaptadorUltimasPosiciones.OnItemClickListener() {
                            @Override
                            public void onClick(RecyclerView.ViewHolder holder, int idPromocion, View v) {

                                if(v.getId()==R.id.imagenUsuario_ult){
                                    Toast.makeText(getContext(),"Has pulsado en la imagen",Toast.LENGTH_LONG).show();
                                }

                               else if(v.getId()==R.id.txtNombre_ult){
                                    Toast.makeText(getContext(),"Has pulsado en el nombre",Toast.LENGTH_LONG).show();
                                }

                            }
                        },getContext());

                            lista.setAdapter(adapter);
                            /*adapter=new Adaptador(listdatos,listener,getContext());
                            lista.setAdapter(adapter);*/


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(LOGTAG, "Error Respuesta en JSON: ");
                            Toast.makeText(getContext(),"Se ha producido un error conectando con el servidor. Inténtalo de nuevo",Toast.LENGTH_LONG).show();
                        }

                        //priority = Request.Priority.IMMEDIATE;

                    }//fin onresponse

                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(LOGTAG, "Error Respuesta en JSON: " + error.getMessage());
                        Toast.makeText(getContext(), "Se ha producido un error conectando al Servidor", Toast.LENGTH_SHORT).show();

                    }
                }
        ) ;

        // Añadir petición a la cola
        AppController.getInstance().addToRequestQueue(myjsonObjectRequest, tag_json_obj_actual);

    }



}
