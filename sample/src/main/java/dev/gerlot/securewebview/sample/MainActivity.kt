package dev.gerlot.securewebview.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import dev.gerlot.securewebview.sample.databinding.MainActivityBinding
import dev.gerlot.securewebview.sample.demos.BreakoutFragment
import dev.gerlot.securewebview.sample.demos.ClearTextTrafficFragment
import dev.gerlot.securewebview.sample.demos.ContentProviderAccessFragment
import dev.gerlot.securewebview.sample.demos.FileAccessFragment
import dev.gerlot.securewebview.sample.demos.JavaScriptBreakoutFragment
import dev.gerlot.securewebview.sample.demos.JavaScriptInjectionFragment
import dev.gerlot.securewebview.sample.demos.OpaqueOriginFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    private var currentlyDisplayedFragmentTag: String = BreakoutFragment.TAG

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBar.toolbar)
        val toggle = ActionBarDrawerToggle(this, binding.drawerLayout, binding.appBar.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_closed)
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
            val securableWebViewFragment: SecurableWebViewFragment? = (supportFragmentManager.findFragmentByTag(currentlyDisplayedFragmentTag) as? SecurableWebViewFragment)
            if (isChecked) {
                securableWebViewFragment?.secureWebView()
            } else {
                securableWebViewFragment?.unSecureWebView()
            }
        }

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, BreakoutFragment.newInstance(), BreakoutFragment.TAG)
                .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.info) {
            InfoBottomSheetFragment.newInstance(currentlyDisplayedFragmentTag).show(supportFragmentManager, InfoBottomSheetFragment.TAG)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showPage(fragment: Fragment, tag: String) {
        currentlyDisplayedFragmentTag = tag
        if (binding.appBar.secureWebViewSwitch.isChecked) {
            (fragment as? SecurableWebViewFragment)?.secureWebView()
        } else {
            (fragment as? SecurableWebViewFragment)?.unSecureWebView()
        }
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment, tag)
            .commit()
        binding.drawerLayout.closeDrawer(GravityCompat.START)
    }

}
