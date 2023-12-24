package com.arduno.remotebt.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_control")
data class RemoteControl(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var temp: List<DataModel> = ArrayList(),
    var mode: List<DataModel> = ArrayList(),
    var power: List<DataModel> = ArrayList(),
)

data class DataModel(
    var key: String,
    var value: String
)