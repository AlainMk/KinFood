package com.alain.mk.kinfood.views;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alain.mk.kinfood.R;
import com.alain.mk.kinfood.models.Food;
import com.bumptech.glide.RequestManager;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.activity_main_item_menu_name) TextView textViewTitle;
    @BindView(R.id.activity_main_item_menu_price) TextView price;
    @BindView(R.id.activity_main_item_menu_image) ImageView imageView;
    @BindView(R.id.activity_main_item_favorite) ImageButton favorite;


    private WeakReference<FoodAdapter.Listener> callbackWeakRef;

    public MenuViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithFood(Food food, RequestManager glide, FoodAdapter.Listener callback){

        this.textViewTitle.setText(food.getFoodName());
        this.price.setText(food.getPrice());

        glide.load(food.getUrlImage()).into(imageView);
        this.favorite.setOnClickListener(this);
        this.callbackWeakRef = new WeakReference<>(callback);
    }

    @Override
    public void onClick(View view) {
        FoodAdapter.Listener callback = callbackWeakRef.get();
        if (callback != null) callback.onClickFavoriteButton(getAdapterPosition());
    }
}