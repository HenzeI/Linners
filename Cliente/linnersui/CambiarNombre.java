package com.example.linnersui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CambiarNombre extends AppCompatActivity {

    private EditText campoCambiarNombre;
    private TextView mostrarNombreUsuarioViejo;
    private Button btnCambiarNombre;
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_nombre);

        campoCambiarNombre = findViewById(R.id.eTCambiarNombre);
        mostrarNombreUsuarioViejo = findViewById(R.id.nombreActual);
        btnCambiarNombre = findViewById(R.id.btnCambiarNombre);

        sharedPreferences = getSharedPreferences("DatosUsuario", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        String nUsuario = sharedPreferences.getString("nomUsuario", null);

        mostrarNombreUsuarioViejo.setText("Nombre de usuario actual " + nUsuario);

        btnCambiarNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nuevoNombre = "" + campoCambiarNombre.getText();
                editor.putString("nomUsuario", nuevoNombre);
                editor.apply();

                Toast.makeText(CambiarNombre.this, "Nombre cambiado correctamente", Toast.LENGTH_SHORT).show();

                intent = new Intent(CambiarNombre.this, PantallaPrincipal.class);
                intent.putExtra("Fragmets", "perfil");
                startActivity(intent);
            }
        });
    }
}