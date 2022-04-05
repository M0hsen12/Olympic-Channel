package com.codeChallenge.olympicChannel.view.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.codeChallenge.olympicChannel.R
import com.codeChallenge.olympicChannel.model.AthleteScore
import kotlinx.android.synthetic.main.item_athlete_score.view.*

class AthleteScoreAdapter() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AthleteScore>() {

        override fun areItemsTheSame(oldItem: AthleteScore, newItem: AthleteScore): Boolean {
            return oldItem.year == newItem.year
        }

        override fun areContentsTheSame(oldItem: AthleteScore, newItem: AthleteScore): Boolean {
            return oldItem == oldItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ViewHolderClass(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_athlete_score,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolderClass -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<AthleteScore>) {
        differ.submitList(list)
    }

    class ViewHolderClass
    constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: AthleteScore) = with(itemView) {

            itemView.item_score_city.text = item.city

            item.gold?.takeIf { it > 0 }?.let { score ->
                itemView.item_score_gold.apply {
                    visibility = View.VISIBLE
                    text = score.toString()
                }
            }
            item.silver?.takeIf { it > 0 }?.let { score ->
                itemView.item_score_silver.apply {
                    visibility = View.VISIBLE
                    text = score.toString()
                }
            }
            item.bronze?.takeIf { it > 0 }?.let { score ->
                itemView.item_score_bronze.apply {
                    visibility = View.VISIBLE
                    text = score.toString()
                }
            }

        }


    }
}



