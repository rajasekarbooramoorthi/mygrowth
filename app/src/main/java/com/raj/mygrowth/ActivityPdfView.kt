package com.raj.mygrowth

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.raj.mygrowth.databinding.ActivityPdfViewBinding

class ActivityPdfView : AppCompatActivity() {

    private lateinit var binding: ActivityPdfViewBinding
    private val fileViewer = "file:///android_asset/pdfjs/viewer.html?file="

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pdfUrl = intent?.getStringExtra("FILE_URL") ?: ""
        binding = ActivityPdfViewBinding.inflate(layoutInflater)
        setToolbarInsetsFullscreen()
        setContentView(binding.root)
        setupWebView(pdfUrl)
    }

    @SuppressLint("AddJavascriptInterface")
    private fun setupWebView(url: String) = with(binding.webView) {
        settings.configurePdfWebView()
        webChromeClient = WebChromeClient()
        webViewClient = WebViewClient()
        addJavascriptInterface(WebAppInterface(url), "Android")

        // HIGH QUALITY PDF
        val finalUrl = "${fileViewer}url=$url#zoom=page-width&dpi=200&disableStream=true"
        binding.progressBar.visibility = View.GONE
        loadUrl(finalUrl)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun WebSettings.configurePdfWebView() {
        javaScriptEnabled = true
        domStorageEnabled = true
        allowFileAccess = true
        allowFileAccessFromFileURLs = true
        allowUniversalAccessFromFileURLs = true
        mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        builtInZoomControls = true
        displayZoomControls = false
        loadWithOverviewMode = true
    }

    inner class WebAppInterface(private val pdfPath: String) {
        @JavascriptInterface
        fun getPdfUrl(): String = pdfPath
    }
}
