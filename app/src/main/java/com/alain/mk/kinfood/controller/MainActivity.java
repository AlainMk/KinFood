package com.alain.mk.kinfood.controller;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.alain.mk.kinfood.R;
import com.alain.mk.kinfood.api.FavoriteHelper;
import com.alain.mk.kinfood.api.FoodHelper;
import com.alain.mk.kinfood.api.UserHelper;
import com.alain.mk.kinfood.configuration.Configuration;
import com.alain.mk.kinfood.controller.auth.ProfileActivity;
import com.alain.mk.kinfood.controller.booking.BookingActivity;
import com.alain.mk.kinfood.controller.booking.LookBookingActivity;
import com.alain.mk.kinfood.controller.detail.DetailActivity;
import com.alain.mk.kinfood.models.Food;
import com.alain.mk.kinfood.utils.ItemClickSupport;
import com.alain.mk.kinfood.views.FoodAdapter;
import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends Configuration implements NavigationView.OnNavigationItemSelectedListener, FoodAdapter.Listener {

    @BindView(R.id.activity_main_drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.activity_main_floatingButton) FloatingActionButton floatingActionButton;
    @BindView(R.id.activity_main_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.main_activity_coordinator_layout) CoordinatorLayout coordinatorLayout;

    // 1 - Identifier for Sign-In Activity
    private static final int RC_SIGN_IN = 123;
    private static final int REGISTER_FAVORITE = 10;

    private FoodAdapter foodAdapter;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseFirestore = FirebaseFirestore.getInstance();
        this.configureMainToolbar();
        this.configureDrawerLayout();
        this.configureNavigationView();
        this.configureRecyclerView();
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Handle SignIn Activity response on activity result
        this.handleResponseAfterSignIn(requestCode, resultCode, data);
    }

    @OnClick(R.id.activity_main_floatingButton)
    public void onClickFloatingButton(){
        // Check if user is connected before launching BookingActivity
        if (this.isCurrentUserLogged()){
            this.startBookingActivity();
        } else {
            this.showSnackBar(this.coordinatorLayout, getString(R.string.error_not_connected));
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle Navigation Item Click
        int id = item.getItemId();

        switch (id){
            case R.id.activity_main_drawer_accueil :
                break;
            case R.id.activity_main_drawer_profile:
                // Start appropriate activity
                if (this.isCurrentUserLogged()){
                    this.startProfileActivity();
                } else {
                    this.startSignInActivity();
                }
                break;
            case R.id.activity_main_drawer_reservation:
                // Check if user is connected before launching BookingActivity
                if (this.isCurrentUserLogged()){
                    this.startLookBookingActivity();
                } else {
                    this.showSnackBar(this.coordinatorLayout, getString(R.string.error_not_connected));
                }
                break;
            default:
                break;
        }

        this.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    // --------------------
    // REST REQUEST
    // --------------------

    // Http request that create user in firestore
    private void createUserInFirestore(){

        if (this.getCurrentUser() != null){

            String urlPicture = (this.getCurrentUser().getPhotoUrl() != null) ? this.getCurrentUser().getPhotoUrl().toString() : null;
            String username = this.getCurrentUser().getDisplayName();
            String uid = this.getCurrentUser().getUid();

            UserHelper.createUser(uid, username, urlPicture).addOnFailureListener(this.onFailureListener());
        }
    }

    // Show Snack Bar with a message
    private void showSnackBar(CoordinatorLayout coordinatorLayout, String message){
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }
    // --------------------
    // UTILS
    // --------------------

    // Method that handles response after SignIn Activity close
    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data){

        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) { // SUCCESS
                // 2 - CREATE USER IN FIRESTORE
                this.createUserInFirestore();
                showSnackBar(this.coordinatorLayout, getString(R.string.connection_succeed));
            } else { // ERRORS
                if (response == null) {
                    showSnackBar(this.coordinatorLayout, getString(R.string.error_authentication_canceled));
                } else if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackBar(this.coordinatorLayout, getString(R.string.error_no_internet));
                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackBar(this.coordinatorLayout, getString(R.string.error_unknown_error));
                }
            }
        }
    }

    // Launch Sign-In Activity
    private void startSignInActivity(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build(),
                                        new AuthUI.IdpConfig.GoogleBuilder().build()))
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.mipmap.ic_launcher)
                        .build(),
                RC_SIGN_IN);
    }

    private void startBookingActivity(){
        Intent intent = new Intent(this, BookingActivity.class);
        startActivity(intent);
    }

    private void startProfileActivity(){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    private void startLookBookingActivity(){
        Intent intent = new Intent(this, LookBookingActivity.class);
        startActivity(intent);
    }

    private void configureRecyclerView(){

        this.foodAdapter = new FoodAdapter(generateOptionsForAdapter(FoodHelper.getFoodCollection()), this, Glide.with(this));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(this.foodAdapter);
        ItemClickSupport.addTo(recyclerView, R.layout.activity_main_item)
                .setOnItemClickListener((rv, position, v) -> this.navigateToDetail(this.foodAdapter.getFood(position)));
    }

    // --------------------
    // UI
    // --------------------
    // Configure RecyclerView with a Query
    private FirestoreRecyclerOptions<Food> generateOptionsForAdapter(Query query){
        return new FirestoreRecyclerOptions.Builder<Food>()
                .setQuery(query, Food.class)
                .setLifecycleOwner(this)
                .build();
    }

    private void navigateToDetail(Food food){

        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(DetailActivity.BUNDLE_KEY_PROJECT_ID, food.getFoodName());
        intent.putExtra(DetailActivity.BUNDLE_KEY_PROJECT_PRICE, food.getPrice());;
        intent.putExtra(DetailActivity.BUNDLE_KEY_PROJECT_IMAGE_URL, food.getUrlImage());
        startActivity(intent);
    }

    // --------------------
    // UI
    // --------------------
    private OnSuccessListener<Void> updateUIAfterRESTRequestsCompleted(final int origin){
        return aVoid -> {
            switch (origin) {
                case REGISTER_FAVORITE:
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, R.string.register_favorite, Snackbar.LENGTH_SHORT);
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

    @Override
    public void onClickFavoriteButton(int position) {

        FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();
        String currentUserId = auth.getCurrentUser().getUid();
        Food food = foodAdapter.getFood(position);

        firebaseFirestore.collection("users/" + currentUserId + "/favorite").document(food.getFoodName()).get().addOnCompleteListener(task -> {
            if (!task.getResult().exists()){

                FavoriteHelper.createFavorite(currentUserId, food.getFoodName()).addOnFailureListener(onFailureListener()).addOnSuccessListener(updateUIAfterRESTRequestsCompleted(REGISTER_FAVORITE));

            }else {
                FavoriteHelper.deleteFavorite(currentUserId, food.getFoodName()).addOnFailureListener(onFailureListener());
            }
        });
     }
}