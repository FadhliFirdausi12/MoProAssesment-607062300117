package com.fadhlifirdausi607062300117.asesment1.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fadhlifirdausi607062300117.asesment1.model.Notes

@Database(entities = [Notes::class], version = 1, exportSchema = false)
abstract class NotesDb : RoomDatabase() {
    abstract val dao: NotesDao

    companion object {
        @Volatile
        private var INSTANCE: NotesDb? = null

        fun getInstance(context: Context): NotesDb {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        NotesDb::class.java,
                        "notes.db"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}