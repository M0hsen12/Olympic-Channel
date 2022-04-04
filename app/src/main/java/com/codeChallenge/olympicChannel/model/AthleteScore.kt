package com.codeChallenge.olympicChannel.model


import com.google.gson.annotations.SerializedName

data class AthleteScore(
    @SerializedName("bronze")
    var bronze: Int? = 0,
    @SerializedName("city")
    var city: String? = "",
    @SerializedName("gold")
    var gold: Int? = 0,
    @SerializedName("silver")
    var silver: Int? = 0,
    @SerializedName("year")
    var year: Int? = 0
)