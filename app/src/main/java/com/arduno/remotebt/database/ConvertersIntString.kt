package com.arduno.remotebt.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ConvertersIntString {
    @TypeConverter
    fun fromStringToIntMap(value: String?): List<DataModel>? {
        if (value == null) {
            return listOf()
        }
        val mapType = object : TypeToken<List<DataModel>>() {}.type
        return Gson().fromJson(value, mapType)
    }

    @TypeConverter
    fun fromIntMapToString(map: List<DataModel>?): String {
        return Gson().toJson(map)
    }

}