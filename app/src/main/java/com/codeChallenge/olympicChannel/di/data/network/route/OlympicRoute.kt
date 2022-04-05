package com.codeChallenge.olympicChannel.di.data.network.route

import com.codeChallenge.olympicChannel.model.Athlete
import com.codeChallenge.olympicChannel.model.AthleteScore
import com.codeChallenge.olympicChannel.model.Games
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OlympicRoute {

    @GET("/games")
    fun getGames(
    ): Observable<Response<List<Games>>>

    @GET("/games/{Id}/athletes")
    fun getAllAthletesInAGame(
        @Path("Id") gameId :Int
    ): Observable<Response<List<Athlete>>>


    @GET("/athletes/{Id}/results")
    fun getScoreOfAthlete(
        @Path("Id") athletesId :Int
    ): Observable<Response<List<AthleteScore>>>


    @GET("/athletes/{Id}")
    fun getAthleteDetail(
        @Path("Id") athletesId :Int
    ): Observable<Response<Athlete>>





}