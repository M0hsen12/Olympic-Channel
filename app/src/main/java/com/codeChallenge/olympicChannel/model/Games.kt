package com.codeChallenge.olympicChannel.model

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject


@JsonObject
class Games(
    @JsonField(name = ["city"])
    var city: String? = null,
    @JsonField(name = ["game_id"])
    var gameId: Int?= null,
    @JsonField(name = ["year"])
    var year: Int? = null
)