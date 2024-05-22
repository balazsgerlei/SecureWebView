package dev.gerlot.securewebview.sample.demos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import dev.gerlot.securewebview.sample.SecurableWebViewFragment
import dev.gerlot.securewebview.sample.WebViewSecureState
import dev.gerlot.securewebview.sample.databinding.BreakoutFragmentBinding

class BreakoutFragment : Fragment(), SecurableWebViewFragment {

    private var _binding: BreakoutFragmentBinding? = null
    private val binding get() = _binding!!

    private var webViewSecureState = WebViewSecureState.INSECURE

    private var currentUrl: String? = INITIAL_URL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                view ?: return
                if (webViewSecureState == WebViewSecureState.INSECURE) {
                    binding.insecureWebView.goBack()
                } else {
                    binding.secureWebView.goBack()
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BreakoutFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.insecureWebView.settings.javaScriptEnabled = true
        binding.insecureWebView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                currentUrl = url
                super.onPageFinished(view, url)
            }

        }
        binding.secureWebView.javaScriptEnabled = true
        //binding.secureWebView.setAllowedHosts(listOf("www.google.com", "maps.google.com", "consent.google.com"))
        /*binding.secureWebView.setDisallowedUrls(listOf(
            SecureWebView.DisallowedUrl(Uri.Builder().scheme("https").authority("google.com").build(), MatchVariant.AUTHORITY_WITHOUT_WWW_AND_PATH),
            SecureWebView.DisallowedUrl(Uri.Builder().scheme("https").authority("google.com").appendPath("").build(), MatchVariant.AUTHORITY_WITHOUT_WWW_AND_PATH),
        ))*/
        binding.secureWebView.setWebViewClient(object : WebViewClient() {

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
            loadUrl(INITIAL_URL)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadUrl(url: String) {
        if (url == "about:blank") {
            if (webViewSecureState == WebViewSecureState.INSECURE) {
                binding.insecureWebView.loadUrl(url)
            } else {
                binding.secureWebView.loadUrl(url)
            }
        } else {
            val urlToLoad = url
                .replace("http://", "https://")
                .run {
                    if (!startsWith("https://")) {
                        "https://$this"
                    } else {
                        this
                    }
                }
            if (webViewSecureState == WebViewSecureState.INSECURE) {
                binding.insecureWebView.loadUrl(urlToLoad)
            } else {
                binding.secureWebView.loadUrl(urlToLoad)
            }
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

        private const val INITIAL_URL = "https://maps.google.com"

        val TAG: String = BreakoutFragment::class.java.canonicalName ?: BreakoutFragment::class.java.name

        fun newInstance() = BreakoutFragment()

    }

}
