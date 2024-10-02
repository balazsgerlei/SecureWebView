package dev.gerlot.securewebview.uri;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.net.Uri;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;
import java.util.Objects;

@RunWith(RobolectricTestRunner.class)
public class AllowedUriListTest {

    @Test
    public void testMatches_emptyAllowList() {
        AllowedUriList allowList = new AllowedUriList();
        assertFalse(allowList.matches(new Uri.Builder().authority("google.com").build()));
        assertFalse(allowList.matches(new Uri.Builder().authority("bing.com").appendPath("search").build()));
    }

    @Test
    public void testMatches_GoogleOnly() {
        AllowedUriList allowList = new AllowedUriList(uri -> Objects.equals(uri.getAuthority(), "google.com"));
        assertTrue(allowList.matches(new Uri.Builder().authority("google.com").build()));
        assertFalse(allowList.matches(new Uri.Builder().authority("bing.com").appendPath("search").build()));
    }

    @Test
    public void testMatches_GoogleAndBing() {
        AllowedUriList allowList = new AllowedUriList(List.of(uri -> Objects.equals(uri.getAuthority(), "google.com") || Objects.equals(uri.getAuthority(), "bing.com")));
        assertTrue(allowList.matches(new Uri.Builder().authority("google.com").build()));
        assertTrue(allowList.matches(new Uri.Builder().authority("bing.com").appendPath("search").build()));
        assertFalse(allowList.matches(new Uri.Builder().authority("duckduckgo.com").appendPath("search").build()));
    }

}
