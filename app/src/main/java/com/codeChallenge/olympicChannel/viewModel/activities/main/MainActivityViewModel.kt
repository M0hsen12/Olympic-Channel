package com.codeChallenge.olympicChannel.viewModel.activities.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.codeChallenge.olympicChannel.di.data.appManager.DataManager
import com.codeChallenge.olympicChannel.view.base.BaseViewModel
import io.reactivex.Observable
import io.reactivex.Observable.interval
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    dataManager: DataManager,
    compositeDisposable: CompositeDisposable
) : BaseViewModel(dataManager, compositeDisposable) {


    init {

    }


    companion object {
        const val TransactionID =
            100  // i could use enum  but want to try every thing in this test project
        const val TransactionAmount = 0.0
        const val TransactionCount = 0
        const val CommissionFeePercentage = 0.7
        const val SYMBOL_REQUEST =
            "USD,BGN,JPY,EUR" // super weird api ,, wont work on list or array i had to use it like this
    }

}