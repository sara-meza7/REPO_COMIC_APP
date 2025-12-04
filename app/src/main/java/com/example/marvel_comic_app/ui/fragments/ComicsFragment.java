package com.example.marvel_comic_app.ui.fragments;

import android.os.Bundle;
import android.util.Log;
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
import com.example.marvel_comic_app.util.PrefsManager; // Asegúrate de tener este import
import com.example.marvel_comic_app.ui.adapters.ComicAdapter;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ComicsFragment extends Fragment {

    private RecyclerView recyclerComics;
    private ComicAdapter adapter;
    private List<Comic> comicList;
    private Button btnSolicitar;

    private static final String TAG = "ComicsFragment";

    public ComicsFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comics, container, false);

        recyclerComics = view.findViewById(R.id.recyclerComics);
        btnSolicitar = view.findViewById(R.id.btnSolicitar);

        recyclerComics.setLayoutManager(new LinearLayoutManager(getContext()));

        comicList = new ArrayList<>();
        adapter = new ComicAdapter(comicList, getContext());
        recyclerComics.setAdapter(adapter);

        loadMockComics();

        btnSolicitar.setOnClickListener(v -> solicitarComics());

        return view;
    }

    private void loadMockComics() {
        comicList.clear(); // Limpiamos la lista por si se llama varias veces
        comicList.add(new Comic("1", "Spider-Man: No Way Home","null",30.99));
        comicList.add(new Comic("2", "Iron Man: Extremis", "null", 22.99));
        comicList.add(new Comic("3", "Thor: God of Thunder","null",45.99));
        comicList.add(new Comic("4", "Captain America: Winter Soldier","null",35.99));
        comicList.add(new Comic("5", "Black Widow: Deadly Origin","null",27.99));
        comicList.add(new Comic("6", "Avengers: Endgame Prelude","null",20.99));
        comicList.add(new Comic("7", "Batman v Superman: Dawn of Justice", "null", 29.99));
        comicList.add(new Comic("8", "The Batman Who Laughs", "null", 31.99));
        comicList.add(new Comic("9", "Green Lantern: The Power of Ion (Todos los anillos)", "null", 34.99));
        comicList.add(new Comic("10", "Spider-Man 2099: Volume 1", "null", 26.99));
        comicList.add(new Comic("11", "Marvel Zombies", "null", 28.99));
        comicList.add(new Comic("12", "X-Men: Dark Phoenix Saga", "null", 32.99));
        comicList.add(new Comic("13", "Nightwing: Better Than Batman", "null", 24.99));
        comicList.add(new Comic("14", "The Punisher: MAX — Born", "null", 29.99));
        comicList.add(new Comic("15", "Daredevil: The Man Without Fear", "null", 27.99));
        comicList.add(new Comic("16", "Justice League: Darkseid War", "null", 33.99));
        adapter.notifyDataSetChanged();
    }

    private void solicitarComics() {
        List<Comic> selectedComics = new ArrayList<>();
        for (Comic comic : comicList) {
            if (comic.isSelected()) {
                selectedComics.add(comic);
            }
        }

        if (selectedComics.isEmpty()) {
            Toast.makeText(getContext(), "Debes seleccionar al menos un cómic", Toast.LENGTH_SHORT).show();
            return;
        }

        double totalPrice = 0;
        JSONArray comicsJsonArray = new JSONArray();
        try {
            for (Comic comic : selectedComics) {
                JSONObject comicObj = new JSONObject();
                comicObj.put("id", comic.getId());
                comicObj.put("title", comic.getTitle());
                comicObj.put("price", comic.getPrice());
                comicsJsonArray.put(comicObj);
                totalPrice += comic.getPrice();
            }

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("comics", comicsJsonArray);
            jsonBody.put("totalPrice", totalPrice);

            if (getContext() != null) {
                PrefsManager prefsManager = new PrefsManager(getContext());
                String userId = prefsManager.getUserId();
                if (userId != null) {
                    jsonBody.put("userId", userId);
                }
            }
            final double finalTotalPrice = totalPrice;


            String url = ApiClient.buildUrl("/solicitudes");

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    jsonBody,
                    response -> {
                        Toast.makeText(getContext(),
                                "Solicitud enviada! Total: " + String.format(Locale.US, "$%.2f", finalTotalPrice),
                                Toast.LENGTH_LONG).show();

                        clearSelections();
                    },
                    error -> {
                        Toast.makeText(getContext(), "Error al enviar la solicitud", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error en la solicitud al backend: ", error);
                    }
            );

            if (getContext() != null) {
                ApiClient.getInstance(getContext()).addToRequestQueue(request);
            }

        } catch (Exception e) {
            Toast.makeText(getContext(), "Error al procesar la solicitud", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error creando el JSON para la solicitud: ", e);
        }
    }

    private void clearSelections() {
        for (Comic comic : comicList) {
            comic.setSelected(false);
        }
        adapter.notifyDataSetChanged();
    }
}
