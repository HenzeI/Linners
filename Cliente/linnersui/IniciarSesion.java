package com.example.linnersui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import jp.wasabeef.blurry.Blurry;
import lineascc.LineasCC;
import pojoslineas.ExcepcionLineas;
import pojoslineas.Parada;
import pojoslineas.Usuario;

public class IniciarSesion extends AppCompatActivity implements View.OnClickListener {

    public Button btnIniciarSesion, btnRegistrarse;

    public EditText camEmail, camContra;
    public TextView errorContra;
    public Usuario usuario = new Usuario();

    private AtomicBoolean exito = new AtomicBoolean(false);

    public String ipBD = "172.16.5.190";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        btnIniciarSesion = findViewById(R.id.btnIniSesionCInSe);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);

        camEmail = findViewById(R.id.campoEmail);
        camContra = findViewById(R.id.campoContrasena);

        errorContra = findViewById(R.id.errores);

        btnIniciarSesion.setOnClickListener(v -> loginUser());
        btnRegistrarse.setOnClickListener(this);
    }

    private void loginUser() {

        String email = camEmail.getText().toString().trim();
        String contrasena = camContra.getText().toString().trim();

        if(email.isEmpty() || contrasena.isEmpty()){
            Toast.makeText(this, "Por favor ingrese todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        new LoginTask(email, contrasena).execute();
    }

    private class LoginTask extends AsyncTask<Void, Void, Boolean> {
        private String email;
        private String contrasena;
        private AlertDialog loadingDialog;
        private View progressBar;
        private View contentView;

        public LoginTask(String email, String contrasena) {
            this.email = email;
            this.contrasena = contrasena;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Crear el AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(IniciarSesion.this, R.style.TransparentDialog);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.progress_dialog, null);
            builder.setView(dialogView);
            builder.setCancelable(false);

            loadingDialog = builder.create();
            loadingDialog.show();

//            progressBar = LayoutInflater.from(IniciarSesion.this).inflate(R.layout.progress_dialog, null);
//
//            ConstraintLayout myConstraintLayout = progressBar.findViewById(R.id.myConstraintLayout);
//
//            if (myConstraintLayout != null){
////                myConstraintLayout.setBackgroundResource(R.drawable.fondop);
//
//                myConstraintLayout.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        Blurry.with(getApplicationContext())
//                                .radius(25) // Ajusta el radio del desenfoque
//                                .sampling(2) // Ajusta el muestreo para controlar el rendimiento
//                                .onto(myConstraintLayout);
//
//                    }
//                });
//
//            }
////            myConstraintLayout.setBackgroundColor(getResources().getColor(R.color.amarilloLinners));
//
//            // Mostrar el dialogo de progreso encima del contenido actual
//            ViewGroup rootView = findViewById(android.R.id.content);
//            rootView.addView(progressBar);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {

                LineasCC linea = new LineasCC(ipBD);

                ArrayList<Usuario> lc = linea.leerUsuarios();
                for (Usuario usu : lc) {
                    if(usu.getCorreoElectronico().equals(email)){
                        usuario = usu;
                        break;
                    }
                }

                if (usuario == null) {
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                    });
                }

                String hashedInputPassword = md5(contrasena);

                if (hashedInputPassword.equals(usuario.getContrasena())) {
                    exito.set(true);
                    runOnUiThread(() -> {
                        errorContra.setVisibility(View.INVISIBLE);
                        camContra.setBackgroundResource(R.drawable.boton_diseno);
                        Toast.makeText(getApplicationContext(), "Login exitoso", Toast.LENGTH_SHORT).show();
                    });
                    return true;
                } else {
                    runOnUiThread(() -> {
                        errorContra.setVisibility(View.VISIBLE);
                        camContra.setBackgroundResource(R.drawable.boton_diseno_fallocampo);
                    });
                    return false;
                }

            } catch (ExcepcionLineas ex) {
                ex.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(getApplicationContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
                });
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);

            if (loadingDialog != null && loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }

//            ViewGroup rootView = findViewById(android.R.id.content);
//            rootView.removeView(progressBar);

            if (success) {
                SharedPreferences sharedPreferences = getSharedPreferences("DatosUsuario", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("idUsuario", "" + usuario.getIdUsuario());
                editor.putString("correo", email);
                editor.putString("nomUsuario", email.replace("@gmail.com", ""));
                editor.putString("localidad", usuario.getLocalidad());
                editor.putString("direccion", usuario.getDireccion());
                editor.putString("direccionCompleta", usuario.getLocalidad() + ", " + usuario.getProvincia());
                editor.apply();

                Intent intent = new Intent(IniciarSesion.this, PantallaPrincipal.class);
                intent.putExtra("Fragmets", "inicio");
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(IniciarSesion.this, "Credenciales inválidas", Toast.LENGTH_SHORT).show();
            }
        }

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


    @Override
    public void onClick(View v) {

        Intent intent = null;

        if (v.getId() == R.id.btnIniSesionCInSe)
        {
//            intent = new Intent(this, MainActivity.class);
        }
        else if (v.getId() == R.id.btnRegistrarse)
        {
            intent = new Intent(this, Registrarse.class);
        }

        startActivity(intent);
    }

}
