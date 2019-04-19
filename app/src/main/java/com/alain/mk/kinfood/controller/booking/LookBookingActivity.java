package com.alain.mk.kinfood.controller.booking;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alain.mk.kinfood.R;
import com.alain.mk.kinfood.api.BookingHelper;
import com.alain.mk.kinfood.base.BaseActivity;
import com.alain.mk.kinfood.models.Booking;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import butterknife.BindView;

public class LookBookingActivity extends BaseActivity {


    @BindView(R.id.activity_detailbooking_recycler_view) RecyclerView recyclerView;

    private BookingAdapter bookingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.configureToolbar();
        this.configureRecyclerView();
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_detailbooking;
    }

    private void configureRecyclerView(){

        this.bookingAdapter = new BookingAdapter(generateOptionsForAdapter(BookingHelper.getOrderBooking()));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(this.bookingAdapter);
    }

    // --------------------
    // UI
    // --------------------
    // Configure RecyclerView with a Query
    private FirestoreRecyclerOptions<Booking> generateOptionsForAdapter(Query query){
        return new FirestoreRecyclerOptions.Builder<Booking>()
                .setQuery(query, Booking.class)
                .setLifecycleOwner(this)
                .build();
    }
}
