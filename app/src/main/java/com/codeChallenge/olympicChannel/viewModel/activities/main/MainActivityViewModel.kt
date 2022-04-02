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
import io.reactivex.schedulers.Schedulers
import java.util.ArrayList
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    dataManager: DataManager,
    compositeDisposable: CompositeDisposable
) : BaseViewModel(dataManager, compositeDisposable) {
    val gamesLiveData = MutableLiveData<List<Games>>()

    init {
        getGames()
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
                    Log.e("TAG", "getAPI: ${it.message}")
                    errorLiveData.postValue(it)
                })
        addDisposable(disposable[0])
    }

    private fun getAthletesForEachGame(games: List<Games>?) {
        val gamesEntityList = ArrayList<GamesEntity>()
        games?.forEachIndexed { index, game ->
            disposable[index.plus(1)] =
                mDataManager.networkManager.getOlympicRoute()
                    .getAllAthletesInAGame(game.game_Id ?: 0)
                    .delay(500, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        if (it?.isSuccessful == true) {
                            gamesEntityList.add(handleList(game,it.body().orEmpty()))

                            if (games.size == index.plus(1)) //last item of list
                                insertGamesIntoDatabase(gamesEntityList)
                        }


                    }, {
                        gamesEntityList.add(handleList(game, emptyList()))
                        Log.e("TAG", "getAPI: ${it.message} -")
                        errorLiveData.postValue(it)
                    })
            addDisposable(disposable[index.plus(1)])
        }
    }

    private fun insertGamesIntoDatabase(list: ArrayList<GamesEntity>) {
        disposable[0]?.dispose()
        disposable[0] =
            mDataManager.databaseManager.gamesDao().insertAll(list)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.e("TAG", "insertGamesIntoDatabase:all done ", )

                }, {
                    Log.e("TAG", "getAPI: ${it.message}")
                    errorLiveData.postValue(it)
                })
        addDisposable(disposable[0])

    }
    private fun getAllItemsInDatabase(){
        disposable[0]?.dispose()
        disposable[0] =
            mDataManager.databaseManager.gamesDao().all()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.e("TAG", "insertGamesIntoDatabase:all done ${it.size} -- ${it.last().athletes.size}", )


                }, {
                    Log.e("TAG", "getAPI: ${it.message}")
                    errorLiveData.postValue(it)
                })
        addDisposable(disposable[0])
    }

    private fun handleList(game: Games,list: List<Athlete>): GamesEntity {
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


    companion object {

    }

    override fun onCleared() {
        super.onCleared()
        disposable.entries.forEach { entry ->
            entry.value?.takeIf { it.isDisposed.not() }?.dispose()
        }
    }
}