package com.foryouandme.data.repository.location.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: LocationDatabaseEntity)

    @Query("SELECT * FROM location")
    suspend fun getLocation(): List<LocationDatabaseEntity>

}