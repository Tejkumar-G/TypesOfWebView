package com.net.typesofwebview

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.net.typesofwebview.Utils.Companion.checkAndUpdateURL
import com.net.typesofwebview.Utils.Companion.showDialog
import kotlinx.android.synthetic.main.fragment_custom_view.*
import kotlinx.android.synthetic.main.fragment_webview.*


class WebViewHandlerFragment : Fragment() {
    // A string to hold the type of WebView to use
    lateinit var type: String

    // Override the onCreateView() method to inflate the WebView layout
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Get type from arguments
        type = getFromArguments(Utils.WEB_VIEW_TYPE)

        // Inflate the layout for the WebView if type is Utils.DEFULT_WEB_VIEW, otherwise inflate the layout for the custom view
        return if (type == Utils.DEFULT_WEB_VIEW) {
            inflater.inflate(R.layout.fragment_webview, container, false)
        } else {
            inflater.inflate(R.layout.fragment_custom_view, container, false)
        }
    }

    // Override the onViewCreated() method to initialize the WebView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get the URL from arguments
        var url = getFromArguments(Utils.URL)

        // Add "https://" to the URL if it doesn't start with "http://" or "https://"
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://$url"
        }

        // Load the URL in the WebView and configure its settings
        if (type == Utils.DEFULT_WEB_VIEW) {
            webView.loadUrl(url)
        } else {
            customView.loadUrl(url)
            initiateCustomWebView()
        }
    }

    // Initialize the custom WebView with JavaScript and zooming enabled
    @SuppressLint("SetJavaScriptEnabled")
    private fun initiateCustomWebView() {
        progressBarCyclic.visibility = View.VISIBLE
        customView.settings.apply {
            // Enable JavaScript
            this.javaScriptEnabled = true

            // Enable zooming
            this.setSupportZoom(true)
            this.builtInZoomControls = true
            this.displayZoomControls = false
        }

        // Set a custom WebViewClient to handle page loading and other events
        val webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                // Show the progress bar when a page starts loading
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // Hide the progress bar when a page finishes loading
                progressBarCyclic.visibility = View.GONE
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                // Show a dialog if there is an error loading a page
                context?.let { showDialog(it, "Got an error!", "URL not found.") }
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val url = request?.url.toString().checkAndUpdateURL()
                // Let WebView handle the URL if it starts with "http://" or "https://"
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    return false
                }
                // If the URL doesn't start with "http://" or "https://", do not load it
                return true
            }
        }

        customView.webViewClient = webViewClient
    }



    /**
     * Returns the value of the key from the arguments.
     * @param key - a string value.
     */
    private fun getFromArguments(key: String): String {
        return arguments?.getString(key) ?: ""
    }
}