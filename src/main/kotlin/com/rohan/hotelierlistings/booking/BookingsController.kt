package com.rohan.hotelierlistings.booking

import com.rohan.hotelier.api.BookingsApi
import com.rohan.hotelier.model.Booking
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class BookingsController (
    val bookingsService: BookingsService
): BookingsApi {

    override fun bookingsGet(): ResponseEntity<List<Booking>> {
        val bookings = bookingsService.getAllBookings()
        return ResponseEntity.ok(bookings)
    }

    override fun bookingIdDelete(bookingId: Int): ResponseEntity<Unit> {
        return if (bookingsService.deleteBookingById(bookingId)) {
            ResponseEntity<Unit>(HttpStatus.OK)
        } else
            ResponseEntity<Unit>(HttpStatus.NOT_FOUND)
    }

    override fun bookingIdGet(bookingId: Int): ResponseEntity<Booking> {
        val booking = bookingsService.getBookingById(bookingId)
        return ResponseEntity.ok(booking)
    }

    override fun bookingPost(booking: Booking): ResponseEntity<Booking> {
        val newBooking = bookingsService.createNewBooking(booking.accommodationId)
        return if (newBooking != null )
            ResponseEntity.ok(newBooking)
        else
            ResponseEntity.badRequest().body(booking)
    }

}