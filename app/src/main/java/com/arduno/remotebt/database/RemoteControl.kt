package com.arduno.remotebt.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "remote_control")
data class RemoteControl(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var nameRemoteControl: String = "",
    var temp: List<DataModel> = ArrayList(),
    var mode: List<DataModel> = ArrayList(),
    var power: List<DataModel> = ArrayList(),
    var fan : List<DataModel> = ArrayList(),
    var sleep: List<DataModel> = ArrayList(),
) :Serializable

data class DataModel(
    var key: String = "",
    var value: String = ""
)