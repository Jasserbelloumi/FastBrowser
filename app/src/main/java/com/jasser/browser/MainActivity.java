package com.jasser.browser;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private EditText urlInput;
    private ToggleButton redirectToggle;
    private boolean isRedirectBlocked = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webview);
        urlInput = findViewById(R.id.url_input);
        redirectToggle = findViewById(R.id.toggle_redirect);

        setupBrowser();

        // التعامل مع إدخال الرابط عند الضغط على Enter
        urlInput.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                String url = urlInput.getText().toString();
                if (!url.startsWith("http")) url = "https://" + url;
                webView.loadUrl(url);
                return true;
            }
            return false;
        });

        // التحكم بميزة منع إعادة التوجيه
        redirectToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isRedirectBlocked = isChecked;
            Toast.makeText(this, "Redirect Block: " + (isChecked ? "ON" : "OFF"), Toast.LENGTH_SHORT).show();
        });
    }

    private void setupBrowser() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        
        // تحسين السرعة وتقليل استهلاك المعالج
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                // إذا كانت الميزة مفعلة، نمنع التوجيه التلقائي للمواقع الخارجية
                return isRedirectBlocked;
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                // نظام Ad-block (Brave-like) لمزيد من السرعة وحماية الموارد
                String url = request.getUrl().toString();
                if (url.contains("ads") || url.contains("analytics") || url.contains("doubleclick") || url.contains("googleadservices")) {
                    return new WebResourceResponse("text/plain", "utf-8", null);
                }
                return super.shouldInterceptRequest(view, request);
            }
        });
    }
}
