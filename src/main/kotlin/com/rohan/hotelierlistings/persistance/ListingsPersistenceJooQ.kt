package com.rohan.hotelierlistings.persistance

import com.rohan.hotelier.model.Accommodation
import com.rohan.hotelier.model.Booking
import com.rohan.hotelier.model.Location
import org.jooq.DSLContext
import org.jooq.generated.tables.Accommodations
import org.jooq.generated.tables.Bookings
import org.jooq.generated.tables.Locations
import org.jooq.impl.DSL
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.OffsetDateTime

@Component
class ListingsPersistenceJooQ(
    @Autowired private val dsl: DSLContext
): ListingsPersistence {

    companion object {
        private const val SUCCESS = 1
    }

    override fun readAllAccommodations(): List<Accommodation> = dsl.selectFrom(Accommodations.ACCOMMODATIONS)
        .fetchInto(Accommodation::class.java)

    override fun readAllLocations(): List<Location> = dsl.selectFrom(Locations.LOCATIONS)
        .fetchInto(Location::class.java)

    override fun readAllBookings(): List<Booking> = dsl.selectFrom(Bookings.BOOKINGS)
        .fetchInto(Booking::class.java)

    override fun readAccommodation(id: Int): Accommodation? = dsl.selectFrom(Accommodations.ACCOMMODATIONS)
        .where(Accommodations.ACCOMMODATIONS.ID.eq(id))
        .fetchOneInto(Accommodation::class.java)

    override fun readLocation(id: Int): Location? = dsl.selectFrom(Locations.LOCATIONS)
        .where(Locations.LOCATIONS.ID.eq(id))
        .fetchOneInto(Location::class.java)

    override fun readBooking(id: Int): Booking? = dsl.selectFrom(Bookings.BOOKINGS)
        .where(Bookings.BOOKINGS.ID.eq(id))
        .fetchOneInto(Booking::class.java)

    override fun createLocation(location: Location): Location? {
        val result = dsl.insertInto(Locations.LOCATIONS)
            .set(Locations.LOCATIONS.CITY, location.city)
            .set(Locations.LOCATIONS.STATE, location.state)
            .set(Locations.LOCATIONS.COUNTRY, location.country)
            .set(Locations.LOCATIONS.ZIP_CODE, location.zipCode.toString())
            .set(Locations.LOCATIONS.ADDRESS, location.address)
            .returning().fetchOne()

        return result?.into(Location::class.java)
    }

    override fun createAccommodation(accommodation: Accommodation): Accommodation? {
        val result = dsl.insertInto(Accommodations.ACCOMMODATIONS)
            .set(Accommodations.ACCOMMODATIONS.NAME, accommodation.name)
            .set(Accommodations.ACCOMMODATIONS.RATING, accommodation.rating)
            .set(Accommodations.ACCOMMODATIONS.CATEGORY, accommodation.category.toString())
            .set(Accommodations.ACCOMMODATIONS.LOCATION_ID, accommodation.locationId)
            .set(Accommodations.ACCOMMODATIONS.IMAGE, accommodation.image.toString())
            .set(Accommodations.ACCOMMODATIONS.REPUTATION, accommodation.reputation)
            .set(Accommodations.ACCOMMODATIONS.REPUTATION_BADGE, accommodation.reputationBadge?.name)
            .set(Accommodations.ACCOMMODATIONS.PRICE, accommodation.price.toBigDecimal())
            .set(Accommodations.ACCOMMODATIONS.AVAILABILITY, accommodation.availability)
            .returning().fetchOne()

        return result?.into(Accommodation::class.java)
    }

    override fun createBooking(accommodationId: Int): Booking? {
        var booking: Booking? = null

        dsl.transaction { configuration ->
            val localDsl = DSL.using(configuration)

            // First, check the availability in Accommodations table
            val availability = localDsl.select(Accommodations.ACCOMMODATIONS.AVAILABILITY)
                .from(Accommodations.ACCOMMODATIONS)
                .where(Accommodations.ACCOMMODATIONS.ID.eq(accommodationId))
                .fetchOne(Accommodations.ACCOMMODATIONS.AVAILABILITY)

            if (availability != null && availability > 0) {
                // If availability is greater than 0, proceed with booking creation
                val result = localDsl.insertInto(Bookings.BOOKINGS)
                    .set(Bookings.BOOKINGS.ACCOMMODATION_ID, accommodationId)
                    .set(Bookings.BOOKINGS.DATE, OffsetDateTime.now().toLocalDateTime())
                    .returning()
                    .fetchOne()

                if (result != null) {
                    booking = result.into(Booking::class.java)

                    // Decrement the availability in Accommodations table
                    localDsl.update(Accommodations.ACCOMMODATIONS)
                        .set(Accommodations.ACCOMMODATIONS.AVAILABILITY, Accommodations.ACCOMMODATIONS.AVAILABILITY.minus(1))
                        .where(Accommodations.ACCOMMODATIONS.ID.eq(accommodationId))
                        .execute()
                }
            }
        }

        return booking
    }

    override fun updateLocation(id: Int, location: Location): Location? {
        val result = dsl.update(Locations.LOCATIONS)
            .set(Locations.LOCATIONS.CITY, location.city)
            .set(Locations.LOCATIONS.STATE, location.state)
            .set(Locations.LOCATIONS.COUNTRY, location.country)
            .set(Locations.LOCATIONS.ZIP_CODE, location.zipCode.toString())
            .set(Locations.LOCATIONS.ADDRESS, location.address)
            .where(Locations.LOCATIONS.ID.eq(id))
            .returning().fetchOne()

        return result?.into(Location::class.java)
    }

    override fun updateAccommodation(id: Int, accommodation: Accommodation): Accommodation? {
        val result = dsl.update(Accommodations.ACCOMMODATIONS)
            .set(Accommodations.ACCOMMODATIONS.NAME, accommodation.name)
            .set(Accommodations.ACCOMMODATIONS.RATING, accommodation.rating)
            .set(Accommodations.ACCOMMODATIONS.CATEGORY, accommodation.category.toString())
            .set(Accommodations.ACCOMMODATIONS.IMAGE, accommodation.image.toString())
            .set(Accommodations.ACCOMMODATIONS.REPUTATION, accommodation.reputation)
            .set(Accommodations.ACCOMMODATIONS.REPUTATION_BADGE, accommodation.reputationBadge?.name)
            .set(Accommodations.ACCOMMODATIONS.PRICE, accommodation.price.toBigDecimal())
            .set(Accommodations.ACCOMMODATIONS.AVAILABILITY, accommodation.availability)
            .where(Accommodations.ACCOMMODATIONS.ID.eq(id))
            .returning().fetchOne()

        return result?.into(Accommodation::class.java)
    }

    override fun deleteAccommodation(id: Int): Boolean {
        val result = dsl.deleteFrom(Accommodations.ACCOMMODATIONS)
            .where(Accommodations.ACCOMMODATIONS.ID.eq(id))
            .execute()

        return result == SUCCESS
    }

    override fun deleteLocation(id: Int): Boolean {
        val result = dsl.deleteFrom(Locations.LOCATIONS)
            .where(Locations.LOCATIONS.ID.eq(id))
            .execute()

        return result == SUCCESS
    }

    override fun deleteBooking(id: Int): Boolean {
        var success = false

        dsl.transaction { configuration ->
            val localDsl = DSL.using(configuration)

            val booking = localDsl.selectFrom(Bookings.BOOKINGS)
                .where(Bookings.BOOKINGS.ID.eq(id))
                .fetchOneInto(Booking::class.java)

            val deleteCount = localDsl.deleteFrom(Bookings.BOOKINGS)
                .where(Bookings.BOOKINGS.ID.eq(id))
                .execute()

            if (deleteCount > 0) {
                // Booking was successfully deleted, so now increment availability

                if (booking != null) {
                    val accommodationId = booking.accommodationId

                    localDsl.update(Accommodations.ACCOMMODATIONS)
                        .set(Accommodations.ACCOMMODATIONS.AVAILABILITY, Accommodations.ACCOMMODATIONS.AVAILABILITY.plus(1))
                        .where(Accommodations.ACCOMMODATIONS.ID.eq(accommodationId))
                        .execute()

                    success = true
                }
            }
        }

        return success
    }

    override fun retrieveRating(rating: Int): List<Accommodation> =
        dsl.selectFrom(Accommodations.ACCOMMODATIONS)
            .where(Accommodations.ACCOMMODATIONS.RATING.eq(rating))
            .fetchInto(Accommodation::class.java)

    override fun retrieveCity(city: String): List<Accommodation> {
        val subQuery = dsl.select(Locations.LOCATIONS.ID)
            .from(Locations.LOCATIONS)
            .where(Locations.LOCATIONS.CITY.eq(city))

        return dsl.selectFrom(Accommodations.ACCOMMODATIONS)
            .where(Accommodations.ACCOMMODATIONS.LOCATION_ID.`in`(subQuery))
            .fetchInto(Accommodation::class.java)
    }

    override fun retrieveReputationBadge(reputationBadge: Accommodation.ReputationBadge): List<Accommodation> =
         dsl.selectFrom(Accommodations.ACCOMMODATIONS)
            .where(Accommodations.ACCOMMODATIONS.REPUTATION_BADGE.eq(reputationBadge.name))
            .fetchInto(Accommodation::class.java)

}