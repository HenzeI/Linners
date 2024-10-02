package com.example.linnersui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class PantallaPrincipal extends AppCompatActivity {

    public BottomNavigationView barrNavegacion;
    public InicioFragment inicio = new InicioFragment();
    public MapaFragment mapa = new MapaFragment();
    public PerfilFragment perfil = new PerfilFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);

        barrNavegacion = findViewById(R.id.barraNavegacion);
        barrNavegacion.setOnItemSelectedListener(bnItemSelectedListener);

        String fragmentToLoad = getIntent().getStringExtra("Fragmets");


        if (fragmentToLoad != null){
                if (fragmentToLoad.equals("mapa")){
                    mapa = new MapaFragment();
                    barrNavegacion.setSelectedItemId(R.id.mapaPantallaPrincipal);
                    cargarFragment(mapa);
                }
                else if (fragmentToLoad.equals("inicio")) {
                    inicio = new InicioFragment();
                    barrNavegacion.setSelectedItemId(R.id.inicioPantallaPrincipal);
                    cargarFragment(inicio);
                }
                else if (fragmentToLoad.equals("perfil")){
                    perfil = new PerfilFragment();
                    barrNavegacion.setSelectedItemId(R.id.perfilPantallaPrincipal);
                    cargarFragment(perfil);
                }
        } else {
            cargarFragment(inicio);
        }
    }

    private final NavigationBarView.OnItemSelectedListener bnItemSelectedListener = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            if (item.getItemId() == R.id.inicioPantallaPrincipal)
            {
                cargarFragment(inicio);
                return true;
            }
            else if (item.getItemId() == R.id.mapaPantallaPrincipal)
            {
                cargarFragment(mapa);
                return true;
            }
            else if (item.getItemId() == R.id.perfilPantallaPrincipal)
            {
                cargarFragment(perfil);
                return true;
            }

            return false;
        }
    };

    public void cargarFragment(Fragment fragment)
    {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contraint, fragment);
        transaction.commit();
    }

    public void navigateToFragment(int itemId, Fragment fragment) {
        MenuItem menuItem = barrNavegacion.getMenu().findItem(itemId);
        menuItem.setChecked(true);
        if (itemId == R.id.inicioPantallaPrincipal) {
            getSupportFragmentManager().beginTransaction().replace(R.id.contraint,
                    fragment).commit();
        } else if (itemId == R.id.mapaPantallaPrincipal) {
            getSupportFragmentManager().beginTransaction().replace(R.id.contraint,
                    fragment).commit();
        } else if (itemId == R.id.perfilPantallaPrincipal) {
            getSupportFragmentManager().beginTransaction().replace(R.id.contraint,
                    fragment).commit();
        }
    }

}