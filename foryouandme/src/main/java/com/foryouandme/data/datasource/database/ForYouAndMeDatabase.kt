package com.foryouandme.data.datasource.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.foryouandme.data.repository.device.database.DeviceInfoDao
import com.foryouandme.data.repository.device.database.DeviceInfoDatabaseEntity
import com.foryouandme.data.repository.location.database.CurrentLocationDao
import com.foryouandme.data.repository.location.database.CurrentLocationDatabaseEntity
import com.foryouandme.data.repository.location.database.HomeLocationDao
import com.foryouandme.data.repository.location.database.HomeLocationDatabaseEntity

@Database(
    entities = [
        DeviceInfoDatabaseEntity::class,
        HomeLocationDatabaseEntity::class,
        CurrentLocationDatabaseEntity::class
    ],
    version = 2
)
@TypeConverters(Converters::class)
abstract class ForYouAndMeDatabase : RoomDatabase() {

    abstract fun deviceInfoDao(): DeviceInfoDao

    abstract fun homeLocationDao(): HomeLocationDao

    abstract fun currentLocationDao(): CurrentLocationDao

}