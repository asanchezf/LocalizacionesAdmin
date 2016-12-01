package com.antonioejemplo.localizacionesadmin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import volley.AppController;

public class AltaUsuarios extends AppCompatActivity {

    private EditText idUsuario;//NO SE MODIFICA
    private EditText nombre;//NO SE MODIFICA
    private EditText password;
    private EditText email;
    private EditText id_Android;//NO SE MODIFICA
    private EditText telefono;
    private EditText alta;//NO SE MODIFICA
    private EditText ultimamodificacion;//NO SE MODIFICA
    private EditText observaciones;


    //private boolean validar = true;

    private Toolbar toolbar;

    // Modo del formulario
    private int modo;
    //private Context contextoActivity;

    //URLS DEL WS
    private static final String EDIT_URL_VOLLEY = "http://petty.hol.es/actualizar_usuario.php";//WS
    private static final String DELETE_URL_VOLLEY = "http://petty.hol.es/borrar_usuario.php";//WS
    //Parametros enviados al WS.
    private static final String KEY_ID_USUARIO = "Id";
    private static final String KEY_PASSWORD = "Password";
    private static final String KEY_EMAIL = "Email";
    private static final String KEY_TELEFONO = "Telefono";
    private static final String KEY_OBSERVACIIONES = "Observaciones";
    private static final String KEY_FECHAMODIFICACION = "FechaModificacion";

    private String Id = null;
    private String Password = null;
    private String Email = null;
    private String Telefono = null;
    private String Observaciones = null;
    private long fechaHora2;
    private String Stringfechahora;
    private Calendar modificacion;

    AlertDialog alert = null;


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
        ultimamodificacion= (EditText) findViewById(R.id.ultimamodificacion);
        observaciones = (EditText) findViewById(R.id.observaciones);

        //Deshabilitamos los controles que no se deben modificar.
        idUsuario.setEnabled(false);
        nombre.setEnabled(false);
        id_Android.setEnabled(false);
        alta.setEnabled(false);
        ultimamodificacion.setEnabled(false);


        Bundle bundle = getIntent().getExtras();
//Se recoge un entero y al Editext hay que pasarle un string en el método setText. Es necesario el String.valueof para que no dé error.
        String idrecogido = String.valueOf(bundle.getInt("Id"));

        idUsuario.setText(idrecogido);
        nombre.setText(bundle.getString("Nombre"));
        id_Android.setText(bundle.getString("ID_Android"));
        telefono.setText(bundle.getString("Telefono"));
        email.setText(bundle.getString("Email"));
        password.setText(bundle.getString("Password"));

        Id = idUsuario.getText().toString().trim();


        String fechaAlta=bundle.getString("FechaAlta");

        //reordenaFecha(fechaAlta);

        alta.setText(reordenaFecha(fechaAlta));

        //alta.setText(bundle.getString("FechaAlta"));

        //Para que no salga un null en el campo el estar vacío
        String observacionesNOnull=bundle.getString("Observaciones");
        if(observacionesNOnull.equals("null")) {
            observaciones.setText("");
            //observaciones.setText(bundle.getString("Observaciones"));
        }
        else observaciones.setText(bundle.getString("Observaciones"));


        //Para que no salga un null en el campo el estar vacío
        String fechaultimaNOnull=bundle.getString("Observaciones");
        if(fechaultimaNOnull.equals("null")) {
            ultimamodificacion.setText("");
        }
        else ultimamodificacion.setText(bundle.getString("FechaModificacion"));

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
    private  String reordenaFecha (String fechaalta){


        String dia=fechaalta.substring(8,10);
        String mes=fechaalta.substring(5,7);
        String anio=fechaalta.substring(0, 4);
        String hora=fechaalta.substring(11, 19);


        String fechafinal=dia+"-"+mes+"-"+anio+" "+hora;


        //String salida="";
        return fechafinal;
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

          if(validar()) {

              //modificarUsuarioconVolley();
              modificarUsuariosinVolley();
          }
            return true;
        }

