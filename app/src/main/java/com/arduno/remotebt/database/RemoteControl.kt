package com.arduno.remotebt.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_control")
data class RemoteControl(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var temp: List<BaseModel> = ArrayList(),
    var mode: List<BaseModel> = ArrayList(),
    var power: List<BaseModel> = ArrayList(),
)

data class BaseModel(
    var key: String,
    var value: String
)