package dev.gerlot.securewebview.url;

import android.net.Uri;

import androidx.annotation.NonNull;

import java.util.Objects;

class AuthorityWithWwwMatcher implements UriMatcher {

    private final Uri mUri;

    public AuthorityWithWwwMatcher(Uri uri) {
        mUri = uri;
    }

    @Override
    public boolean matches(@NonNull Uri uri) {
        return Objects.equals(mUri.getAuthority(), uri.getAuthority());
    }

}
