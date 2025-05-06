package com.fadhlifirdausi607062300117.asesment1.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.fadhlifirdausi607062300117.asesment1.model.Notes
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {

    @Insert
    suspend fun insert(notes: Notes)

    @Update
    suspend fun update(notes: Notes)

    @Query("SELECT * FROM notes ORDER BY tanggal DESC, waktu DESC")
    fun getCatatan(): Flow<List<Notes>>

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getCatatanById(id: Long): Notes?

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteById(id: Long)
}