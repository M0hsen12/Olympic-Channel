package com.codeChallenge.olympicChannel.view.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codeChallenge.olympicChannel.BuildConfig
import com.codeChallenge.olympicChannel.R
import com.codeChallenge.olympicChannel.model.Athlete
import kotlinx.android.synthetic.main.item_home_athlete.view.*


class AthleteAdapter(private val onItemsClicked: ((game: Athlete,imageView: ImageView) -> Unit)? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Athlete>() {

        override fun areItemsTheSame(oldItem: Athlete, newItem: Athlete): Boolean {
            return oldItem.athleteId == newItem.athleteId
        }

        override fun areContentsTheSame(oldItem: Athlete, newItem: Athlete): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ViewHolderClass(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_home_athlete,
                parent,
                false
            ),
            onItemsClicked
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolderClass -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Athlete>) {
        differ.submitList(list)
    }

    class ViewHolderClass
    constructor(
        itemView: View,
        private val onItemsClicked: ((game: Athlete,imageView:ImageView) -> Unit)? = null
    ) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("CheckResult")
        fun bind(item: Athlete) = with(itemView) {
            itemView.item_athlete_pic.apply {
                setOnClickListener {
                    onItemsClicked?.invoke(item,this)
                }
                Glide.with(this.context)
                    .load("${BuildConfig.baseUrl}/athletes/${item.athleteId}/photo")
                    .into(this)
            }



            itemView.item_athlete_name.text = "${item.name} ${item.surname}"



        }
    }


}

