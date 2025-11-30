package com.raj.mygrowth

import android.graphics.Bitmap
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

        val webView = binding.webView
        val progressBar = binding.progressBar

        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.settings.builtInZoomControls = true
        webView.settings.displayZoomControls = false

        // Full Drive URL
        val driveUrl = "https://drive.google.com/file/d/1f5eNFJwwzQyPgjVcjX3UpSlhrdzfU3ba/view?usp=sharing"

        // Extract only file id
        val fileId = driveUrl.substringAfter("/d/").substringBefore("/")

        // Direct downloadable url
        val fileUrl = fileId

        // Detect type from link
        val urlLower = driveUrl.lowercase()
        val finalUrl = if (urlLower.endsWith(".pdf")) {
            // PDF → open directly in WebView
            fileUrl
        } else {
            // DOC, DOCX → open using Google Docs Viewer
            "https://docs.google.com/gview?embedded=true&url=$fileUrl"
        }

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                progressBar.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                progressBar.visibility = View.GONE
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                Toast.makeText(this@ActivityWebView, "Unable to load document", Toast.LENGTH_SHORT).show()
            }
        }

        webView.webChromeClient = WebChromeClient()
        webView.loadUrl(finalUrl)
    }

    override fun onBackPressed() {
        if (binding.webView.canGoBack()) binding.webView.goBack()
        else super.onBackPressed()
    }

}