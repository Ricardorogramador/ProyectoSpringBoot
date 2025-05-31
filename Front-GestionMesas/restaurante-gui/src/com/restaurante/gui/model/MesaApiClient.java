package com.restaurante.gui.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.util.*;

public class MesaApiClient {
    private static final String BASE_URL = "http://localhost:8080/api/mesas";

    public static List<Mesa> obtenerMesas() {
        List<Mesa> mesas = new ArrayList<>();
        try {
            URL url = new URL(BASE_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder respuesta = new StringBuilder();

            while ((inputLine = in.readLine()) != null){
                respuesta.append(inputLine);
            }
            in.close();

            JSONArray jsonArray = new JSONArray(respuesta.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Mesa mesa = new Mesa();
                mesa.setId(jsonObject.getLong("id"));
                mesa.setNumero(jsonObject.getInt("numero"));
                mesa.setOcupada(jsonObject.getBoolean("ocupada"));
                mesa.setLimpieza(jsonObject.optBoolean("limpieza",false));
                mesas.add(mesa);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mesas;
    }
    public static void presetMesas(int cantidad) {
        try {
            URL url = new URL(BASE_URL + "/preset?cantidad=" + cantidad);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.getResponseCode();
            con.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void cambiarEstadoMesa(Long id, String estado) {
        try {
            URL url = new URL(BASE_URL + "/" + id + "/estado?estado=" + estado);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("PUT");
            con.getResponseCode();
            con.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
