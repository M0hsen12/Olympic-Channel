package com.codeChallenge.olympicChannel.model


import com.fasterxml.jackson.annotation.JsonIgnore
import com.google.gson.annotations.SerializedName
import java.util.ArrayList

data class Athlete(
    @SerializedName("athlete_id")
    var athleteId: String? = null,

    @SerializedName("bio")
    var bio: String? = null,

    @SerializedName("dateOfBirth")
    var dateOfBirth: String? = null,

    @SerializedName("height")
    var height: Int? = null,

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("photo_id")
    var photoId: Int? = null,

    @SerializedName("surname")
    var surname: String? = null,

    @SerializedName("weight")
    var weight: Int? = null,

    @JsonIgnore
    var score: ArrayList<AthleteScore>? = ArrayList()

)