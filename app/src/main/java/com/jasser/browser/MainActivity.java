package com.jasser.browser;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private EditText urlInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webview);
        urlInput = findViewById(R.id.url_input);
        Button goButton = findViewById(R.id.go_button);
        Button backBtn = findViewById(R.id.back_btn);
        Button forwardBtn = findViewById(R.id.forward_btn);
        Button refreshBtn = findViewById(R.id.refresh_btn);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.google.com");

        goButton.setOnClickListener(v -> {
            String url = urlInput.getText().toString();
            if (!url.startsWith("http")) url = "https://" + url;
            webView.loadUrl(url);
        });

        backBtn.setOnClickListener(v -> { if(webView.canGoBack()) webView.goBack(); });
        forwardBtn.setOnClickListener(v -> { if(webView.canGoForward()) webView.goForward(); });
        refreshBtn.setOnClickListener(v -> webView.reload());
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) { webView.goBack(); } 
        else { super.onBackPressed(); }
    }
}
