package dev.gerlot.securewebview.url;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.net.Uri;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;
import java.util.Objects;

@RunWith(RobolectricTestRunner.class)
public class AllowedUrlListTest {

    @Test
    public void testMatches_emptyAllowList() {
        AllowedUrlList allowList = new AllowedUrlList();
        assertFalse(allowList.matches(new Uri.Builder().authority("google.com").build()));
        assertFalse(allowList.matches(new Uri.Builder().authority("bing.com").appendPath("search").build()));
    }

    @Test
    public void testMatches_GoogleOnly() {
        AllowedUrlList allowList = new AllowedUrlList(uri -> Objects.equals(uri.getAuthority(), "google.com"));
        assertTrue(allowList.matches(new Uri.Builder().authority("google.com").build()));
        assertFalse(allowList.matches(new Uri.Builder().authority("bing.com").appendPath("search").build()));
    }

    @Test
    public void testMatches_GoogleAndBing() {
        AllowedUrlList allowList = new AllowedUrlList(List.of(uri -> Objects.equals(uri.getAuthority(), "google.com") || Objects.equals(uri.getAuthority(), "bing.com")));
        assertTrue(allowList.matches(new Uri.Builder().authority("google.com").build()));
        assertTrue(allowList.matches(new Uri.Builder().authority("bing.com").appendPath("search").build()));
        assertFalse(allowList.matches(new Uri.Builder().authority("duckduckgo.com").appendPath("search").build()));
    }

}
