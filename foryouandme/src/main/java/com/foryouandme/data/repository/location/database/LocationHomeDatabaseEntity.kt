package com.foryouandme.data.repository.location.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.foryouandme.entity.location.LocationCoordinates
import com.squareup.moshi.Json

@Entity(tableName = "home_location")
data class HomeLocationDatabaseEntity(
    @PrimaryKey @Json(name = "id") val id: Int,
    @Json(name = "longitude") val longitude: Double,
    @Json(name = "latitude") val latitude: Double,
) {

    fun toLocationCoordinates(): LocationCoordinates = LocationCoordinates(latitude, longitude)

}

@Entity(tableName = "current_location")
data class CurrentLocationDatabaseEntity(
    @PrimaryKey @Json(name = "id") val id: Int,
    @Json(name = "longitude") val longitude: Double,
    @Json(name = "latitude") val latitude: Double,
) {

    fun toLocationCoordinates(): LocationCoordinates = LocationCoordinates(latitude, longitude)

}

// allow only 2 type of items home and current
fun LocationCoordinates.toHomeDatabaseEntity(): HomeLocationDatabaseEntity =
    HomeLocationDatabaseEntity(0, latitude, longitude)

// allow only 1 item
fun LocationCoordinates.toCurrentDatabaseEntity(): CurrentLocationDatabaseEntity =
    CurrentLocationDatabaseEntity(0, latitude, longitude)