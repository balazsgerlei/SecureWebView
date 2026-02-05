package dev.gerlot.securewebview.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
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
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Don't show the UI behind system bars and cutouts when the keyboard is open
        // as Extract UI doesn't extend behind them either. Showing the UI besides Extract UI
        // looks weird so its better to just show a simple background in these cases
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout())
            val isKeyboardVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            view.setPadding(
                if (isKeyboardVisible) systemBars.left else 0,
                0,
                if (isKeyboardVisible) systemBars.right else 0,
                0
            )
            insets // Return the original insets so other views can use them
        }
        // Shift the content of the Toolbar to accommodate system bars and cutouts
        // as this component doesn't do this automatically for some reason.
        ViewCompat.setOnApplyWindowInsetsListener(binding.appBar.toolbar) { view, insets ->
            val cutoutInsets = insets.getInsets(WindowInsetsCompat.Type.displayCutout())
            view.updatePadding(
                left = cutoutInsets.left,
                right = cutoutInsets.right,
                // The top padding is handled by the AppBarLayout/System
            )
            insets // Return the original insets so other views can use them
        }
        // Shift the content of the NavigationView to accommodate system bars and cutouts
        // as this component doesn't do this automatically for some reason.
        ViewCompat.setOnApplyWindowInsetsListener(binding.navigationView) { view, insets ->
            val systemBarInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout())
            view.updatePadding(
                left = systemBarInsets.left,
                top = systemBarInsets.top,
                right = systemBarInsets.right,
                // The top padding is handled by the AppBarLayout/System
            )
            insets // Return the original insets so other views can use them
        }

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
