package com.baleshapp.pocketpaper.data.model.weather

data class Wind(
    var speed: String? = null, //Wind speed. Unit Default: meter/sec, Metric: meter/sec, Imperial: miles/hour.
    var deg: String? = null, //Wind direction, degrees (meteorological)
    var gust: String? = null // Wind gust. Unit Default: meter/sec, Metric: meter/sec, Imperial: miles/hour
)
