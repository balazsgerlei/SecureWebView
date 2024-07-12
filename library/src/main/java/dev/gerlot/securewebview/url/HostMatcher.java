package dev.gerlot.securewebview.url;

import android.net.Uri;

import androidx.annotation.NonNull;

import java.util.Objects;

public class HostMatcher implements UriMatcher {

    private final Uri mUri;

    public HostMatcher(Uri uri) {
        mUri = uri;
    }

    @Override
    public boolean matches(@NonNull Uri uri) {
        return Objects.equals(uri.getHost(), mUri.getHost());
    }
}
