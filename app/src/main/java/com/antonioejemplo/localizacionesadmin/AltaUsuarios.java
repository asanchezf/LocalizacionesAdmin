package com.antonioejemplo.localizacionesadmin;

import android.content.Context;
import android.content.pm.ActivityInfo;
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

public class AltaUsuarios extends AppCompatActivity {

    private EditText idUsuario;
    private EditText nombre;
    private EditText password;
    private EditText email;
    private EditText id_Android;
    private EditText telefono;
    private EditText alta;
    private EditText observaciones;


    //private boolean validar = true;

    private Toolbar toolbar;

    // Modo del formulario
    private int modo;


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

            //guardar();

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

}
