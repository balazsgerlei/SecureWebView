package dev.gerlot.securewebview.uri;

import android.net.Uri;

import androidx.annotation.NonNull;

import java.util.Objects;

public class AuthorityAndPathMatcher implements UriMatcher {

    private final Uri mUri;

    private final UriMatcher mAuthorityMatcher;

    public AuthorityAndPathMatcher(Uri uri) {
        this(uri, true);
    }

    public AuthorityAndPathMatcher(Uri uri, boolean includeWwwPrefix) {
        mUri = uri;
        mAuthorityMatcher = new AuthorityMatcher(uri, includeWwwPrefix);
    }

    @Override
    public boolean matches(@NonNull Uri uri) {
        return mAuthorityMatcher.matches(uri) && Objects.equals(mUri.getPath(), uri.getPath());
    }

}
