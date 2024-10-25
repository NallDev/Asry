package com.nalldev.asry.util

import android.app.UiModeManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Build
import android.widget.EditText
import androidx.appcompat.app.AppCompatDelegate
import com.nalldev.asry.R
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

object CommonHelper {
    fun clearEditTexts(editTexts: List<EditText>) {
        editTexts.forEach {
            it.text.clear()
        }
    }

    fun getErrorMessage(throwable: Throwable, context: Context): String {
        return when (throwable) {
            is IOException -> context.getString(R.string.no_internet_connection)
            is HttpException -> {
                val errorMessage = parseErrorBody(throwable)
                if (!errorMessage.isNullOrBlank()) {
                    errorMessage
                } else {
                    RemoteHelper.remoteErrorMessage(context, throwable.code())
                }
            }
            else -> context.getString(R.string.error_code_default)
        }
    }

    private fun parseErrorBody(httpException: HttpException): String? {
        return try {
            val errorJsonString = httpException.response()?.errorBody()?.string()?.let {
                JSONObject(it)
            }

            errorJsonString?.getString("message")
        } catch (_: Exception) {
            null
        }
    }

    fun useLightTheme(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val uiModeManager =
                context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
            uiModeManager.setApplicationNightMode(
                UiModeManager.MODE_NIGHT_NO
            )
        } else {
            AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }

    fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height, matrix, true
        )
    }
}