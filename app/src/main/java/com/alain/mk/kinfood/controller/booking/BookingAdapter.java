package com.alain.mk.kinfood.controller.booking;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.alain.mk.kinfood.R;
import com.alain.mk.kinfood.models.Booking;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class BookingAdapter extends FirestoreRecyclerAdapter<Booking, BookingViewHolder> {

    private Booking booking;

    public BookingAdapter(@NonNull FirestoreRecyclerOptions<Booking> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull BookingViewHolder holder, int position, @NonNull Booking model) {

        holder.updateWithBooking(this.getBooking(position));
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new BookingViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.activity_detailbooking_item, viewGroup, false));
    }

    public Booking getBooking(int position){

        return this.booking = getItem(position);
    }
}
