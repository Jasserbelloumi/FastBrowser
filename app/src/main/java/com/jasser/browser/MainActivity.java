package com.jasser.browser;

import android.os.Bundle;
import android.webkit.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    WebView webView;
    boolean blockRedirect = true; // ميزة منع إعادة التوجيه

    @Override
    protected void Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webview);
        setupBrowser();
    }

    private void setupBrowser() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        
        // تقليل استهلاك الموارد (Resource Optimization)
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                // منع إعادة التوجيه إذا كانت الميزة مفعلة
                return blockRedirect; 
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                // منطق بسيط لمنع الإعلانات (Ad-blocking)
                if (request.getUrl().getHost().contains("ads") || request.getUrl().getHost().contains("doubleclick")) {
                    return new WebResourceResponse("text/plain", "utf-8", null);
                }
                return super.shouldInterceptRequest(view, request);
            }
        });
    }
}
