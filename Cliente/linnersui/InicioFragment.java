package com.example.linnersui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.location.Geocoder;
import android.location.LocationListener;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Vibrator;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import lineascc.LineasCC;
import pojoslineas.ExcepcionLineas;
import pojoslineas.LineaParada;
import pojoslineas.Parada;
import pojoslineas.ParadaFavorita;
import pojoslineas.Usuario;

public class InicioFragment extends Fragment implements LocationListener, OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    public String ipBD = "172.16.5.190";
    private static final int ONE_KM = 1000;
    private final int FINE_PERMISSION_CODE = 1;
    private ArrayList<Address> listOfGeoPositions;
    public GoogleMap mapa;
    public ArrayList<ModeloLinea> modeloLinea = new ArrayList<>();
    public int imagenLogoLinners = R.drawable.logo_amarillo_linners;
    public int imagenLogoAlsa = R.drawable.logoalsa;
    public int imagenLogoGijon = R.drawable.gijonemtusa;
    public int imagenLogoL1 = R.drawable.l1;
    public int imagenLogoL2 = R.drawable.l2;
    public int imagenLogoL3 = R.drawable.l3;
    public int imagenLogoL4 = R.drawable.l4;
    public int imagenLogoL22 = R.drawable.l222;
    public int imagenLogoL12 = R.drawable.l12;
    public int imagenLogoL24 = R.drawable.l24;
    public TextView direccionPP, localidadPP;
    private Location currentLocation;
    private FusedLocationProviderClient mfusedLocationProviderClient;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private PlacesClient placesClient;
    private Address closestAddress;
    private Location paradaCercana = new Location("");
    private Location paradaMapa;
    public String nomDireccion;

    public SharedPreferences sharedPreferences;
    public Integer idParadaAPf;
    public String localidadParada;

    public boolean esFavorita;

    private static final int LONG_PRESS_TIMEOUT = 1000; // Tiempo para considerar un long press (500 ms)
    private Handler handler = new Handler();
    private boolean isLongPress = false;
    private int originalColor;
    private int pulsarColor;
    private Vibrator vibrator;

    public Usuario usuario;
    public Parada parada;
    public ParadaFavorita pf;
    private AlertDialog loadingDialog;

    public ImageView imagenPin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String apiKey = getString(R.string.google_maps_key);
        if (!Places.isInitialized()) {
            Places.initialize(getContext(), apiKey);
        }
        placesClient = Places.createClient(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inicio, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        direccionPP = view.findViewById(R.id.direccionPP);
        localidadPP = view.findViewById(R.id.localidadPP);

        imagenPin = view.findViewById(R.id.imageView2);

        sharedPreferences = requireContext().getSharedPreferences("DatosUsuario", Context.MODE_PRIVATE);

        adaptarRVinicio(view);

        mfusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                getLastLocation();
            } else {
                if (loadingDialog != null && loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
                Toast.makeText(getContext(), "Permisos denegados", Toast.LENGTH_SHORT).show();
            }
        });
        checkLocationPermission();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.TransparentDialog);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.progress_dialog, null);
        builder.setView(dialogView);
        builder.setCancelable(false);

        loadingDialog = builder.create();
        loadingDialog.show();

