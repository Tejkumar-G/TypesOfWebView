package com.net.typesofwebview

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import com.net.typesofwebview.Utils.Companion.checkAndUpdateURL
import com.net.typesofwebview.Utils.Companion.hideKeyboard
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), FragmentManager.OnBackStackChangedListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set click listeners on buttons
        toOpenWebView.setOnClickListener {
            val url = urlText.editText?.text.toString()
            if (!isValidUrl(url)) {
                Utils.showDialog(this, "Error!", if (url.isNotEmpty()) "$url is an invalid URL" else "URL should be added")
                return@setOnClickListener
            }
            hideKeyboard(this)
            openFragment(bundleOf(Utils.URL to url, Utils.WEB_VIEW_TYPE to Utils.DEFULT_WEB_VIEW))
        }

        toOpenCustomWebView.setOnClickListener {
            val url = urlText.editText?.text.toString()
            if (!isValidUrl(url)) {
                Utils.showDialog(this, "Error!", if (url.isNotEmpty()) "$url is an invalid URL" else "URL should be added")
                return@setOnClickListener
            }
            hideKeyboard(this)
            openFragment(bundleOf(Utils.URL to url, Utils.WEB_VIEW_TYPE to Utils.CUSTOM_WEB_VIEW))
        }

        toOpenBrowser.setOnClickListener {
            val url = urlText.editText?.text.toString()
            if (!isValidUrl(url)) {
                Utils.showDialog(this, "Error!", if (url.isNotEmpty()) "$url is an invalid URL" else "URL should be added")
                return@setOnClickListener
            }
            hideKeyboard(this)
            openInBrowser(url)
        }

        // Set the listener to the FragmentManager
        supportFragmentManager.addOnBackStackChangedListener(this)


    }

    override fun onResume() {
        super.onResume()
        // Hide the fragment container and show the content screen
        closeFragmentContainerAndAddContentScreen()
    }

    /**
     * Checks if the URL is valid.
     */
    private fun isValidUrl(url: String): Boolean {
        val pattern = Patterns.WEB_URL
        val matcher = pattern.matcher(url)
        return matcher.matches()
    }

    /**
     * Opens the URL in a browser.
     */
    private fun openInBrowser(url: String) {
        // Check if Chrome is installed
        val packageName = "com.android.chrome"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url.checkAndUpdateURL()))
        intent.setPackage(packageName)
        val isChromeInstalled = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null


        // If Chrome is installed, open the URL in Chrome
        if (isChromeInstalled) {
            intent.setPackage(packageName)
            startActivity(intent)
        } else {
            // Otherwise, open the URL in any available browser
            intent.setPackage(null)
            startActivity(Intent.createChooser(intent, "Select Browser"))
        }
    }


    /**
     * Opens a WebViewHandlerFragment to handle the URL.
     */
    private fun openFragment(bundle: Bundle) {

        val fragment = WebViewHandlerFragment()
        fragment.arguments = bundle
        fragmentContainer.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .add(R.id.fragmentContainer, fragment)
            .commit()
        contentScreen.visibility = View.GONE
    }
    override fun onBackStackChanged() {
        val backStackEntryCount = supportFragmentManager.backStackEntryCount
        if (backStackEntryCount == 0) {
            closeFragmentContainerAndAddContentScreen()
        }
    }

    private fun closeFragmentContainerAndAddContentScreen() {
        fragmentContainer.visibility = View.GONE
        contentScreen.visibility = View.VISIBLE
    }

}