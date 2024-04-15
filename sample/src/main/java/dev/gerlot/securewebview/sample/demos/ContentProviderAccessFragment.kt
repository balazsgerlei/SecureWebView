package dev.gerlot.securewebview.sample.demos

import android.graphics.Bitmap
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
import dev.gerlot.securewebview.sample.databinding.ContentProviderAccessFragmentBinding

class ContentProviderAccessFragment: Fragment(), SecurableWebViewFragment {

    private var _binding: ContentProviderAccessFragmentBinding? = null
    private val binding get() = _binding!!

    private var webViewSecureState = WebViewSecureState.INSECURE

    private var currentUrl: String? = null

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
        _binding = ContentProviderAccessFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.insecureWebView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                currentUrl = url
                binding.urlInput.setText(url)
            }

        }
        binding.secureWebView.setWebViewClient(object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                currentUrl = url
                binding.urlInput.setText(url)
            }

        })

        if (webViewSecureState == WebViewSecureState.INSECURE) {
            binding.viewFlipper.displayedChild = 0
        } else {
            binding.viewFlipper.displayedChild = 1
        }

        if (savedInstanceState == null) {
            currentUrl = INITIAL_URI
            loadUrl(INITIAL_URI)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadUrl(url: String) {
        if (webViewSecureState == WebViewSecureState.INSECURE) {
            binding.insecureWebView.loadUrl(url)
        } else {
            // To avoid escaping non-web URIs we need to use the method that does not try escaping JavaScript
            binding.secureWebView.loadUrlWithoutEscapingJavascript(url)
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

        private const val INITIAL_URI = "content://dev.gerlot.securewebview.sample.assets/android_robot.png"

        val TAG: String = ContentProviderAccessFragment::class.java.canonicalName ?: ContentProviderAccessFragment::class.java.name

        fun newInstance() = ContentProviderAccessFragment()

    }

}
