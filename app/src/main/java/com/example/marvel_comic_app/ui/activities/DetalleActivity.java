package com.example.marvel_comic_app.ui.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.marvel_comic_app.R;
import com.example.marvel_comic_app.network.ApiClient;
import com.squareup.picasso.Picasso;
import org.json.JSONObject;

public class DetalleActivity extends AppCompatActivity {

    ImageView imgHero;
    TextView txtName, txtFullName, txtIntelligence, txtStrength, txtSpeed, txtPower;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        imgHero = findViewById(R.id.imgHeroDetail);
        txtName = findViewById(R.id.txtHeroDetailName);
        txtFullName = findViewById(R.id.txtFullName);
        txtIntelligence = findViewById(R.id.txtIntelligence);
        txtStrength = findViewById(R.id.txtStrength);
        txtSpeed = findViewById(R.id.txtSpeed);
        txtPower = findViewById(R.id.txtPower);

        String heroId = getIntent().getStringExtra("heroId");
        String heroName = getIntent().getStringExtra("heroName");
        String heroImage = getIntent().getStringExtra("heroImage");

        txtName.setText(heroName);
        Picasso.get().load(heroImage).into(imgHero);

        loadHeroDetails(heroId);
    }

    private void loadHeroDetails(String heroId) {
        String url = "https://superheroapi.com/api/10158190334933491/" + heroId;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        JSONObject biography = response.getJSONObject("biography");
                        JSONObject powerstats = response.getJSONObject("powerstats");

                        txtFullName.setText("Full Name: " + biography.getString("full-name"));
                        txtIntelligence.setText("Intelligence: " + powerstats.getString("intelligence"));
                        txtStrength.setText("Strength: " + powerstats.getString("strength"));
                        txtSpeed.setText("Speed: " + powerstats.getString("speed"));
                        txtPower.setText("Power: " + powerstats.getString("power"));

                    } catch (Exception e) {
                        Toast.makeText(this, "Error al cargar detalles", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Error de conexión", Toast.LENGTH_SHORT).show()
        );

        ApiClient.getInstance(this).addToRequestQueue(request);
    }
}