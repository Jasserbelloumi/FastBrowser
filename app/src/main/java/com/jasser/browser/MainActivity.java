package com.jasser.browser;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // إنشاء المتصفح مباشرة
        WebView webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        
        // تعيين المتصفح كواجهة أساسية
        setContentView(webView);
        
        // تحميل الموقع
        webView.loadUrl("https://www.google.com");
    }
}
