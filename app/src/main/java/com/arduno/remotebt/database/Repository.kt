package com.arduno.remotebt.database

import androidx.lifecycle.LiveData

interface Repository {
    fun insert(model: RemoteControl)
    fun deleteById(id: Int)
    fun deleteAll()
    fun getById(id: Int): RemoteControl?
    fun getLiveData(): LiveData<List<RemoteControl>>

}