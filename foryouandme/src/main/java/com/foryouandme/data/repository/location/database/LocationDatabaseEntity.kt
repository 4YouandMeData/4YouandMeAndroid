package com.foryouandme.data.repository.location.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.foryouandme.entity.location.Location
import com.squareup.moshi.Json

@Entity(tableName = "location")
data class LocationDatabaseEntity(
    @PrimaryKey @Json(name = "id") val id: Int,
    @Json(name = "longitude") val longitude: Double,
    @Json(name = "latitude") val latitude: Double,
) {

    fun toLocation(): Location = Location(latitude, longitude)

}

// allow only 1 item
fun Location.toDatabaseEntity(): LocationDatabaseEntity =
    LocationDatabaseEntity(0, latitude, longitude)