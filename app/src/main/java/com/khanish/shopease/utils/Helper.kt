package com.khanish.shopease.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.khanish.shopease.databinding.LoadingBinding

class Helper {

    companion object {
        fun setSignUpDialog(
            layoutInflater: LayoutInflater,
            requireContext: Context
        ): AlertDialog {
            val dialogBinding = LoadingBinding.inflate(layoutInflater)
            val dialog = AlertDialog.Builder(requireContext).create();
            dialog.setView(dialogBinding.root)
            dialog.setCancelable(false)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            return dialog

        }

    }
}