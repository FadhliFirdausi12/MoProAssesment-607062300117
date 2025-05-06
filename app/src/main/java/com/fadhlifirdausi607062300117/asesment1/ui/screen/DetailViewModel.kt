package com.fadhlifirdausi607062300117.asesment1.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fadhlifirdausi607062300117.asesment1.database.NotesDao
import com.fadhlifirdausi607062300117.asesment1.model.Notes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class DetailViewModel(private val dao: NotesDao) : ViewModel() {

    private val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    private val _data = MutableStateFlow<Notes?>(null)
    val data: StateFlow<Notes?> = _data

    fun insertNote(judul: String, catatan: String, mood: String, tanggal: String) {
        val waktu = timeFormat.format(Date())
        val note = Notes(
            judul = judul,
            catatan = catatan,
            mood = mood,
            tanggal = tanggal,
            waktu = waktu
        )
        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(note)
        }
    }

    fun updateNote(id: Long, judul: String, catatan: String, mood: String, tanggal: String) {
        val waktu = timeFormat.format(Date())
        val note = Notes(
            id = id,
            judul = judul,
            catatan = catatan,
            mood = mood,
            tanggal = tanggal,
            waktu = waktu
        )
        viewModelScope.launch(Dispatchers.IO) {
            dao.update(note)
        }
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteById(id)
        }
    }

    fun getAllNotes(): Flow<List<Notes>> {
        return dao.getCatatan()
    }

    suspend fun getNoteById(id: Long): Notes? {
        return dao.getCatatanById(id)
    }
}