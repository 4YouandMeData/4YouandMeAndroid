package com.foryouandme.data.repository.device.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.util.*

@Dao
interface DeviceInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeviceInfo(deviceInfo: DeviceInfoDatabaseEntity)

    @Query("SELECT * FROM device_info")
    suspend fun getDeviceInfo(): List<DeviceInfoDatabaseEntity>

    @Query("DELETE FROM device_info WHERE timestamp < :timestamp")
    suspend fun deleteDeviceInfoOlderThan(timestamp: Date)

    @Query("DELETE FROM device_info WHERE timestamp = :timestamp")
    suspend fun deleteDeviceInfo(timestamp: Date)

}