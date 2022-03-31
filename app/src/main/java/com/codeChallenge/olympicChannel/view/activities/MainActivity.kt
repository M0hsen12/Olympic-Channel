package com.codeChallenge.olympicChannel.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

    }
}