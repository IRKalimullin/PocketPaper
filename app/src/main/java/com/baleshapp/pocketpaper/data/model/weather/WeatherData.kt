package com.baleshapp.pocketpaper.data.model.weather

data class WeatherData(
    var dt: Long? = null, //Time of data forecasted, unix, UTC
    var main: Main? = null,
    var weather: Weather? = null,
    //var clouds: Clouds? =null,
    var wind: Wind? = null,
    var visibility: Int? = null, //Average visibility, metres. The maximum value of the visibility is 10km
    var pop: Float? = null, //Probability of precipitation. The values of the parameter vary between 0 and 1, where 0 is equal to 0%, 1 is equal to 100%
    var rain: Rain? = null,
    var snow: Snow? = null,
    var sys: Sys? = null,
    var dtTxt: String? = null //Time of data forecasted, ISO, UTC
)
