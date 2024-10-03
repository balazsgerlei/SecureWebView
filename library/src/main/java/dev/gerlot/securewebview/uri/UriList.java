package dev.gerlot.securewebview.uri;

import android.net.Uri;

import androidx.annotation.NonNull;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * A list of URIs which can be matched against certain matchers to allow or disallow opening them
 * in a SecureWebView.
 */
public class UriList extends AbstractList<UriMatcher> implements UriMatcher {
    protected final List<UriMatcher> mUriMatchers;

    public UriList() {
        mUriMatchers = new ArrayList<>();
    }

    public UriList(UriMatcher... matchers) {
        mUriMatchers = Arrays.asList(matchers);
    }

    public UriList(Collection<UriMatcher> matchers) {
        mUriMatchers = new ArrayList<>(matchers);
    }

    @Override
    public UriMatcher get(int index) {
        return mUriMatchers.get(index);
    }

    @Override
    public int size() {
        return mUriMatchers.size();
    }

    @Override
    public UriMatcher set(int index, UriMatcher element) {
        return mUriMatchers.set(index, element);
    }

    @Override
    public void add(int index, UriMatcher element) {
        mUriMatchers.add(index, element);
    }

    @Override
    public UriMatcher remove(int index) {
        return mUriMatchers.remove(index);
    }

    @Override
    public boolean matches(@NonNull Uri uri) {
        boolean hasAnyMatches = false;

        for (UriMatcher matcher : mUriMatchers) {
            if (matcher.matches(uri)) {
                hasAnyMatches = true;
            }
        }

        return hasAnyMatches;
    }
}
