package com.rohan.hotelierlistings.accommodation

import com.rohan.hotelier.model.Accommodation
import com.rohan.hotelierlistings.persistance.ListingsPersistence
import org.springframework.stereotype.Service
import java.net.URI

@Service
class AccommodationsService (
    private val listingsPersistence: ListingsPersistence
) {
    internal fun getAllAccommodations(): List<Accommodation> = listingsPersistence.readAllAccommodations()

    internal fun getAccommodationById(accommodationId: Int): Accommodation? = listingsPersistence.readAccommodation(accommodationId)

    internal fun deleteAccommodationById(accommodationId: Int): Boolean = listingsPersistence.deleteAccommodation(accommodationId)

    internal fun createNewAccommodation(accommodation: Accommodation): Accommodation? =
        checkAccommodation(accommodation)?.let { listingsPersistence.createAccommodation(it) }

    internal fun updateAccommodation(accommodationId: Int, accommodation: Accommodation): Accommodation? =
        checkAccommodation(accommodation)?.let { listingsPersistence.updateAccommodation(accommodationId, it) }

    internal fun getAccommodationsInCity(city: String): List<Accommodation> =
        listingsPersistence.retrieveCity(city)

    internal fun getAccommodationsByRating(rating: Int): List<Accommodation> =
        listingsPersistence.retrieveRating(rating)

    internal fun getAccommodationsByBadge(reputationBadge: String): List<Accommodation> =
        listingsPersistence.retrieveReputationBadge(Accommodation.ReputationBadge.valueOf(reputationBadge))

    private fun getReputationBadge(reputation: Int): Accommodation.ReputationBadge {
        return when {
            reputation <= 500 -> Accommodation.ReputationBadge.RED
            reputation <= 799 -> Accommodation.ReputationBadge.YELLOW
            else -> Accommodation.ReputationBadge.GREEN
        }
    }

    private fun checkAccommodation(accommodation: Accommodation): Accommodation? {
        return if (isNameValid(accommodation.name) && isImageValid(accommodation.image))
            Accommodation(
            name = accommodation.name,
            rating = accommodation.rating,
            category = accommodation.category,
            locationId = accommodation.locationId,
            image = accommodation.image,
            reputation = accommodation.reputation,
            price = accommodation.price,
            availability = accommodation.availability,
            reputationBadge = getReputationBadge(accommodation.reputation)
        ) else
            null
    }

    fun isNameValid(name: String): Boolean {
        val prohibitedWords = listOf("Free", "Offer", "Book", "Website")
        val minLength = 11 // Minimum length of 11 characters

        return name.length > minLength && !prohibitedWords.any { word -> name.contains(word, ignoreCase = true) }
    }

    fun isImageValid(image: URI): Boolean {
        return try {
            // Check if the URI's scheme (protocol) is either "http" or "https"
            image.scheme == "http" || image.scheme == "https"
        } catch (e: Exception) {
            false
        }
    }
}