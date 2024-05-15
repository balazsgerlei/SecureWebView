package dev.gerlot.securewebview.sample.demos

import android.graphics.Bitmap
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import dev.gerlot.securewebview.sample.R
import dev.gerlot.securewebview.sample.SecurableWebViewFragment
import dev.gerlot.securewebview.sample.WebViewSecureState
import dev.gerlot.securewebview.sample.databinding.JavascriptInjectionFragmentBinding
import dev.gerlot.securewebview.sample.util.makeClearableEditText


class JavaScriptInjectionFragment: Fragment(), SecurableWebViewFragment {

    private var _binding: JavascriptInjectionFragmentBinding? = null
    private val binding get() = _binding!!

    private var webViewSecureState = WebViewSecureState.INSECURE

    private var currentUrl: String? = null

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
        binding.insecureWebView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                currentUrl = url
                view ?: return
                binding.urlInput.setText(url)
            }

        }
        binding.insecureWebView.webChromeClient = WebChromeClient()

        binding.secureWebView.javaScriptEnabled = true
        binding.secureWebView.setWebViewClient(object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                currentUrl = url
                view ?: return
                binding.urlInput.setText(url)
            }

        })
        binding.secureWebView.setWebChromeClient(WebChromeClient())

        binding.urlInput.setImeActionLabel(resources.getString(R.string.load_url), KeyEvent.KEYCODE_ENTER)
        binding.urlInput.setOnEditorActionListener { _, actionId, event ->
            if (actionId == KeyEvent.KEYCODE_ENTER || actionId == KeyEvent.KEYCODE_ENDCALL
                || event?.keyCode == KeyEvent.KEYCODE_ENTER) {
                loadUrl(binding.urlInput.text.toString())
            }
            false
        }
        binding.urlInput.makeClearableEditText()

        if (webViewSecureState == WebViewSecureState.INSECURE) {
            binding.viewFlipper.displayedChild = 0
        } else {
            binding.viewFlipper.displayedChild = 1
        }

        if (savedInstanceState == null) {
            currentUrl = INJECTED_JAVASCRIPT
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

        private const val MALICIOUS_PARAMETER = "alert(\"Hello World\")"

        private const val INJECTED_JAVASCRIPT = "javascript:void($MALICIOUS_PARAMETER)"

        val TAG: String = JavaScriptInjectionFragment::class.java.canonicalName ?: JavaScriptInjectionFragment::class.java.name

        fun newInstance() = JavaScriptInjectionFragment()

    }

}
