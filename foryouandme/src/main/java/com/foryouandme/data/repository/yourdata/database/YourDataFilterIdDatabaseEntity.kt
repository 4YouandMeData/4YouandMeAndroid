package com.foryouandme.data.repository.yourdata.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.foryouandme.entity.yourdata.YourDataFilter

@Entity(tableName = "your_data_filter_ids")
data class YourDataFilterIdDatabaseEntity(
    @ColumnInfo(name = "id") @PrimaryKey val id: String
)

fun YourDataFilter.toDatabaseEntity(): YourDataFilterIdDatabaseEntity =
    YourDataFilterIdDatabaseEntity(id = id)