package com.fadhlifirdausi607062300117.asesment1.ui.screen

import android.graphics.Bitmap
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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

class MainViewModelRecipes : ViewModel() {

    var data = mutableStateOf(emptyList<Recipes>())
        private set

    var status = MutableStateFlow(ApiStatus.LOADING)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    fun retrievedata(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                val allData = RecipesApi.service.getRecipes(userId) // Ambil semua data dari API
                val filteredData = allData.filter { it.mine == "1" } // ✅ Hanya data milik user
                data.value = filteredData
                status.value = ApiStatus.SUCCES
            } catch (e: Exception) {
                Log.d("MainViewModelRecipes", "Failure: ${e.message}")
                status.value = ApiStatus.FAILED
            }
        }
    }
    fun saveData(userId: String, nama: String, namaLatin: String, bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = RecipesApi.service.postRecipe(
                    userId,
                    nama.toRequestBody("text/plain".toMediaTypeOrNull()),
                    namaLatin.toRequestBody("text/plain".toMediaTypeOrNull()),
                    bitmap.toMultipartBody()
                )

                if (result.status == "success")
                    retrievedata(userId)
                else
                    throw Exception(result.message)
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    private fun Bitmap.toMultipartBody(): MultipartBody.Part {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val byteArray = stream.toByteArray()
        val requestBody = byteArray.toRequestBody(
            "image/jpg".toMediaTypeOrNull(), 0, byteArray.size)
        return MultipartBody.Part.createFormData(
            "image", "image.jpg", requestBody)
    }


    fun deleteData(userId: String, id: String) { // Ubah parameter id menjadi String
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = RecipesApi.service.deleteRecipe(userId, id)
                if (result.status == "success") {
                    retrievedata(userId)
                } else {
                    errorMessage.value = result.message ?: "Gagal menghapus"
                }
            } catch (e: Exception) {
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }

fun clearMessage() {errorMessage.value = null}


}