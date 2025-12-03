package com.example.marvel_comic_app.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
    private EditText edtBuscarHeroe;

    // Constructor vacío
    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerHeroes);
        edtBuscarHeroe = view.findViewById(R.id.edtBuscarHeroe);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        heroList = new ArrayList<>();
        adapter = new HeroAdapter(heroList, getContext());
        recyclerView.setAdapter(adapter);

        loadHeroes("a");

        setupSearchView();

        return view;
    }

    private void setupSearchView() {
        final Handler handler = new Handler();
        final int delay = 500;

        edtBuscarHeroe.addTextChangedListener(new TextWatcher() {
            private Runnable searchRunnable;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Cancelar la búsqueda anterior si el usuario sigue escribiendo
                if (searchRunnable != null) {
                    handler.removeCallbacks(searchRunnable);
                }

                searchRunnable = () -> {
                    String searchText = s.toString().trim();

                    if (searchText.isEmpty()) {
                        // Si está vacío, volver a mostrar héroes con "a"
                        loadHeroes("a");
                    } else {
                        // Buscar con el texto ingresado
                        loadHeroes(searchText);
                    }
                };

                // Esperar 500ms después de que el usuario deje de escribir
                handler.postDelayed(searchRunnable, delay);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No necesario
            }
        });
    }

    private void loadHeroes(String searchTerm) {
        // Limpiar la lista antes de cargar nuevos resultados
        heroList.clear();
        adapter.notifyDataSetChanged();

        // API de Superhéroes con término de búsqueda dinámico
        String url = "https://superheroapi.com/api/834ae4e93d1f0213444bb38f67504765/search/" + searchTerm;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        String responseStatus = response.getString("response");

                        // Verificar si la API encontró resultados
                        if (responseStatus.equals("success")) {
                            JSONArray results = response.getJSONArray("results");
                            for (int i = 0; i < results.length(); i++) {
                                JSONObject heroObj = results.getJSONObject(i);

                                String id = heroObj.getString("id");
                                String name = heroObj.getString("name");

                                // Cargamos la imagen desde Akabab API usando el mismo ID
                                loadHeroImage(id, name);
                            }
                        } else {
                            // No se encontraron resultados
                            Toast.makeText(getContext(), "No se encontraron héroes", Toast.LENGTH_SHORT).show();
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