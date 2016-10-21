package com.antonioejemplo.localizacionesadmin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AltaUsuarios extends AppCompatActivity {

    private EditText idUsuario;//NO SE MODIFICA
    private EditText nombre;//NO SE MODIFICA
    private EditText password;
    private EditText email;
    private EditText id_Android;//NO SE MODIFICA
    private EditText telefono;
    private EditText alta;//NO SE MODIFICA
    private EditText observaciones;


    //private boolean validar = true;

    private Toolbar toolbar;

    // Modo del formulario
    private int modo;
    private Context contextoActivity;

    //URL DEL WS
    private static final String EDIT_URL_VOLLEY = "http://petty.hol.es/actualizar_usuario.php";//WS
    //Parametros enviados al WS.
    private static final String KEY_ID_USUARIO = "Id";
    private static final String KEY_PASSWORD = "Password";
    private static final String KEY_EMAIL = "Email";
    private static final String KEY_TELEFONO = "Telefono";
    private static final String KEY_OBSERVACIIONES = "Observaciones";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_usuarios);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(getResources().getColor(R.color.md_white_1000_75));
        TypedValue typedValueColorPrimaryDark = new TypedValue();
        AltaUsuarios.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValueColorPrimaryDark, true);
        final int colorPrimaryDark = typedValueColorPrimaryDark.data;
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(colorPrimaryDark);
            // getWindow().setTitleColor(getResources().getColor(R.color.md_white_1000_75));
            //getWindow().setTitleColor(getResources().getColor(R.color.md_white_1000_75));

        }


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        idUsuario = (EditText) findViewById(R.id.idUsuario);
        nombre = (EditText) findViewById(R.id.nombre);
        id_Android = (EditText) findViewById(R.id.id_Android);
        password = (EditText) findViewById(R.id.password);
        email = (EditText) findViewById(R.id.email);
        telefono = (EditText) findViewById(R.id.telefono);
        alta = (EditText) findViewById(R.id.alta);
        observaciones = (EditText) findViewById(R.id.observaciones);

        //Deshabilitamos los controles que no se deben modificar.
        idUsuario.setEnabled(false);
        nombre.setEnabled(false);
        id_Android.setEnabled(false);
        alta.setEnabled(false);


        Bundle bundle = getIntent().getExtras();
//Se recoge un entero y al Editext hay que pasarle un string en el método setText. Es necesario el String.valueof para que no dé error.
        String idrecogido = String.valueOf(bundle.getInt("Id"));

        idUsuario.setText(idrecogido);
        nombre.setText(bundle.getString("Nombre"));
        id_Android.setText(bundle.getString("ID_Android"));
        telefono.setText(bundle.getString("Telefono"));
        email.setText(bundle.getString("Email"));
        password.setText(bundle.getString("Password"));
        alta.setText(bundle.getString("FechaAlta"));



/*
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        //Bundle bundle = getIntent().getExtras();
        //establecerModo(bundle.getInt(FragmentUsuarios.C_MODO));

        //Asignamos eventos al edittext observaciones para incluir contador de caracteres.
        observaciones.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //Sacamos el número de caracteres en un textview
                TextView contador = (TextView) findViewById(R.id.texto_contador);


              /*String tamanoString = String.valueOf(s.length());
              contador.setText(tamanoString);*/


                //Establecemos  el maxlengt del control observaciones dinámicamente a 200 caracteres.
                int maxLength = 200;
                InputFilter[] fArray = new InputFilter[1];
                fArray[0] = new InputFilter.LengthFilter(maxLength);
                observaciones.setFilters(fArray);

                //Restamos sobre 200 que es el maxlength de observaciones
                int resta = maxLength - s.length();
                String tamano = String.valueOf(resta);
                contador.setText(tamano);


            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.edit, menu);

