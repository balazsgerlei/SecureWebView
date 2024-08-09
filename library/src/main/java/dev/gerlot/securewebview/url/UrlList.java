package dev.gerlot.securewebview.url;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public abstract class UrlList extends AbstractList<UriMatcher> {
    protected final List<UriMatcher> mUriMatchers;

    protected UrlList() {
        mUriMatchers = new ArrayList<>();
    }

    protected UrlList(UriMatcher... matchers) {
        mUriMatchers = Arrays.asList(matchers);
    }

    protected UrlList(Collection<UriMatcher> matchers) {
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
}
