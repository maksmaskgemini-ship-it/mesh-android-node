package com.grubiyan.meshnode

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private val PWA_URL = "http://100.73.128.2:8000/app/"

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webView = WebView(this)
        setContentView(webView)
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.allowFileAccess = true
        webView.webViewClient = WebViewClient()
        webView.addJavascriptInterface(Bridge(), "AndroidBridge")
        webView.loadUrl(PWA_URL)
        ContextCompat.startForegroundService(this, Intent(this, MeshForegroundService::class.java))
    }

    inner class Bridge {
        @JavascriptInterface
        fun saveTrainingData(json: String) {
            File(filesDir, "training_data.jsonl").appendText(json + "\n")
        }
        @JavascriptInterface
        fun startTraining() {
            TermuxBridge.runCommand(this@MainActivity,
                "python3 /data/data/com.grubiyan.meshnode/files/train.py")
        }
        @JavascriptInterface
        fun getPwaUrl(): String = PWA_URL
    }
}
