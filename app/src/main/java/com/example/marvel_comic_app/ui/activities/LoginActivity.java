package com.example.marvel_comic_app.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import com.example.marvel_comic_app.R;
import com.example.marvel_comic_app.model.Usuario;
import com.example.marvel_comic_app.network.ApiClient;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.marvel_comic_app.network.ApiClient;
import com.example.marvel_comic_app.util.PrefsManager;

import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {
    EditText edtEmailLogin, edtPasswordLogin;
    Button btnLogin;
    TextView txtGoRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmailLogin = findViewById(R.id.edtEmailLogin);
        edtPasswordLogin = findViewById(R.id.edtPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        txtGoRegister = findViewById(R.id.txtGoRegister);

        btnLogin.setOnClickListener(v -> loginUser());

        txtGoRegister.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegistroActivity.class))
        );
    }

    private void loginUser() {
        String email = edtEmailLogin.getText().toString();
        String pass = edtPasswordLogin.getText().toString();

        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Se deben llenar todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", email);
            jsonBody.put("password", pass);

            String url = ApiClient.buildUrl("/login");

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    jsonBody,
                    response -> {
                        try {
                            // Guardar datos del usuario en SharedPreferences
                            String userName = response.getString("name");
                            PrefsManager prefsManager = new PrefsManager(this);
                            prefsManager.saveUserData(userName, email);

                            Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } catch (Exception e) {
                            Toast.makeText(LoginActivity.this, "Error al procesar datos", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> {
                        Toast.makeText(LoginActivity.this, "Invalid login", Toast.LENGTH_SHORT).show();
                    }
            );

            ApiClient.getInstance(this).addToRequestQueue(request);

        } catch (Exception e) {
            Toast.makeText(this, "Error al crear la solicitud", Toast.LENGTH_SHORT).show();
        }
    }
}


