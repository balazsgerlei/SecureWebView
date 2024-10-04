package dev.gerlot.securewebview.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.gerlot.securewebview.sample.databinding.InfoBottomSheetFragmentBinding
import dev.gerlot.securewebview.sample.demos.BreakoutFragment
import dev.gerlot.securewebview.sample.demos.ClearTextTrafficFragment
import dev.gerlot.securewebview.sample.demos.ContentProviderAccessFragment
import dev.gerlot.securewebview.sample.demos.FileAccessFragment
import dev.gerlot.securewebview.sample.demos.JavaScriptBreakoutFragment
import dev.gerlot.securewebview.sample.demos.JavaScriptInjectionFragment
import dev.gerlot.securewebview.sample.demos.OpaqueOriginFragment

private const val ARG_CURRENTLY_DISPLAYED_FRAGMENT_TAG = "currently_displayed_fragment_tag"

class InfoBottomSheetFragment: BottomSheetDialogFragment() {

    private var currentlyDisplayedFragmentTag: String? = null

    private var _binding: InfoBottomSheetFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentlyDisplayedFragmentTag = arguments?.getString(ARG_CURRENTLY_DISPLAYED_FRAGMENT_TAG)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (_binding == null) _binding = InfoBottomSheetFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val titleText: String = when(currentlyDisplayedFragmentTag) {
            BreakoutFragment.TAG -> {
                getString(R.string.breakout_demo)
            }
            JavaScriptBreakoutFragment.TAG -> {
                getString(R.string.javascript_breakout_demo)
            }
            ClearTextTrafficFragment.TAG -> {
                getString(R.string.clear_text_traffic_demo)
            }
            FileAccessFragment.TAG -> {
                getString(R.string.file_access_demo)
            }
            ContentProviderAccessFragment.TAG -> {
                getString(R.string.content_provider_access_demo)
            }
            OpaqueOriginFragment.TAG -> {
                getString(R.string.opaque_origin_demo)
            }
            JavaScriptInjectionFragment.TAG -> {
                getString(R.string.javascript_evaluation_demo)
            }
            else -> { "" }
        }
        binding.titleTv.text = titleText


        val descriptionText: String = when(currentlyDisplayedFragmentTag) {
            BreakoutFragment.TAG -> {
                getString(R.string.breakout_demo_description)
            }
            JavaScriptBreakoutFragment.TAG -> {
                getString(R.string.javascript_breakout_demo_description)
            }
            ClearTextTrafficFragment.TAG -> {
                getString(R.string.clear_text_traffic_demo_description)
            }
            FileAccessFragment.TAG -> {
                getString(R.string.file_access_demo_description)
            }
            ContentProviderAccessFragment.TAG -> {
                getString(R.string.content_provider_access_demo_description)
            }
            OpaqueOriginFragment.TAG -> {
                getString(R.string.opaque_origin_demo_description)
            }
            JavaScriptInjectionFragment.TAG -> {
                getString(R.string.javascript_evaluation_demo_description)
            }
            else -> { "" }
        }
        binding.descriptionTv.text = descriptionText
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        val TAG: String = InfoBottomSheetFragment::class.java.canonicalName ?: InfoBottomSheetFragment::class.java.name

        fun newInstance(currentlyDisplayedPage: String) = InfoBottomSheetFragment().apply {
            arguments = bundleOf(ARG_CURRENTLY_DISPLAYED_FRAGMENT_TAG to currentlyDisplayedPage)
        }

    }

}
