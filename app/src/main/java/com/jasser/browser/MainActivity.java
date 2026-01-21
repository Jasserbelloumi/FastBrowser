package com.jasser.browser;

import android.app.DownloadManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.webkit.WebSettingsCompat;
import androidx.webkit.WebViewFeature;
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
        
        setupWebView();
        setupControls();
    }

    private void setupWebView() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        
        webView.setWebViewClient(new WebViewClient() {
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

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (isBlockRedirectEnabled && !url.contains(view.getUrl().split("/")[2])) {
                    Toast.makeText(MainActivity.this, "تم منع إعادة توجيه", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });

        webView.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setMimeType(mimetype);
            request.addRequestHeader("cookie", CookieManager.getInstance().getCookie(url));
            request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimetype));
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimetype));
            ((DownloadManager) getSystemService(DOWNLOAD_SERVICE)).enqueue(request);
            Toast.makeText(this, "بدء التحميل...", Toast.LENGTH_SHORT).show();
        });

        webView.loadUrl("https://www.google.com");
    }

    private void setupControls() {
        findViewById(R.id.go_button).setOnClickListener(v -> {
            String url = urlInput.getText().toString();
            if (!url.startsWith("http")) url = "https://" + url;
            webView.loadUrl(url);
        });
        findViewById(R.id.back_btn).setOnClickListener(v -> { if(webView.canGoBack()) webView.goBack(); });
        findViewById(R.id.forward_btn).setOnClickListener(v -> { if(webView.canGoForward()) webView.goForward(); });
        findViewById(R.id.refresh_btn).setOnClickListener(v -> webView.reload());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.browser_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        item.setChecked(!item.isChecked());

        if (id == R.id.action_dark_mode) {
            if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                WebSettingsCompat.setForceDark(webView.getSettings(), item.isChecked() ? WebSettingsCompat.FORCE_DARK_ON : WebSettingsCompat.FORCE_DARK_OFF);
            }
        } else if (id == R.id.action_turbo_mode) {
            webView.getSettings().setLoadsImagesAutomatically(!item.isChecked());
            webView.getSettings().setJavaScriptEnabled(!item.isChecked());
            webView.reload();
        } else if (id == R.id.action_block_redirect) {
            isBlockRedirectEnabled = item.isChecked();
        } else if (id == R.id.action_adblock) {
            isAdBlockEnabled = item.isChecked();
        }
        return true;
    }
}
