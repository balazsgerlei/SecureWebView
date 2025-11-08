package dev.gerlot.securewebview.uri;

import android.net.Uri;

public final class Uris {

    // region Popular search engines

    public static final Uri GOOGLE = new Uri.Builder().authority("google.com").build();
    public static final Uri GOOGLE_EMPTY_PATH = new Uri.Builder().authority("google.com").appendPath("").build();
    public static final Uri BING = new Uri.Builder().authority("bing.com").build();
    public static final Uri DUCKDUCKGO = new Uri.Builder().authority("duckduckgo.com").build();
    public static final Uri ECOSIA = new Uri.Builder().authority("ecosia.org").build();
    public static final Uri YAHOO_SEARCH = new Uri.Builder().authority("search.yahoo.com").build();
    public static final Uri BRAVE_SEARCH = new Uri.Builder().authority("search.brave.com").build();
    public static final Uri YEP = new Uri.Builder().authority("yep.com").build();
    public static final Uri OPENVERSE = new Uri.Builder().authority("openverse.org").build();
    public static final Uri STARTPAGE = new Uri.Builder().authority("startpage.com").build();
    public static final Uri SWISSCOWS = new Uri.Builder().authority("swisscows.com").build();
    public static final Uri GIBIRU = new Uri.Builder().authority("gibiru.com").build();
    public static final Uri WIKI = new Uri.Builder().authority("wiki.com").build();
    public static final Uri DOGPILE = new Uri.Builder().authority("dogpile.com").build();
    public static final Uri ASK_DOT_COM = new Uri.Builder().authority("ask.com").build();
    public static final Uri BAIDU = new Uri.Builder().authority("baidu.com").build();
    public static final Uri YANDEX = new Uri.Builder().authority("yandex.com").build();
    public static final Uri NAVER = new Uri.Builder().authority("naver.com").build();
    public static final Uri SEZNAM = new Uri.Builder().authority("seznam.com").build();
    public static final Uri KAGI = new Uri.Builder().authority("kagi.com").build();
    public static final Uri QWANT = new Uri.Builder().authority("qwant.com").build();
    public static final Uri INFO_DOT_COM = new Uri.Builder().authority("info.com").build();
    public static final Uri YOU_DOT_COM = new Uri.Builder().authority("you.com").build();

    // endregion

    // region AI chat bots

    public static final Uri CHATGPT = new Uri.Builder().authority("chatgpt.com").build();
    public static final Uri COPILOT = new Uri.Builder().authority("copilot.microsoft.com").build();
    public static final Uri GEMINI = new Uri.Builder().authority("gemini.google.com").build();
    public static final Uri CLAUDE = new Uri.Builder().authority("claude.ai").build();
    public static final Uri PERPLEXITY = new Uri.Builder().authority("perplexity.ai").build();
    public static final Uri GROK = new Uri.Builder().authority("grok.x.ai").build();
    public static final Uri MISTRAL = new Uri.Builder().authority("chat.mistral.ai").build();
    public static final Uri PI = new Uri.Builder().authority("pi.ai").build();
    public static final Uri HUGGING_CHAT = new Uri.Builder().authority("huggingface.co").path("chat").build();

    // endregion

    private Uris() {
        // no need to construct this class
    }

}
