package com.fadhlifirdausi607062300117.asesment1.ui.screen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fadhlifirdausi607062300117.asesment1.model.Recipes
import com.fadhlifirdausi607062300117.asesment1.network.ApiStatus
import com.fadhlifirdausi607062300117.asesment1.network.RecipesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainViewModelRecipes : ViewModel() {

    var data = mutableStateOf(emptyList<Recipes>())
        private set

    var status = MutableStateFlow(ApiStatus.LOADING)
        private set

    init {
        retrievedata()
    }

  fun retrievedata() {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                data.value = RecipesApi.service.getRecipes()
                status.value = ApiStatus.SUCCES
            }catch (e: Exception){
                Log.d("MainViewModelRecipes", "Failure: ${e.message}")
                status.value = ApiStatus.FAILED

            }
        }
    }
}