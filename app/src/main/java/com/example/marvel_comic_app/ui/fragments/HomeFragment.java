package com.example.marvel_comic_app.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.marvel_comic_app.R;
import com.example.marvel_comic_app.model.Hero;
import com.example.marvel_comic_app.network.ApiClient;
import com.example.marvel_comic_app.ui.adapters.HeroAdapter;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private HeroAdapter adapter;
    private List<Hero> heroList;

    // Constructor vacío
    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerHeroes);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        heroList = new ArrayList<>();
        adapter = new HeroAdapter(heroList, getContext());
        recyclerView.setAdapter(adapter);

        loadHeroes();

        return view;
    }

    private void loadHeroes() {
        // API de Superhéroes para obtener datos y stats
        String url = "https://superheroapi.com/api/834ae4e93d1f0213444bb38f67504765/search/a";

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        JSONArray results = response.getJSONArray("results");
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject heroObj = results.getJSONObject(i);

                            String id = heroObj.getString("id");
                            String name = heroObj.getString("name");

                            // Cargamos la imagen desde Akabab API usando el mismo ID
                            loadHeroImage(id, name);
                        }
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Error al cargar héroes", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show()
        );

        ApiClient.getInstance(getContext()).addToRequestQueue(request);
    }

    private void loadHeroImage(String heroId, String heroName) {
        // API de Akabab para obtener la imagen del héroe
        String akababUrl = "https://akabab.github.io/superhero-api/api/id/" + heroId + ".json";

        JsonObjectRequest imageRequest = new JsonObjectRequest(
                Request.Method.GET,
                akababUrl,
                null,
                response -> {
                    try {
                        String imageUrl = response.getJSONObject("images").getString("md");

                        // Creamos el héroe con la imagen de Akabab
                        Hero hero = new Hero(heroId, heroName, imageUrl);
                        heroList.add(hero);
                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        // Si falla Akabab, podemos usar una imagen por defecto o simplemente no agregar
                        Toast.makeText(getContext(), "Error al cargar imagen de " + heroName, Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    // Si falla la carga de imagen, podemos usar una imagen por defecto
                    Toast.makeText(getContext(), "Error de imagen para " + heroName, Toast.LENGTH_SHORT).show();
                }
        );

        ApiClient.getInstance(getContext()).addToRequestQueue(imageRequest);
    }
}