        if (id == R.id.eliminar_usuario) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("¿Seguro que deseas eliminar el registro seleccionado?")
                    .setCancelable(false)
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            //borrarUsuarioconVolley();//NO funciona. No da error pero no borra el registro
                           borrarUsuariosinVolley();
                            finish();
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.cancel();
                        }
                    });
            alert = builder.create();
            alert.show();




              //borrarUsuariosinVolley();
            //borrarUsuarioconVolley();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void borrarUsuariosinVolley() {
        HiloBorrarUsuario hiloconexion = new HiloBorrarUsuario();
        hiloconexion.execute(DELETE_URL_VOLLEY);   // Parámetros que recibe doInBackground

    }

    public class HiloBorrarUsuario extends AsyncTask<String, Void, String> {

        //PRUEBA PARA ARCHIVOS SERVIDOR DESDE RESTCLIENT:
                 /*{"Id":"114"}*/

        @Override
        protected String doInBackground(String... params) {



            String cadena = params[0];
            URL url = null; // Url de donde queremos obtener información
            String devuelve = "";

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
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        result.append(line);
                        //response+=line;
                    }

                    //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                    JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                    //Accedemos al vector de resultados

                    int resultJSON = Integer.parseInt(respuestaJSON.getString("estado"));   // estado es el nombre del campo en el JSON

                    if (resultJSON == 1) {      // hay un registro que mostrar
                        devuelve = "El usuario se ha eliminado correctamente";

                        /*Intent intent = new Intent(AltaUsuarios.this, MainActivity.class);
                        startActivity(intent);
                        finish();*/

                    } else if (resultJSON == 2) {
                        devuelve = "El usuario no ha podido ser eliminado.";
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


            Toast.makeText(getApplicationContext(), devuelve, Toast.LENGTH_LONG).show();

        }
    }


    private void borrarUsuarioconVolley() {
        String tag_json_obj_actual = "json_obj_req_actual";

        //final String Id = idUsuario.getText().toString().trim();

        //final int Id=121;

        //Id = idUsuario.getText().toString().trim();

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Procesando datos... espera por favor.");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DELETE_URL_VOLLEY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            //DEVUELVE EL SIGUIENTE JSON: {"estado":1,"usuario":{"Id":"10","Username":"Pepe","Password":"1","Email":"email"}}

                            //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                            JSONObject respuestaJSON = null;   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                            respuestaJSON = new JSONObject(response.toString());

                            //int resultJSON = Integer.parseInt(respuestaJSON.getString("estado"));   // estado es el nombre del campo en el JSON..Devuelve un entero

                            //String resultJSON = respuestaJSON.getString("estado");
                            int resultJSON=respuestaJSON.getInt("estado");

                            if (resultJSON == 1) {

                                pDialog.dismiss();

                               /* Snackbar snack = Snackbar.make(btnRegistrarse, R.string.alta_registro_ok, Snackbar.LENGTH_LONG);
                                ViewGroup group = (ViewGroup) snack.getView();
                                group.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                                snack.show();*/

                                Toast.makeText(getApplicationContext(), "Registro eliminado correctamente.", Toast.LENGTH_SHORT).show();

                            } else if (resultJSON == 2) {

                                pDialog.dismiss();
                                //El usuario no existe... Le informamos
                                //Toast.makeText(Login.this,R.string.usuarionoexist, Toast.LENGTH_LONG).show();

                               /* Snackbar snack = Snackbar.make(btnRegistrarse, R.string.alta_usuario_error, Snackbar.LENGTH_LONG);
                                ViewGroup group = (ViewGroup) snack.getView();
                                group.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                                snack.show();*/
                                Toast.makeText(getApplicationContext(), "No se ha podido eliminar el registro", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "No se ha podido eliminar el registro", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Toast.makeText(Login.this,error.toString(),Toast.LENGTH_LONG ).show();
                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "No se ha podido eliminar el registro", Toast.LENGTH_SHORT).show();
                        /*Snackbar snack = Snackbar.make(btnRegistrarse, error.toString(), Snackbar.LENGTH_LONG);
                        ViewGroup group = (ViewGroup) snack.getView();
                        group.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        snack.show();*/
                    }
                })


        {
            //CABECERA DE LA PETICIÓN
           /* @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                return headers;
            }*/

            //PARAMETROS ENVIADOS EN LA PETICIÓN POST
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_ID_USUARIO, Id);


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

    private boolean validar() {


        Id = idUsuario.getText().toString().trim();
        Password = password.getText().toString().trim();
        Email = email.getText().toString().trim();
        Telefono = telefono.getText().toString().trim();
        Observaciones = observaciones.getText().toString().trim();


        if (Password.isEmpty() || Email.isEmpty() || Telefono.isEmpty()) {
            Toast.makeText(getApplicationContext(),"Debes rellenar los campos contraseña, email y teléfono",Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
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


    private void modificarUsuarioconVolley() {

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

                                Toast.makeText(getApplicationContext(), "Registro actualizado", Toast.LENGTH_SHORT).show();

                            } else if (resultJSON == "2") {

                                pDialog.dismiss();
                                //El usuario no existe... Le informamos
                                //Toast.makeText(Login.this,R.string.usuarionoexist, Toast.LENGTH_LONG).show();

                               /* Snackbar snack = Snackbar.make(btnRegistrarse, R.string.alta_usuario_error, Snackbar.LENGTH_LONG);
                                ViewGroup group = (ViewGroup) snack.getView();
                                group.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                                snack.show();*/
                                Toast.makeText(getApplicationContext(), "No se ha podido actualizar el registro", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "No se ha podido actualizar el registro", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Toast.makeText(Login.this,error.toString(),Toast.LENGTH_LONG ).show();
                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "No se ha podido actualizar el registro", Toast.LENGTH_SHORT).show();
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
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                return headers;
            }

            //PARAMETROS ENVIADOS EN LA PETICIÓN POST
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
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

        Calendar calendarNow = new GregorianCalendar(TimeZone.getTimeZone("Europe/Madrid"));

        Calendar c1 = GregorianCalendar.getInstance();
        //System.out.println("Fecha actual: "+c1.getTime().toLocaleString());
        //usuario="Antonio";
        //usuario="Susana";
        fechaHora2 = System.currentTimeMillis();

        //System.out.println("Fecha del sistema: " + fechaHora2);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        Stringfechahora = sdf.format(fechaHora2);
        Log.v("", "Fecha del sistema: " + Stringfechahora);
       // modificacion = calendarNow;

        ObtenerWebService hiloconexion = new ObtenerWebService();
        hiloconexion.execute(EDIT_URL_VOLLEY);   // Parámetros que recibe doInBackground
    }

    public class ObtenerWebService extends AsyncTask<String, Void, String> {

        //PRUEBA PARA ARCHIVOS SERVIDOR DESDE RESTCLIENT:
                 /*{"Id":"114",
                "Username":"Madrid",
                "Password":"1",
                "Email":"antoniom.sanchezf@gmail.com",
                "Telefono":"659355808",
                "FechaCreacion":"2016-05-10 00:00:00",
                "Observaciones":"Observaciones modificadas",
                "FechaModificacion":"04-11-2016 00:00:00"}*/

        @Override
        protected String doInBackground(String... params) {


            String cadena = params[0];
            URL url = null; // Url de donde queremos obtener información
            String devuelve = "";

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
                jsonParam.put(KEY_FECHAMODIFICACION, Stringfechahora);

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
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        result.append(line);
                        //response+=line;
                    }

                    //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                    JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                    //Accedemos al vector de resultados

                    int resultJSON = Integer.parseInt(respuestaJSON.getString("estado"));   // estado es el nombre del campo en el JSON

                    if (resultJSON == 1) {      // hay un registro que mostrar
                        devuelve = "El usuario se ha modificado correctamente";

                        /*Intent intent = new Intent(AltaUsuarios.this, MainActivity.class);
                        startActivity(intent);
                        finish();*/

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


            Toast.makeText(getApplicationContext(), devuelve, Toast.LENGTH_LONG).show();

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

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        //SOBREESCRIBIMOS PARA INHABILITAR EL BOTÓN Y QUE VUELVA A LA ACTIVITY PRINCIPAL
        //super.onBackPressed();
        /*Intent intent=new Intent(AltaUsuarios.this,MainActivity.class);
        finish();
        startActivity(intent);*/
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (alert != null) {
            alert.dismiss();
        }
    }
}
