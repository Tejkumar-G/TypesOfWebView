package com.net.typesofwebview

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager


class Utils {
    companion object {
        const val URL = "url"
        const val WEB_VIEW_TYPE = "web view type"
        const val DEFULT_WEB_VIEW = "default web view"
        const val CUSTOM_WEB_VIEW = "custom web view"

        /**
         * Show the default android dialog box.
         */
        fun showDialog(context: Context, title: String, message: String) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(title)
            builder.setMessage(message)
            builder.setPositiveButton("OK") { _, _ ->
                // Handle button click
            }
            builder.show()
        }

        /**
         * Checks and updates the url with http.
         */
        fun String.checkAndUpdateURL(): String {
            if (!this.startsWith("http://") && !this.startsWith("https://")) {
                return "http://$this"
            }
            return this
        }


        /**
         * Closes the key board.
         */
        fun hideKeyboard(activity: Activity) {
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            //Find the currently focused view, so we can grab the correct window token from it.
            var view = activity.currentFocus
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

    }
}