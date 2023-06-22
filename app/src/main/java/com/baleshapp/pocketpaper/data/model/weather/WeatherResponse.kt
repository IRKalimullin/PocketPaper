package com.baleshapp.pocketpaper.data.model.weather

data class WeatherResponse(
    var cod: Int? = null, //Internal parameter
    var message: String? = null, // Internal parameter
    var cnt: Int? = null, //A number of timestamps returned in the API response
    var list: List<WeatherData>? = null,
    var city: City? = null
)