package dev.gerlot.securewebview.url;

import android.net.Uri;

import androidx.annotation.NonNull;

/**
 * Determines whether a {@link android.net.Uri} matches some set of criteria.
 * Implementations of this type can be used to control the set of browsers used by AppAuth
 * for authorization.
 */
public interface UriMatcher {

    /**
     * @return true if the browser matches some set of criteria.
     */
    boolean matches(@NonNull Uri uri);

}
