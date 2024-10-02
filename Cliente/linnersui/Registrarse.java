package com.example.linnersui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import lineascc.LineasCC;
import pojoslineas.ExcepcionLineas;
import pojoslineas.ParadaFavorita;
import pojoslineas.Usuario;

public class Registrarse extends AppCompatActivity implements View.OnClickListener {

    // Datos personales
    private EditText rcampoDni, rcampoNombre, rcampoApellido1, rcampoApellido2;

    // Datos de direccion
    private EditText rcampoDireccion, rcampoLocalidad, rcampoProvincia;

    // Datos de contacto
    private EditText rcampoCorrElec, rcampoTelef, rcampoContra, rcampoRepiteContra;

    private Button btnrRegistrarse;
    public String ipBD = "172.16.5.190";

    public boolean esExiste;
    public boolean esExisteCorreo;
    public boolean esExisteTelefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        rcampoDni = findViewById(R.id.rcampoDni);
        rcampoNombre = findViewById(R.id.rcampoNombre);
        rcampoApellido1 = findViewById(R.id.rcampoApellido1);
        rcampoApellido2 = findViewById(R.id.rcampoApellido2);

        rcampoDireccion = findViewById(R.id.rcampoDireccion);
        rcampoLocalidad = findViewById(R.id.rcampoLocalidad);
        rcampoProvincia = findViewById(R.id.rcampoProvincia);

        rcampoCorrElec = findViewById(R.id.rcampoCorrElec);
        rcampoTelef = findViewById(R.id.rcampoTelef);
        rcampoContra = findViewById(R.id.rcampoContra);
        rcampoRepiteContra = findViewById(R.id.rcampoRepiteContra);

        btnrRegistrarse = findViewById(R.id.btnrRegistrarse);

