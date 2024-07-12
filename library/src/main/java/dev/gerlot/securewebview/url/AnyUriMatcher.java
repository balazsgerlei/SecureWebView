package dev.gerlot.securewebview.url;

import android.net.Uri;

import androidx.annotation.NonNull;

/**
 * Matches any URI.
 */
public class AnyUriMatcher implements UriMatcher {

    /**
     * The singleton instance.
     */
    public static final AnyUriMatcher INSTANCE = new AnyUriMatcher();

    private AnyUriMatcher() {
        // no need to construct separate instances
    }

    @Override
    public boolean matches(@NonNull Uri uri) {
        return true;
    }

}
