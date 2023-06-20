package com.baleshapp.pocketpaper.data.model.weather

data class City(
    var id: Int? = null, //City ID. Please note that built-in geocoder functionality has been deprecated.
    var name: String? = null, //City name. Please note that built-in geocoder functionality has been deprecated.
    //var coord
    var country: String? = null, //Country code (GB, JP etc.). Please note that built-in geocoder functionality has been deprecated.
    var population: String? = null, //City population
    var timezone: String? = null, //Shift in seconds from UTC
    var sunrise: String? = null, //Sunrise time, Unix, UTC
    var sunset: String? = null //Sunset time, Unix, UTC
)
