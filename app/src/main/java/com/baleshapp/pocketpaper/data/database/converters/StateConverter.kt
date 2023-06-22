package com.baleshapp.pocketpaper.data.database.converters

import androidx.room.TypeConverter
import com.baleshapp.pocketpaper.data.model.States
import com.baleshapp.pocketpaper.data.model.TaskTag

class StateConverter {

    @TypeConverter
    fun fromState(state: States): String{
        return state.name
    }

    @TypeConverter
    fun toState(stateName: String): States{
        return when (stateName){
            "CREATED" -> States.CREATED
            "CANCELED" -> States.CANCELED
            "DELETED" -> States.DELETED
            "EDITED" -> States.EDITED
            else -> States.CREATED
        }
    }

}