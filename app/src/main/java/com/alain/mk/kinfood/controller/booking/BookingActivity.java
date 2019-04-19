package com.alain.mk.kinfood.controller.booking;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alain.mk.kinfood.R;
import com.alain.mk.kinfood.api.BookingHelper;
import com.alain.mk.kinfood.api.UserHelper;
import com.alain.mk.kinfood.base.BaseActivity;
import com.alain.mk.kinfood.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;

import butterknife.BindView;
import butterknife.OnClick;


public class BookingActivity extends BaseActivity {

    @BindView(R.id.activity_booking_first_name) EditText firstName;
    @BindView(R.id.activity_booking_last_name) EditText lastName;
    @BindView(R.id.activity_booking_people_number) EditText peopleNumber;
    @BindView(R.id.activity_booking_phone) EditText phoneNumber;
    @BindView(R.id.activity_booking_email) EditText email;
    @BindView(R.id.activity_booking_date) EditText bookingDate;
    @BindView(R.id.activity_booking_hour) EditText bookingHour;
    @BindView(R.id.activity_booking_coordinateLayout) CoordinatorLayout rootView;
    @BindView(R.id.booking_activity_progress_bar) ProgressBar progressBar;

    @Nullable
    private User modelCurrentUser;
    private static final int REGISTER_BOOKING = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.configureToolbar();
        this.getCurrentUserFromFirestore();
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_booking;
    }

    @OnClick(R.id.activity_booking_button_sender)
    public void onClickSendBooking(){

        this.createBookingInFirebase();
    }

    // --------------------
    // REST REQUESTS
    // --------------------
    // 4 - Get Current User from Firestore
    private void getCurrentUserFromFirestore(){
        UserHelper.getUser(getCurrentUser().getUid()).addOnSuccessListener(documentSnapshot -> modelCurrentUser = documentSnapshot.toObject(User.class));
    }

    private void createBookingInFirebase(){

        FirebaseAuth auth;

        auth = FirebaseAuth.getInstance();
        String currentUserId = auth.getCurrentUser().getUid();

        this.progressBar.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(firstName.getText()) && !TextUtils.isEmpty(lastName.getText()) && !TextUtils.isEmpty(peopleNumber.getText()) && !TextUtils.isEmpty(phoneNumber.getText()) &&
                !TextUtils.isEmpty(email.getText()) && !TextUtils.isEmpty(bookingDate.getText()) && !TextUtils.isEmpty(bookingHour.getText()) && modelCurrentUser != null && currentUserId != null){

            BookingHelper.createBookingForFood(firstName.getText().toString(), lastName.getText().toString(), peopleNumber.getText().toString(), phoneNumber.getText().toString(),
                    email.getText().toString(), bookingDate.getText().toString(), bookingHour.getText().toString(), this.modelCurrentUser, currentUserId).addOnFailureListener(this.onFailureListener()).addOnSuccessListener(this.updateUIAfterRESTRequestsCompleted(REGISTER_BOOKING));

        }

        this.firstName.setText("");
        this.lastName.setText("");
        this.peopleNumber.setText("");
        this.phoneNumber.setText("");
        this.email.setText("");
        this.bookingDate.setText("");
        this.bookingHour.setText("");

    }

    // --------------------
    // UI
    // --------------------

    private OnSuccessListener<DocumentReference> updateUIAfterRESTRequestsCompleted(final int origin) {
        return documentReference -> {
            switch (origin) {
                case REGISTER_BOOKING:
                    progressBar.setVisibility(View.INVISIBLE);
                    Snackbar snackbar = Snackbar.make(rootView, R.string.register_succeed, Snackbar.LENGTH_SHORT);
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
