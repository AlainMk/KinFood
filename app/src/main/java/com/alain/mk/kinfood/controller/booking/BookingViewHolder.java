package com.alain.mk.kinfood.controller.booking;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alain.mk.kinfood.R;
import com.alain.mk.kinfood.models.Booking;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookingViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.activity_detailbooking_texte_date) TextView textViewDate;
    @BindView(R.id.activity_detailbooking_texte_hour) TextView textViewHour;

    public BookingViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithBooking(Booking booking){

        this.textViewDate.setText(booking.getBookingDate());
        this.textViewHour.setText(booking.getBookingHour());
    }
}
