package com.example.marvel_comic_app.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.marvel_comic_app.R;
import com.example.marvel_comic_app.model.Hero;
import com.example.marvel_comic_app.ui.activities.DetalleActivity;
import com.squareup.picasso.Picasso;
import java.util.List;

public class HeroAdapter extends RecyclerView.Adapter<HeroAdapter.HeroViewHolder> {

    private List<Hero> heroList;
    private Context context;

    public HeroAdapter(List<Hero> heroList, Context context) {
        this.heroList = heroList;
        this.context = context;
    }

    @NonNull
    @Override
    public HeroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hero, parent, false);
        return new HeroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HeroViewHolder holder, int position) {
        Hero hero = heroList.get(position);
        holder.txtName.setText(hero.getName());

        Picasso.get()
                .load(hero.getImageUrl())
                .placeholder(R.drawable.marvel_logo)
                .into(holder.imgHero);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetalleActivity.class);
            intent.putExtra("heroId", hero.getId());
            intent.putExtra("heroName", hero.getName());
            intent.putExtra("heroImage", hero.getImageUrl());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return heroList.size();
    }

    static class HeroViewHolder extends RecyclerView.ViewHolder {
        ImageView imgHero;
        TextView txtName;

        public HeroViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHero = itemView.findViewById(R.id.imgHero);
            txtName = itemView.findViewById(R.id.txtHeroName);
        }
    }
}