package dev.gerlot.securewebview.uri;

import android.net.Uri;

import androidx.annotation.NonNull;

import java.util.Collection;

/**
 * An allowList of URLs which can be opened in the SecureWebView.
 */
public class AllowedUriList extends UriList implements UriMatcher {

    public AllowedUriList() {
        super();
    }

    public AllowedUriList(UriMatcher... matchers) {
        super(matchers);
    }

    public AllowedUriList(Collection<UriMatcher> matchers) {
        super(matchers);
    }

    @Override
    public boolean matches(@NonNull Uri uri) {
        boolean hasAnyMatches = false;

        for (UriMatcher matcher : mUriMatchers) {
            if (matcher.matches(uri)) {
                hasAnyMatches = true;
            }
        }

        return hasAnyMatches;
    }

}