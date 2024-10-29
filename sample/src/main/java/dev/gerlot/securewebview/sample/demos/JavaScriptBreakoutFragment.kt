package dev.gerlot.securewebview.sample.demos

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import dev.gerlot.securewebview.sample.SecurableWebViewFragment
import dev.gerlot.securewebview.sample.WebViewSecureState
import dev.gerlot.securewebview.sample.databinding.JavascriptBreakoutFragmentBinding
import dev.gerlot.securewebview.uri.AuthorityAndPathMatcher
import dev.gerlot.securewebview.uri.UriList
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class JavaScriptBreakoutFragment: Fragment(), SecurableWebViewFragment {

    private var _binding: JavascriptBreakoutFragmentBinding? = null
    private val binding get() = _binding!!

    private var webViewSecureState = WebViewSecureState.INSECURE

    //private var currentlyLoadedData: String? = null
    private var currentUrl: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = JavascriptBreakoutFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Due to how this demo is implemented (we use JavaScript to navigate away)
        // we need to enable JavaScript
        binding.insecureWebView.settings.javaScriptEnabled = true
        binding.secureWebView.javaScriptEnabled = true

        // Due to how this demo is implemented (we load the initial HTML that contains
        // the JavaScript that navigates away) we need to allow file access and loading data URIs
        binding.secureWebView.allowFileAccess = true
        binding.secureWebView.allowLoadingDataUri = true

        binding.secureWebView.setAllowedUriList(
            UriList(AuthorityAndPathMatcher(Uri.parse(INITIAL_URI)))
        )

        binding.insecureWebView.setWebViewClient(object : WebViewClient() {

            override fun onPageFinished(view: WebView?, url: String?) {
                currentUrl = url
                super.onPageFinished(view, url)
            }

        })

        if (webViewSecureState == WebViewSecureState.INSECURE) {
            binding.viewFlipper.displayedChild = 0
        } else {
            binding.viewFlipper.displayedChild = 1
        }

        if (savedInstanceState == null) {
            loadUrl(INITIAL_URI)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @OptIn(ExperimentalEncodingApi::class)
    private fun loadData(data: String) {
        if (webViewSecureState == WebViewSecureState.INSECURE) {
            binding.insecureWebView.loadData(Base64.encode(data.toByteArray()), "text/html", "base64")
        } else {
            binding.secureWebView.loadDataWithBaseURL("https://www.example.com", data, "text/html", "UTF-8", null)
        }
    }

    private fun loadUrl(url: String) {
        currentUrl = url
        if (webViewSecureState == WebViewSecureState.INSECURE) {
            binding.insecureWebView.loadUrl(url)
        } else {
            binding.secureWebView.loadUrl(url)
        }
    }

    override fun secureWebView() {
        webViewSecureState = WebViewSecureState.SECURE
        if (view != null) {
            binding.viewFlipper.displayedChild = 1
            currentUrl?.let {
                loadUrl(it)
            }
        }
    }

    override fun unSecureWebView() {
        webViewSecureState = WebViewSecureState.INSECURE
        if (view != null) {
            binding.viewFlipper.displayedChild = 0
            currentUrl?.let {
                loadUrl(it)
            }
        }
    }

    companion object {

        private const val INITIAL_URI = "file:///android_asset/javascript_breakout.html"

        val TAG: String = JavaScriptBreakoutFragment::class.java.canonicalName ?: JavaScriptBreakoutFragment::class.java.name

        fun newInstance() = JavaScriptBreakoutFragment()

    }

}
