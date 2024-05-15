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

import org.apache.commons.text.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;

public class SecureWebView extends FrameLayout {

    private View rootView;
    private WebView webView;

    private WebViewClient webViewClient;

    private boolean alwaysOpenPagesInWebView = false;
    private boolean allowFileAccess = false;

    private List<String> allowedHosts = null;

    private List<DisallowedUrl> disallowedUrls = new ArrayList<>();

    public void setAlwaysOpenPagesInWebView(boolean alwaysOpenPagesInWebView) {
        this.alwaysOpenPagesInWebView = alwaysOpenPagesInWebView;
    }

    public void setAllowedHosts(List<String> allowedHosts) {
        this.allowedHosts = allowedHosts;
    }

    public void setDisallowedUrls(List<DisallowedUrl> disallowedUrls) {
        this.disallowedUrls = disallowedUrls;
    }

    public void clearDisallowedUrls() {
        this.disallowedUrls = null;
    }

    public void addToDisallowedUrls(List<DisallowedUrl> disallowedUrls) {
        this.disallowedUrls.addAll(disallowedUrls);
    }

    public void addPopularSearchEnginesToDisallowedUrls() {
        final List<DisallowedUrl> disallowedUrlList = new ArrayList<>();

        disallowedUrlList.add(new DisallowedUrl(new Uri.Builder().authority("google.com").build(), MatchVariant.AUTHORITY_WITHOUT_WWW_AND_PATH));
        disallowedUrlList.add(new DisallowedUrl(new Uri.Builder().authority("google.com").appendPath("").build(), MatchVariant.AUTHORITY_WITHOUT_WWW_AND_PATH));
        disallowedUrlList.add(new DisallowedUrl(new Uri.Builder().authority("bing.com").build(), MatchVariant.AUTHORITY_CONTAIN));
        disallowedUrlList.add(new DisallowedUrl(new Uri.Builder().authority("duckduckgo.com").build(), MatchVariant.AUTHORITY_CONTAIN));
        disallowedUrlList.add(new DisallowedUrl(new Uri.Builder().authority("ecosia.org").build(), MatchVariant.AUTHORITY_CONTAIN));
        disallowedUrlList.add(new DisallowedUrl(new Uri.Builder().authority("search.yahoo.com").build(), MatchVariant.AUTHORITY_CONTAIN));
        disallowedUrlList.add(new DisallowedUrl(new Uri.Builder().authority("search.brave.com").build(), MatchVariant.AUTHORITY_CONTAIN));
        disallowedUrlList.add(new DisallowedUrl(new Uri.Builder().authority("yep.com").build(), MatchVariant.AUTHORITY_CONTAIN));
        disallowedUrlList.add(new DisallowedUrl(new Uri.Builder().authority("openverse.org").build(), MatchVariant.AUTHORITY_CONTAIN));
        disallowedUrlList.add(new DisallowedUrl(new Uri.Builder().authority("startpage.com").build(), MatchVariant.AUTHORITY_CONTAIN));
        disallowedUrlList.add(new DisallowedUrl(new Uri.Builder().authority("swisscows.com").build(), MatchVariant.AUTHORITY_CONTAIN));
        disallowedUrlList.add(new DisallowedUrl(new Uri.Builder().authority("gibiru.com").build(), MatchVariant.AUTHORITY_CONTAIN));
        disallowedUrlList.add(new DisallowedUrl(new Uri.Builder().authority("wiki.com").build(), MatchVariant.AUTHORITY_CONTAIN));
        disallowedUrlList.add(new DisallowedUrl(new Uri.Builder().authority("dogpile.com").build(), MatchVariant.AUTHORITY_CONTAIN));
        disallowedUrlList.add(new DisallowedUrl(new Uri.Builder().authority("startpage.com").build(), MatchVariant.AUTHORITY_CONTAIN));
        disallowedUrlList.add(new DisallowedUrl(new Uri.Builder().authority("ask.com").build(), MatchVariant.AUTHORITY_CONTAIN));
        disallowedUrlList.add(new DisallowedUrl(new Uri.Builder().authority("baidu.com").build(), MatchVariant.AUTHORITY_CONTAIN));

        addToDisallowedUrls(disallowedUrlList);
    }

    public void addAiChatBotsToDisallowedUrls() {
        final List<DisallowedUrl> disallowedUrlList = new ArrayList<>();

        disallowedUrlList.add(new DisallowedUrl(new Uri.Builder().authority("chatgpt.com").build(), MatchVariant.AUTHORITY_CONTAIN));
        disallowedUrlList.add(new DisallowedUrl(new Uri.Builder().authority("copilot.microsoft.com").build(), MatchVariant.AUTHORITY_CONTAIN));
        disallowedUrlList.add(new DisallowedUrl(new Uri.Builder().authority("gemini.google.com").build(), MatchVariant.AUTHORITY_CONTAIN));

        addToDisallowedUrls(disallowedUrlList);
    }