//        direccionPP.setOnLongClickListener(v ->{
//            Toast.makeText(getContext(), "TextView mantenido pulsado", Toast.LENGTH_SHORT).show();
//            // Puedes agregar más acciones aquí
//            return true; // Devuelve true si el evento se maneja completamente
//        });

        originalColor = ContextCompat.getColor(getContext(), R.color.amarilloLinners);
        pulsarColor = ContextCompat.getColor(getContext(), R.color.amarilloLinnersPulsar);

        vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);

        direccionPP.setOnTouchListener(new View.OnTouchListener() {
            private Runnable longPressRunnable = new Runnable() {
                @Override
                public void run() {
                    if (isLongPress) {
                        if (vibrator != null && vibrator.hasVibrator()) {
                            vibrator.vibrate(100);
                        }

                        usuario = new Usuario();
                        parada = new Parada();
                        pf = new ParadaFavorita();

                        String idUsuarioString = sharedPreferences.getString("idUsuario", null);
                        Integer idUsuario;
                        if (idUsuarioString != null) {
                            idUsuario = Integer.parseInt(idUsuarioString);
                            usuario.setIdUsuario(idUsuario);
                        } else {
                            requireActivity().runOnUiThread(() -> {
                                SpannableString title = new SpannableString("Inicio de sesión necesario");
                                int amarilloLinners = getResources().getColor(R.color.amarilloLinners);
                                title.setSpan(new ForegroundColorSpan(amarilloLinners), 0, title.length(), 0);

                                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                                dialog.setTitle(title);
                                dialog.setMessage("Debes de Iniciar sesión o registrarte para accedes a estos privilegios");
                                dialog.setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss());
                                dialog.show();
                            });
                            return;
                        }

                        String localidadUsuario = sharedPreferences.getString("localidad", null);

                        if(!localidadParada.equals(localidadUsuario)){
                            requireActivity().runOnUiThread(() -> {
                                SpannableString title = new SpannableString("Esta parada no pertenece a tu localidad");
                                int amarilloLinners = getResources().getColor(R.color.amarilloLinners);
                                title.setSpan(new ForegroundColorSpan(amarilloLinners), 0, title.length(), 0);

                                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                                dialog.setTitle(title);
                                dialog.setMessage("No puedes añadir esta parada como favorita, ya que no pertenece a tu localidad.");
                                dialog.setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss());
                                dialog.show();
                            });
                        }

                        parada.setIdParada(idParadaAPf);

                        pf.setUsuario(usuario);
                        pf.setParada(parada);

                        new Thread(() -> {
                            try {
                                LineasCC linea = new LineasCC(ipBD);

                                ArrayList<ParadaFavorita> lc = linea.leerParadasFavoritas();
                                boolean esFavorita = false;
                                int contarPf = 0;
                                for (ParadaFavorita buscarIgualPf : lc) {
                                    if(buscarIgualPf.getUsuario().getIdUsuario().equals(idUsuario)){
                                        if(buscarIgualPf.getParada().getIdParada().equals(idParadaAPf)){
                                            esFavorita = true;
                                            break;
                                        }
                                        contarPf++;
                                    }
                                }

                                if(contarPf > 4){
                                    requireActivity().runOnUiThread(() -> {
                                        SpannableString title = new SpannableString("Límite máximo superado");
                                        int amarilloLinners = getResources().getColor(R.color.amarilloLinners);
                                        title.setSpan(new ForegroundColorSpan(amarilloLinners), 0, title.length(), 0);

                                        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                                        dialog.setTitle(title);
                                        dialog.setMessage("Solo puedes tener 5 paradas como favoritas.");
                                        dialog.setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss());
                                        dialog.show();
                                    });
                                }

                                boolean finalEsFavorita = esFavorita;
                                requireActivity().runOnUiThread(() -> {
                                    if (finalEsFavorita) {
                                        dialogNoPf();
                                    } else {
                                        new Thread(() -> {
                                            try {
                                                LineasCC linea2 = new LineasCC(ipBD);
                                                linea2.insertarParadaFavorita(pf);

                                                requireActivity().runOnUiThread(() -> {
                                                    SpannableString title = new SpannableString("Nueva parada favorita");
                                                    int amarilloLinners = getResources().getColor(R.color.amarilloLinners);
                                                    title.setSpan(new ForegroundColorSpan(amarilloLinners), 0, title.length(), 0);

                                                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());

                                                    LayoutInflater inflater = getLayoutInflater();
                                                    View dialogView = inflater.inflate(R.layout.dialogpf2, null);

                                                    ImageView dialogImage = dialogView.findViewById(R.id.imagenId);

                                                    dialogImage.setImageResource(R.drawable.estrella);

                                                    dialog.setView(dialogView);
                                                    dialog.setTitle(title);
                                                    dialog.setMessage(nomDireccion + " añadida a tu lista de paradas favoritas.");
                                                    dialog.setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss());
                                                    dialog.show();
                                                });

                                            } catch (ExcepcionLineas ex) {
                                                requireActivity().runOnUiThread(() -> {
//                                                    Toast.makeText(getContext(), "Error al insertar parada favorita", Toast.LENGTH_SHORT).show();
                                                    ex.printStackTrace();
                                                });
                                            }
                                        }).start();

                                        String usuarioid = sharedPreferences.getString("idUsuario", null);
                                        if (usuarioid != null){
                                            comprobarSiEsPF();
                                        }
                                    }
                                });

                            } catch (ExcepcionLineas ex) {
//                                requireActivity().runOnUiThread(() -> {
//                                    Toast.makeText(getContext(), "Error al leer paradas favoritas", Toast.LENGTH_SHORT).show();
//                                });
                            }
                        }).start();
                    }
                }
            };

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isLongPress = true;
                        direccionPP.setTextColor(pulsarColor);
                        handler.postDelayed(longPressRunnable, LONG_PRESS_TIMEOUT);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        handler.removeCallbacks(longPressRunnable);
                        direccionPP.setTextColor(originalColor);
                        isLongPress = false;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (!v.isPressed()) {
                            handler.removeCallbacks(longPressRunnable);
                            direccionPP.setTextColor(originalColor);
                            isLongPress = false;
                        }
                        break;
                }
                return true;
            }
        });
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION);
        } else {
            getLastLocation();
        }
    }

    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<Location> task = mfusedLocationProviderClient.getLastLocation();    
            task.addOnSuccessListener(location -> {
                if (location != null) {

                    if (paradaMapa != null){
                        currentLocation = paradaMapa;
                    }else {
                        currentLocation = location;
                    }

                    SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapaLinners);
                    if (mapFragment != null) {
                        mapFragment.getMapAsync(this);
                    }


                    try {
                        getListOfPositions(new OnPositionsLoadedListener() {
                            @Override
                            public void onPositionsLoaded(ArrayList<Address> positions) {
                                listOfGeoPositions = positions;
                                if (currentLocation != null) {
                                    closestAddress = findClosestAddress(currentLocation, listOfGeoPositions);
                                    if (closestAddress != null) {
                                        paradaCercana = new Location("");
                                        paradaCercana.setLatitude(closestAddress.getLatitude());
                                        paradaCercana.setLongitude(closestAddress.getLongitude());

                                        LatLng closestLatLng = new LatLng(closestAddress.getLatitude(), closestAddress.getLongitude());
                                        mapa.addMarker(new MarkerOptions().position(closestLatLng).title("Parada de autobús más cercana"));
                                        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(closestLatLng, 17));

                                        getAddressFromCoordinates(closestLatLng.latitude, closestLatLng.longitude);
                                    } else {
                                        Toast.makeText(getContext(), "No hay paradas de autobuses cerca", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onError(Exception e) {
                                Toast.makeText(getContext(), "Error al cargar la posición", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (ExcepcionLineas e) {
                        throw new RuntimeException(e);
                    }

                }
            });
        }
    }

    private void getListOfPositions(OnPositionsLoadedListener listener) throws ExcepcionLineas{
        new Thread(() -> {
            ArrayList<Address> geoPositions = new ArrayList<>();

            try {
                LineasCC linea = new LineasCC(ipBD);

                ArrayList<Parada> lc = linea.leerParadas();
                for (Parada parada : lc) {
                    Address geoPosition = new Address(Locale.getDefault());
                    geoPosition.setLatitude(Double.parseDouble(parada.getLatitud()));
                    geoPosition.setLongitude(Double.parseDouble(parada.getLongitud()));
                    geoPositions.add(geoPosition);
                }

                requireActivity().runOnUiThread(() -> listener.onPositionsLoaded(geoPositions));
            } catch (ExcepcionLineas ex) {
                requireActivity().runOnUiThread(() -> {
                    SpannableString title = new SpannableString("No se pudo conectar");
                    int amarilloLinners = getResources().getColor(R.color.amarilloLinners);
                    title.setSpan(new ForegroundColorSpan(amarilloLinners), 0, title.length(), 0);

                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setTitle(title);
                    dialog.setMessage("Se produjo un problema al intentar establecer una conexión con el servidor.");
                    dialog.setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss());
                    dialog.show();
                });
            } catch (NullPointerException e){
                requireActivity().runOnUiThread(() -> {
                    SpannableString title = new SpannableString("No se pudo conectar");
                    int amarilloLinners = getResources().getColor(R.color.amarilloLinners);
                    title.setSpan(new ForegroundColorSpan(amarilloLinners), 0, title.length(), 0);

                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setTitle(title);
                    dialog.setMessage("Se produjo un problema al intentar establecer una conexión con el servidor.");
                    dialog.setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss());
                    dialog.show();
                });
            }
        }).start();
    }

    interface OnPositionsLoadedListener {
        void onPositionsLoaded(ArrayList<Address> positions);
        void onError(Exception e);
    }

    public Address findClosestAddress(Location currentLocation, ArrayList<Address> addresses) {
        if (addresses == null || addresses.isEmpty()) {
            return null;
        }

        Address closestAddress = null;
        float minDistance = Float.MAX_VALUE;

        for (Address address : addresses) {
            Location location = new Location("");
            location.setLatitude(address.getLatitude());
            location.setLongitude(address.getLongitude());

            float distance = currentLocation.distanceTo(location);
            if (distance < minDistance) {
                minDistance = distance;
                closestAddress = address;
            }
        }

        if (minDistance < ONE_KM) {
            return closestAddress;
        } else {
            return null;
        }
    }

    private void setUpModeloLinea(View view) {

        new Thread(() -> {
            try {
                LineasCC linea = new LineasCC(ipBD);
                Integer idParada = 0;

                ArrayList<LineaParada> lc = linea.leerLineasParadas();
                for(LineaParada buscarId : lc) {
                    if(buscarId.getParada().getPDireccion().equals(nomDireccion)){
                        idParada = buscarId.getParada().getIdParada();
                        break;
                    }
                }

                for(LineaParada buscarLineas : lc)
                {
                    if(buscarLineas.getParada().getIdParada().equals(idParada)){
//                        ModeloLinea modelo = new ModeloLinea
//                                ("" + buscarLineas.getParada().getIdParada()
//                                        , "" + buscarLineas.getLinea().getIdLinea()
//                                        , "2,50", imagenLogo);
//                        modeloLinea.add(modelo);
                        if (buscarLineas.getLinea().getIdLinea() == 1){

                            String precioComa = "" + buscarLineas.getLinea().getPrecio();
                            String precio = precioComa.replace(".", ",") + " €";

                            ModeloLinea modelo = new ModeloLinea(
                                    "El Hoyo",
                                    "Juzgados",
                                    precio,
                                    imagenLogoL1,
                                    imagenLogoAlsa);

                            for (int i = 0; i < modeloLinea.size(); i++) {
                                if (modeloLinea.get(i).getOrigen().equals(modelo.getOrigen())){
                                    modeloLinea.remove(i);
                                }
                            }

                            modeloLinea.add(modelo);
                        }
                        if (buscarLineas.getLinea().getIdLinea() == 2){

                            String precioComa = "" + buscarLineas.getLinea().getPrecio();
                            String precio = precioComa.replace(".", ",") + " €";

                            ModeloLinea modelo = new ModeloLinea(
                                    "Iglesia Oriñón",
                                    "Rda. San Francisco",
                                    precio,
                                    imagenLogoL2,
                                    imagenLogoAlsa);

                            for (int i = 0; i < modeloLinea.size(); i++) {
                                if (modeloLinea.get(i).getOrigen().equals(modelo.getOrigen())){
                                    modeloLinea.remove(i);
                                }
                            }

                            modeloLinea.add(modelo);
                        }
                        if (buscarLineas.getLinea().getIdLinea() == 5){

                            String precioComa = "" + buscarLineas.getLinea().getPrecio();
                            String precio = precioComa.replace(".", ",") + " €";

                            ModeloLinea modelo = new ModeloLinea(
                                    "El Corte Ingles",
                                    "Hospital de Cabueñes",
                                    precio,
                                    imagenLogoL22,
                                    imagenLogoGijon);

                            for (int i = 0; i < modeloLinea.size(); i++) {
                                if (modeloLinea.get(i).getOrigen().equals(modelo.getOrigen())){
                                    modeloLinea.remove(i);
                                }
                            }

                            modeloLinea.add(modelo);
                        }
                        if (buscarLineas.getLinea().getIdLinea() == 6){

                            String precioComa = "" + buscarLineas.getLinea().getPrecio();
                            String precio = precioComa.replace(".", ",") + " €";

                            ModeloLinea modelo = new ModeloLinea(
                                    "Cerillero",
                                    "Contrueces",
                                    precio,
                                    imagenLogoL12,
                                    imagenLogoGijon);

                            for (int i = 0; i < modeloLinea.size(); i++) {
                                if (modeloLinea.get(i).getOrigen().equals(modelo.getOrigen())){
                                    modeloLinea.remove(i);
                                }
                            }

                            modeloLinea.add(modelo);
                        }
                        if (buscarLineas.getLinea().getIdLinea() == 7){

                            String precioComa = "" + buscarLineas.getLinea().getPrecio();
                            String precio = precioComa.replace(".", ",") + " €";

                            ModeloLinea modelo = new ModeloLinea(
                                    "Monteana",
                                    "La Pedrera",
                                    precio,
                                    imagenLogoL24,
                                    imagenLogoGijon);

                            for (int i = 0; i < modeloLinea.size(); i++) {
                                if (modeloLinea.get(i).getOrigen().equals(modelo.getOrigen())){
                                    modeloLinea.remove(i);
                                }
                            }

                            modeloLinea.add(modelo);
                        }
                    }
                }

                requireActivity().runOnUiThread(() -> {
                    if (modeloLinea.isEmpty()) {
                    } else {
                        adaptarRVinicio(view);
                    }
                });
            } catch (ExcepcionLineas ex) {
//                requireActivity().runOnUiThread(() -> {
//
//                });
            }
        }).start();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mapa = googleMap;

        LatLng cordenadas = new LatLng(paradaCercana.getLatitude(), paradaCercana.getLongitude());
        mapa.addMarker(new MarkerOptions().position(cordenadas));
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(cordenadas, 17));

        mapa.getUiSettings().setZoomControlsEnabled(true);

        getAddressFromCoordinates(cordenadas.latitude, cordenadas.longitude);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_PERMISSION_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }else {
                Toast.makeText(getContext(), "Debe de aceptar los permisos", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getAddressFromCoordinates(double latitud, double longitude){
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> address = geocoder.getFromLocation(latitud, longitude, 1);
            if (address !=null && !address.isEmpty()){
                Address direccion = address.get(0);
                nomDireccion = direccion.getFeatureName();
                String nomLocalidad = direccion.getLocality();
                String nomPais = direccion.getCountryName();
                direccionPP.setText(nomDireccion);

                if (direccionPP.getText() != null){
                    if (loadingDialog != null && loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }
                }

                localidadPP.setText(nomLocalidad + ", " + nomPais);

                String usuarioid = sharedPreferences.getString("idUsuario", null);
                if (usuarioid != null){
                    comprobarSiEsPF();
                }
                setUpModeloLinea(getView());
                buscarIdNombreDiccion();
            }
            else {
//                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
//                dialog.setTitle("No se pudo encontrar una parada");
//                dialog.setMessage("No hay paradas cercanas en tu ubicación");
//                dialog.setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss());
//                dialog.show();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public Location ubiMapa(Location ubicacion){
        paradaMapa = ubicacion;
        return paradaMapa;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {}
    @Override
    public void onMapClick(@NonNull LatLng latLng) {}
    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {}

    public void adaptarRVinicio(View view){
        LineaRecyclerViewAdapter adapter = new LineaRecyclerViewAdapter(modeloLinea);

        RecyclerView recyclerView = view.findViewById(R.id.reViewLista);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    public void dialogNoPf(){
        SpannableString title = new SpannableString("Ya es favorita");
        int amarilloLinners = getResources().getColor(R.color.amarilloLinners);
        title.setSpan(new ForegroundColorSpan(amarilloLinners), 0, title.length(), 0);

        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialogpf, null);

        ImageView dialogImage = dialogView.findViewById(R.id.imagenId);

        dialogImage.setImageResource(R.drawable.estrella_nopf);

        dialog.setView(dialogView);
        dialog.setTitle(title);
        dialog.setMessage("No puedes añadir " + nomDireccion + " a tus paradas favoritas porque ya esta añadida a la lista.");
        dialog.setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss());
        dialog.show();
    }

    public void dialogNuevaPf(){

    }

    public void buscarIdNombreDiccion(){
        new Thread(() -> {
            try {
                LineasCC linea = new LineasCC(ipBD);

                ArrayList<Parada> lc = linea.leerParadas();
                for (Parada buscarIdParada : lc) {
                    if(buscarIdParada.getPDireccion().equals(nomDireccion)){
                        idParadaAPf = buscarIdParada.getIdParada();
                        localidadParada = buscarIdParada.getPLocalidad();
                    }
                }

                requireActivity().runOnUiThread(() -> {

                });

            } catch (ExcepcionLineas ex) {
                requireActivity().runOnUiThread(() -> {

                });
            }
        }).start();
    }

    public void comprobarSiEsPF(){
        new Thread(() -> {
            try {
                LineasCC linea = new LineasCC(ipBD);

                Integer idUsuario = Integer.parseInt(sharedPreferences.getString("idUsuario", null));

                Log.d("Prueba", "lo que hay dentro " + idUsuario);


                    ArrayList<ParadaFavorita> lc = linea.leerParadasFavoritas();
                    for (ParadaFavorita siesPf : lc) {
                        if (siesPf.getUsuario().getIdUsuario().equals(idUsuario)){
                            if(siesPf.getParada().getPDireccion().equals(nomDireccion)){
                                imagenPin.setImageResource(R.drawable.pinpf2);
                            }
                        }
                    }

                    requireActivity().runOnUiThread(() -> {

                    });


            } catch (ExcepcionLineas ex) {
                requireActivity().runOnUiThread(() -> {

                });
            }
        }).start();
    }
}