package dev.gerlot.securewebview.url;

import android.net.Uri;

import androidx.annotation.NonNull;

import java.util.Collection;

/**
 * An allowList of URLs which can be opened in the SecureWebView.
 */
public class AllowedUrlList extends UrlList implements UriMatcher {

    public AllowedUrlList() {
        super();
    }

    public AllowedUrlList(UriMatcher... matchers) {
        super(matchers);
    }

    public AllowedUrlList(Collection<UriMatcher> matchers) {
        super(matchers);
    }

    @Override
    public boolean matches(@NonNull Uri uri) {
        for (UriMatcher matcher : mUriMatchers) {
            if (matcher.matches(uri)) {
                return true;
            }
        }

        return false;
    }

}
