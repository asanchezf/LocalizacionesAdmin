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

import modelos.TodasLasPosiciones;


/**
 * Fragmento principal que contiene el RecyclerView con los usuarios
 */

public class FragmentTodasPosiciones extends Fragment{
    private static final String LOGTAG = "OBTENER MARCADORES";
    /*
        Adaptador del recycler view
         */
    private AdaptadorTodasPosiciones adapter;
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
    private List<TodasLasPosiciones> listdatos;//Se le enviará al Adaptador
    private TodasLasPosiciones todasLasPosiciones;

    //Variable que le pasamos a la llamada del adaptador. Necesita un listener
    private Adaptador.OnItemClickListener listener;
    //private Context contexto;


    public FragmentTodasPosiciones() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Context contexto = getActivity();

       // listener= OnItemClickListener;
        View v = inflater.inflate(R.layout.fragment_todas_posiciones, container, false);

        lista = (RecyclerView) v.findViewById(R.id.reciclador);

        //lista.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(contexto);
        //lista.setLayoutManager(lManager);
        lista.setLayoutManager(
                new LinearLayoutManager(contexto, LinearLayoutManager.VERTICAL, false));

        requestQueue = Volley.newRequestQueue(contexto);
        traerTodasPosiciones();

       //La llamada al adaptador llega vacía. Hay que llamarle desde el método traerUsuarios();
        /*adapter=new Adaptador(listdatos,listener,getContext());
        lista.setAdapter(adapter);*/
        //adapter=new Adaptador(listdatos,this,this);

        //return super.onCreateView(inflater, container, savedInstanceState);
        return v;
    }

    private double conversionVelocidad(double speed) {
        //Convierte la velocidad de Millas/h a KM/h

        double speedConvertida = (speed / 1000) * 3600;

        return speedConvertida;
    }
    private void traerTodasPosiciones() {

        String tag_json_obj_actual = "json_obj_req_actual";
        //http://petty.hol.es/obtener_localizaciones.php
        String patronUrl = "http://petty.hol.es/obtener_localizaciones_todas.php";
        String uri = String.format(patronUrl);

        listdatos= new ArrayList<TodasLasPosiciones>();

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
                        int suVelocidad = 0;//Para convertir la velocidad a Km/h

                        /*  {"Id":"10","Id_Usuario":"10","Poblacion":"M\u00f3stoles","Calle":"Calle Rubens",
                        "Numero":" 12","Longitud":"-3.87124","Latitud":"40.3294","Velocidad":"0.37042078375816",
                        "FechaHora":"13-09-2016 18:08:48","Modificado":"0000-00-00 00:00:00","Username":"Pepe",
                        "Password":"1","Email":"email","ID_Android":null,"Telefono":"89977665",
                        "FechaCreacion":"2016-05-10 17:11:08"}*/

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

                                    //velocidad = json_array.getJSONObject(z).getString("Velocidad");

                                    //Hacemos casting a int para que no traiga tantos decimales
                                    suVelocidad = (int) conversionVelocidad(json_array.getJSONObject(z).getDouble("Velocidad"));
                                    latitud = json_array.getJSONObject(z).getString("Latitud");
                                    longitud = json_array.getJSONObject(z).getString("Longitud");
                                    calle = json_array.getJSONObject(z).getString("Calle");
                                    poblacion = json_array.getJSONObject(z).getString("Poblacion");
                                    numero = json_array.getJSONObject(z).getString("Numero");


                                    todasLasPosiciones=new TodasLasPosiciones();
                                    todasLasPosiciones.setId(id);
                                    todasLasPosiciones.setUsername(nombre);
                                    todasLasPosiciones.setCalle(calle);
                                    todasLasPosiciones.setFechaHora(fecha);
                                    todasLasPosiciones.setLatitud(latitud);
                                    todasLasPosiciones.setLongitud(longitud);
                                    todasLasPosiciones.setNumero(numero);
                                    todasLasPosiciones.setPoblacion(poblacion);
                                    todasLasPosiciones.setVelocidad(String.valueOf(suVelocidad));

                                    listdatos.add(todasLasPosiciones);
                                    Log.d(LOGTAG, "Tamaño listadatos: "+listdatos.size());

                                }

                           // }new Adaptador.OnItemClickListener()

                        //Al adaptador le pasamos la lista, el listener y el contexto
                        //Le pasamos new Adaptador.OnItemClickListener() para inicializar el listener
                        adapter=new AdaptadorTodasPosiciones(listdatos, new AdaptadorTodasPosiciones.OnItemClickListener() {
                            @Override
                            public void onClick(RecyclerView.ViewHolder holder, int idPromocion, View v) {

                                if(v.getId()==R.id.imagenUsuario_todas){
                                    Toast.makeText(getContext(),"Has pulsado en la imagen",Toast.LENGTH_LONG).show();
                                }

                               else if(v.getId()==R.id.txtNombre_todas){
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
