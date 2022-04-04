package com.codeChallenge.olympicChannel.viewModel.activities.main

import android.annotation.SuppressLint
import android.util.Log
import com.codeChallenge.olympicChannel.di.data.appManager.DataManager
import com.codeChallenge.olympicChannel.di.data.database.entity.GamesEntity
import com.codeChallenge.olympicChannel.model.Athlete
import com.codeChallenge.olympicChannel.model.AthleteScore
import com.codeChallenge.olympicChannel.model.Games
import com.codeChallenge.olympicChannel.view.base.BaseViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import retrofit2.Response
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    dataManager: DataManager,
    compositeDisposable: CompositeDisposable
) : BaseViewModel(dataManager, compositeDisposable) {
    private val gamesEntityList = ArrayList<GamesEntity>()
    private val athleteList = ArrayList<Athlete>()
    private val mapGamesWithAthletes = hashMapOf<Games, List<Athlete>>()

    var gamesListProcessor = BehaviorProcessor.create<List<GamesEntity>>()
    val TAG = "QQQ"

    init {
        syncData()
    }

    private fun syncData() {
        getAllItemsInDatabase {
            if (it.isEmpty())
                getGames()
            else
                gamesListProcessor.onNext(it)

        }

    }


    private fun getGames() {
        disposable[0]?.dispose()
        disposable[0] =
            mDataManager.networkManager.getOlympicRoute()
                .getGames()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.isSuccessful)
                        getAthletesForEachGame(it.body())


                }, {
                    Log.e("TAG", "getAPI: games ${it.message}")
                    errorLiveData.postValue(it)
                })
        addDisposable(disposable[0])
    }

    private fun getAthletesForEachGame(games: List<Games>?) {

        disposable[1]?.dispose()
        disposable[1] = Observable.fromIterable(games)
            .map { eachGame ->
                getPairOfAthletesAndGame(eachGame)
            }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { pair ->
                prepareAthleteListForGetScore(pair, games)
            }

        addDisposable(disposable[1])
    }

    private fun prepareAthleteListForGetScore(
        pair: Pair<Games, Observable<Response<List<Athlete>>>>,
        mainGamesList: List<Games>?
    ) {

        disposable[2] =
            pair.second.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        if (it.isSuccessful) {
                            athleteList.addAll(it.body().orEmpty())
                            mapGamesWithAthletes[pair.first] = it.body().orEmpty()
                        }

                    }, {
                        Log.e(TAG, "on error  $it ")
                    }, {
                        if (mainGamesList?.size == mapGamesWithAthletes.size)
                            getScoreOfEachAthlete()

                    })

        addDisposable(disposable[2])
    }


    private fun getScoreOfEachAthlete() {
        var counter = 0
        disposable[3] = Observable.fromIterable(athleteList)
            .flatMap {
                mDataManager.networkManager.getOlympicRoute()
                    .getScoreOfAthlete(it.athleteId?.toInt() ?: 0)
            }.subscribe(
                {
                    if (it.isSuccessful) {
                        athleteList[counter].score = it.body().orEmpty() as ArrayList<AthleteScore>
                        counter++
                    }
                }, {
                    Log.e(TAG, "on error: ${it.localizedMessage}")
                }, {
                    Log.e(TAG, "getScoreOfEachAthlete1:WHERE I SHOULD BE ")
                    setupGameEntityWithScores()

                })
        addDisposable(disposable[3])
    }


    private fun setupGameEntityWithScores() {

        mapGamesWithAthletes.forEach {

            gamesEntityList.add(
                GamesEntity(
                    city = it.key.city ?: "",
                    gameId = it.key.game_Id ?: 0,
                    year = it.key.year ?: 0,
                    athletes = handleScore(it.value)
                )
            )
        }


        insertGamesIntoDatabase()

    }

    private fun handleScore(mapList: List<Athlete>): List<Athlete> {
        mapList.forEach { mapAthlete ->
            athleteList.find { it == mapAthlete }?.let {
                mapAthlete.score = it.score
            }
        }

        return mapList
    }


    private fun getPairOfAthletesAndGame(game: Games): Pair<Games, Observable<Response<List<Athlete>>>> {
        return Pair(
            game, mDataManager.networkManager.getOlympicRoute()
                .getAllAthletesInAGame(game.game_Id ?: 0)
        )
    }


    private fun insertGamesIntoDatabase() {
        CoroutineScope(IO).launch {
            mDataManager.databaseManager.gamesDao().insertAll(gamesEntityList)
        }.invokeOnCompletion {
            gamesListProcessor.onNext(gamesEntityList)
        }
    }

    private fun getAllItemsInDatabase(onItemReceives: (list: List<GamesEntity>) -> Unit) {
        CoroutineScope(IO).launch {
            val list = mDataManager.databaseManager.gamesDao().all()
            onItemReceives.invoke(list)
        }
    }


    override fun onCleared() {
        super.onCleared()
        disposable.entries.forEach { entry ->
            entry.value?.takeIf { it.isDisposed.not() }?.dispose()
        }
    }
}