package dev.gerlot.securewebview.sample.demos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import dev.gerlot.securewebview.sample.SecurableWebViewFragment
import dev.gerlot.securewebview.sample.WebViewSecureState
import dev.gerlot.securewebview.sample.databinding.JavascriptInjectionFragmentBinding


class JavaScriptInjectionFragment: Fragment(), SecurableWebViewFragment {

    private var _binding: JavascriptInjectionFragmentBinding? = null
    private val binding get() = _binding!!

    private var webViewSecureState = WebViewSecureState.INSECURE

    private var currentlyLoadedScript: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = JavascriptInjectionFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.insecureWebView.settings.javaScriptEnabled = true
        binding.insecureWebView.webViewClient = WebViewClient()
        binding.insecureWebView.webChromeClient = WebChromeClient()

        binding.secureWebView.javaScriptEnabled = true
        binding.secureWebView.setWebViewClient(WebViewClient())
        binding.secureWebView.setWebChromeClient(WebChromeClient())

        if (webViewSecureState == WebViewSecureState.INSECURE) {
            binding.viewFlipper.displayedChild = 0
        } else {
            binding.viewFlipper.displayedChild = 1
        }

        if (savedInstanceState == null) {
            currentlyLoadedScript = INJECTED_JAVASCRIPT
            loadUrl(INJECTED_JAVASCRIPT)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadUrl(url: String) {
        binding.urlInput.setText(url)
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
            currentlyLoadedScript?.let {
                loadUrl(it)
            }
        }
    }

    override fun unSecureWebView() {
        webViewSecureState = WebViewSecureState.INSECURE
        if (view != null) {
            binding.viewFlipper.displayedChild = 0
            currentlyLoadedScript?.let {
                loadUrl(it)
            }
        }
    }

    companion object {

        private const val MALICIOUS_PARAMETER = "alert(\"Hello World\")"

        private const val INJECTED_JAVASCRIPT = "javascript:void($MALICIOUS_PARAMETER)"

        val TAG: String = JavaScriptInjectionFragment::class.java.canonicalName ?: JavaScriptInjectionFragment::class.java.name

        fun newInstance() = JavaScriptInjectionFragment()

    }

}
