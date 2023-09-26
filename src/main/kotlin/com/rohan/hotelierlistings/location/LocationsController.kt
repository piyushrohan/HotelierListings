package com.rohan.hotelierlistings.location

import com.rohan.hotelier.api.LocationsApi
import com.rohan.hotelier.model.Location
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class LocationsController (
    val locationsService: LocationsService
): LocationsApi {

    override fun locationsGet(): ResponseEntity<List<Location>> {
        val locations = locationsService.getAllLocations()
        return ResponseEntity.ok(locations)
    }

    override fun locationIdDelete(locationId: Int): ResponseEntity<Unit> {
        return if (locationsService.deleteLocationById(locationId)) {
            ResponseEntity<Unit>(HttpStatus.OK)
        } else
            ResponseEntity<Unit>(HttpStatus.NOT_FOUND)
    }

    override fun locationIdGet(locationId: Int): ResponseEntity<Location> {
        val location = locationsService.getLocationnById(locationId)
        return ResponseEntity.ok(location)
    }

    override fun locationPost(location: Location): ResponseEntity<Location> {
        val record = locationsService.createNewLocation(location)
        return ResponseEntity.ok(record)
    }

    override fun locationIdPut(locationId: Int, location: Location): ResponseEntity<Location> {
        val update = locationsService.updateLocation(locationId, location)
        return ResponseEntity.ok(update)
    }

}