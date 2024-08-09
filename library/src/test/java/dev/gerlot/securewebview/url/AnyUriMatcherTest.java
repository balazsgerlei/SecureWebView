package dev.gerlot.securewebview.url;

import static org.junit.Assert.*;

import android.net.Uri;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class AnyUriMatcherTest {

    @Test
    public void testMatches_any() {
        AnyUriMatcher matcher = AnyUriMatcher.INSTANCE;

        final Uri google = new Uri.Builder().authority("google.com").build();
        assertTrue(matcher.matches(google));

        final Uri bing = new Uri.Builder().authority("bing.com").build();
        assertTrue(matcher.matches(bing));
    }
}