package dev.gerlot.securewebview;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.Arrays;

import dev.gerlot.securewebview.url.AllowedUrlList;
import dev.gerlot.securewebview.url.AuthorityAndPathMatcher;
import dev.gerlot.securewebview.url.AuthorityContainmentMatcher;
import dev.gerlot.securewebview.url.DeniedUrlList;
import dev.gerlot.securewebview.url.UriMatcher;
import dev.gerlot.securewebview.url.Uris;

public class SecureWebView extends FrameLayout {

    private View rootView;
    private WebView webView;

    private WebViewClient webViewClient;

    private boolean alwaysOpenPagesInWebView = false;
    private boolean allowFileAccess = false;

    private AllowedUrlList allowedUrlList = null;

    private DeniedUrlList disallowedUrlList = null;

    public void setAlwaysOpenPagesInWebView(boolean alwaysOpenPagesInWebView) {
        this.alwaysOpenPagesInWebView = alwaysOpenPagesInWebView;
    }

    public void setAllowedUrlList(AllowedUrlList allowedUrlList) {
        this.allowedUrlList = allowedUrlList;
    }

    public void setDisallowedUrlList(DeniedUrlList disallowedUrlList) {
        this.disallowedUrlList = disallowedUrlList;
    }

    public void clearDeniedUrls() {
        this.disallowedUrlList = null;
    }

    public void deny(UriMatcher... denyList) {
        this.disallowedUrlList.addAll(Arrays.asList(denyList));
    }

    public void addPopularSearchEnginesToDenyList() {
        deny(
                new AuthorityAndPathMatcher(Uris.GOOGLE, false),
                new AuthorityAndPathMatcher(Uris.GOOGLE_EMPTY_PATH, false),
                new AuthorityContainmentMatcher(Uris.BING),
                new AuthorityContainmentMatcher(Uris.DUCKDUCKGO),
                new AuthorityContainmentMatcher(Uris.ECOSIA),
                new AuthorityContainmentMatcher(Uris.YAHOO_SEARCH),
                new AuthorityContainmentMatcher(Uris.BRAVE_SEARCH),
                new AuthorityContainmentMatcher(Uris.YEP),
                new AuthorityContainmentMatcher(Uris.OPENVERSE),
                new AuthorityContainmentMatcher(Uris.STARTPAGE),
                new AuthorityContainmentMatcher(Uris.SWISSCOWS),
                new AuthorityContainmentMatcher(Uris.GIBIRU),
                new AuthorityContainmentMatcher(Uris.WIKI),
                new AuthorityContainmentMatcher(Uris.DOGPILE),
                new AuthorityContainmentMatcher(Uris.ASK_DOT_COM),
                new AuthorityContainmentMatcher(Uris.BAIDU)
        );
    }

    public void addAiChatBotsToDenyList() {
        deny(
                new AuthorityContainmentMatcher(Uris.CHATGPT),
                new AuthorityContainmentMatcher(Uris.COPILOT),
                new AuthorityContainmentMatcher(Uris.GEMINI)
        );
    }

    private class SecureWebViewClient extends WebViewClient {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if (shouldBlockRequest(request.getUrl())) {
                return true;
            }

            if (webViewClient != null) {
                if (alwaysOpenPagesInWebView) {
                    webViewClient.shouldOverrideUrlLoading(view, request);
                    return false;
                } else {
                    return webViewClient.shouldOverrideUrlLoading(view, request);
                }
            }

            return false;
        }

        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            if (shouldBlockRequest(request.getUrl())) {
                new Handler(getContext().getMainLooper()).post(view::stopLoading);
            }
            return super.shouldInterceptRequest(view, request);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (shouldBlockRequest(Uri.parse(url))) {
                return true;
            }

            if (webViewClient != null) {
                if (alwaysOpenPagesInWebView) {
                    webViewClient.shouldOverrideUrlLoading(view, url);
                    return false;
                } else {
                    return webViewClient.shouldOverrideUrlLoading(view, url);
                }
            }

            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (webViewClient != null) {
                webViewClient.onPageStarted(view, url, favicon);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (webViewClient != null) {
                webViewClient.onPageFinished(view, url);
            }
        }

    }

    public SecureWebView(Context context) {
        super(context);
        init(context);
    }

    public SecureWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(final Context context) {
        this.rootView = inflate(context, R.layout.secure_web_view, this);
        this.webView = rootView.findViewById(R.id.webView);

        setAllowFileAccess(false);
        this.webView.getSettings().setAllowContentAccess(false);
        this.webView.setWebViewClient(new SecureWebViewClient());

        this.disallowedUrlList = new DeniedUrlList();
        addPopularSearchEnginesToDenyList();
        addAiChatBotsToDenyList();
    }

    private boolean shouldBlockRequest(final Uri uri) {
        return shouldBlockRequest(uri, true, false);
    }

    private boolean shouldBlockRequest(final Uri uri, boolean allowDataUri, boolean allowJavaScript) {
        if ("http".equals(uri.getScheme())) {
            return true;
        }

        if (!allowDataUri && "data".equals(uri.getScheme())) {
            return true;
        }

        if (!allowJavaScript && "javascript".equals(uri.getScheme())) {
            return true;
        }

        if (!allowFileAccess && "file".equals(uri.getScheme())) {
            return true;
        }

        final boolean allowed = allowedUrlList == null || allowedUrlList.matches(uri);
        if (!allowed) {
            return true;
        }

        return disallowedUrlList.matches(uri);
    }

    public void setWebViewClient(WebViewClient client) {
        webViewClient = client;
    }

    public void setWebChromeClient(WebChromeClient client) {
        this.webView.setWebChromeClient(client);
    }

    protected void loadUrl(String url, boolean allowJavaScript) {
        if (shouldBlockRequest(Uri.parse(url), false, allowJavaScript)) {
            return;
        }
        webView.loadUrl(url);
    }

    public void loadUrl(String url) {
        loadUrl(url, false);
    }

    public void evaluateJavascript(String script, ValueCallback<String> resultCallback) {
        webView.evaluateJavascript(script, resultCallback);
    }

    public void goBack() {
        webView.goBack();
    }

    public void setJavaScriptEnabled(boolean enabled) {
        webView.getSettings().setJavaScriptEnabled(enabled);
    }

    public boolean getJavaScriptEnabled() {
        return webView.getSettings().getJavaScriptEnabled();
    }

    public void setAllowFileAccess(boolean allow) {
        allowFileAccess = allow;
        webView.getSettings().setAllowFileAccess(allow);
    }

    public boolean getAllowFileAccess() {
        return allowFileAccess;
    }

    public void setAllowContentAccess(boolean allow) {
        webView.getSettings().setAllowContentAccess(allow);
    }

    public boolean getAllowContentAccess() {
        return webView.getSettings().getAllowContentAccess();
    }

    public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
        webView.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
    }

}
