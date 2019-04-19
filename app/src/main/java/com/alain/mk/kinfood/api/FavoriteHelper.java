package com.alain.mk.kinfood.api;

import com.alain.mk.kinfood.models.Favorite;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

public class FavoriteHelper {

    public static final String COLLECTION_NAME = "favorite";

    // --- COLLECTION REFERENCE ---

    public static DocumentReference getFavoriteCollection(String id){
        return UserHelper.getUsersCollection()
                .document(id)
                .collection(COLLECTION_NAME)
                .document();

    }

    public static Task<Void> createFavorite(String uid, String foodName){

        Favorite favoriteToCreate = new Favorite(foodName);
        return UserHelper.getUsersCollection()
                .document(uid)
                .collection(COLLECTION_NAME)
                .document(foodName)
                .set(favoriteToCreate);
    }

    public static Task<Void> deleteFavorite(String uid, String foodName){

        return UserHelper.getUsersCollection()
                .document(uid)
                .collection(COLLECTION_NAME)
                .document(foodName)
                .delete();
    }

}
