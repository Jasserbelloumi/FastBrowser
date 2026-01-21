package com.jasser.browser;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.ByteArrayInputStream;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private EditText urlInput;
    private boolean isBlockRedirectEnabled = false;
    private boolean isAdBlockEnabled = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webview);
        urlInput = findViewById(R.id.url_input);
        Button goButton = findViewById(R.id.go_button);

        setupWebView();

        goButton.setOnClickListener(v -> {
            String url = urlInput.getText().toString();
            if (!url.startsWith("http")) url = "https://" + url;
            webView.loadUrl(url);
        });
    }

    private void setupWebView() {
        webView.getSettings().setJavaScriptEnabled(true);
        // توفير الموارد: منع الصور من التحميل التلقائي إذا كان الموقع ثقيلاً (اختياري)
        webView.getSettings().setLoadsImagesAutomatically(true);
        
        webView.setWebViewClient(new WebViewClient() {
            // ميزة منع الإعلانات
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                if (isAdBlockEnabled) {
                    String url = request.getUrl().toString();
                    if (url.contains("googleads") || url.contains("doubleclick") || url.contains("adservice")) {
                        return new WebResourceResponse("text/plain", "utf-8", new ByteArrayInputStream("".getBytes()));
                    }
                }
                return super.shouldInterceptRequest(view, request);
            }

            // ميزة منع إعادة التوجيه
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (isBlockRedirectEnabled && !url.contains(view.getUrl().split("/")[2])) {
                    Toast.makeText(MainActivity.this, "تم منع إعادة توجيه مشبوهة", Toast.LENGTH_SHORT).show();
                    return true; 
                }
                return false;
            }
        });
        
        webView.loadUrl("https://www.google.com");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.browser_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_block_redirect) {
            isBlockRedirectEnabled = !item.isChecked();
            item.setChecked(isBlockRedirectEnabled);
            return true;
        } else if (id == R.id.action_adblock) {
            isAdBlockEnabled = !item.isChecked();
            item.setChecked(isAdBlockEnabled);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
