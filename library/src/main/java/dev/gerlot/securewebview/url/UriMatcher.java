package dev.gerlot.securewebview.url;

import android.net.Uri;

import androidx.annotation.NonNull;

/**
 * Determines whether a {@link android.net.Uri} matches some set of criteria.
 */
public interface UriMatcher {

    /**
     * @return true if the browser matches some set of criteria.
     */
    boolean matches(@NonNull Uri uri);

}
