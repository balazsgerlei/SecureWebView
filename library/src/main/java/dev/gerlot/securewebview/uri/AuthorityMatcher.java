package dev.gerlot.securewebview.uri;

import android.net.Uri;

import androidx.annotation.NonNull;

public class AuthorityMatcher implements UriMatcher {

    private final UriMatcher mMatchStrategy;

    public AuthorityMatcher(Uri uri) {
        this(uri, true);
    }

    public AuthorityMatcher(Uri uri, boolean includeWwwPrefix) {
        mMatchStrategy = includeWwwPrefix ? new AuthorityWithWwwMatcher(uri) : new AuthorityWithoutWwwMatcher(uri);
    }

    @Override
    public boolean matches(@NonNull Uri uri) {
        return mMatchStrategy.matches(uri);
    }

}
