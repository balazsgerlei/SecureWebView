package dev.gerlot.securewebview.clients;

import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class DefaultSecureWebViewClient extends WebViewClient {

    private final boolean alwaysOpenLinksInWebView;

    public DefaultSecureWebViewClient(final boolean alwaysOpenLinksInWebView) {
        this.alwaysOpenLinksInWebView = alwaysOpenLinksInWebView;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        if (alwaysOpenLinksInWebView) {
            return false;
        }

        return super.shouldOverrideUrlLoading(view, request);
    }

}
