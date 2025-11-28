package com.example.marvel_comic_app.ui.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.marvel_comic_app.R;
import com.example.marvel_comic_app.ui.fragments.ComicsFragment;
import com.example.marvel_comic_app.ui.fragments.ConfigFragment;
import com.example.marvel_comic_app.ui.fragments.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //referenciar el BottomNavigationView desde el layout:
        bottomNav = findViewById(R.id.bottomNav);

        //ccargar el fragmento inicial (HomeFrag) cuando se crea la actividad  por primera vez
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }

        // configuracion del listener para manejar los clics en los ítems del menú
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.nav_comics) {
                selectedFragment = new ComicsFragment();
            } else if (itemId == R.id.nav_config) {
                selectedFragment = new ConfigFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true; //indicar que el evento ha sido manejado
            }

            return false;
        });
    }

    //met. para reemplazar el contenido del FrameLayout con un nuevo fragmento
    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.commit();
    }
}










