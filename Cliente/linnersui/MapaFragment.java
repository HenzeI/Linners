package com.example.linnersui;

import android.app.AlertDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapaFragment extends Fragment implements LocationListener, OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener{

    public Button bParadaCercana;
    public ImageButton btnBuscar;
    public EditText buscadorParada;
    public Intent intent = null;
    public GoogleMap mapa;
    public Geocoder geo;
    public Location ubiSeleccionada = new Location("");
    public LatLng buscadorLatLng;
    public Marker marker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mapa, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bParadaCercana = view.findViewById(R.id.buscarParadaCercana);

        buscadorParada = view.findViewById(R.id.buscadorMapa);

        bParadaCercana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InicioFragment inicio = new InicioFragment();
                inicio.ubiMapa(ubiSeleccionada);

                ((PantallaPrincipal) getActivity()).navigateToFragment(R.id.inicioPantallaPrincipal, inicio);

            }
        });

        btnBuscar = view.findViewById(R.id.btnBuscar);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String direccion = String.valueOf(buscadorParada.getText());
                geo = new Geocoder(getContext());
                int maxResultados = 1;
                List<Address> adress = null;
                try {
                    adress = geo.getFromLocationName(direccion, maxResultados);

                    if (adress != null && !adress.isEmpty()) {
                        buscadorLatLng = new LatLng(adress.get(0).getLatitude(), adress.get(0).getLongitude());

                        onMapReady(mapa);

                    } else {
                        SpannableString title = new SpannableString("No se encuentra " + direccion);
                        int amarilloLinners = getResources().getColor(R.color.amarilloLinners);
                        title.setSpan(new ForegroundColorSpan(amarilloLinners), 0, title.length(), 0);

                        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                        dialog.setTitle(title);
                        dialog.setMessage("Comprueba que la búsqueda esté bien escrita. Prueba a añadir una ciudad, un estado o un código postal.");
                        dialog.setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss());
                        dialog.show();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        SupportMapFragment mapaFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapaLinners);
        if (mapaFragment != null) {
            mapaFragment.getMapAsync(this);
        }
    }

//    @Override
//    public void onClick(View v) {
//        if (v.getId() == R.id.buscarParadaCercana)
//        {
//            InicioFragment inicio = new InicioFragment();
//            inicio.ubiMapa(ubiSeleccionada);
//            cargarFragment(inicio);
//        }
//    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mapa = googleMap;
        this.mapa.setOnMapClickListener(this);
        this.mapa.setOnMapLongClickListener(this);
        if (marker != null){
            marker.remove();
        }

        if (mapa != null && buscadorLatLng != null) {
            mapa.clear();
            mapa.addMarker(new MarkerOptions().position(buscadorLatLng));
            mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(buscadorLatLng, 17));

            ubiSeleccionada.setLatitude(buscadorLatLng.latitude);
            ubiSeleccionada.setLongitude(buscadorLatLng.longitude);

            bParadaCercana.setVisibility(View.VISIBLE);
            bParadaCercana.setEnabled(true);
        } else {
            LatLng defUbicacion = new LatLng(43.385605,-3.2288129);
            mapa.moveCamera(CameraUpdateFactory.newLatLng(defUbicacion));
            marker = mapa.addMarker(new MarkerOptions().position(defUbicacion).title("Catro-Urdiales"));
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {}
    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        ubiSeleccionada.setLatitude(latLng.latitude);
        ubiSeleccionada.setLongitude(latLng.longitude);
        ubicacionMarcada();
    }
    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        ubiSeleccionada.setLatitude(latLng.latitude);
        ubiSeleccionada.setLongitude(latLng.longitude);
        ubicacionMarcada();
    }

    private void ubicacionMarcada(){
        if (marker != null){
            marker.remove();
        }

        mapa.clear();

        LatLng defUbicacion = new LatLng(ubiSeleccionada.getLatitude(),ubiSeleccionada.getLongitude());
        mapa.animateCamera(CameraUpdateFactory.newLatLng(defUbicacion));
        marker = mapa.addMarker(new MarkerOptions().position(defUbicacion));

        bParadaCercana.setVisibility(View.VISIBLE);
        bParadaCercana.setEnabled(true);
    }
}