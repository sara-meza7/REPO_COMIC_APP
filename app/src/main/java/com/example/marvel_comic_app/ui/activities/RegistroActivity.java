package com.example.marvel_comic_app.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.marvel_comic_app.R;
import com.example.marvel_comic_app.network.ApiClient;
import com.example.marvel_comic_app.util.ValidationUtils;
import org.json.JSONObject;
import java.util.Calendar;

public class RegistroActivity extends AppCompatActivity {
    EditText edtName, edtEmail, edtPassword, edtDate;
    Button btnRegister;
    TextView txtLogin;

    private static final String TAG = "REGISTRO_APP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtDate = findViewById(R.id.edtDate);
        btnRegister = findViewById(R.id.btnRegister);
        txtLogin = findViewById(R.id.txtLogin);

        edtDate.setOnClickListener(v -> showDatePicker());
        btnRegister.setOnClickListener(v -> registerUser());
        txtLogin.setOnClickListener(v ->
                startActivity(new Intent(RegistroActivity.this, LoginActivity.class))
        );
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year1, month1, day1) -> edtDate.setText(day1 + "/" + (month1 + 1) + "/" + year1),
                year, month, day);

        dialog.show();
    }

    private void registerUser() {
        String name = edtName.getText().toString();
        String email = edtEmail.getText().toString();
        String pass = edtPassword.getText().toString();
        String date = edtDate.getText().toString();

        if (!ValidationUtils.validateRegister(name, email, pass, date, this))
            return;

        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("name", name);
            jsonBody.put("email", email);
            jsonBody.put("password", pass);
            jsonBody.put("birthDate", date);

            String url = ApiClient.buildUrl("/register");
            Log.d(TAG, "Intentando registrar en URL: " + url);
            Log.d(TAG, "Enviando JSON: " + jsonBody.toString());

            JsonObjectRequest request = getJsonObjectRequest(jsonBody);
            ApiClient.getInstance(this).addToRequestQueue(request);

        } catch (Exception e) {
            Log.e(TAG, "Excepción al crear la petición JSON: ", e);
            Toast.makeText(this, "Error al crear la solicitud", Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    private JsonObjectRequest getJsonObjectRequest(JSONObject jsonBody) {
        String url = ApiClient.buildUrl("/register");

        return new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonBody,
                response -> {
                    Log.d(TAG, "Registro exitoso. Respuesta: " + response.toString());

                    Toast.makeText(RegistroActivity.this, "Usuario creado", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegistroActivity.this, LoginActivity.class));
                    finish();
                },
                error -> {
                    if (error.networkResponse != null) {
                        // El servidor respondió, pero con un código de error (4xx, 5xx)
                        Log.e(TAG, "Error de servidor. Código: " + error.networkResponse.statusCode);
                        Log.e(TAG, "Datos del error: " + new String(error.networkResponse.data));
                    } else {
                        // No hubo respuesta del servidor (Timeout, sin conexión, IP incorrecta, etc.)
                        Log.e(TAG, "Error de conexión (Sin respuesta del servidor)", error);
                    }

                    Toast.makeText(RegistroActivity.this, "Error al registrarse", Toast.LENGTH_SHORT).show();
                }
        );
    }
}

