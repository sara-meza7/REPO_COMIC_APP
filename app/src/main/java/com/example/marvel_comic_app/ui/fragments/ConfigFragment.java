package com.example.marvel_comic_app.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.marvel_comic_app.R;
import com.example.marvel_comic_app.ui.activities.LoginActivity;
import com.example.marvel_comic_app.util.PrefsManager;

public class ConfigFragment extends Fragment {

    private TextView txtUserName, txtUserEmail;
    Button btnLogout;
    private PrefsManager prefsManager;

    //Constructor vacio
    public ConfigFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_config, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prefsManager = new PrefsManager(requireActivity());      // Se usa el requireActivity() para traer el contexto

        txtUserName = view.findViewById(R.id.txtUserName);
        txtUserEmail = view.findViewById(R.id.txtUserEmail);
        btnLogout = view.findViewById(R.id.btnLogout);

        // 2. Carga y muestra los datos del usuario
        loadUserData();

        // 3. Configura el listener para el botón de cerrar sesión
        btnLogout.setOnClickListener(v -> logout());
    }

    private void loadUserData() {
        // Leemos los datos guardados en SharedPreferences usando nuestro PrefsManager
        String userName = prefsManager.getUserName();
        String userEmail = prefsManager.getUserEmail();

        // Mostramos los datos en los TextViews correspondientes
        txtUserName.setText(userName);
        txtUserEmail.setText(userEmail);
    }

    private void logout() {
        // Borrar los datos del usuario que se registro del SharedPreferences
        prefsManager.clearUserData();

        //Intent para ir a la pantalla de LoginActivity
        Intent intent = new Intent(getActivity(), LoginActivity.class);

        // Se Añaden flags para limpiar el historial de actividades para evitar que se pueda presionar "atrás" y volver a la app sin iniciar sesión.
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Iniciar en la actividad de Login
        startActivity(intent);

        // Finalizar la actividad actual (MainActivity)
        if (getActivity() != null) {
            getActivity().finish();
        }
    }
}