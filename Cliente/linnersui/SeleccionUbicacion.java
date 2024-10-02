package com.example.linnersui;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;

public class SeleccionUbicacion extends AppCompatActivity implements View.OnClickListener{

    public Intent intent = null;
    public MapaFragment mapa = new MapaFragment();
    public Button btnDarUbicacion, btnElegirUbicacion, btnIniciarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_ubicacion);

        btnDarUbicacion = findViewById(R.id.btnDarUbicacion);
        btnElegirUbicacion = findViewById(R.id.btnElegirUbicacion);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);

        btnDarUbicacion.setOnClickListener(this);
        btnElegirUbicacion.setOnClickListener(this);
        btnIniciarSesion.setOnClickListener(this);
    }

    public void onClick(View v) {

        if (v.getId() == R.id.btnDarUbicacion)
        {
            intent = new Intent(this, PantallaPrincipal.class);
            startActivity(intent);
        }
        else if (v.getId() == R.id.btnElegirUbicacion)
        {
            intent = new Intent(this, PantallaPrincipal.class);
            intent.putExtra("Fragmets", "mapa");
            startActivity(intent);
        }
        else if (v.getId() == R.id.btnIniciarSesion)
        {
            intent = new Intent(this, IniciarSesion.class);
            startActivity(intent);
        }
    }
}