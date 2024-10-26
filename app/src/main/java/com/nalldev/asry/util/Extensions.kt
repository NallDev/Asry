package com.nalldev.asry.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.exifinterface.media.ExifInterface
import java.io.File
import java.util.Date

fun Activity.hideKeyboard() {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = currentFocus ?: View(this)
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Activity.clearAllFocus() {
    val view = currentFocus
    view?.clearFocus()
}

fun Context.getPackageInfo(): PackageInfo {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
    } else {
        packageManager.getPackageInfo(packageName, 0)
    }
}

fun Context.createTempFile(): File {
    return File.createTempFile(Date().toString(), ".jpg", this.applicationContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES))
}

fun Bitmap.getRotatedBitmap(file: File): Bitmap {
    val orientation = ExifInterface(file).getAttributeInt(
        ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED
    )

    return when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> CommonHelper.rotateImage(this, 90F)
        ExifInterface.ORIENTATION_ROTATE_180 -> CommonHelper.rotateImage(this, 180F)
        ExifInterface.ORIENTATION_ROTATE_270 -> CommonHelper.rotateImage(this, 270F)
        ExifInterface.ORIENTATION_NORMAL -> this
        else -> this
    }
}

fun Context.addPersistenceUri(uri : Uri, callback : ((Uri) -> Unit)? = null) {
    val addFlag = Intent.FLAG_GRANT_READ_URI_PERMISSION
    contentResolver.takePersistableUriPermission(uri, addFlag)
    callback?.invoke(uri)
}

fun Context.showToast(message : String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}