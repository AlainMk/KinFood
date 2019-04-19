package com.alain.mk.kinfood.api;


import com.alain.mk.kinfood.models.Booking;
import com.alain.mk.kinfood.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class BookingHelper {

    public static final String COLLECTION_NAME = "booking";

    public static CollectionReference getAllBooking(){

        return FirebaseFirestore.getInstance()
                .collection(COLLECTION_NAME);
    }

    public static Query getOrderBooking(){

        FirebaseAuth auth;

        auth = FirebaseAuth.getInstance();
        String currentUserId = auth.getCurrentUser().getUid();

        return FirebaseFirestore.getInstance()
                .collection(COLLECTION_NAME)
                .whereEqualTo("userId", currentUserId);
    }

    public static Task<DocumentReference> createBookingForFood(String firstName, String lastName, String peopleNumber, String phoneNumber, String email, String bookingDate, String bookingHour, User userSender, String userId){

        Booking booking = new Booking(firstName, lastName, peopleNumber, phoneNumber, email, bookingDate, bookingHour, userSender, userId);

        return BookingHelper.getAllBooking()
                .add(booking);
    }

}