/*        if (modo == MainActivity.C_EDITAR) {
            getMenuInflater().inflate(R.menu.edit, menu);
        }

        else {
            getMenuInflater().inflate(R.menu.alta, menu);

        }*/


        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.nuevo_usuario) {

            //guardar();

            return true;
        }


        if (id == R.id.modificar_usuario) {

            //modificarUsuarioconVolley();
            modificarUsuariosinVolley();

            return true;
        }

        if (id == R.id.eliminar_usuario) {

            //borrar(id_recogido);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void establecerModo(int m) {
        this.modo = m;

        if (modo == FragmentUsuarios.C_VISUALIZAR) {
            this.setTitle(nombre.getText().toString());
            this.setEdicion(false);
        } else if (modo == FragmentUsuarios.C_CREAR) {
            // this.setTitle(R.string.hipoteca_crear_titulo);
            this.setEdicion(true);
        } else if (modo == FragmentUsuarios.C_EDITAR) {
            // this.setTitle(R.string.agenda_editar_titulo);
            this.setEdicion(true);
        }
    }

    private void setEdicion(boolean opcion) {
//        nombre.setEnabled(opcion);
//        apellidos.setEnabled(opcion);
//        direc.setEnabled(opcion);
//        telefono.setEnabled(opcion);
//        email.setEnabled(opcion);
//
//        radio1.setEnabled(opcion);
//        radio2.setEnabled(opcion);
//        radio3.setEnabled(opcion);
//        radio4.setEnabled(opcion);
//
//
//        observaciones.setEnabled(opcion);

        // Controlamos visibilidad de botonera
        // LinearLayout v = (LinearLayout) findViewById(R.id.botonera);


       /* if (opcion)
            v.setVisibility(View.VISIBLE);

        else
            v.setVisibility(View.GONE);
*/

       /* if(modo==MainActivity.C_EDITAR){
            //m.setGroupVisible(R.id.modificar_usuario,true);



        }*/


        // Lineas para ocultar el teclado virtual (Hide keyboard)
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(nombre.getWindowToken(), 0);
    }


    private void modificarUsuarioconVolley(){

        String tag_json_obj_actual = "json_obj_req_actual";

        final String Id = idUsuario.getText().toString().trim();
        final String Password = password.getText().toString().trim();
        final String Email = email.getText().toString().trim();
        final String Telefono = telefono.getText().toString().trim();
        final String Observaciones = observaciones.getText().toString().trim();

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Transfiriendo datos... espera por favor.");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EDIT_URL_VOLLEY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            //DEVUELVE EL SIGUIENTE JSON: {"estado":1,"usuario":{"Id":"10","Username":"Pepe","Password":"1","Email":"email"}}

                            //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                            JSONObject respuestaJSON = null;   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                            respuestaJSON = new JSONObject(response.toString());

                            //int resultJSON = Integer.parseInt(respuestaJSON.getString("estado"));   // estado es el nombre del campo en el JSON..Devuelve un entero
                            String resultJSON = respuestaJSON.getString("estado");

                            if (resultJSON == "1") {

                                pDialog.dismiss();

                               /* Snackbar snack = Snackbar.make(btnRegistrarse, R.string.alta_registro_ok, Snackbar.LENGTH_LONG);
                                ViewGroup group = (ViewGroup) snack.getView();
                                group.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                                snack.show();*/

                                Toast.makeText(getApplicationContext(),"Registro actualizado",Toast.LENGTH_SHORT).show();

                            } else if (resultJSON == "2") {

                                pDialog.dismiss();
                                //El usuario no existe... Le informamos
                                //Toast.makeText(Login.this,R.string.usuarionoexist, Toast.LENGTH_LONG).show();

                               /* Snackbar snack = Snackbar.make(btnRegistrarse, R.string.alta_usuario_error, Snackbar.LENGTH_LONG);
                                ViewGroup group = (ViewGroup) snack.getView();
                                group.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                                snack.show();*/
                                Toast.makeText(getApplicationContext(),"No se ha podido actualizar el registro",Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"No se ha podido actualizar el registro",Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Toast.makeText(Login.this,error.toString(),Toast.LENGTH_LONG ).show();
                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"No se ha podido actualizar el registro",Toast.LENGTH_SHORT).show();
                        /*Snackbar snack = Snackbar.make(btnRegistrarse, error.toString(), Snackbar.LENGTH_LONG);
                        ViewGroup group = (ViewGroup) snack.getView();
                        group.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        snack.show();*/
                    }
                })



        {
            //CABECERA DE LA PETICIÓN
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                return headers;
            }

            //PARAMETROS ENVIADOS EN LA PETICIÓN POST
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_ID_USUARIO, Id);
                params.put(KEY_PASSWORD, Password);
                params.put(KEY_EMAIL, Email);
                params.put(KEY_TELEFONO, Telefono);
                params.put(KEY_OBSERVACIIONES, Observaciones);

                //PROBAR ESTO:
                /*JSONObject obj = new JSONObject();
                try {
                    obj.put("uno", "dato1");
                    obj.put("dos", "dato2");
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/




                return params;
            }
        };

        /*RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);*/

        // Añadir petición a la cola
        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj_actual);

    }

    private void modificarUsuariosinVolley() {

        //NO SE UTILIZA VOLLEY ESTA VEZ PORQUE ESTÁ DEVOLVIENDO ERRORES...
        //Llamada al Ws utilizando AsyncTask. Los parámetros los pasa en formato Json. Hay que descodificarlos desde el Ws.
        ObtenerWebService hiloconexion = new ObtenerWebService();
        hiloconexion.execute(EDIT_URL_VOLLEY);   // Parámetros que recibe doInBackground
    }

    public class ObtenerWebService extends AsyncTask<String,Void,String> {

        final String Id = idUsuario.getText().toString().trim();
        final String Password = password.getText().toString().trim();
        final String Email = email.getText().toString().trim();
        final String Telefono = telefono.getText().toString().trim();
        final String Observaciones = observaciones.getText().toString().trim();
        @Override
        protected String doInBackground(String... params) {


            String cadena = params[0];
            URL url = null; // Url de donde queremos obtener información
            String devuelve ="";

            try {
                HttpURLConnection urlConn;

                DataOutputStream printout;
                DataInputStream input;
                url = new URL(cadena);
                urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setDoInput(true);
                urlConn.setDoOutput(true);
                urlConn.setUseCaches(false);
                urlConn.setRequestProperty("Content-Type", "application/json");
                urlConn.setRequestProperty("Accept", "application/json");
                urlConn.connect();
                //PASAMOS LOS PARAMETROS POR JSON. EN EL WS HAY QUE DECODIFICAR LOS VALORES....=============
                //Creo el Objeto JSON
                JSONObject jsonParam = new JSONObject();
                jsonParam.put(KEY_ID_USUARIO, Id);
                jsonParam.put(KEY_PASSWORD, Password);
                jsonParam.put(KEY_EMAIL, Email);
                jsonParam.put(KEY_TELEFONO, Telefono);
                jsonParam.put(KEY_OBSERVACIIONES, Observaciones);

                // Envio los parámetros post.
                OutputStream os = urlConn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(jsonParam.toString());
                writer.flush();
                writer.close();

                int respuesta = urlConn.getResponseCode();


                StringBuilder result = new StringBuilder();

                if (respuesta == HttpURLConnection.HTTP_OK) {

                    String line;
                    BufferedReader br=new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                    while ((line=br.readLine()) != null) {
                        result.append(line);
                        //response+=line;
                    }

                    //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                    JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                    //Accedemos al vector de resultados

                    int resultJSON = Integer.parseInt(respuestaJSON.getString("estado"));   // estado es el nombre del campo en el JSON

                    if (resultJSON == 1) {      // hay un registro que mostrar
                        devuelve = "El usuario se ha modificado correctamente";

                        Intent intent=new Intent(AltaUsuarios.this,MainActivity.class);
                        startActivity(intent);


                    } else if (resultJSON == 2) {
                        devuelve = "El usuario no ha podido ser modificado.";
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return devuelve;



            //return null;
        }



        @Override
        protected void onPostExecute(String devuelve) {
            super.onPostExecute(devuelve);
            //limpiarDatos();

            //Toast.makeText(getApplicationContext(),devuelve,Toast.LENGTH_LONG).show();


            Toast.makeText(getApplicationContext(),devuelve,Toast.LENGTH_LONG).show();

            /*Snackbar snack = Snackbar.make(btnRegistrarse, devuelve, Snackbar.LENGTH_LONG);
            ViewGroup group = (ViewGroup) snack.getView();
            group.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            snack.show();*/

            /*if(podemoslogarnos) {
                Intent intent = new Intent(Registro.this, MainActivity.class);
                startActivity(intent);

                //Animación
                overridePendingTransition(R.animator.login_in,
                        R.animator.login_out);

                finish();

            }*/
        }
    }
}
