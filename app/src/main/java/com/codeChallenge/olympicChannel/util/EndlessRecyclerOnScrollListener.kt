package com.codeChallenge.olympicChannel.util


import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class EndlessRecyclerOnScrollListener : RecyclerView.OnScrollListener (){
    private var loading = true
    private var previousItemCount = 0

    var currentPage = 0
    var totalPages: Long = 0


    fun refresh() {
        currentPage = 0
        totalPages = 0
        previousItemCount = 0
        loading = true
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val layoutManager = when {
            (recyclerView.layoutManager is LinearLayoutManager) -> recyclerView.layoutManager as LinearLayoutManager
            (recyclerView.layoutManager is GridLayoutManager) -> recyclerView.layoutManager as GridLayoutManager
            else -> throw Exception("unknown layout manager")
        }
        val lastPosition = layoutManager.findLastVisibleItemPosition()
        if (lastPosition == recyclerView.adapter!!.itemCount - 1 && !loading) {
            if (currentPage < totalPages){
                currentPage++
                onLoadMore(currentPage)
                loading = true
            }
        }
        if (recyclerView.adapter!!.itemCount > previousItemCount){
            previousItemCount = recyclerView.adapter!!.itemCount
            loading = false

        }
    }

    abstract fun onLoadMore(currentPage: Int)

}

