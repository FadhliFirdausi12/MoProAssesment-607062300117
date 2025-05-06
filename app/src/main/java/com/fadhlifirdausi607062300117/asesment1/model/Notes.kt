package com.fadhlifirdausi607062300117.asesment1.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Notes(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val judul: String,
    val catatan: String,
    val mood: String,
    val tanggal: String,  // format: YYYY-MM-DD (misalnya: "2025-05-03")
    val waktu: String     // format: HH:mm:ss (misalnya: "14:23:01")
)