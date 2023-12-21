package com.arduno.remotebt.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ConvertersIntString {
    @TypeConverter
    fun fromStringToIntMap(value: String?): Map<Int, String>? {
        if (value == null) {
            return emptyMap()
        }
        val mapType = object : TypeToken<Map<Int, String>>() {}.type
        return Gson().fromJson(value, mapType)
    }

    @TypeConverter
    fun fromIntMapToString(map: Map<Int, String>?): String {
        return Gson().toJson(map)
    }

    @TypeConverter
    fun fromStringToStringMap(value: String?): Map<String, String>? {
        if (value == null) {
            return emptyMap()
        }
        val mapType = object : TypeToken<Map<String, String>>() {}.type
        return Gson().fromJson(value, mapType)
    }

    @TypeConverter
    fun fromStringMapToString(map: Map<String, String>?): String {
        return Gson().toJson(map)
    }
}