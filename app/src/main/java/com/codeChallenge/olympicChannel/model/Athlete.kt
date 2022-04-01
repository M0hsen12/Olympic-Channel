package com.codeChallenge.olympicChannel.model


import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject

@JsonObject
data class Athlete(
    @JsonField(name = ["athlete_id"])
    var athleteId: String? = null,
    @JsonField(name = ["bio"])
    var bio: String? = null,
    @JsonField(name = ["dateOfBirth"])
    var dateOfBirth: String? = null,
    @JsonField(name = ["height"])
    var height: Int? = null,
    @JsonField(name = ["name"])
    var name: String? = null,
    @JsonField(name = ["photo_id"])
    var photoId: Int? = null,
    @JsonField(name = ["surname"])
    var surname: String? = null,
    @JsonField(name = ["weight"])
    var weight: Int? = null
)