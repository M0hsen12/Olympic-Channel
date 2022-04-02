package com.codeChallenge.olympicChannel.di.data.network

import com.codeChallenge.olympicChannel.di.data.network.route.OlympicRoute
import com.github.pwittchen.reactivenetwork.library.rx2.Connectivity
import io.reactivex.subjects.BehaviorSubject
import retrofit2.Retrofit
import javax.inject.Inject


class NetworkManager @Inject constructor(
    var networkConnectivity: BehaviorSubject<Connectivity>,
    @NetworkModule.RestApi private val retrofitRestApi: Retrofit,
) {

    fun <T> create(tClass: Class<T>): T {
        return retrofitRestApi.create(tClass)
    }

    fun getOlympicRoute(): OlympicRoute {
        return retrofitRestApi.create(OlympicRoute::class.java)
    }


}
