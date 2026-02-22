package dev.gerlot.securewebview.sample.demos

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import dev.gerlot.securewebview.sample.R
import dev.gerlot.securewebview.sample.SecurableWebViewFragment
import dev.gerlot.securewebview.sample.WebViewSecureState
import dev.gerlot.securewebview.sample.databinding.JavascriptInjectionFragmentBinding
import dev.gerlot.securewebview.sample.util.hideKeyboard
import dev.gerlot.securewebview.sample.util.makeClearableEditText
import dev.gerlot.securewebview.sample.util.setOnDoneActionListener

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

            override fun onPageFinished(view: WebView?, url: String?) {
                currentUrl = url
                view ?: return
                _binding ?: return
                binding.urlInput.setText(url)
                super.onPageFinished(view, url)
            }

        }
        binding.insecureWebView.webChromeClient = WebChromeClient()

        binding.secureWebView.javaScriptEnabled = true
        binding.secureWebView.setWebViewClient(object : WebViewClient() {

            override fun onPageFinished(view: WebView?, url: String?) {
                currentUrl = url
                view ?: return
                _binding ?: return
                binding.urlInput.setText(url)
                super.onPageFinished(view, url)
            }

        })
        binding.secureWebView.setWebChromeClient(WebChromeClient())

        binding.urlInput.setImeActionLabel(resources.getString(R.string.load_url), KeyEvent.KEYCODE_ENTER)
        binding.urlInput.setOnDoneActionListener {
            loadUrl(binding.urlInput.text.toString())
            binding.urlInput.clearFocus()
            false
        }
        binding.urlInput.makeClearableEditText()

        if (webViewSecureState == WebViewSecureState.INSECURE) {
            binding.viewFlipper.displayedChild = 0
        } else {
            binding.viewFlipper.displayedChild = 1
        }

        if (savedInstanceState == null) {
            loadUrl(INITIAL_URI)
        } else if (webViewSecureState == WebViewSecureState.INSECURE) {
            binding.insecureWebView.restoreState(savedInstanceState)
        } else {
            binding.secureWebView.restoreState(savedInstanceState)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (webViewSecureState == WebViewSecureState.INSECURE) {
            binding.insecureWebView.saveState(outState)
        } else {
            binding.secureWebView.saveState(outState)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.hideKeyboard()
        _binding = null
    }

    private fun loadUrl(url: String) {
        currentUrl = url
        if (url != binding.urlInput.text.toString()) {
            binding.urlInput.setText(url)
        }
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

        private const val INITIAL_URI = "javascript:void(alert(\"Hello World\"))"

        val TAG: String = JavaScriptInjectionFragment::class.java.canonicalName ?: JavaScriptInjectionFragment::class.java.name

        fun newInstance() = JavaScriptInjectionFragment()

    }

}
