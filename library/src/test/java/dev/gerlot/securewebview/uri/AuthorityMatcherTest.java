package dev.gerlot.securewebview.uri;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.net.Uri;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class AuthorityMatcherTest {

    public static final Uri GOOGLE = new Uri.Builder().authority("google.com").appendPath("search").build();

    @Test
    public void testMatches_withWwwPrefix() {
        AuthorityMatcher matcher = new AuthorityMatcher(GOOGLE);
        assertFalse(matcher.matches(new Uri.Builder().authority("www.google.com").build()));
    }

    @Test
    public void testMatches_withoutWwwPrefix() {
        AuthorityMatcher matcher = new AuthorityMatcher(GOOGLE, false);
        assertTrue(matcher.matches(new Uri.Builder().authority("www.google.com").build()));
    }

}
