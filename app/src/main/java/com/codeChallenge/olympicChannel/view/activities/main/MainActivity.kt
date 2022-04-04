package com.codeChallenge.olympicChannel.view.activities.main

import android.app.Dialog
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.codeChallenge.olympicChannel.R
import com.codeChallenge.olympicChannel.databinding.ActivityMainBinding
import com.codeChallenge.olympicChannel.di.data.database.entity.GamesEntity
import com.codeChallenge.olympicChannel.di.viewModelsInjections.InjectionViewModelProvider
import com.codeChallenge.olympicChannel.util.EndlessRecyclerOnScrollListener
import com.codeChallenge.olympicChannel.util.getListForPagination
import com.codeChallenge.olympicChannel.util.materialSimpleProgressDialog
import com.codeChallenge.olympicChannel.view.adapter.HomePageAdapter
import com.codeChallenge.olympicChannel.view.base.BaseActivity
import com.codeChallenge.olympicChannel.viewModel.activities.main.MainActivityViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import java.util.ArrayList
import javax.inject.Inject

class MainActivity : BaseActivity<ActivityMainBinding, MainActivityViewModel>() {

    @Inject
    lateinit var mViewModelFactoryActivity: InjectionViewModelProvider<MainActivityViewModel>
    override fun getLayoutId() = R.layout.activity_main
    private var disposable = CompositeDisposable()
    private var gamesList = ArrayList<GamesEntity>()
    lateinit var progressDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUI()
        observeLiveData()

    }

    private fun observeLiveData() {
        viewModel?.apply {
            this@MainActivity.disposable.add(gamesListProcessor.subscribe {
                runOnUiThread {
                    progressDialog.dismiss()
                    setupRecyclerView(it)
                }
            })
        }


    }

    private fun setupRecyclerView(it: List<GamesEntity>) {
        val sortedlist = it.sortedByDescending { it.year }
        binding.mainRvGames.apply {

            layoutManager = LinearLayoutManager(context)
            val myAdapter = HomePageAdapter(
                onItemsClicked = {

                })
            adapter = myAdapter
            val endlessHandlerHomeRv =
                object : EndlessRecyclerOnScrollListener() {
                    override fun onLoadMore(currentPage: Int) {
                        handlePagination(myAdapter,sortedlist,currentPage)
                    }
                }
            clearOnScrollListeners()
            addOnScrollListener(endlessHandlerHomeRv)
            endlessHandlerHomeRv.totalPages = sortedlist.size.toLong()
            gamesList.addAll(getListForPagination(sortedlist, 0))
            myAdapter.submitList(gamesList)

        }

    }

    private fun handlePagination(
        myAdapter: HomePageAdapter,
        sortedlist: List<GamesEntity>,
        currentPage: Int
    ) {
        progressDialog.show()
        CoroutineScope(Main).launch {
            delay(1000) // mimic api call latency
            val start = gamesList.size
            gamesList.addAll(getListForPagination(sortedlist, currentPage))
            myAdapter.submitList(gamesList)
            myAdapter.notifyItemRangeInserted(start, PAGE_SIZE)
        }.invokeOnCompletion {
            progressDialog.dismiss()
        }

    }



    private fun initUI() {
        progressDialog = materialSimpleProgressDialog(this)
        progressDialog.show()
        viewModel = mViewModelFactoryActivity.get(this, MainActivityViewModel::class)
    }


    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }

    companion object {
        const val PAGE_SIZE = 6
        const val PAGE_NUMBER = 0

    }
}