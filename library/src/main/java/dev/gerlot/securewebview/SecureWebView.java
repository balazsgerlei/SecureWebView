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

import dev.gerlot.securewebview.uri.AuthorityAndPathMatcher;
import dev.gerlot.securewebview.uri.AuthorityContainmentMatcher;
import dev.gerlot.securewebview.uri.UriList;
import dev.gerlot.securewebview.uri.UriMatcher;
import dev.gerlot.securewebview.uri.Uris;

public class SecureWebView extends FrameLayout {

    private View rootView;
    private WebView webView;

    private WebViewClient webViewClient;

    private boolean alwaysOpenPagesInWebView = false;
    private boolean allowFileAccess = false;
    private boolean allowLoadingDataUri = false;

    private UriList allowedUriList = null;

    private UriList disallowedUriList = null;

    public void setAlwaysOpenPagesInWebView(boolean alwaysOpenPagesInWebView) {
        this.alwaysOpenPagesInWebView = alwaysOpenPagesInWebView;
    }

    public void setAllowedUriList(UriList allowedUriList) {
        this.allowedUriList = allowedUriList;
    }

    public void setDisallowedUriList(UriList disallowedUriList) {
        this.disallowedUriList = disallowedUriList;
    }

    public void clearDisallowedUriList() {
        this.disallowedUriList = null;
    }

    public void disallowUris(UriMatcher... disallowedUriList) {
        this.disallowedUriList.addAll(Arrays.asList(disallowedUriList));
    }

    public void addPopularSearchEnginesToDisallowedUriList() {
        disallowUris(
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

    public void addAiChatBotsToDisallowedUriList() {
        disallowUris(
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

        this.disallowedUriList = new UriList();
        addPopularSearchEnginesToDisallowedUriList();
        addAiChatBotsToDisallowedUriList();
    }

    private boolean shouldBlockRequest(final Uri uri) {
        return shouldBlockRequest(uri, false);
    }

    private boolean shouldBlockRequest(final Uri uri, boolean allowJavaScript) {
        if ("http".equals(uri.getScheme())) {
            return true;
        }

        if (!allowJavaScript && "javascript".equals(uri.getScheme())) {
            return true;
        }

        if (!allowFileAccess && "file".equals(uri.getScheme())) {
            return true;
        }

        if (!allowLoadingDataUri && "data".equals(uri.getScheme())) {
            return true;
        }

        final boolean allowed = allowedUriList == null || allowedUriList.matches(uri);
        if (!allowed) {
            return true;
        }

        return disallowedUriList != null && disallowedUriList.matches(uri);
    }

    public void setWebViewClient(WebViewClient client) {
        webViewClient = client;
    }

    public void setWebChromeClient(WebChromeClient client) {
        this.webView.setWebChromeClient(client);
    }

    protected void loadUrl(String url, boolean allowJavaScript) {
        if (shouldBlockRequest(Uri.parse(url), allowJavaScript)) {
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

    public void stopLoading() {
        webView.stopLoading();
    }

    public void reload() {
        webView.reload();
    }

    public boolean canGoBack() {
        return webView.canGoBack();
    }

    public void goBack() {
        webView.goBack();
    }

    public boolean canGoForward() {
        return webView.canGoForward();
    }

    public void goForward() {
        webView.goForward();
    }

    public boolean canGoBackOrForward(int steps) {
        return webView.canGoBackOrForward(steps);
    }

    public void goBackOrForward(int steps) {
        webView.goBackOrForward(steps);
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

    public void setAllowLoadingDataUri(boolean allow) {
        allowLoadingDataUri = allow;
    }

    public boolean getAllowLoadingDataUri() {
        return allowLoadingDataUri;
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
