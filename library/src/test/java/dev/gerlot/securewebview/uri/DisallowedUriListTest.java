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
public class DisallowedUriListTest {

    @Test
    public void testMatches_emptyDenyList() {
        DisallowedUriList denyList = new DisallowedUriList();
        assertTrue(denyList.matches(new Uri.Builder().authority("google.com").build()));
        assertTrue(denyList.matches(new Uri.Builder().authority("bing.com").appendPath("search").build()));
    }

    @Test
    public void testMatches_singleUrl() {
        DisallowedUriList denyList = new DisallowedUriList(uri -> Objects.equals(uri.getAuthority(), "google.com"));
        assertFalse(denyList.matches(new Uri.Builder().authority("google.com").build()));
        assertTrue(denyList.matches(new Uri.Builder().authority("bing.com").appendPath("search").build()));
    }

    @Test
    public void testMatches_multipleUrl() {
        DisallowedUriList denyList = new DisallowedUriList(List.of(uri -> Objects.equals(uri.getAuthority(), "google.com"), uri -> Objects.equals(uri.getAuthority(), "bing.com")));
        assertFalse(denyList.matches(new Uri.Builder().authority("google.com").build()));
        assertFalse(denyList.matches(new Uri.Builder().authority("bing.com").appendPath("search").build()));
        assertTrue(denyList.matches(new Uri.Builder().authority("duckduckgo.com").appendPath("search").build()));
    }

}
