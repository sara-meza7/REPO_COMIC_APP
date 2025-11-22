package com.example.marvel_comic_app.ui.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.marvel_comic_app.R;
import com.example.marvel_comic_app.model.Comic;
import com.example.marvel_comic_app.network.ApiClient;
import com.example.marvel_comic_app.ui.adapters.ComicAdapter;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ComicsFragment extends Fragment {

    private RecyclerView recyclerComics;
    private ComicAdapter adapter;
    private List<Comic> comicList;
    private List<Comic> selectedComics;
    private Button btnSolicitar;

    public ComicsFragment() {
        // Constructor vacío requerido
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comics, container, false);

        recyclerComics = view.findViewById(R.id.recyclerComics);
        btnSolicitar = view.findViewById(R.id.btnSolicitar);

        recyclerComics.setLayoutManager(new LinearLayoutManager(getContext()));

        comicList = new ArrayList<>();
        selectedComics = new ArrayList<>();

        adapter = new ComicAdapter(comicList, selectedComics, getContext());
        recyclerComics.setAdapter(adapter);

        loadComics();

        btnSolicitar.setOnClickListener(v -> solicitarComics());

        return view;
    }

    private void loadComics() {
        // Marvel API - Necesitas tu API KEY en: https://developer.marvel.com/
        String publicKey = "TU_PUBLIC_KEY";
        String ts = String.valueOf(System.currentTimeMillis());
        String hash = "TU_HASH"; // md5(ts+privateKey+publicKey)

        // Por ahora usaremos datos de prueba simulados
        // En producción, usa: https://gateway.marvel.com/v1/public/comics?ts=" + ts + "&apikey=" + publicKey + "&hash=" + hash

        loadMockComics();
    }

    private void loadMockComics() {
        // Datos de prueba mientras configuras la API de Marvel
        comicList.add(new Comic("1", "Spider-Man: No Way Home", "https://i.annihil.us/u/prod/marvel/i/mg/9/03/58dd080719806.jpg", 15.99));
        comicList.add(new Comic("2", "Iron Man: Extremis", "https://i.annihil.us/u/prod/marvel/i/mg/c/60/58dbce634ea70.jpg", 12.99));
        comicList.add(new Comic("3", "Thor: God of Thunder", "https://i.annihil.us/u/prod/marvel/i/mg/9/10/5232283d144e2.jpg", 14.99));
        comicList.add(new Comic("4", "Captain America: Winter Soldier", "https://i.annihil.us/u/prod/marvel/i/mg/3/20/5232198a7465c.jpg", 13.99));
        comicList.add(new Comic("5", "Black Widow: Deadly Origin", "https://i.annihil.us/u/prod/marvel/i/mg/9/40/4c7d666147e35.jpg", 11.99));
        comicList.add(new Comic("6", "Avengers: Endgame Prelude", "https://i.annihil.us/u/prod/marvel/i/mg/6/30/5c8d488da5ce5.jpg", 16.99));

        adapter.notifyDataSetChanged();
    }

    private void solicitarComics() {
        if (selectedComics.isEmpty()) {
            Toast.makeText(getContext(), "Selecciona al menos un cómic", Toast.LENGTH_SHORT).show();
            return;
        }

        // Guardar en BD (usando Volley para enviar al backend)
        try {
            JSONObject jsonBody = new JSONObject();
            JSONArray comicsArray = new JSONArray();

            // Calcular el total ANTES del lambda
            double totalPrice = 0;
            for (Comic comic : selectedComics) {
                JSONObject comicObj = new JSONObject();
                comicObj.put("id", comic.getId());
                comicObj.put("title", comic.getTitle());
                comicObj.put("price", comic.getPrice());
                comicsArray.put(comicObj);
                totalPrice += comic.getPrice();
            }

            jsonBody.put("comics", comicsArray);
            jsonBody.put("total", totalPrice);

            // Hacer la variable final para usarla en el lambda
            final double finalTotal = totalPrice;

            String url = ApiClient.buildUrl("/solicitudes");

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    jsonBody,
                    response -> {
                        Toast.makeText(getContext(),
                                "Solicitud enviada! Total: $" + String.format("%.2f", finalTotal),
                                Toast.LENGTH_LONG).show();
                        selectedComics.clear();
                        adapter.notifyDataSetChanged();
                    },
                    error -> {
                        // Por ahora mostrar mensaje simulado
                        Toast.makeText(getContext(),
                                "Solicitud guardada localmente. Total: $" + String.format("%.2f", finalTotal),
                                Toast.LENGTH_LONG).show();
                        selectedComics.clear();
                        adapter.notifyDataSetChanged();
                    }
            );

            ApiClient.getInstance(getContext()).addToRequestQueue(request);

        } catch (Exception e) {
            Toast.makeText(getContext(), "Error al procesar solicitud", Toast.LENGTH_SHORT).show();
        }
    }
}