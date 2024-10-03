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
public class UriListTest {

    @Test
    public void testMatches_emptyUriList() {
        UriList uriList = new UriList();
        assertFalse(uriList.matches(new Uri.Builder().authority("google.com").build()));
        assertFalse(uriList.matches(new Uri.Builder().authority("bing.com").appendPath("search").build()));
    }

    @Test
    public void testMatches_GoogleOnly() {
        UriList uriList = new UriList(uri -> Objects.equals(uri.getAuthority(), "google.com"));
        assertTrue(uriList.matches(new Uri.Builder().authority("google.com").build()));
        assertFalse(uriList.matches(new Uri.Builder().authority("bing.com").appendPath("search").build()));
    }

    @Test
    public void testMatches_GoogleAndBing() {
        UriList uriList = new UriList(List.of(uri -> Objects.equals(uri.getAuthority(), "google.com"), uri -> Objects.equals(uri.getAuthority(), "bing.com")));
        assertTrue(uriList.matches(new Uri.Builder().authority("google.com").build()));
        assertTrue(uriList.matches(new Uri.Builder().authority("bing.com").appendPath("search").build()));
        assertFalse(uriList.matches(new Uri.Builder().authority("duckduckgo.com").appendPath("search").build()));
    }

}
