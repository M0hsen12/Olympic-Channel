package com.codeChallenge.olympicChannel.view.activities.main

import android.app.Dialog
import android.os.Bundle
import android.transition.Transition
import android.transition.TransitionInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import com.codeChallenge.olympicChannel.R
import com.codeChallenge.olympicChannel.databinding.ActivityMainBinding
import com.codeChallenge.olympicChannel.di.data.database.entity.GamesEntity
import com.codeChallenge.olympicChannel.di.viewModelsInjections.InjectionViewModelProvider
import com.codeChallenge.olympicChannel.model.Athlete
import com.codeChallenge.olympicChannel.util.EndlessRecyclerOnScrollListener
import com.codeChallenge.olympicChannel.util.getListForPagination
import com.codeChallenge.olympicChannel.util.materialSimpleProgressDialog
import com.codeChallenge.olympicChannel.view.adapter.HomePageAdapter
import com.codeChallenge.olympicChannel.view.base.BaseActivity
import com.codeChallenge.olympicChannel.view.fragments.detail.FragmentAthleteDetail
import com.codeChallenge.olympicChannel.viewModel.activities.main.MainActivityViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import javax.inject.Inject


class MainActivity : BaseActivity<ActivityMainBinding, MainActivityViewModel>() {

    @Inject
    lateinit var mViewModelFactoryActivity: InjectionViewModelProvider<MainActivityViewModel>
    override fun getLayoutId() = R.layout.activity_main
    private var disposable = CompositeDisposable()
    private var gamesList = ArrayList<GamesEntity>()
    private lateinit var progressDialog: Dialog
    private lateinit var frameLayout: FrameLayout

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
                onItemsClicked = {athlete,imageView ->
                    sendUserToAthleteDetailFragment(athlete,imageView)
                })
            adapter = myAdapter
            val endlessHandlerHomeRv =
                object : EndlessRecyclerOnScrollListener() {
                    override fun onLoadMore(currentPage: Int) {
                        handlePagination(myAdapter, sortedlist, currentPage)
                    }
                }
            clearOnScrollListeners()
            addOnScrollListener(endlessHandlerHomeRv)
            endlessHandlerHomeRv.totalPages = sortedlist.size.toLong()
            gamesList.addAll(getListForPagination(sortedlist, 0))
            myAdapter.submitList(gamesList)

        }

    }

    private fun sendUserToAthleteDetailFragment(it: Athlete, imageView: ImageView) {

        if (!this::frameLayout.isInitialized)
            frameLayout = createFrameLayoutForFragment()


        val athleteFragment =
            FragmentAthleteDetail.getInstance(it.athleteId)

        val changeTransform: Transition =
            TransitionInflater.from(this).inflateTransition(R.transition.shared_image)
        val explodeTransform: Transition =
            TransitionInflater.from(this).inflateTransition(android.R.transition.explode)

        athleteFragment.sharedElementReturnTransition = changeTransform
        athleteFragment.exitTransition = explodeTransform

        athleteFragment.sharedElementEnterTransition = changeTransform
        athleteFragment.enterTransition = explodeTransform

        supportFragmentManager.beginTransaction()
            .add(
                frameLayout.id,
                athleteFragment,
                ATHLETE_FRAGMENT_TAG
            ).addSharedElement(imageView,"athleteProfile").addToBackStack(ATHLETE_FRAGMENT_TAG)
            .commit()

    }

    private fun createFrameLayoutForFragment(): FrameLayout {
        val frameLayout = FrameLayout(this)
        val parentViewID = binding.parent.id
        val frameLayoutParams = ConstraintLayout.LayoutParams(
            0,
            0
        )
        frameLayout.layoutParams = frameLayoutParams
        frameLayout.id = View.generateViewId()
        binding.parent.addView(frameLayout, binding.parent.size)
        val set = ConstraintSet()
        set.clone(binding.parent)
        set.connect(frameLayout.id, ConstraintSet.TOP, parentViewID, ConstraintSet.TOP)
        set.connect(frameLayout.id, ConstraintSet.LEFT, parentViewID, ConstraintSet.LEFT)
        set.connect(frameLayout.id, ConstraintSet.RIGHT, parentViewID, ConstraintSet.RIGHT)
        set.connect(frameLayout.id, ConstraintSet.BOTTOM, parentViewID, ConstraintSet.BOTTOM)
        set.applyTo(binding.parent)

        return frameLayout
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
        progressDialog = materialSimpleProgressDialog(this,getString(R.string.syncData))
        progressDialog.show()
        viewModel = mViewModelFactoryActivity.get(this, MainActivityViewModel::class)
    }


    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }

    companion object {
        const val PAGE_SIZE = 6
        const val ATHLETE_FRAGMENT_TAG = "ATHLETE_FRAGMENT_TAG"

    }
}