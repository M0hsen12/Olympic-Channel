package com.codeChallenge.olympicChannel.util

import android.app.Dialog
import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.codeChallenge.olympicChannel.R
import com.codeChallenge.olympicChannel.databinding.DialogSimpleProgressBinding
import com.codeChallenge.olympicChannel.di.data.database.entity.GamesEntity
import com.codeChallenge.olympicChannel.util.NetworkErrors.NETWORK_ERROR_TIMEOUT
import com.codeChallenge.olympicChannel.util.NetworkErrors.NETWORK_ERROR_UNKNOWN
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import retrofit2.HttpException
import java.io.IOException
import java.util.regex.Pattern


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


/**
 * Reference: https://medium.com/@douglas.iacovelli/how-to-handle-errors-with-retrofit-and-coroutines-33e7492a912
 */

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T?
): ApiResult<T?> {
    val TAG = "BBB"
    return withContext(dispatcher) {
        try {
            // throws TimeoutCancellationException
            withTimeout(6000L) {
                ApiResult.Success(apiCall.invoke())
            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            when (throwable) {
                is TimeoutCancellationException -> {
                    val code = 408 // timeout error code
                    ApiResult.GenericError(code, NETWORK_ERROR_TIMEOUT)
                }
                is IOException -> {
                    ApiResult.NetworkError
                }
                is HttpException -> {
                    val code = throwable.code()
                    val errorResponse = convertErrorBody(throwable)
                    Log.e(TAG, "safeApiCall: ")
//                    cLog(errorResponse)
                    ApiResult.GenericError(
                        code,
                        errorResponse
                    )
                }
                else -> {
                    Log.e(TAG, "safeApiCall error:$NETWORK_ERROR_UNKNOWN ")
//                    cLog(NETWORK_ERROR_UNKNOWN)
                    ApiResult.GenericError(
                        null,
                        NETWORK_ERROR_UNKNOWN
                    )
                }
            }
        }
    }
}

private fun convertErrorBody(throwable: HttpException): String? {
    return try {
        throwable.response()?.errorBody()?.string()
    } catch (exception: Exception) {
        "ERROR_UNKNOWN $exception"
    }
}
//suspend fun <T> safeCacheCall(
//    dispatcher: CoroutineDispatcher,
//    cacheCall: suspend () -> T?
//): CacheResult<T?> {
//    return withContext(dispatcher) {
//        try {
//            // throws TimeoutCancellationException
//            withTimeout(CACHE_TIMEOUT){
//                CacheResult.Success(cacheCall.invoke())
//            }
//        } catch (throwable: Throwable) {
//            throwable.printStackTrace()
//            when (throwable) {
//
//                is TimeoutCancellationException -> {
//                    CacheResult.GenericError(CACHE_ERROR_TIMEOUT)
//                }
//                else -> {
//                    cLog(CACHE_ERROR_UNKNOWN)
//                    CacheResult.GenericError(CACHE_ERROR_UNKNOWN)
//                }
//            }
//        }
//    }
//}




