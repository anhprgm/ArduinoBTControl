package com.arduno.remotebt.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_control")
data class RemoteControl(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var temp: Map<Int, String> = HashMap(),
    var mode: Map<String, String> = HashMap(),
    var power: Map<String, String> = HashMap(),
)