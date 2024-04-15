package dev.gerlot.securewebview.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.gerlot.securewebview.sample.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, MainFragment.newInstance(), MainFragment.TAG)
                .commit()
        }
    }

}
