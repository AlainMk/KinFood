package com.alain.mk.kinfood.views;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.alain.mk.kinfood.R;
import com.alain.mk.kinfood.models.Food;
import com.bumptech.glide.RequestManager;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class FoodAdapter extends FirestoreRecyclerAdapter<Food, MenuViewHolder> {

    public interface Listener {
        void onClickFavoriteButton(int position);
    }

    // FOR COMMUNICATION
    private final Listener callback;
    private FirebaseFirestore firebaseFirestore;

    private final RequestManager glide;
    private Food food;

    public FoodAdapter(@NonNull FirestoreRecyclerOptions<Food> options, Listener callback, RequestManager glide) {
        super(options);
        this.callback = callback;
        this.glide = glide;
    }

    @Override
    protected void onBindViewHolder(@NonNull MenuViewHolder holder, int position, @NonNull Food model) {

        FirebaseAuth auth;

        auth = FirebaseAuth.getInstance();
        String currentUserId = auth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();

        holder.updateWithFood(this.getFood(position), this.glide, this.callback);

        firebaseFirestore.collection("users/" + currentUserId + "/favorite").document(this.getFood(position).getFoodName()).addSnapshotListener((documentSnapshot, e) -> {
            if (documentSnapshot.exists()){
                holder.favorite.setImageResource(R.mipmap.ic_favorite_accent);
            }else {
                holder.favorite.setImageResource(R.mipmap.ic_favorite_gray);
            }
        });

    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MenuViewHolder(LayoutInflater.from(viewGroup.getContext())
        .inflate(R.layout.activity_main_item, viewGroup, false));
    }

    public Food getFood(int position){

         return this.food= getItem(position);
    }
}
