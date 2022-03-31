package com.codeChallenge.olympicChannel.view.activities.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.codeChallenge.olympicChannel.R
import com.codeChallenge.olympicChannel.databinding.ActivityMainBinding
import com.codeChallenge.olympicChannel.di.viewModelsInjections.InjectionViewModelProvider
import com.codeChallenge.olympicChannel.view.base.BaseActivity
import com.codeChallenge.olympicChannel.viewModel.activities.main.MainActivityViewModel
import javax.inject.Inject

class MainActivity : BaseActivity<ActivityMainBinding, MainActivityViewModel>() {

    @Inject
    lateinit var mViewModelFactoryActivity: InjectionViewModelProvider<MainActivityViewModel>

    override fun getLayoutId() = R.layout.activity_main


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUI()
        observeLiveData()

    }

    private fun observeLiveData() {
        viewModel?.gamesLiveData?.observe(this){
            Log.e("TAG", "observeLiveData:${it.size} " )
        }

    }

    private fun initUI() {
        viewModel = mViewModelFactoryActivity.get(this, MainActivityViewModel::class)

    }
}