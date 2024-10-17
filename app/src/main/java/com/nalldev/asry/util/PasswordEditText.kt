package com.nalldev.asry.util

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doOnTextChanged
import com.nalldev.asry.R

class PasswordEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    init {
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        hint = context.getString(R.string.password_hint)

        doOnTextChanged { text, _, _, _ ->
            error = if (text.toString().length < 8) {
                context.getString(R.string.min_character_msg)
            } else {
                null
            }
        }
    }
}