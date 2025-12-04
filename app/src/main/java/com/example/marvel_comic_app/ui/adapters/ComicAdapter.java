package com.example.marvel_comic_app.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.marvel_comic_app.R;
import com.example.marvel_comic_app.model.Comic;
import java.util.List;
import java.util.Locale;

public class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.ComicViewHolder> {

    private final List<Comic> comicList;
    private final Context context;

    // El constructor ya no necesita la lista de seleccionados
    public ComicAdapter(List<Comic> comicList, Context context) {
        this.comicList = comicList;
        this.context = context;
    }

    @NonNull
    @Override
    public ComicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comic, parent, false);
        return new ComicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComicViewHolder holder, int position) {
        Comic comic = comicList.get(position);

        holder.txtTitle.setText(comic.getTitle());
        holder.txtPrice.setText(String.format(Locale.US, "$%.2f", comic.getPrice()));

        // Limpia el listener anterior para evitar bugs en vistas recicladas
        holder.checkBox.setOnCheckedChangeListener(null);

        // Establece el estado actual del checkbox
        holder.checkBox.setChecked(comic.isSelected());

        // Establece la imagen del logo
        holder.imgComic.setImageResource(R.drawable.marvel_logo);

        // Cuando el checkbox cambia, simplemente actualiza el estado del objeto Comic
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            comic.setSelected(isChecked);
        });

        // Hacemos que toda la tarjeta sea "clicable" para invertir el estado del checkbox
        holder.itemView.setOnClickListener(v -> holder.checkBox.setChecked(!holder.checkBox.isChecked()));
    }

    @Override
    public int getItemCount() {
        return comicList.size();
    }

    static class ComicViewHolder extends RecyclerView.ViewHolder {
        ImageView imgComic;
        TextView txtTitle, txtPrice;
        CheckBox checkBox;

        public ComicViewHolder(@NonNull View itemView) {
            super(itemView);
            imgComic = itemView.findViewById(R.id.imgComic);
            txtTitle = itemView.findViewById(R.id.txtComicTitle);
            txtPrice = itemView.findViewById(R.id.txtComicPrice);
            checkBox = itemView.findViewById(R.id.checkBoxComic);
        }
    }
}
