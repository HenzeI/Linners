package com.example.linnersui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import lineascc.LineasCC;
import pojoslineas.ExcepcionLineas;
import pojoslineas.Parada;
import pojoslineas.ParadaFavorita;
import pojoslineas.Usuario;

public class GestionarParadaFavorita extends AppCompatActivity implements PfRecyclerViewAdapter.OnItemClickListener{
    public SharedPreferences sharedPreferences;
    public ArrayList<ModeloParadaFavorita> modeloParadaFavoritas = new ArrayList<>();
    public TextView noPf;
    public String ipBD = "172.16.5.190";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestionar_parada_favorita);

        noPf = findViewById(R.id.noHayPf);

        sharedPreferences = getSharedPreferences("DatosUsuario", Context.MODE_PRIVATE);

        setUpModeloPf();
        adaptarRV();
    }

    private void setUpModeloPf() {

        new Thread(() -> {

            String idUsuario = sharedPreferences.getString("idUsuario", null);

            try {
                LineasCC linea = new LineasCC(ipBD);
                ArrayList<ParadaFavorita> lc = linea.leerParadasFavoritas();

                for (ParadaFavorita paradaFavorita : lc) {
                    String idPaUsuario = "" + paradaFavorita.getUsuario().getIdUsuario();
                    if (idPaUsuario.equals(idUsuario)){
                        Parada parada = paradaFavorita.getParada();
                        if (parada != null){
                            Integer idParadaFavorita = paradaFavorita.getIdPFavorita();
                            String direccionS = parada.getPDireccion();
                            String localidadProvinciaS = parada.getPLocalidad() + ", " + parada.getPProvincia();
                            ModeloParadaFavorita modelo = new ModeloParadaFavorita(idParadaFavorita,direccionS, localidadProvinciaS);
                            modeloParadaFavoritas.add(modelo);
                        }
                    }
                }

                runOnUiThread(() -> {
                    if (modeloParadaFavoritas.isEmpty()) {
                        noPf.setEnabled(true);
                        noPf.setVisibility(View.VISIBLE);
                    } else {
                        adaptarRV();
                    }
                });
            } catch (ExcepcionLineas ex) {
                runOnUiThread(() -> {

                });
            }
        }).start();
    }

    public void adaptarRV(){
        RecyclerView recyclerView = findViewById(R.id.reViewParadasFavoritas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        PfRecyclerViewAdapter adapter = new PfRecyclerViewAdapter(this, modeloParadaFavoritas, this);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onImageClick(int position) {
        ModeloParadaFavorita paradaFavorita = modeloParadaFavoritas.get(position);

        new Thread(() -> {
            try {
                LineasCC linea = new LineasCC(ipBD);
                linea.eliminarParadaFavorita(paradaFavorita.getIdParadaFavorita());

                runOnUiThread(() -> {
                    if (modeloParadaFavoritas.isEmpty()) {
                        noPf.setEnabled(true);
                        noPf.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(this, "Has eliminado " + paradaFavorita.getDireccion() + " de tus favoritas." , Toast.LENGTH_SHORT).show();
                        adaptarRV();
                    }
                });
            } catch (ExcepcionLineas ex) {
                runOnUiThread(() -> {

                });
            }
        }).start();

        modeloParadaFavoritas.remove(position);
    }
}