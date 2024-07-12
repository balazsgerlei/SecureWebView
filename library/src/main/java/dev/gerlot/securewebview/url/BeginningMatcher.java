package dev.gerlot.securewebview.url;

import android.net.Uri;

import androidx.annotation.NonNull;

public class BeginningMatcher implements UriMatcher {

    private final Uri mUri;

    public BeginningMatcher(Uri uri) {
        mUri = uri;
    }

    @Override
    public boolean matches(@NonNull Uri uri) {
        return uri.toString().startsWith(mUri.toString());
    }

}