        btnrRegistrarse.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        new Thread(() -> {

            existeDni();

            try {
                LineasCC linea = new LineasCC(ipBD);
                Usuario usuario = new Usuario();

                String dni = rcampoDni.getText().toString().trim();
                String nombreUsuario = "" + rcampoNombre.getText();
                String apellido1 = "" + rcampoApellido1.getText();
                String apellido2 = "" + rcampoApellido2.getText();
                String direccion = "" + rcampoDireccion.getText();
                String localidad = "" + rcampoLocalidad.getText();
                String provincia = "" + rcampoProvincia.getText();
                String correo = rcampoCorrElec.getText().toString().trim();
                String telefono = rcampoTelef.getText().toString().trim();
                String nuevaContra = "" + rcampoContra.getText();
                String repetContra = "" + rcampoRepiteContra.getText();

                if (dni.isEmpty() || nombreUsuario.isEmpty() || apellido1.isEmpty() || direccion.isEmpty() ||
                localidad.isEmpty() || provincia.isEmpty() || correo.isEmpty() || telefono.isEmpty() ||
                nuevaContra.isEmpty() || repetContra.isEmpty()){
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Por favor ingrese todos los campos", Toast.LENGTH_SHORT).show();
                    });
                    return;
                }

                if (!nuevaContra.equals(repetContra)){
                    runOnUiThread(() -> {
                        rcampoContra.setBackgroundResource(R.drawable.boton_diseno_fallocampo);
                        rcampoRepiteContra.setBackgroundResource(R.drawable.boton_diseno_fallocampo);
                        Toast.makeText(Registrarse.this, "Contraseñas diferentes", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    runOnUiThread(() -> {
                        rcampoContra.setBackgroundResource(R.drawable.boton_diseno);
                        rcampoRepiteContra.setBackgroundResource(R.drawable.boton_diseno);
                    });

                }

                if (esExiste){
                    runOnUiThread(() -> {
                        rcampoDni.setBackgroundResource(R.drawable.boton_diseno_fallocampo);
                        Toast.makeText(Registrarse.this, "Ese DNI ya esta en uso", Toast.LENGTH_SHORT).show();
                    });
                    return;
                } else{
                    runOnUiThread(() -> {
                        rcampoDni.setBackgroundResource(R.drawable.boton_diseno);
                    });
                }

                if (esExisteCorreo){
                    runOnUiThread(() -> {
                        rcampoCorrElec.setBackgroundResource(R.drawable.boton_diseno_fallocampo);
                        Toast.makeText(Registrarse.this, "Ese Correo ya esta en uso", Toast.LENGTH_SHORT).show();
                    });
                    return;
                } else {
                    runOnUiThread(() -> {
                        rcampoCorrElec.setBackgroundResource(R.drawable.boton_diseno);
                    });
                }

                if (esExisteTelefono){
                    runOnUiThread(() -> {
                        rcampoTelef.setBackgroundResource(R.drawable.boton_diseno_fallocampo);
                        Toast.makeText(Registrarse.this, "Ese Telefono ya esta en uso", Toast.LENGTH_SHORT).show();
                    });
                    return;
                } else {
                    runOnUiThread(() -> {
                        rcampoTelef.setBackgroundResource(R.drawable.boton_diseno);
                    });
                }


                // Comprobar DNI
//                if(isValidDNI(dni)){
//                    // Pasa al meetodo
//                } else {
//                    runOnUiThread(() -> {
//                        Toast.makeText(Registrarse.this, "DNI inválido", Toast.LENGTH_SHORT).show();
//                    });
//                    return;
//                }

                // Comprobar Correo electronico
                if (isValidEmail(correo)){
                    runOnUiThread(() -> {
                        rcampoCorrElec.setBackgroundResource(R.drawable.boton_diseno);
                    });
                } else {
                    runOnUiThread(() -> {
                        rcampoCorrElec.setBackgroundResource(R.drawable.boton_diseno_fallocampo);
                        Toast.makeText(Registrarse.this, "Correo invalido", Toast.LENGTH_SHORT).show();
                    });
                    return;
                }

                // Comprobar telefono
                if (isValidPhoneNumber(telefono)){
                    runOnUiThread(() -> {
                        rcampoTelef.setBackgroundResource(R.drawable.boton_diseno);
                    });
                } else {
                    runOnUiThread(() -> {
                        rcampoTelef.setBackgroundResource(R.drawable.boton_diseno_fallocampo);
                        Toast.makeText(Registrarse.this, "telefono invalido", Toast.LENGTH_SHORT).show();
                    });
                    return;
                }

                usuario.setDni(dni);
                usuario.setNombre("" + rcampoNombre.getText());
                usuario.setApellido1("" + rcampoApellido1.getText());
                usuario.setApellido2("" + rcampoApellido2.getText());
                usuario.setDireccion("" + rcampoDireccion.getText());
                usuario.setLocalidad("" + rcampoLocalidad.getText());
                usuario.setProvincia("" + rcampoProvincia.getText());
                usuario.setCorreoElectronico(correo);
                usuario.setTelefono(telefono);



                String md5Contra = md5(nuevaContra);

                usuario.setContrasena(md5Contra);

                linea.insertarUsuario(usuario);

                runOnUiThread(() -> {
                    Toast.makeText(getApplicationContext(), "Registro exitoso", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Registrarse.this, IniciarSesion.class);
                    startActivity(intent);
                    finish();
                });

            } catch (ExcepcionLineas ex) {
                runOnUiThread(() -> {
                    Toast.makeText(Registrarse.this, "Error al intentar registrarse", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();

    }

    private boolean isValidDNI(String dni) {
        if (dni.length() != 9) {
            return false; // El DNI debe tener exactamente 9 caracteres
        }

        // Extraer la parte numérica y la letra del DNI
        String numberPart = dni.substring(0, 8);
        char letter = Character.toUpperCase(dni.charAt(8));

        // Calcular la letra de control
        int dniNumber;
        try {
            dniNumber = Integer.parseInt(numberPart);
        } catch (NumberFormatException e) {
            return false; // Si la parte numérica no es un número válido
        }

        // Letras de control del DNI español
        final String DNI_LETTERS = "TRWAGMYFPDXBNJZSQVHLCKE";
        char expectedLetter = DNI_LETTERS.charAt(dniNumber % 23);

        // Comprobar si la letra es correcta
        return letter == expectedLetter;
    }

    private boolean isValidEmail(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPhoneNumber(String telefono) {
        // Expresión regular para números de teléfono españoles
        Pattern pattern = Pattern.compile("^(\\+34|0034|34)?[6|7][0-9]{8}$");
        return telefono != null && pattern.matcher(telefono).matches();
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

    public void existeDni(){
        new Thread(() -> {
            try {
                LineasCC linea = new LineasCC(ipBD);
                ArrayList<Usuario> usuarios = linea.leerUsuarios();
                String campoDni = "" + rcampoDni.getText();
                String campoCorreo = "" + rcampoCorrElec.getText();
                String campoTele = "" + rcampoTelef.getText();
                boolean siExiste = false;
                boolean siExisteCorreo = false;
                boolean siExisteTele = false;

                for(Usuario exDni : usuarios){
                    if (exDni.getDni().equals(campoDni)){
                        siExiste = true;
                        esExiste = siExiste;
                    }
                    else {
                        esExiste = siExiste;
                    }
                    if (exDni.getCorreoElectronico().equals(campoCorreo)){
                        siExisteCorreo = true;
                        esExisteCorreo = siExisteCorreo;
                    }
                    else {
                        esExisteCorreo = siExisteCorreo;
                    }
                    if (exDni.getTelefono().equals(campoTele)){
                        siExisteTele = true;
                        esExisteTelefono = siExisteTele;
                    }
                    else {
                        esExisteTelefono = siExisteTele;
                    }
                }


                boolean finalSiExiste = siExiste;
                runOnUiThread(() -> {
                    esExiste = finalSiExiste;
                });

            } catch (ExcepcionLineas ex) {
                runOnUiThread(() -> {
                    Toast.makeText(Registrarse.this, "Error al intentar registrarse", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }
}