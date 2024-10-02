package com.example.linnersui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ElegirUbicacion extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    public TextView tvLongitud, tvLatitud;
    public GoogleMap mapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elegir_ubicacion);

        tvLongitud = findViewById(R.id.tvLongitud);
        tvLatitud = findViewById(R.id.tvLatitud);

        SupportMapFragment mapaFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapaLinners);
        mapaFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mapa = googleMap;
        this.mapa.setOnMapClickListener(this);
        this.mapa.setOnMapLongClickListener(this);

        LatLng catroUrdiales = new LatLng(43.385605,-3.2288129);
        mapa.addMarker(new MarkerOptions().position(catroUrdiales).title("Catro-Urdiales"));
        mapa.moveCamera(CameraUpdateFactory.newLatLng(catroUrdiales));
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        tvLatitud.setText(""+latLng.latitude);
        tvLongitud.setText(""+latLng.longitude);
    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        tvLatitud.setText(""+latLng.latitude);
        tvLongitud.setText(""+latLng.longitude);
    }
}