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

import modelos.Usuarios;


/**
 * Fragmento principal que contiene el RecyclerView con los usuarios
 */

public class FragmentUsuarios extends Fragment{
    private static final String LOGTAG = "OBTENER MARCADORES";
    /*
        Adaptador del recycler view
         */
    private Adaptador adapter;
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
    private List<Usuarios> listdatos;//Se le enviará al Adaptador
    private Usuarios usuarios;

    //Variable que le pasamos a la llamada del adaptador. Necesita un listener
    private Adaptador.OnItemClickListener listener;
    //private Context contexto;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Context contexto = getActivity();

       // listener= OnItemClickListener;
        View v = inflater.inflate(R.layout.fragment_usuarios, container, false);

        lista = (RecyclerView) v.findViewById(R.id.reciclador);

        //lista.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(contexto);
        //lista.setLayoutManager(lManager);
        lista.setLayoutManager(
                new LinearLayoutManager(contexto, LinearLayoutManager.VERTICAL, false));

        requestQueue = Volley.newRequestQueue(contexto);
        traerUsuarios();

       //La llamada al adaptador llega vacía. Hay que llamarle desde el método traerUsuarios();
        /*adapter=new Adaptador(listdatos,listener,getContext());
        lista.setAdapter(adapter);*/
        //adapter=new Adaptador(listdatos,this,this);

        //return super.onCreateView(inflater, container, savedInstanceState);
        return v;
    }

    private void traerUsuarios() {

        String tag_json_obj_actual = "json_obj_req_actual";

        String patronUrl = "http://petty.hol.es/obtener_usuarios.php";
        String uri = String.format(patronUrl);

        listdatos= new ArrayList<Usuarios>();

        Log.v(LOGTAG, "Ha llegado a immediateRequestTiempoActual. Uri: " + uri);

        myjsonObjectRequest = new MyJSonRequestImmediate(
                Request.Method.GET,
                uri,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response2) {

                        //String id = "";
                        int id;
                        String username = "";
                        String password = "";
                        String email = "";
                        String id_Android = "";
                        String telefono = "";
                        String alta = "";




                        try {

                            //for (int i = 0; i < response2.length(); i++) {
                                //JSONObject json_estado = response2.getJSONObject("estado");
                                String resultJSON = response2.getString("estado");

                                JSONArray json_array = response2.getJSONArray("alumnos");
                                for (int z = 0; z < json_array.length(); z++) {
                                    //OJO: se ha cambiado a int. Antes era un String
                                    id= json_array.getJSONObject(z).getInt("Id");
                                    username = json_array.getJSONObject(z).getString("Username");
                                    password = json_array.getJSONObject(z).getString("Password");
                                    email = json_array.getJSONObject(z).getString("Email");
                                    id_Android = json_array.getJSONObject(z).getString("ID_Android");

                                    telefono = json_array.getJSONObject(z).getString("Telefono");
                                    alta = json_array.getJSONObject(z).getString("FechaCreacion");

                                    usuarios=new Usuarios();
                                    usuarios.setId(id);
                                    usuarios.setUsername(username);
                                    usuarios.setPassword(password);
                                    usuarios.setEmail(email);
                                    usuarios.setID_Android(id_Android);
                                    usuarios.setTelefono(telefono);
                                    usuarios.setFechaCreacion(alta);

                                    listdatos.add(usuarios);
                                    Log.d(LOGTAG, "Tamaño listadatos: "+listdatos.size());

                                }

                           // }new Adaptador.OnItemClickListener()

                        //Al adaptador le pasamos la lista, el listener y el contexto
                        //Le pasamos new Adaptador.OnItemClickListener() para inicializar el listener
                        adapter=new Adaptador(listdatos, new Adaptador.OnItemClickListener() {
                            @Override
                            public void onClick(RecyclerView.ViewHolder holder, int idPromocion, View v) {

                                if(v.getId()==R.id.imagenUsuario){
                                    Toast.makeText(getContext(),"Has pulsado en la imagen",Toast.LENGTH_LONG).show();
                                }

                               else if(v.getId()==R.id.txtNombre){
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
