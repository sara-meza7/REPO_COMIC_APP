package com.example.marvel_comic_app.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.marvel_comic_app.R;
import com.example.marvel_comic_app.ui.activities.LoginActivity;
import com.example.marvel_comic_app.util.PrefsManager;

public class ConfigFragment extends Fragment {

    private TextView txtUserName, txtUserEmail;
    private Button btnLogout, btnDeleteAccount;
    private PrefsManager prefsManager;

    //Constructor vacio
    public ConfigFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_config, container, false);

        txtUserName = view.findViewById(R.id.txtUserName);
        txtUserEmail = view.findViewById(R.id.txtUserEmail);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnDeleteAccount = view.findViewById(R.id.btnDeleteAccount);

        prefsManager = new PrefsManager(getContext());

        // Cargar datos del usuario desde SharedPreferences
        loadUserData();

        btnLogout.setOnClickListener(v -> logout());
        btnDeleteAccount.setOnClickListener(v -> deleteAccount());

        return view;
    }

    private void loadUserData() {
        String name = prefsManager.getUserName();
        String email = prefsManager.getUserEmail();

        txtUserName.setText(name != null ? name : "Usuario");
        txtUserEmail.setText(email != null ? email : "email@example.com");
    }

    private void logout() {
        prefsManager.clearUserData();
        Toast.makeText(getContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }

    private void deleteAccount() {
        Toast.makeText(getContext(), "Función de eliminar cuenta en desarrollo", Toast.LENGTH_SHORT).show();
        // Aquí implementar la lógica para eliminar la cuenta del servidor
    }
}