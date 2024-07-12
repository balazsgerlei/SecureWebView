package dev.gerlot.securewebview.url;

import android.net.Uri;

import androidx.annotation.NonNull;

public class ExactUriMatcher implements UriMatcher {

    private final Uri mUri;

    public ExactUriMatcher(Uri uri) {
        mUri = uri;
    }

    @Override
    public boolean matches(@NonNull Uri uri) {
        return mUri.equals(uri);
    }

}
