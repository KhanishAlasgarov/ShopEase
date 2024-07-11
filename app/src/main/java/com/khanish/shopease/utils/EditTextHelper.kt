package com.khanish.shopease.utils

import android.content.Context
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.khanish.shopease.R

class EditTextHelper(
    private val context: Context,
    private val editTexts: List<EditText>,
    private val button: MaterialButton
) {

    init {
        setupEditTextsListeners()
        buttonState(button, false)
    }

    private fun setupEditTextsListeners() {
        editTexts.forEach { editText ->
            setupEditText(editText)
        }
    }

    private fun setupEditText(editText: EditText) {
        editText.addTextChangedListener {
            updateEditTextState(editText)
            updateButtonState()
        }

        editText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                updateEditTextState(editText)
                updateButtonState()
            }
        }
    }

    private fun updateEditTextState(editText: EditText) {
        val container = editText.parent.parent as? TextInputLayout
        val inputValue = editText.text.toString().trim()
        if (inputValue.isBlank()) {
            container?.apply {
                isErrorEnabled = true
                error = "Cannot be empty"
            }
        } else {
            container?.apply {
                isErrorEnabled = false
                error = null
            }
        }
    }

    private fun updateButtonState() {
        val isEnabled = editTexts.none { isEditTextEmpty(it) }
        buttonState(button, isEnabled)
    }

    private fun buttonState(button: MaterialButton, isEnabled: Boolean) {
        val backgroundColor = ContextCompat.getColor(
            context,
            if (isEnabled) R.color.darkGrey_1A else R.color.light_grey_CC
        )
        button.setBackgroundColor(backgroundColor)
    }

    private fun isEditTextEmpty(editText: EditText): Boolean {
        val inputValue = editText.text.toString().trim()
        return inputValue.isBlank()
    }
}