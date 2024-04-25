package dev.gerlot.securewebview.sample.demos

import android.graphics.Bitmap
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import dev.gerlot.securewebview.sample.R
import dev.gerlot.securewebview.sample.SecurableWebViewFragment
import dev.gerlot.securewebview.sample.WebViewSecureState
import dev.gerlot.securewebview.sample.databinding.ClearTextTrafficFragmentBinding
import dev.gerlot.securewebview.sample.util.makeClearableEditText

class ClearTextTrafficFragment  : Fragment(), SecurableWebViewFragment {

    private var _binding: ClearTextTrafficFragmentBinding? = null
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
        _binding = ClearTextTrafficFragmentBinding.inflate(inflater, container, false)
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
        binding.secureWebView.setWebViewClient(object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                currentUrl = url
                view ?: return
                binding.urlInput.setText(url)
            }

        })

        binding.urlInput.setImeActionLabel(resources.getString(R.string.load_url), KeyEvent.KEYCODE_ENTER)
        binding.urlInput.setOnEditorActionListener { _, actionId, event ->
            if (actionId == KeyEvent.KEYCODE_ENTER || event?.keyCode == KeyEvent.KEYCODE_ENTER) {
                loadUrl(binding.urlInput.text.toString())
                true
            } else {
                false
            }
        }
        binding.urlInput.makeClearableEditText()

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

        private const val INITIAL_URL = "http://neverssl.com"

        val TAG: String = ClearTextTrafficFragment::class.java.canonicalName ?: ClearTextTrafficFragment::class.java.name

        fun newInstance() = ClearTextTrafficFragment()

    }

}
