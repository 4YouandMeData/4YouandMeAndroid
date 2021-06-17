package com.foryouandme.data.repository.yourdata.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface YourDataFilterIdDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertYourDataFilterId(vararg filterId: YourDataFilterIdDatabaseEntity)

    @Query("SELECT * FROM your_data_filter_ids")
    suspend fun getYourDataFilterIds(): List<YourDataFilterIdDatabaseEntity>

}