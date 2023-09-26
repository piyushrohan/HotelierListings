package com.rohan.hotelierlistings.location

import com.rohan.hotelier.model.Location
import com.rohan.hotelierlistings.persistance.ListingsPersistence
import org.springframework.stereotype.Service

@Service
class LocationsService (
    private val listingsPersistence: ListingsPersistence
) {
    fun getAllLocations(): List<Location>? = listingsPersistence.readAllLocations()

    fun deleteLocationById(locationId: Int): Boolean = listingsPersistence.deleteLocation(locationId)

    fun getLocationnById(locationId: Int): Location? = listingsPersistence.readLocation(locationId)

    fun createNewLocation(location: Location): Location? = listingsPersistence.createLocation(location)

    fun updateLocation(locationId: Int, location: Location): Location? = listingsPersistence.updateLocation(locationId, location)

}