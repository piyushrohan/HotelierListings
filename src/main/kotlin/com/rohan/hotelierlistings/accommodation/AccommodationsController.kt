package com.rohan.hotelierlistings.accommodation

import com.rohan.hotelier.api.AccommodationsApi
import com.rohan.hotelier.model.Accommodation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class AccommodationsController (
    val accommodationsService: AccommodationsService
): AccommodationsApi {

    override fun accommodationsGet(): ResponseEntity<List<Accommodation>> {
        val accommodations = accommodationsService.getAllAccommodations()
        return ResponseEntity.ok(accommodations)
    }

    override fun accommodationIdGet(accommodationId: Int): ResponseEntity<Accommodation> {
        val accommodation = accommodationsService.getAccommodationById(accommodationId)
        return ResponseEntity.ok(accommodation)
    }

    override fun accommodationIdDelete(accommodationId: Int): ResponseEntity<Unit> {
        return if (accommodationsService.deleteAccommodationById(accommodationId)) {
            ResponseEntity<Unit>(HttpStatus.OK)
        } else
            ResponseEntity<Unit>(HttpStatus.NOT_FOUND)
    }

    override fun accommodationPost(accommodation: Accommodation): ResponseEntity<Accommodation> {
        val record = accommodationsService.createNewAccommodation(accommodation)
        return if (record != null )
            ResponseEntity.ok(record)
        else
            ResponseEntity.badRequest().body(accommodation)
    }

    override fun accommodationIdPut(accommodationId: Int, accommodation: Accommodation): ResponseEntity<Accommodation> {
        val update = accommodationsService.updateAccommodation(accommodationId, accommodation)
        return if (update != null )
            ResponseEntity.ok(update)
        else
            ResponseEntity.badRequest().body(accommodation)
    }

    override fun accommodationsByCityGet(city: String): ResponseEntity<List<Accommodation>> {
        val accommodations = accommodationsService.getAccommodationsInCity(city)
        return ResponseEntity.ok(accommodations)
    }

    override fun accommodationsByRatingGet(rating: Int): ResponseEntity<List<Accommodation>> {
        val accommodations = accommodationsService.getAccommodationsByRating(rating)
        return ResponseEntity.ok(accommodations)
    }

    override fun accommodationsByReputationBadgeGet(reputationBadge: String): ResponseEntity<List<Accommodation>> {
        val accommodations = accommodationsService.getAccommodationsByBadge(reputationBadge)
        return ResponseEntity.ok(accommodations)
    }

}