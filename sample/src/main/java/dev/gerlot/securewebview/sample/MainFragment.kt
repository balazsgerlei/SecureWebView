package dev.gerlot.securewebview.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import dev.gerlot.securewebview.sample.databinding.MainFragmentBinding
import dev.gerlot.securewebview.sample.demos.BreakoutFragment
import dev.gerlot.securewebview.sample.demos.ClearTextTrafficFragment
import dev.gerlot.securewebview.sample.demos.ContentProviderAccessFragment
import dev.gerlot.securewebview.sample.demos.FileAccessFragment
import dev.gerlot.securewebview.sample.demos.JavaScriptBreakoutFragment
import dev.gerlot.securewebview.sample.demos.JavaScriptInjectionFragment
import dev.gerlot.securewebview.sample.demos.OpaqueOriginFragment

class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private var currentlyDisplayedFragmentTag: String = BreakoutFragment.TAG

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as? AppCompatActivity)?.setSupportActionBar(binding.appBar.toolbar)

        val toggle = ActionBarDrawerToggle(requireActivity(), binding.drawerLayout, binding.appBar.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_closed)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.breakout -> showPage(BreakoutFragment.newInstance(), BreakoutFragment.TAG)
                R.id.javascript_breakout -> showPage(JavaScriptBreakoutFragment.newInstance(), JavaScriptBreakoutFragment.TAG)
                R.id.cleartexttraffic -> showPage(ClearTextTrafficFragment.newInstance(), ClearTextTrafficFragment.TAG)
                R.id.file_access -> showPage(FileAccessFragment.newInstance(), FileAccessFragment.TAG)
                R.id.content_provider_access -> showPage(ContentProviderAccessFragment.newInstance(), ContentProviderAccessFragment.TAG)
                R.id.opaque_origin -> showPage(OpaqueOriginFragment.newInstance(), OpaqueOriginFragment.TAG)
                R.id.javascript_evaluation -> showPage(JavaScriptInjectionFragment.newInstance(), JavaScriptInjectionFragment.TAG)
            }
            true
        }

        binding.appBar.secureWebViewSwitch.setOnCheckedChangeListener { _, isChecked ->
            val securableWebViewFragment: SecurableWebViewFragment? = (childFragmentManager.findFragmentByTag(currentlyDisplayedFragmentTag) as? SecurableWebViewFragment)
            if (isChecked) {
                securableWebViewFragment?.secureWebView()
            } else {
                securableWebViewFragment?.unSecureWebView()
            }
        }

        if (savedInstanceState == null) {
            childFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, BreakoutFragment.newInstance(), BreakoutFragment.TAG)
                .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.info) {
            InfoBottomSheetFragment.newInstance(currentlyDisplayedFragmentTag).show(childFragmentManager, InfoBottomSheetFragment.TAG)
            return true
        }
        return false
    }

    private fun showPage(fragment: Fragment, tag: String) {
        currentlyDisplayedFragmentTag = tag
        if (binding.appBar.secureWebViewSwitch.isChecked) {
            (fragment as? SecurableWebViewFragment)?.secureWebView()
        } else {
            (fragment as? SecurableWebViewFragment)?.unSecureWebView()
        }
        childFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment, tag)
            .commit()
        binding.drawerLayout.closeDrawer(GravityCompat.START)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        val TAG: String = MainFragment::class.java.canonicalName ?: MainFragment::class.java.name

        fun newInstance() = MainFragment()

    }
}
