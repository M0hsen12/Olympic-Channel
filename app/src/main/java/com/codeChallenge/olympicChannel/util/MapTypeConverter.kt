package com.codeChallenge.olympicChannel.util

import androidx.room.TypeConverter
import com.codeChallenge.olympicChannel.model.Athlete
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*


object MapTypeConverter {

    @TypeConverter
    fun storedStringToAthlete(data: String?): List<Athlete?>? {
        val gson = Gson()
        if (data == null) {
            return Collections.emptyList()
        }
        val listType = object : TypeToken<List<Athlete?>?>() {}.type
        return gson.fromJson<List<Athlete?>>(data, listType)
    }

    @TypeConverter
    fun athleteToStoredString(myObjects: List<Athlete?>?): String? {
        val gson = Gson()
        return gson.toJson(myObjects)
    }



}