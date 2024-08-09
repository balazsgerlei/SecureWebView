package dev.gerlot.securewebview.url;

import android.net.Uri;

import androidx.annotation.NonNull;

import java.util.Objects;

class AuthorityWithoutWwwMatcher implements UriMatcher {

    private final Uri mUri;

    public AuthorityWithoutWwwMatcher(Uri uri) {
        mUri = uri;
    }

    @Override
    public boolean matches(@NonNull Uri uri) {
        return Objects.equals(trimAuthority(mUri.getAuthority()), trimAuthority(uri.getAuthority()));
    }

    private String trimAuthority(String authority) {
        return authority != null ? authority.replace("www.", "") : null;
    }

}

