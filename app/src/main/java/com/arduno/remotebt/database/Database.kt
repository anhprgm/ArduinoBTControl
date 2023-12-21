package com.arduno.remotebt.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [RemoteControl::class], version = 1, exportSchema = false)
@TypeConverters(ConvertersIntString::class)
abstract class Database : RoomDatabase() {

    abstract fun dao(): Dao

    companion object {
        private const val DB_NAME = "database"

        fun getInstance(context: android.content.Context): com.arduno.remotebt.database.Database {
            return androidx.room.Room.databaseBuilder(
                context, com.arduno.remotebt.database.Database::class
                    .java, DB_NAME
            )
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()

        }
    }
}