package dev.gerlot.securewebview.uri;

import android.net.Uri;

import androidx.annotation.NonNull;

public class AuthorityContainmentMatcher implements UriMatcher {

    private final Uri mUri;

    public AuthorityContainmentMatcher(Uri uri) {
        mUri = uri;
    }

    @Override
    public boolean matches(@NonNull Uri uri) {
        return uri.getAuthority() != null && mUri.getAuthority() != null && uri.getAuthority().contains(mUri.getAuthority());
    }

}
