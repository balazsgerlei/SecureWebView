package dev.gerlot.securewebview.sample.demos

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import dev.gerlot.securewebview.sample.R
import dev.gerlot.securewebview.sample.SecurableWebViewFragment
import dev.gerlot.securewebview.sample.WebViewSecureState
import dev.gerlot.securewebview.sample.databinding.FileAccessFragmentBinding
import dev.gerlot.securewebview.sample.util.hideKeyboard
import dev.gerlot.securewebview.sample.util.makeClearableEditText
import dev.gerlot.securewebview.sample.util.setOnDoneActionListener

class FileAccessFragment : Fragment(), SecurableWebViewFragment {

    private var _binding: FileAccessFragmentBinding? = null
    private val binding get() = _binding!!

    private var webViewSecureState = WebViewSecureState.INSECURE

    private var currentUrl: String? = null

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                loadUrl(INITIAL_URI)
            } else {
                // Explain to the user that the feature is unavailable because the
                // feature requires a permission that the user has denied.
                // Won't implement this for a sample app
            }
        }


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
        _binding = FileAccessFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // For demonstrative purposes we explicitly enable file access on versions
        // where it is disabled by default, otherwise (on Android 9 and below)
        // we rely on the default false value
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            binding.insecureWebView.settings.allowFileAccess = true
        }
        binding.insecureWebView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                currentUrl = url
                view ?: return
                _binding ?: return
                binding.urlInput.setText(url)
                super.onPageFinished(view, url)
            }

        }
        binding.secureWebView.setWebViewClient(object : WebViewClient() {

            override fun onPageFinished(view: WebView?, url: String?) {
                currentUrl = url
                view ?: return
                _binding ?: return
                binding.urlInput.setText(url)
                super.onPageFinished(view, url)
            }

        })

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
            val permissionToUse = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_IMAGES else Manifest.permission.READ_EXTERNAL_STORAGE
            if (ContextCompat.checkSelfPermission(requireContext(), permissionToUse) == PackageManager.PERMISSION_GRANTED) {
                loadUrl(INITIAL_URI)
            } else if (shouldShowRequestPermissionRationale(permissionToUse)) {
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected, and what
                // features are disabled if it's declined.
                // Won't implement this for a sample app
            } else {
                requestPermissionLauncher.launch(permissionToUse)
            }
        }
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

        private const val INITIAL_URI = "file:///storage/emulated/0/Download/android_robot.png"

        val TAG: String = FileAccessFragment::class.java.canonicalName ?: FileAccessFragment::class.java.name

        fun newInstance() = FileAccessFragment()

    }

}
