package com.codeChallenge.olympicChannel.view.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.codeChallenge.olympicChannel.BuildConfig
import com.codeChallenge.olympicChannel.R
import com.codeChallenge.olympicChannel.databinding.FragmentAthleteDetailBinding
import com.codeChallenge.olympicChannel.di.viewModelsInjections.InjectionViewModelProvider
import com.codeChallenge.olympicChannel.model.Athlete
import com.codeChallenge.olympicChannel.model.AthleteScore
import com.codeChallenge.olympicChannel.util.LinkUtil
import com.codeChallenge.olympicChannel.util.SimplePlayer
import com.codeChallenge.olympicChannel.util.materialSimpleProgressDialog
import com.codeChallenge.olympicChannel.view.adapter.AthleteScoreAdapter
import com.codeChallenge.olympicChannel.view.base.BaseFragment
import com.codeChallenge.olympicChannel.viewModel.fragments.athleteDetail.FragmentAthleteDetailViewModel

import com.google.android.material.shape.CornerFamily
import javax.inject.Inject


class FragmentAthleteDetail :
    BaseFragment<FragmentAthleteDetailBinding, FragmentAthleteDetailViewModel>() {


    @Inject
    lateinit var mViewModelFactoryActivity: InjectionViewModelProvider<FragmentAthleteDetailViewModel>
    override fun getLayoutId() = R.layout.fragment_athlete_detail
    private lateinit var progressDialog: Dialog
    private var player = SimplePlayer()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        observeLiveData()

    }

    private fun observeLiveData() {
        progressDialog =
            materialSimpleProgressDialog(requireContext(), getString(R.string.fetchData))
        progressDialog.show()

        viewModel?.athleteDetailLiveDate?.observe(viewLifecycleOwner) {
            setupUI(it)
        }
    }


    private fun setupUI(pair: Pair<Athlete?, List<AthleteScore>>) {
        progressDialog.dismiss()
        binding.detailGroup.visibility = View.VISIBLE
        binding.detailBack.setOnClickListener {
            activity?.supportFragmentManager?.popBackStackImmediate()
        }

        handleAthleteText(pair.first)
        handleAthleteImage(pair.first)
        handleAthleteRv(pair.second)
        buildPlayer()

    }

    private fun handleAthleteRv(list: List<AthleteScore>) {
        binding.detailAthleteRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            val myAdapter = AthleteScoreAdapter()
            adapter = myAdapter
            myAdapter.submitList(list)
        }

    }

    private fun handleAthleteImage(athlete: Athlete?) {
        binding.detailAthletePic.apply {
            val radius = resources.getDimension(R.dimen.default_corner_radius)
            Glide.with(this)
                .load("${BuildConfig.baseUrl}/athletes/${athlete?.athleteId}/photo")
                .into(this)
            this.shapeAppearanceModel = shapeAppearanceModel
                .toBuilder()
                .setTopRightCorner(CornerFamily.ROUNDED, radius)
                .setTopLeftCorner(CornerFamily.ROUNDED, radius)
                .build()
        }

    }

    @SuppressLint("SetTextI18n")
    private fun handleAthleteText(athlete: Athlete?) {
        binding.apply {
            detailTitle.text = "${athlete?.name} ${athlete?.surname} details"
            detailAthleteName.text = "Name : ${athlete?.name} ${athlete?.surname}"
            detailAthleteBirth.text = "DOB : ${athlete?.dateOfBirth}"
            detailAthleteHeight.text = "Height : ${athlete?.height}"
            detailAthleteWeight.text = "Weight : ${athlete?.weight}"
            detailAthleteBio.apply {
                text = athlete?.bio
                LinkUtil.autoLink(this)
            }
        }

    }

    private fun buildPlayer() {
        player.apply {
            initPLayer(viewModel?.mDataManager?.context!!)
            setMediaSource(DUMMY_HLS_LINK)
            addListenerToPlayer(
                onErrorListener = {
                    //do something on error
                },
                onPlayerStart = {
                    //do on start
                },
                onPlayerEnded = {
                    // do on end
                })
            setPlayerFromView(binding.videoPlayerView)
        }

    }


    private fun initUI() {
        viewModel = mViewModelFactoryActivity.get(this, FragmentAthleteDetailViewModel::class)

        arguments?.getString(EXTRA_ATHLETE_ID)?.let {
            viewModel?.getDetailOfAthlete(it.toInt())

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player.stopPlayer()
        player.releasePlayer()
    }


    companion object {
        private const val EXTRA_ATHLETE_ID = "EXTRA_ATHLETE_ID"
        private const val DUMMY_HLS_LINK =
            "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8" // i didnt have link for youtube

        fun getInstance(
            athlete_ID: String?
        ): FragmentAthleteDetail {
            return FragmentAthleteDetail().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_ATHLETE_ID, athlete_ID ?: "0")
                }
            }
        }
    }
}