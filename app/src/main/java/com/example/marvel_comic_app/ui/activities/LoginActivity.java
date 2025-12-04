package com.example.marvel_comic_app.ui.activities;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import com.example.marvel_comic_app.R;
import com.example.marvel_comic_app.network.ApiClient;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.marvel_comic_app.util.PrefsManager;

import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {
    EditText edtEmailLogin, edtPasswordLogin;
    Button btnLogin;
    TextView txtGoRegistro;

    private static final String TAG = "LOGIN_APP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmailLogin = findViewById(R.id.edtEmailLogin);
        edtPasswordLogin = findViewById(R.id.edtPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        txtGoRegistro = findViewById(R.id.txtGoRegistro);

        btnLogin.setOnClickListener(v -> loginUser());

        txtGoRegistro.setOnClickListener(v ->
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

            String url = ApiClient.buildUrl("/login"); //end point
            Log.d(TAG, "Intentando login en URL: " + url);
            Log.d(TAG, "Enviando JSON: " + jsonBody.toString());

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    jsonBody,
                    response -> {
                        try {
                            Log.d(TAG, "Login exitoso. Respuesta: " + response.toString());

                            JSONObject userObject = response.getJSONObject("user");

                            String userName = userObject.getString("name");
                            String userEmail = userObject.getString("email");
                            String userId = response.getString("userId");


                            PrefsManager prefsManager = new PrefsManager(this);
                            prefsManager.saveUserData(userName, userEmail, userId);

                            Toast.makeText(LoginActivity.this, "¡Bienvenido, " + userName + "!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();

                        } catch (Exception e) {
                            Log.e(TAG, "Error al procesar la respuesta JSON del login", e);
                            Toast.makeText(LoginActivity.this, "Error al procesar los datos del servidor", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> {
                        if (error.networkResponse != null && error.networkResponse.statusCode == 401) {
                            // Error 401: Credenciales inválidas
                            Log.w(TAG, "Error 401: Credenciales inválidas.");
                            Toast.makeText(LoginActivity.this, "Correo o contraseña incorrectos", Toast.LENGTH_LONG).show();
                        } else {
                            // Para CUALQUIER OTRO tipo de error (sin conexión, error del servidor, etc.)
                            Log.e(TAG, "Error de Volley en login", error);
                            Toast.makeText(LoginActivity.this, "Error de conexión o del servidor", Toast.LENGTH_LONG).show();
                        }
                    }
            );

            ApiClient.getInstance(this).addToRequestQueue(request);

        } catch (Exception e) {
            Log.e(TAG, "Excepción al crear la petición de login", e);
            Toast.makeText(this, "Error al crear la solicitud", Toast.LENGTH_SHORT).show();
        }
    }
}


