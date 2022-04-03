package com.codeChallenge.olympicChannel.model

import com.google.gson.annotations.SerializedName


data class Games(
    @SerializedName("city")
    var city: String? = null,
    @SerializedName("game_id")
    var game_Id: Int?= null,
    @SerializedName("year")
    var year: Int? = null
)