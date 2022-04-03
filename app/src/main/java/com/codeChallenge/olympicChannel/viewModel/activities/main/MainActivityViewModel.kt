package com.codeChallenge.olympicChannel.viewModel.activities.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.codeChallenge.olympicChannel.di.data.appManager.DataManager
import com.codeChallenge.olympicChannel.di.data.database.entity.GamesEntity
import com.codeChallenge.olympicChannel.model.Athlete
import com.codeChallenge.olympicChannel.model.Games
import com.codeChallenge.olympicChannel.view.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import java.util.ArrayList
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    dataManager: DataManager,
    compositeDisposable: CompositeDisposable
) : BaseViewModel(dataManager, compositeDisposable) {
    private val gamesEntityList = ArrayList<GamesEntity>()
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
        games?.forEachIndexed { index, game ->
            disposable[index.plus(1)] =
                mDataManager.networkManager.getOlympicRoute()
                    .getAllAthletesInAGame(game.game_Id ?: 0)
                    .delay(100, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        if (it?.isSuccessful == true) {
                            gamesEntityList.add(handleList(game, it.body().orEmpty()))

                            if (games.size == index.plus(1))  //last item of list
                                insertGamesIntoDatabase()

                        }


                    }, {
                        gamesEntityList.add(handleList(game, emptyList()))
                        Log.e("TAG", "getAPI athleses list: ${it.message} -")
                        errorLiveData.postValue(it)
                    })
            addDisposable(disposable[index.plus(1)])
        }
    }

    private fun insertGamesIntoDatabase() {
        CoroutineScope(IO).launch {
            delay(5000)
            mDataManager.databaseManager.gamesDao().insertAll(gamesEntityList)
        }.invokeOnCompletion { Log.e(TAG, "insertGamesIntoDatabase: DONE ") }
    }

    private fun getAllItemsInDatabase(onItemReceives: (list: List<GamesEntity>) -> Unit) {
        CoroutineScope(IO).launch {
            val list = mDataManager.databaseManager.gamesDao().all()
            onItemReceives.invoke(list)
        }
    }

    private fun handleList(game: Games, list: List<Athlete>): GamesEntity {
        return GamesEntity(
            gameId = game.game_Id ?: 0,
            city = game.city ?: "",
            year = game.year ?: 0,
            athletes = list
        )
    }

    private fun getAthlete(id: Int) {
        disposable[1]?.dispose()
        disposable[1] =
            mDataManager.networkManager.getOlympicRoute()
                .getAllAthletesInAGame(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it?.isSuccessful == true) {

                        Log.e("TAG", "$id getAthlete: ${it.body()?.last()?.surname}")


                    }

                }, {
                    Log.e("TAG", "getAPI: ${it.message}")
                    errorLiveData.postValue(it)
                })
        addDisposable(disposable[1])
    }




    override fun onCleared() {
        super.onCleared()
        disposable.entries.forEach { entry ->
            entry.value?.takeIf { it.isDisposed.not() }?.dispose()
        }
    }
}