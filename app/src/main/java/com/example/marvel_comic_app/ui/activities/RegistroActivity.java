package com.example.marvel_comic_app.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

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

        // para seleccionar la fecha
        edtDate.setOnClickListener(v -> showDatePicker());

        // click del registro
        btnRegister.setOnClickListener(v -> registerUser());

        // Ir a login
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

        // Validaciones
        if (!ValidationUtils.validateRegister(name, email, pass, date, this))
            return;

        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("name", name);
            jsonBody.put("email", email);
            jsonBody.put("password", pass);
            jsonBody.put("birthDate", date);

            JsonObjectRequest request = getJsonObjectRequest(jsonBody);

            ApiClient.getInstance(this).addToRequestQueue(request);

        } catch (Exception e) {
            Toast.makeText(this, "Error al crear la solicitud", Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    private JsonObjectRequest getJsonObjectRequest(JSONObject jsonBody) {
        String url = ApiClient.buildUrl("/register");

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonBody,
                response -> {
                    Toast.makeText(RegistroActivity.this, "Usuario creado", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegistroActivity.this, LoginActivity.class));
                    finish();
                },
                error -> {
                    Toast.makeText(RegistroActivity.this, "Error al registrarse", Toast.LENGTH_SHORT).show();
                }
        );
        return request;
    }
}
