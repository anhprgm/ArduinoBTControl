package com.arduno.remotebt.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ConvertersIntString {
    @TypeConverter
    fun fromStringToIntMap(value: String?): List<BaseModel>? {
        if (value == null) {
            return listOf()
        }
        val mapType = object : TypeToken<List<BaseModel>>() {}.type
        return Gson().fromJson(value, mapType)
    }

    @TypeConverter
    fun fromIntMapToString(map: List<BaseModel>?): String {
        return Gson().toJson(map)
    }

}