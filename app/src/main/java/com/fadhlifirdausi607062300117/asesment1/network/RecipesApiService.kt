package com.fadhlifirdausi607062300117.asesment1.network

import com.fadhlifirdausi607062300117.asesment1.model.Recipes
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val  BASE_URL = "https://raw.githubusercontent.com/indraazimi/mobpro1-compose/static-api/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()


interface RecipesApiService {
    @GET("static-api.json")
    suspend fun getRecipes(): List<Recipes>
}

object RecipesApi{
    val service: RecipesApiService by lazy{
        retrofit.create(RecipesApiService::class.java)
    }
    fun getRecipesUrl(imageId: String): String{
        return "$BASE_URL$imageId.jpg"
    }
}