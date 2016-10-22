package com.antonioejemplo.localizacionesadmin;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private MainFragment mainFragment;
    private FragmentUsuarios fragmentUsuario;
    private FragmentUltmasPosiciones fragmentUltimas;
    private FragmentTodasPosiciones fragmentTodasPosiciones;
    AlertDialog alert = null;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getSupportActionBar().setTitle("Administración");

        //SE DECLARAN LOS FRAGMENTS
        mainFragment = new MainFragment();
        fragmentUsuario = new FragmentUsuarios();
        fragmentUltimas = new FragmentUltmasPosiciones();
        fragmentTodasPosiciones = new FragmentTodasPosiciones();


        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Fragmento de inicio
        setFragment(0);

        //setupNavigationDrawerContent(navigationView);
        /*if (navigationView != null) {
            setupNavigationDrawerContent(navigationView);
        }*/

        //setupNavigationDrawerContent(navigationView);

        // Creación del fragmento principal siempre que import sea support.v4
        /*if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.drawer_layout, new MainFragment(),"MainFragment")
                    .commit();
        }*/

    }


    public void setFragment(int position) {
        android.support.v4.app.FragmentManager fragmentManager;
        android.support.v4.app.FragmentTransaction fragmentTransaction;
        switch (position) {
            case 0:
                /*fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                MainFragment mainfragment = new MainFragment();
                fragmentTransaction.replace(R.id.fragment, mainfragment);//Si la clase MainFragment importa android.support.v4.app.Fragment
                fragmentTransaction.commit();*/
                //IMPLEMENTACIÓN USADA CUANDO LA CLASE FRAGMENT IMPORTA app.fragment
                FragmentManager fragmentManager1=getFragmentManager();
                FragmentTransaction transaction=fragmentManager1.beginTransaction();
                MainFragment mainFragment=new MainFragment();
                transaction.replace(R.id.fragment,mainFragment);//Si la clase MainFragment importa app.fragment
                //fragmentManager1.beginTransaction().remove(fragmentUltimas);

                transaction.commit();

                break;
            case 1:
                //IMPLEMENTACIÓN USADA CUANDO LA CLASE FRAGMENT IMPORTA android.support.v4.app.Fragment PARA COMPATIBILIDAD DE VERSIONES ANTERIORES A LA V3
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                FragmentUsuarios fragmentUsuario = new FragmentUsuarios();
                fragmentTransaction.replace(R.id.fragment, fragmentUsuario, "usuarios_tag");//Si la clase MainFragment importa android.support.v4.app.Fragment
                //fragmentTransaction.addToBackStack(null);//Botón BackStack
                //getSupportActionBar().setTitle("Usuarios");

                fragmentTransaction.commit();

                break;
            case 2:
                //ULTIMAS POSICIONES
                //FragmentUltmasPosiciones fragmentUltimas = FragmentUltmasPosiciones.newInstance();
                FragmentUltmasPosiciones fragmentUltimas =new FragmentUltmasPosiciones();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment, fragmentUltimas);
                //ft.addToBackStack(null);//Botón BackStack
                //getSupportActionBar().setTitle("Últimas posiciones");
                ft.commit();
                // Registrar escucha onMapReadyCallback
                //fragmentUltimas.getMapAsync(this);

                break;

            case 3:
                //TODAS LAS POSICIONES
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                FragmentTodasPosiciones fragmentTodasPosiciones = new FragmentTodasPosiciones();
                fragmentTransaction.replace(R.id.fragment, fragmentTodasPosiciones);
                getSupportActionBar().setTitle("Todas las posiciones");
                //fragmentTransaction.addToBackStack(null);

                fragmentTransaction.commit();
                break;





            case 4:
                //Mapa todas las posiciones
                Intent intent=new Intent(MainActivity.this,MapaTodas.class);

                //Al se un mapa no se aprecia el efecto.
                /*overridePendingTransition(R.anim.fade_in,
                        R.anim.fade_out);*/
                startActivity(intent);




                break;

        }
    }


    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else {
                salidaControlada();
            }


            //super.onBackPressed();
            //salidaControlada();
            //setFragment(0);
            //super.onBackPressed();

        /*if(drawer.isDrawerOpen(Gravity.LEFT)){
            drawer.closeDrawer(Gravity.LEFT);
        }else if(!drawer.isDrawerVisible(Gravity.LEFT)&& !drawer.isDrawerOpen(Gravity.LEFT)){
            super.onBackPressed();
            salidaControlada();
        }*/

    }



    private void salidaControlada() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Seguro que deseas salir de la aplicación?")
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
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


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (alert != null) {
            alert.dismiss();
        }
    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action

            // Toast.makeText(getApplicationContext(),"Hola. soy el item1",Toast.LENGTH_LONG).show();

            /*FragmentManager fm =  getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            FragmentUsuarios fragmentUsuarios = new FragmentUsuarios();
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_main, fragmentUsuarios)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack("Nombre opcional para este estado de la pila de atrás")
                    .commit();*/
            setFragment(1);
            item.setChecked(true);
            getSupportActionBar().setTitle(item.getTitle());
            drawer.closeDrawer(GravityCompat.START);


        } else if (id == R.id.nav_gallery) {
            //Toast.makeText(getApplicationContext(),"Hola. soy el item 2",Toast.LENGTH_LONG).show();
            setFragment(2);
            item.setChecked(true);
            getSupportActionBar().setTitle(item.getTitle());
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_slideshow) {

            //Toast.makeText(getApplicationContext(),"Hola. soy el item 3",Toast.LENGTH_LONG).show();
            setFragment(3);
            item.setChecked(true);
            getSupportActionBar().setTitle(item.getTitle());
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_manage) {

            //Toast.makeText(getApplicationContext(), "Hola. soy el item 4", Toast.LENGTH_LONG).show();
            setFragment(4);
            item.setChecked(true);
            getSupportActionBar().setTitle(item.getTitle());
            drawer.closeDrawer(GravityCompat.START);
        }

        else if (id == R.id.nav_share) {

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);


        return true;
    }


}
