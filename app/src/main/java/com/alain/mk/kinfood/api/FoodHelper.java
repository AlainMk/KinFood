package com.alain.mk.kinfood.api;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FoodHelper {

    private static final String COLLECTION_NAME = "foods";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getFoodCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

}
