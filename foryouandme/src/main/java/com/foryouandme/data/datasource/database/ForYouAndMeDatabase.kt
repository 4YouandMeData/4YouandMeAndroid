package com.foryouandme.data.datasource.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.foryouandme.data.repository.device.database.DeviceInfoDao
import com.foryouandme.data.repository.device.database.DeviceInfoDatabaseEntity
import com.foryouandme.data.repository.location.database.HomeLocationDao
import com.foryouandme.data.repository.location.database.HomeLocationDatabaseEntity
import com.foryouandme.data.repository.yourdata.database.YourDataFilterIdDao
import com.foryouandme.data.repository.yourdata.database.YourDataFilterIdDatabaseEntity

@Database(
    entities = [
        DeviceInfoDatabaseEntity::class,
        HomeLocationDatabaseEntity::class,
        YourDataFilterIdDatabaseEntity::class
    ],
    version = 3
)
@TypeConverters(Converters::class)
abstract class ForYouAndMeDatabase : RoomDatabase() {

    abstract fun deviceInfoDao(): DeviceInfoDao

    abstract fun homeLocationDao(): HomeLocationDao

    abstract fun yourDataFilterIdDao(): YourDataFilterIdDao

}