package com.rohan.hotelierlistings.persistance

import com.rohan.hotelier.model.Accommodation
import com.rohan.hotelier.model.Booking
import com.rohan.hotelier.model.Location

interface ListingsPersistence {

    fun readAllAccommodations(): List<Accommodation>

    fun readAllLocations(): List<Location>

    fun readAllBookings(): List<Booking>

    fun readAccommodation(id: Int): Accommodation?

    fun readLocation(id: Int): Location?

    fun readBooking(id: Int): Booking?

    fun createLocation(location: Location): Location?

    fun createAccommodation(accommodation: Accommodation): Accommodation?

    fun createBooking(accommodationId: Int): Booking?

    fun updateLocation(id: Int, location: Location): Location?

    fun updateAccommodation(id: Int, accommodation: Accommodation): Accommodation?

    fun deleteAccommodation(id: Int): Boolean

    fun deleteLocation(id: Int): Boolean

    fun deleteBooking(id: Int): Boolean

    fun retrieveRating(rating: Int): List<Accommodation>

    fun retrieveCity(city: String): List<Accommodation>

    fun retrieveReputationBadge(reputationBadge: Accommodation.ReputationBadge): List<Accommodation>

}