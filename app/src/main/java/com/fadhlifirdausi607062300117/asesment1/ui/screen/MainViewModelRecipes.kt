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
import kotlinx.coroutines.delay
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
                val allData = RecipesApi.service.getRecipes(userId)
                val filteredData = allData.filter { it.mine == "1" }
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
                if (result.status == "success") {
                    delay(1000)
                    retrievedata(userId)
                } else {
                    throw Exception(result.message)
                }
            } catch (e: Exception) {
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    fun editData(userId: String, id: String, nama: String, namaLatin: String, bitmap: Bitmap?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = if (bitmap != null) {
                    // Edit dengan gambar
                    RecipesApi.service.updateRecipe(
                        userId,
                        id,
                        nama.toRequestBody("text/plain".toMediaTypeOrNull()),
                        namaLatin.toRequestBody("text/plain".toMediaTypeOrNull()),
                        bitmap.toMultipartBody()
                    )
                } else {
                    // Edit tanpa gambar
                    RecipesApi.service.updateRecipeTanpaGambar(
                        userId,
                        id,
                        nama.toRequestBody("text/plain".toMediaTypeOrNull()),
                        namaLatin.toRequestBody("text/plain".toMediaTypeOrNull())
                    )
                }

                if (response.status == "success") {
                    delay(1000)
                    retrievedata(userId)
                } else {
                    throw Exception(response.message)
                }
            } catch (e: Exception) {
                errorMessage.value = "Gagal mengedit: ${e.message}"
            }
        }
    }


    fun deleteData(userId: String, id: String) {
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

    private fun Bitmap.toMultipartBody(): MultipartBody.Part {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val byteArray = stream.toByteArray()
        val requestBody = byteArray.toRequestBody("image/jpg".toMediaTypeOrNull(), 0, byteArray.size)
        return MultipartBody.Part.createFormData("image", "image.jpg", requestBody)
    }

    fun clearMessage() {
        errorMessage.value = null
    }
}
