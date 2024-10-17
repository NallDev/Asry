package com.nalldev.asry.util

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.util.PatternsCompat
import androidx.core.widget.doOnTextChanged
import com.nalldev.asry.R

class EmailEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    init {
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

        hint = context.getString(R.string.email_hint)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START

        doOnTextChanged { text, _, _, _ ->
            error = if (!isValidEmail(text.toString())) {
                context.getString(R.string.invalid_email_msg)
            } else {
                null
            }
        }
    }

    private fun isValidEmail(email: CharSequence): Boolean {
        return PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
    }
}