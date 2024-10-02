package com.example.linnersui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DireccionCam extends AppCompatActivity {

    private EditText campoCambiarDireccion;
    private TextView mostrarDireccionUsuarioViejo;
    private Button btnCambiarDireccion;
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Intent intent = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direccion_cam);

        campoCambiarDireccion = findViewById(R.id.eTCambiarDireccion);
        mostrarDireccionUsuarioViejo = findViewById(R.id.direccionActual);
        btnCambiarDireccion = findViewById(R.id.btnCambiarDireccion);

        sharedPreferences = getSharedPreferences("DatosUsuario", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        String direccion = sharedPreferences.getString("direccion", null);

        mostrarDireccionUsuarioViejo.setText("Dirección actual " + direccion);

        btnCambiarDireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nuevaDireccion = "" + campoCambiarDireccion.getText();
                editor.putString("direccion", nuevaDireccion);
                editor.apply();

                Toast.makeText(DireccionCam.this, "Dirección cambiada correctamente", Toast.LENGTH_SHORT).show();

                intent = new Intent(DireccionCam.this, PantallaPrincipal.class);
                intent.putExtra("Fragmets", "perfil");
                startActivity(intent);
            }
        });
    }
}