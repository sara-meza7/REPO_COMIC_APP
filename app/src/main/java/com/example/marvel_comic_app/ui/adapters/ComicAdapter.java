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
import com.squareup.picasso.Picasso;
import java.util.List;

public class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.ComicViewHolder> {

    private List<Comic> comicList;
    private List<Comic> selectedComics;
    private Context context;

    public ComicAdapter(List<Comic> comicList, List<Comic> selectedComics, Context context) {
        this.comicList = comicList;
        this.selectedComics = selectedComics;
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
        holder.txtPrice.setText("$" + String.format("%.2f", comic.getPrice()));
        holder.checkBox.setChecked(comic.isSelected());

        Picasso.get()
                .load(comic.getImageUrl())
                .placeholder(R.drawable.marvel_logo)
                .into(holder.imgComic);

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            comic.setSelected(isChecked);
            if (isChecked) {
                if (!selectedComics.contains(comic)) {
                    selectedComics.add(comic);
                }
            } else {
                selectedComics.remove(comic);
            }
        });

        holder.itemView.setOnClickListener(v -> {
            holder.checkBox.setChecked(!holder.checkBox.isChecked());
        });
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