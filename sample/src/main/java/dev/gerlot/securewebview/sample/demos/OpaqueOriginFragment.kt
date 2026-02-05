package dev.gerlot.securewebview.sample.demos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dev.gerlot.securewebview.sample.SecurableWebViewFragment
import dev.gerlot.securewebview.sample.WebViewSecureState
import dev.gerlot.securewebview.sample.databinding.OpaqueOriginFragmentBinding
import java.io.BufferedReader
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class OpaqueOriginFragment: Fragment(), SecurableWebViewFragment {

    private var _binding: OpaqueOriginFragmentBinding? = null
    private val binding get() = _binding!!

    private var webViewSecureState = WebViewSecureState.INSECURE

    private var currentlyLoadedData: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = OpaqueOriginFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Due to how this demo is implemented (we use JavaScript fetch call
        // to demonstrate calling into another page to trigger same origin check)
        // we need to enable JavaScript, but it's not required for loadData itself
        binding.insecureWebView.settings.javaScriptEnabled = true
        binding.secureWebView.javaScriptEnabled = true

        // Due to how this demo is implemented (we load the initial HTML that contains
        // the JavaScript that fetches the resource) we need to allow loading data URIs
        binding.secureWebView.allowLoadingDataUri = true

        if (webViewSecureState == WebViewSecureState.INSECURE) {
            binding.viewFlipper.displayedChild = 0
        } else {
            binding.viewFlipper.displayedChild = 1
        }

        if (savedInstanceState == null) {
            val html = requireContext().assets.open("same_origin_test.html").bufferedReader().use(BufferedReader::readText)
            currentlyLoadedData = html
            loadData(html)
        } else if (webViewSecureState == WebViewSecureState.INSECURE) {
            binding.insecureWebView.restoreState(savedInstanceState)
        } else {
            binding.secureWebView.restoreState(savedInstanceState)
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

    override fun secureWebView() {
        webViewSecureState = WebViewSecureState.SECURE
        if (view != null) {
            binding.viewFlipper.displayedChild = 1
            currentlyLoadedData?.let {
                loadData(it)
            }
        }
    }

    override fun unSecureWebView() {
        webViewSecureState = WebViewSecureState.INSECURE
        if (view != null) {
            binding.viewFlipper.displayedChild = 0
            currentlyLoadedData?.let {
                loadData(it)
            }
        }
    }

    companion object {

        val TAG: String = OpaqueOriginFragment::class.java.canonicalName ?: OpaqueOriginFragment::class.java.name

        fun newInstance() = OpaqueOriginFragment()

    }

}
