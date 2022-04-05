package com.codeChallenge.olympicChannel.util

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.Window
import androidx.databinding.DataBindingUtil
import com.codeChallenge.olympicChannel.R
import com.codeChallenge.olympicChannel.databinding.DialogSimpleProgressBinding
import com.codeChallenge.olympicChannel.di.data.database.entity.GamesEntity



fun materialSimpleProgressDialog(
    context: Context,
    title: String = ""
): Dialog {
    return Dialog(context, R.style.ThemeDialog_Dark).apply {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)
        val binder = DataBindingUtil.inflate<DialogSimpleProgressBinding>(
            LayoutInflater.from(context),
            R.layout.dialog_simple_progress,
            null,
            false
        )
        binder.title.text = title
        setContentView(binder.root)
    }
}

fun getListForPagination(
    sortedlist: List<GamesEntity>,
    currentPage: Int
): List<GamesEntity> {
    val returnList = ArrayList<GamesEntity>()

    when (currentPage) {
        0 ->
            returnList.addAll(sortedlist.slice(0..5))

        1 ->
            returnList.addAll(sortedlist.slice(6..11))

        2 ->
            returnList.addAll(sortedlist.slice(11..sortedlist.size.minus(1)))

    }

    return returnList
}






