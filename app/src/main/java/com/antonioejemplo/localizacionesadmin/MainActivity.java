package com.antonioejemplo.localizacionesadmin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener( this);

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
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                MainFragment mainfragment = new MainFragment();
                fragmentTransaction.replace(R.id.fragment, mainfragment);
                fragmentTransaction.commit();
                break;
            case 1:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                FragmentUsuarios fragmentUsuario = new FragmentUsuarios();
                fragmentTransaction.replace(R.id.fragment, fragmentUsuario);
                fragmentTransaction.commit();
                break;

            case 2:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                FragmentUltmasPosiciones fragmentUltimas = new FragmentUltmasPosiciones();
                fragmentTransaction.replace(R.id.fragment, fragmentUltimas);
                fragmentTransaction.commit();
                break;
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
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
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action

            Toast.makeText(getApplicationContext(),"Hola. soy el item1",Toast.LENGTH_LONG).show();

            /*FragmentManager fm =  getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            FragmentUsuarios fragmentUsuarios = new FragmentUsuarios();
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_main, fragmentUsuarios)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack("Nombre opcional para este estado de la pila de atrás")
                    .commit();*/
            setFragment(0);


        }
        else if (id == R.id.nav_gallery) {
            Toast.makeText(getApplicationContext(),"Hola. soy el item 2",Toast.LENGTH_LONG).show();
            setFragment(1);
        } else if (id == R.id.nav_slideshow) {

            Toast.makeText(getApplicationContext(),"Hola. soy el item 3",Toast.LENGTH_LONG).show();
            setFragment(2);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);


        return true;
    }





}
