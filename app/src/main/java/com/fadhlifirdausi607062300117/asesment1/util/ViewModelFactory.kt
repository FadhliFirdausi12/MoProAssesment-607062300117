package com.fadhlifirdausi607062300117.asesment1.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fadhlifirdausi607062300117.asesment1.database.NotesDao
import com.fadhlifirdausi607062300117.asesment1.ui.screen.DetailViewModel
import com.fadhlifirdausi607062300117.asesment1.ui.screen.MainViewModelDb

class ViewModelFactory (
    private val dao: NotesDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModelDb:: class.java)) {
            return MainViewModelDb(dao) as T
        }else if(modelClass.isAssignableFrom(DetailViewModel::class.java)){
            return DetailViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}