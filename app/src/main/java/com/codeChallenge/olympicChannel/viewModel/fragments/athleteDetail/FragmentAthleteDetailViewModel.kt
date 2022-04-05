package com.codeChallenge.olympicChannel.viewModel.fragments.athleteDetail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codeChallenge.olympicChannel.di.data.appManager.DataManager
import com.codeChallenge.olympicChannel.model.Athlete
import com.codeChallenge.olympicChannel.model.AthleteScore
import com.codeChallenge.olympicChannel.view.base.BaseViewModel
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FragmentAthleteDetailViewModel @Inject constructor(
    dataManager: DataManager,
    compositeDisposable: CompositeDisposable
) : BaseViewModel(dataManager, compositeDisposable) {

    var athleteDetailLiveDate =
        MutableLiveData<Pair<Athlete?, List<AthleteScore>>>()

    fun getDetailOfAthlete(athleteId: Int) {
        disposable[1]?.dispose()
        disposable[1] =
            Observable.zip(
                mDataManager.networkManager.getOlympicRoute().getAthleteDetail(athleteId),
                mDataManager.networkManager.getOlympicRoute().getScoreOfAthlete(athleteId)
            ) { athleteDetail, athleteScore ->
                Pair(athleteDetail, athleteScore)
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { Log.e("TAG", "getDetailOfAthlete: ${it.localizedMessage}") }
                .subscribe {
                    if (it.first.isSuccessful)
                        athleteDetailLiveDate.postValue(
                            Pair(
                                it.first.body(),
                                it.second.body().orEmpty()
                            )
                        )

                }
        addDisposable(disposable[1])

    }


}