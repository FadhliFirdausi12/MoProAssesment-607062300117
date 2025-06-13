package com.fadhlifirdausi607062300117.asesment1.network

import com.fadhlifirdausi607062300117.asesment1.model.OpStatus
import com.fadhlifirdausi607062300117.asesment1.model.Recipes
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

private const val BASE_URL = "https://gh.d3ifcool.org/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()


interface HewanApiService {
    @GET("hewan.php")
    suspend fun getRecipes(): List<Recipes>


    @Multipart
    @POST("hewan.php")
    suspend fun postRecipe(
        @Header("Authorization") userId: String,
        @Part("nama") nama: RequestBody,
        @Part("namaLatin") namaLatin: RequestBody,
        @Part image: MultipartBody.Part
    ): OpStatus
}

object RecipesApi{
    val service: HewanApiService by lazy{
        retrofit.create(HewanApiService::class.java)
    }
    fun getRecipesUrl(imageId: String): String{
        return "${BASE_URL}image.php?id=$imageId"
    }
}

enum class ApiStatus {LOADING, SUCCES, FAILED}













//modul 12 reference static API
//private const val  BASE_URL = "https://raw.githubusercontent.com/indraazimi/mobpro1-compose/static-api/"
//
//private val moshi = Moshi.Builder()
//    .add(KotlinJsonAdapterFactory())
//    .build()
//
//private val retrofit = Retrofit.Builder()
//    .addConverterFactory(MoshiConverterFactory.create(moshi))
//    .baseUrl(BASE_URL)
//    .build()
//
//
//interface RecipesApiService {
//    @GET("static-api.json")
//    suspend fun getRecipes(): List<Recipes>
//}
//
//object RecipesApi{
//    val service: RecipesApiService by lazy{
//        retrofit.create(RecipesApiService::class.java)
//    }
//    fun getRecipesUrl(imageId: String): String{
//        return "$BASE_URL$imageId.jpg"
//    }
//}
//
//enum class ApiStatus {LOADING, SUCCES, FAILED}