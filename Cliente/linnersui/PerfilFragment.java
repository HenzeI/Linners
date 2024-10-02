package com.example.linnersui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class PerfilFragment extends Fragment implements View.OnClickListener {

    public TextView nombreUsuario, direccionUsuario;
    public Button btnCamNombre, btnCamDireccion, btnCamContrasena, btnGestPf, btnCerraSesion;
    public Intent intent = null;
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_perfil, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nombreUsuario = view.findViewById(R.id.perNomUsuario);
        direccionUsuario = view.findViewById(R.id.perDirecUsu);

        btnCamNombre = view.findViewById(R.id.btnCamNombre);
        btnCamDireccion = view.findViewById(R.id.btnCamDireccion);
        btnCamContrasena = view.findViewById(R.id.btnCamContrasena);
        btnGestPf = view.findViewById(R.id.btnGestiPaFavo);
        btnCerraSesion = view.findViewById(R.id.btnCerrarSesion);

        btnCamNombre.setOnClickListener(this);
        btnCamDireccion.setOnClickListener(this);
        btnCamContrasena.setOnClickListener(this);
        btnGestPf.setOnClickListener(this);
        btnCerraSesion.setOnClickListener(this);

        sharedPreferences = requireContext().getSharedPreferences("DatosUsuario", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        String idUsuario = sharedPreferences.getString("idUsuario", null);
        String correo = sharedPreferences.getString("correo", null);
        String direccionCompleta = sharedPreferences.getString("direccionCompleta", null);
        String direccion = sharedPreferences.getString("direccion", null);

        String nUsu = sharedPreferences.getString("nomUsuario", null);
        Log.d("Prueba", "nombre en el shared" + nUsu);

        if (idUsuario != null){
            nombreUsuario.setText(nUsu);
            direccionUsuario.setText(direccion + ", " +direccionCompleta);
        }
        else{
            nombreUsuario.setText("Anónimo");
            direccionUsuario.setText("Calle 1, Madrid, España");
        }


    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnCamNombre)
        {
            if (nombreUsuario.getText().equals("Anónimo")){
                adverIniSesion();
            }
            else{
                intent = new Intent(getContext(), CambiarNombre.class);
                startActivity(intent);
            }
        }
        else if (v.getId() == R.id.btnCamDireccion)
        {
            if (nombreUsuario.getText().equals("Anónimo")){
                adverIniSesion();
            }
            else{
                intent = new Intent(getContext(), DireccionCam.class);
                startActivity(intent);
            }
        }
        else if (v.getId() == R.id.btnCamContrasena)
        {
            if (nombreUsuario.getText().equals("Anónimo")){
                adverIniSesion();
            }
            else{
                intent = new Intent(getContext(), CambiarContra.class);
                startActivity(intent);
            }
        }
        else if (v.getId() == R.id.btnGestiPaFavo)
        {
            if (nombreUsuario.getText().equals("Anónimo")){
                adverIniSesion();
            }
            else{
                intent = new Intent(getContext(), GestionarParadaFavorita.class);
                startActivity(intent);
            }

        }
        else if (v.getId() == R.id.btnCerrarSesion){
            editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            intent = new Intent(getContext(), SeleccionUbicacion.class);
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contraint, new PerfilFragment())
                        .addToBackStack(null)
                        .commit();
            }
            startActivity(intent);
        }
    }

    public void adverIniSesion(){
        SpannableString title = new SpannableString("Inicio de sesión necesario");
        int amarilloLinners = getResources().getColor(R.color.amarilloLinners);
        title.setSpan(new ForegroundColorSpan(amarilloLinners), 0, title.length(), 0);

        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle(title);
        dialog.setMessage("Debes de Iniciar sesión o registrarte para accedes a estos privilegios");
        dialog.setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss());
        dialog.show();
    }

    public void actualizarNombreUsuario(String nuevoNombre) {
        if (nombreUsuario != null) {
            nombreUsuario.setText(nuevoNombre);
        }
    }
}