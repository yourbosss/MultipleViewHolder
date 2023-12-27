package com.example.lab9_1

import com.example.lab9_1.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//Todo
interface WeatherAPI {
    companion object {
        fun createAPI(): WeatherAPI {
            val retrofitBuilder = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofitBuilder.create(WeatherAPI::class.java)
        }
    }

    @GET("forecast")
    fun getForecast(
        @Query("q") city: String,
        @Query("appid") key: String,
        @Query("units") units: String,
        @Query("lang") language: String
    ): Call<WeatherNW>
}