package com.codeChallenge.olympicChannel.view.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.codeChallenge.olympicChannel.BuildConfig
import com.codeChallenge.olympicChannel.R
import com.codeChallenge.olympicChannel.databinding.FragmentAthleteDetailBinding
import com.codeChallenge.olympicChannel.di.viewModelsInjections.InjectionViewModelProvider
import com.codeChallenge.olympicChannel.model.Athlete
import com.codeChallenge.olympicChannel.model.AthleteScore
import com.codeChallenge.olympicChannel.util.LinkUtil
import com.codeChallenge.olympicChannel.util.materialSimpleProgressDialog
import com.codeChallenge.olympicChannel.view.activities.main.MainActivity
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

    @SuppressLint("SetTextI18n")
    private fun setupUI(pair: Pair<Athlete?, List<AthleteScore>>) {
        binding.apply {
            progressDialog.dismiss()
            detailGroup.visibility = View.VISIBLE
            detailTitle.text = "${pair.first?.name} ${pair.first?.surname} details."
            detailAthleteName.text = "Name : ${pair.first?.name} ${pair.first?.surname}"
            detailAthleteBirth.text = "DOB : ${pair.first?.dateOfBirth}"
            detailAthleteHeight.text = "Height : ${pair.first?.height}"
            detailAthleteWeight.text = "Weight : ${pair.first?.weight}"
            detailAthleteBio.apply {
                text = pair.first?.bio
                LinkUtil.autoLink(this)
            }

            detailAthletePic.apply {
                val radius = resources.getDimension(R.dimen.default_corner_radius)
                Glide.with(this)
                    .load("${BuildConfig.baseUrl}/athletes/${pair.first?.athleteId}/photo")
                    .into(this)
                this.shapeAppearanceModel = shapeAppearanceModel
                    .toBuilder()
                    .setTopRightCorner(CornerFamily.ROUNDED, radius)
                    .setTopLeftCorner(CornerFamily.ROUNDED, radius)
                    .build()
            }

            detailAthleteRv.apply {
                layoutManager = LinearLayoutManager(requireContext())
                val myAdapter = AthleteScoreAdapter()
                adapter = myAdapter
                myAdapter.submitList(pair.second)
            }

            detailBack.setOnClickListener {
                activity?.supportFragmentManager?.popBackStackImmediate()

            }

        }
    }

    private fun initUI() {
        viewModel = mViewModelFactoryActivity.get(this, FragmentAthleteDetailViewModel::class)

        arguments?.getString(EXTRA_ATHLETE_ID)?.let {
            viewModel?.getDetailOfAthlete(it.toInt())
        }
    }


    companion object {
        private const val EXTRA_ATHLETE_ID = "EXTRA_ATHLETE_ID"

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