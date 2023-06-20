package com.baleshapp.pocketpaper.data.model.weather


data class Main(
    var temp: String? = null, // Temperature. Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit.
    var feelsLike: String? = null, //This temperature parameter accounts for the human perception of weather. Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit.
    var tempMin: String? = null, //Minimum temperature at the moment of calculation.
    var tempMax: String? = null, //Maximum temperature at the moment of calculation.
    var pressure: String? = null, //Atmospheric pressure on the sea level by default, hPa
    var seaLevel: String? = null, //Atmospheric pressure on the sea level, hPa
    var grndLevel: String? = null, //Atmospheric pressure on the ground level, hPa
    var humidity: String? = null, //Humidity, %
    var tempKf: String? = null //Internal parameter
)
