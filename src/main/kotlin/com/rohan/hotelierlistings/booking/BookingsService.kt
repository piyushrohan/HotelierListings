package com.rohan.hotelierlistings.booking

import com.rohan.hotelier.model.Booking
import com.rohan.hotelierlistings.persistance.ListingsPersistence
import org.springframework.stereotype.Service

@Service
class BookingsService (
    private val listingsPersistence: ListingsPersistence
) {
    internal fun createNewBooking(accommodationId: Int): Booking? = listingsPersistence.createBooking(accommodationId)
    internal fun getAllBookings(): List<Booking>? = listingsPersistence.readAllBookings()

    internal fun deleteBookingById(bookingId: Int): Boolean = listingsPersistence.deleteBooking(bookingId)

    internal fun getBookingById(bookingId: Int): Booking? = listingsPersistence.readBooking(bookingId)


}