package dev.gerlot.securewebview.uri;

import android.net.Uri;

import androidx.annotation.NonNull;

import java.util.Collection;

/**
 * A denyList of URLs. This will reject a match for any URL on the list, and permit all others.
 */
public class DeniedUriList extends UriList implements UriMatcher {

    public DeniedUriList() {
        super();
    }

    public DeniedUriList(UriMatcher... matchers) {
        super(matchers);
    }

    public DeniedUriList(Collection<UriMatcher> matchers) {
        super(matchers);
    }

    @Override
    public boolean matches(@NonNull Uri uri) {
        for (UriMatcher matcher : mUriMatchers) {
            if (matcher.matches(uri)) {
                return false;
            }
        }

        return true;
    }
}
