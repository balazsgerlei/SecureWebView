package dev.gerlot.securewebview.uri;

import static org.junit.Assert.*;

import android.net.Uri;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class BeginningMatcherTest {

    public static final Uri GOOGLE = new Uri.Builder().authority("google.com").appendPath("search").build();
    private static final UriMatcher MATCHER = new BeginningMatcher(GOOGLE);

    @Test
    public void testMatches_same() {
        assertTrue(MATCHER.matches(GOOGLE));
    }

    @Test
    public void testMatches_equal() {
        assertTrue(MATCHER.matches(new Uri.Builder().authority("google.com").appendPath("search").build()));
    }

    @Test
    public void testMatches_differentAuthority() {
        assertFalse(MATCHER.matches(new Uri.Builder().authority("bing.com").appendPath("search").build()));
    }

    @Test
    public void testMatches_differentPath() {
        assertFalse(MATCHER.matches(new Uri.Builder().authority("google.com").appendPath("post").build()));
    }

    @Test
    public void testMatches_beginning() {
        assertTrue(MATCHER.matches(new Uri.Builder().authority("google.com").appendPath("searchResult").appendQueryParameter("q", "foo").build()));
    }

}
