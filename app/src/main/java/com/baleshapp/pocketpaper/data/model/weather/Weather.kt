package com.baleshapp.pocketpaper.data.model.weather

data class Weather(
    var id: Int? = null, //Weather condition id
    var main: String? = null, //Group of weather parameters (Rain, Snow, Extreme etc.)
    var description: String? = null, //Weather condition within the group. You can get the output in your language.
    var icon: Int? = null //Weather icon id
)
