package dev.gerlot.securewebview.uri;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.net.Uri;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class AuthorityContainmentMatcherTest {

    public static final Uri GOOGLE = new Uri.Builder().authority("google.com").appendPath("search").build();
    private static final UriMatcher MATCHER = new AuthorityContainmentMatcher(GOOGLE);

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
        assertTrue(MATCHER.matches(new Uri.Builder().authority("google.com").appendPath("post").build()));
    }

}
