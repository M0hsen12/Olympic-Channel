package com.codeChallenge.olympicChannel.di.data.network.route

import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OlympicRoute {

    @GET("v1/latest")
    fun getCurrency(
        @Query("access_key") apiKey: String,
        @Query("symbols") symbols: String
    ): Observable<Response<StrictMath>>


}