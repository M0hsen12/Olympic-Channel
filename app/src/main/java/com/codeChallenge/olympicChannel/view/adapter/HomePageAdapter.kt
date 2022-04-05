package com.codeChallenge.olympicChannel.view.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.*
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import com.codeChallenge.olympicChannel.R
import com.codeChallenge.olympicChannel.di.data.database.entity.GamesEntity
import com.codeChallenge.olympicChannel.model.Athlete
import com.codeChallenge.olympicChannel.util.EndlessRecyclerOnScrollListener
import com.codeChallenge.olympicChannel.view.adapter.HomePageAdapter.Companion.NORMAL_LIST_VIEW_TYPE
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.item_home_games.view.*
import kotlin.math.ceil


class HomePageAdapter(
    private val onItemsClicked: (game: Athlete,imageView:ImageView) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<GamesEntity>() {
        override fun areItemsTheSame(oldItem: GamesEntity, newItem: GamesEntity): Boolean {
            return newItem.gameId == oldItem.gameId
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: GamesEntity, newItem: GamesEntity): Boolean {
            return newItem == oldItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    fun submitList(list: List<GamesEntity>) {
        differ.submitList(list)
    }

    override fun getItemViewType(position: Int): Int {
        return if (differ.currentList[position].athletes.isEmpty()) EMPTY_LIST_VIEW_TYPE else NORMAL_LIST_VIEW_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            EMPTY_LIST_VIEW_TYPE -> {
                HomeEmptyViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_home_games_empty_list,
                        parent,
                        false
                    )
                )
            }
            else -> {
                HomeBasicViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_home_games,
                        parent,
                        false
                    ),
                    onItemsClicked
                )
            }
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HomeBasicViewHolder -> {
                holder.bind(differ.currentList[position])
            }
            is HomeEmptyViewHolder -> {
                holder.bind(differ.currentList[position])
            }


        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    class HomeBasicViewHolder(
        itemView: View,
        private val onItemsClicked: (game: Athlete,imageView:ImageView) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: GamesEntity) = with(itemView) {
            itemView.item_home_title.text = "${item.city}  ${item.year}"
            itemView.item_home_rv.apply {
                layoutManager = LinearLayoutManager(this.context, HORIZONTAL, false)
                val myAdapter = AthleteAdapter { athlete,imgView->
                    onItemsClicked.invoke(athlete,imgView)
                }
                adapter = myAdapter

                val list = orderListByGlobalPoint(item.athletes, item.year)
                myAdapter.submitList(list)

            }
        }

        private fun orderListByGlobalPoint(athletes: List<Athlete>, currentYear: Int) =
            athletes.sortedByDescending { ath ->
                ath.score?.find { it.year == currentYear }?.getGlobalScore()?:0
            }


    }

    class HomeEmptyViewHolder(
        itemView: View,
    ) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            item: GamesEntity
        ) = with(itemView) {
            itemView.item_home_title.text = "${item.city}  ${item.year}"
        }

    }

    companion object {

        const val EMPTY_LIST_VIEW_TYPE = 0
        const val NORMAL_LIST_VIEW_TYPE = 1
    }
}


