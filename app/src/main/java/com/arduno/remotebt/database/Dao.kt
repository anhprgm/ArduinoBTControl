package com.arduno.remotebt.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(model: RemoteControl)
    @Query("DELETE FROM remote_control WHERE id = :id")
    fun deleteById(id: Int)
    @Query("DELETE FROM remote_control")
    fun deleteAll()

    @Query("SELECT * FROM remote_control WHERE id = :id")
    fun getById(id: Int): RemoteControl?

    @Query("SELECT * FROM remote_control")
    fun getLiveData(): LiveData<List<RemoteControl>>
}