    public enum MatchVariant {
        FULL_URI, AUTHORITY, AUTHORITY_WITHOUT_WWW, AUTHORITY_AND_PATH, AUTHORITY_WITHOUT_WWW_AND_PATH, AUTHORITY_CONTAIN, HOST, BEGINNING
    }

    public static class DisallowedUrl {

        private final Uri uri;

        private final MatchVariant matchVariant;

        public DisallowedUrl(final Uri uri, final MatchVariant matchVariant) {
            this.uri = uri;
            this.matchVariant = matchVariant;
        }

        public boolean matchesUri(final Uri uri) {
            if (this.uri == null || uri == null) return false;

            switch (matchVariant) {
                case FULL_URI -> { return this.uri.equals(uri); }
                case AUTHORITY, AUTHORITY_WITHOUT_WWW -> {
                    final String disallowedAuthority = matchVariant == MatchVariant.AUTHORITY_WITHOUT_WWW ? trimAuthority(this.uri.getAuthority()) : this.uri.getAuthority();
                    final String authorityToMatch = matchVariant == MatchVariant.AUTHORITY_WITHOUT_WWW ? trimAuthority(uri.getAuthority()) : uri.getAuthority();
                    return disallowedAuthority != null && disallowedAuthority.equals(authorityToMatch);
                }
                case AUTHORITY_AND_PATH, AUTHORITY_WITHOUT_WWW_AND_PATH -> {
                    final String disallowedAuthority = matchVariant == MatchVariant.AUTHORITY_WITHOUT_WWW_AND_PATH ? trimAuthority(this.uri.getAuthority()) : this.uri.getAuthority();
                    final String authorityToMatch = matchVariant == MatchVariant.AUTHORITY_WITHOUT_WWW_AND_PATH ? trimAuthority(uri.getAuthority()) : uri.getAuthority();
                    final boolean authoritiesEqual = disallowedAuthority != null && disallowedAuthority.equals(authorityToMatch);
                    final boolean pathEqual = this.uri.getPath() != null && this.uri.getPath().equals(uri.getPath());
                    return authoritiesEqual && (pathEqual || (this.uri.getPath() == null && uri.getPath() == null));
                }
                case AUTHORITY_CONTAIN -> { return uri.getAuthority() != null && this.uri.getAuthority() != null && uri.getAuthority().contains(this.uri.getAuthority()); }
                case HOST -> { return uri.getHost() != null && uri.getHost().equals(this.uri.getHost()); }
                case BEGINNING -> { return uri.toString().startsWith(this.uri.toString());  }
                default -> { return false; }
            }
        }

        private String trimAuthority(String authority) {
            return authority != null ? authority.replace("www.", "") : null;
        }

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

        this.disallowedUrls = new ArrayList<>();
        addPopularSearchEnginesToDisallowedUrls();
        addAiChatBotsToDisallowedUrls();
    }

    private boolean shouldBlockRequest(final Uri uri) {
        if ("http".equals(uri.getScheme())) {
            return true;
        }

        if (!allowFileAccess && "file".equals(uri.getScheme())) {
            return true;
        }

        final boolean allowedByAllowedHostList = allowedHosts == null || allowedHosts.contains(uri.getHost());
        if (!allowedByAllowedHostList) {
            return true;
        }

        if (disallowedUrls != null) {
            for (final DisallowedUrl disallowedUrl : disallowedUrls) {
                if (disallowedUrl.matchesUri(uri)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setWebViewClient(WebViewClient client) {
        webViewClient = client;
    }

    public void setWebChromeClient(WebChromeClient client) {
        this.webView.setWebChromeClient(client);
    }

    protected void loadUrl(String url, boolean allowJavaScript) {
        if (shouldBlockRequest(Uri.parse(url))) {
            return;
        }
        final String urlToLoad = allowJavaScript ? url : StringEscapeUtils.escapeEcmaScript(url);
        webView.loadUrl(urlToLoad);
    }

    public void loadUrl(String url) {
        loadUrl(url, false);
    }

    @Deprecated
    public void loadUrlWithoutEscapingJavascript(String url) {
        loadUrl(url, true);
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

    public void loadDataWithBaseURL(String baseUrl,String data, String mimeType, String encoding, String historyUrl) {
        webView.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
    }

}
