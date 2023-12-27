package com.example.lab9_1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab9_1.Constants.API_CITY
import com.example.lab9_1.Constants.API_KEY
import com.example.lab9_1.Constants.API_LANG
import com.example.lab9_1.Constants.API_UNITS
import com.example.lab9_1.Constants.STATE_WEATHER
import com.example.lab9_1.Constants.TIMBER_TAG
import com.example.lab9_1.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber


class MainActivity : AppCompatActivity() {
    private val weatherAdapter = WeatherAdapter()
    private var weatherList = listOf<WeatherNW.DataWeather>()
    private lateinit var binding: ActivityMainBinding
    private var weatherAPI = WeatherAPI.createAPI()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recycleViewInit()
        //ресурсы не эффективно используются, пару переворотов и зависает

        if (savedInstanceState == null) {
            loadWeather()
            Timber.tag(TIMBER_TAG).d("Залез в сеть.")
        } else {
            Timber.tag(TIMBER_TAG).d("Ничего не сделал")
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val jsonWeather = Gson().toJson(weatherList)
        outState.putString(STATE_WEATHER, jsonWeather)
        Timber.tag(TIMBER_TAG).d("Сохранил")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        val type = object : TypeToken<List<WeatherNW.DataWeather>>() {}.type
        weatherList = Gson().fromJson(savedInstanceState.getString(STATE_WEATHER), type)
        Timber.tag(TIMBER_TAG).d("Восстановил")
        weatherAdapter.submitList(weatherList)
    }

    private fun recycleViewInit() {
        binding.rvWeather.adapter = weatherAdapter
        binding.rvWeather.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
    }

    private fun loadWeather() {
        weatherAPI.getForecast(API_CITY, API_KEY, API_UNITS, API_LANG)
            .enqueue(object : Callback<WeatherNW> {
                override fun onResponse(call: Call<WeatherNW>, response: Response<WeatherNW>) {
                    if (response.isSuccessful) {
                        weatherList = response.body()?.list!!
                        weatherAdapter.submitList(weatherList)
                    }
                }
                override fun onFailure(call: Call<WeatherNW>, trowable: Throwable) {
                    Timber.tag(TIMBER_TAG).e(trowable)
                }
            })
    }
}