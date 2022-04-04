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
) {
    fun getGlobalScore(): Int {

        return ((gold ?: 0) * GOLD_POINT) + ((silver ?: 0) * SILVER_POINT) + ((bronze
            ?: 0) * BRONZE_POINT)
    }


    companion object {
        const val GOLD_POINT = 5
        const val SILVER_POINT = 3
        const val BRONZE_POINT = 1
    }
}