package com.curso.drawer;

import android.os.AsyncTask;

import com.curso.drawer.entities.Post;
import com.google.gson.Gson;

import java.net.HttpURLConnection;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MiTarea extends AsyncTask<String, Void, Post> {


    private final IPost ipost;

    public MiTarea(IPost ipost) {

        this.ipost=ipost;
    }

    @Override
        protected void onPreExecute() {
            System.err.println("Listo para ejecutarse");
        }

        @Override
        protected Post doInBackground(String... params) {
            System.err.println("En ejecución");
                    URL url = null;
                    HttpURLConnection urlConnection = null;
                    StringBuilder respuesta;
                    System.out.println("Iniciando .... \n");

                    try {
                        try {
                            url = new URL(params[0]);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        urlConnection = (HttpURLConnection) url.openConnection();
                        urlConnection.setRequestMethod("GET");
                        urlConnection.setRequestProperty("Accept", "application/json");

                        if (urlConnection.getResponseCode() != 200) {
                            throw new RuntimeException("Failed : HTTP error code : "
                                    + urlConnection.getResponseCode());
                        }

                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                        BufferedReader br = new BufferedReader(new InputStreamReader(
                                (urlConnection.getInputStream())));

                        String output;
                        respuesta = new StringBuilder();
                        System.out.println("Output from Server .... \n");
                        while ((output = br.readLine()) != null) {
                            System.err.println(output);
                            respuesta.append(output);
                        }
                        Gson gson=new Gson();
                       Post post= gson.fromJson(respuesta.toString(),Post.class);

                        return post;

                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

            return null;
        }

        @Override
        protected void onPostExecute(Post s) {



            if(ipost!=null){

                    ipost.receive(s);
                }


        }


    }
