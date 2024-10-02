package com.example.linnersui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import lineascc.LineasCC;
import pojoslineas.ExcepcionLineas;
import pojoslineas.ParadaFavorita;
import pojoslineas.Usuario;

public class CambiarContra extends AppCompatActivity {

    private EditText campoNuevaContra, campoRepitaContra;
    private Button btnCambiarContra;
    public TextView errorContra;
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public String ipBD = "172.16.5.190";
    public Intent intent = null;
    public Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_contra);

        campoNuevaContra = findViewById(R.id.eTNuevaContra);
        campoRepitaContra = findViewById(R.id.eTRepetirContra);
        btnCambiarContra = findViewById(R.id.btnCambiarNombre);
        errorContra = findViewById(R.id.errores);

        sharedPreferences = getSharedPreferences("DatosUsuario", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        usuario = new Usuario();

        btnCambiarContra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("Prueba", "Contraseña nueva " + campoNuevaContra.getText());
                Log.d("Prueba", "Contraseña nueva repetir " + campoRepitaContra.getText());

                if(!campoNuevaContra.getText().toString().equals(campoRepitaContra.getText().toString())){
                    errorContra.setVisibility(View.VISIBLE);
                    campoNuevaContra.setBackgroundResource(R.drawable.boton_diseno_fallocampo);
                    campoRepitaContra.setBackgroundResource(R.drawable.boton_diseno_fallocampo);
                }
                else{
                    errorContra.setVisibility(View.GONE);
                    campoNuevaContra.setBackgroundResource(R.drawable.boton_diseno);
                    campoRepitaContra.setBackgroundResource(R.drawable.boton_diseno);

                    new Thread(() -> {
                        try {
                            String contrasena = "" + campoNuevaContra.getText();

                            LineasCC linea = new LineasCC(ipBD);
                            Integer idUsuario = Integer.parseInt(sharedPreferences.getString("idUsuario", null));
                            usuario = linea.leerUsuario(idUsuario);

                            if (usuario.getIdUsuario() != null){
                                usuario.setContrasena(md5(contrasena));
                            }

                            runOnUiThread(() -> {
                                new Thread(() -> {
                                    try {
                                        LineasCC linea2 = new LineasCC(ipBD);
                                        linea2.modificarUsuario(idUsuario, usuario);

                                    } catch (ExcepcionLineas ex) {
                                        runOnUiThread(() -> {
                                            Toast.makeText(CambiarContra.this, "Error al cambiar la contraseña", Toast.LENGTH_SHORT).show();
                                            ex.printStackTrace();
                                        });
                                    }
                                }).start();
                            });

                        } catch (ExcepcionLineas ex) {
//                                requireActivity().runOnUiThread(() -> {
//                                    Toast.makeText(getContext(), "Error al leer paradas favoritas", Toast.LENGTH_SHORT).show();
//                                });
                        }
                    }).start();

                    Toast.makeText(CambiarContra.this, "Contraseña cambiada exitosamente", Toast.LENGTH_SHORT).show();

                    intent = new Intent(CambiarContra.this, PantallaPrincipal.class);
                    intent.putExtra("Fragmets", "perfil");
                    startActivity(intent);
                }
            }
        });

    }

    private String md5(String s) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();

            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}