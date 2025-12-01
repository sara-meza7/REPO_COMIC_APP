package com.example.marvel_comic_app.ui.activities;

import static com.example.marvel_comic_app.network.ApiConstants.API_TOKEN;

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
    TextView txtHeroPublisher, txtHeroNombre, txtHeroNombreReal;
    TextView txtHeroIntelligence, txtHeroStrength, txtHeroSpeed, txtHeroPower, txtHeroCombat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        // Inicializar vistas según el XML
        imgHero = findViewById(R.id.img_hero);
        txtHeroPublisher = findViewById(R.id.txt_hero_publisher);
        txtHeroNombre = findViewById(R.id.txt_hero_nombre);
        txtHeroNombreReal = findViewById(R.id.txt_hero_nombre_real);
        txtHeroIntelligence = findViewById(R.id.txt_hero_intelligence);
        txtHeroStrength = findViewById(R.id.txt_hero_strength);
        txtHeroSpeed = findViewById(R.id.txt_hero_speed);
        txtHeroPower = findViewById(R.id.txt_hero_power);
        txtHeroCombat = findViewById(R.id.txt_hero_combat);

        // Obtener datos del Intent
        String heroId = getIntent().getStringExtra("heroId");
        String heroName = getIntent().getStringExtra("heroName");
        String heroImage = getIntent().getStringExtra("heroImage");

        // Establecer nombre del héroe
        txtHeroNombre.setText(heroName);

        // Cargar imagen
        Picasso.get().load(heroImage).into(imgHero);

        // Cargar detalles completos
        loadHeroDetails(heroId);
    }

    private void loadHeroDetails(String heroId) {
        String url = "https://superheroapi.com/api/"+API_TOKEN+"/" + heroId;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        // Obtener biografía
                        JSONObject biography = response.getJSONObject("biography");

                        // Obtener estadísticas de poder
                        JSONObject powerstats = response.getJSONObject("powerstats");

                        // Establecer publisher
                        txtHeroPublisher.setText(biography.getString("publisher").toUpperCase());

                        // Establecer nombre real
                        txtHeroNombreReal.setText(biography.getString("full-name"));

                        // Establecer estadísticas
                        txtHeroIntelligence.setText(powerstats.getString("intelligence"));
                        txtHeroStrength.setText(powerstats.getString("strength"));
                        txtHeroSpeed.setText(powerstats.getString("speed"));
                        txtHeroPower.setText(powerstats.getString("power"));
                        txtHeroCombat.setText(powerstats.getString("combat"));

                    } catch (Exception e) {
                        Toast.makeText(this, "Error al cargar detalles", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Error de conexión", Toast.LENGTH_SHORT).show()
        );

        ApiClient.getInstance(this).addToRequestQueue(request);
    }
}