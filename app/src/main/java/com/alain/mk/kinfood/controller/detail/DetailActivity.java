package com.alain.mk.kinfood.controller.detail;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alain.mk.kinfood.R;
import com.alain.mk.kinfood.api.FavoriteHelper;
import com.alain.mk.kinfood.api.FoodHelper;
import com.alain.mk.kinfood.base.BaseActivity;
import com.alain.mk.kinfood.models.Food;
import com.alain.mk.kinfood.views.FoodAdapter;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import butterknife.BindView;
import butterknife.OnClick;

public class DetailActivity extends BaseActivity {

    @BindView(R.id.activity_detail_title) TextView textViewTitle;
    @BindView(R.id.activity_detail_price) TextView price;
    @BindView(R.id.activity_detail_image) ImageView imageView;
    @BindView(R.id.activity_detail_root_view) CoordinatorLayout rootView;
    @BindView(R.id.activity_detail_favorite) ImageButton favorite;

    public static final String BUNDLE_KEY_PROJECT_ID = "foodName";
    public static final String BUNDLE_KEY_PROJECT_IMAGE_URL = "urlImage";
    public static final String BUNDLE_KEY_PROJECT_PRICE = "price";

    private static final int REGISTER_FAVORITE = 10;

    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.configureToolbar();

        firebaseFirestore = FirebaseFirestore.getInstance();

        this.updateWithFood();
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_detail;
    }

    @OnClick(R.id.activity_detail_favorite)
    public void onClickButtonFavorite(){

        FirebaseAuth auth;

        auth = FirebaseAuth.getInstance();
        String currentUserId = auth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();
        String title = getIntent().getStringExtra("foodName");

        firebaseFirestore.collection("users/" + currentUserId + "/favorite").document(title).get().addOnCompleteListener(task -> {
            if (!task.getResult().exists()){

                FavoriteHelper.createFavorite(currentUserId, title).addOnFailureListener(onFailureListener()).addOnSuccessListener(updateUIAfterRESTRequestsCompleted(REGISTER_FAVORITE));

            }else {
                FavoriteHelper.deleteFavorite(currentUserId, title).addOnFailureListener(onFailureListener());
            }
        });
    }

    public void updateWithFood(){

        String title = getIntent().getStringExtra("foodName");
        String prices = getIntent().getStringExtra("price");
        String image = getIntent().getStringExtra("urlImage");

        textViewTitle.setText(title);
        price.setText(prices);
        Glide.with(this).load(image).into(imageView);

        FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();
        String currentUserId = auth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("users/" + currentUserId + "/favorite").document(title).addSnapshotListener((documentSnapshot, e) -> {
            if (documentSnapshot.exists()){
                favorite.setImageResource(R.mipmap.ic_favorite_accent);
            }else {
                favorite.setImageResource(R.mipmap.ic_favorite_gray);
            }
        });
    }

    // --------------------
    // UI
    // --------------------
    private OnSuccessListener<Void> updateUIAfterRESTRequestsCompleted(final int origin){
        return aVoid -> {
            switch (origin) {
                case REGISTER_FAVORITE:
                    Snackbar snackbar = Snackbar.make(rootView, R.string.register_favorite, Snackbar.LENGTH_SHORT);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                    break;
                default:
                    break;
            }
        };
    }

}
