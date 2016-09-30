package com.antonioejemplo.localizacionesadmin;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import modelos.Usuarios;

/**
 * Created by Susana on 26/09/2016.
 */

public class GsonUsuariosParser {


    public ArrayList<Usuarios> leerFlujoJson(InputStream in) throws IOException {
        // Nueva instancia de la clase Gson
        Gson gson = new Gson();

        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        ArrayList<Usuarios> arrayListUsuarios = new ArrayList<>();

        // Iniciar el array
        reader.beginArray();

        while (reader.hasNext()) {
            // Lectura de objetos
            Usuarios usuario = gson.fromJson(reader, Usuarios.class);
            arrayListUsuarios.add(usuario);
        }


        reader.endArray();
        reader.close();
        return arrayListUsuarios;
    }


}
