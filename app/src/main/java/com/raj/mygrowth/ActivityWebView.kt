package com.raj.mygrowth

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.raj.mygrowth.databinding.ActivityWebViewBinding

class ActivityWebView : AppCompatActivity() {

    private lateinit var binding: ActivityWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get URL from Intent or fallback to default
        val fileUrl = intent?.getStringExtra("FILE_URL")
            ?: ""

        // Encode URL for Google Docs viewer
        val viewerUrl = "https://docs.google.com/gview?embedded=true&url=${Uri.encode(fileUrl)}"

        setupWebView(viewerUrl)
    }

    private fun setupWebView(url: String) {
        with(binding.webView) {
            settings.apply {
                javaScriptEnabled = true
                loadWithOverviewMode = true
                useWideViewPort = true
                builtInZoomControls = true
                displayZoomControls = false
            }

            webChromeClient = WebChromeClient()
            webViewClient = object : WebViewClient() {

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    binding.progressBar.visibility = View.VISIBLE
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    binding.progressBar.visibility = View.GONE
                }

                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        this@ActivityWebView,
                        "Unable to load document: ${error?.description}",
                        Toast.LENGTH_SHORT
                    ).show()
                    println("WebView Error --> $error")
                }
            }

            loadUrl(url)
        }
    }

    override fun onBackPressed() {
        if (binding.webView.canGoBack()) binding.webView.goBack()
        else super.onBackPressed()
    }
}