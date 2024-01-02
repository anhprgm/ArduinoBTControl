package com.arduno.remotebt.database

import androidx.lifecycle.LiveData

class DatabaseManager(private var database: Database) : Repository {
    override fun insert(model: RemoteControl) {
        database.dao().insert(model)
    }

    override fun deleteById(id: Int) {
        database.dao().deleteById(id)
    }

    override fun deleteAll() {
        database.dao().deleteAll()
    }

    override fun getById(id: Int): RemoteControl? {
        return database.dao().getById(id)
    }

    override fun getLiveData(): LiveData<List<RemoteControl>> {
        return database.dao().getLiveData()
    }

    override fun delete(id: Int) {
        database.dao().delete(id)
    }
}