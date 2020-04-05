package com.codeparams.hotelsearch.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.codeparams.hotelsearch.R
import com.codeparams.hotelsearch.data.HotelSortBy
import com.codeparams.hotelsearch.databinding.MainActivityBinding
import com.codeparams.hotelsearch.ui.list.ListFragment
import com.codeparams.hotelsearch.ui.map.MapFragment
import com.google.android.gms.security.ProviderInstaller
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: MainActivityViewModel

    private var menu: Menu? = null

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(BUNDLE_KEY_SORT_BY, viewModel.sortBy)
        outState.putInt(BUNDLE_KEY_NAV_LOCATION, viewModel.navLocation)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)

        // https://stackoverflow.com/a/30302235
        ProviderInstaller.installIfNeeded(applicationContext)

        val savedSortBy = savedInstanceState?.getSerializable(BUNDLE_KEY_SORT_BY) as? HotelSortBy
        val navLocation = savedInstanceState?.getInt(BUNDLE_KEY_NAV_LOCATION)

        viewModel = ViewModelProvider(this, viewModelFactory).get(MainActivityViewModel::class.java)
        viewModel.init(savedSortBy, navLocation)

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.main_activity
        )

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.app_full_title)
        supportActionBar?.subtitle = " " // placeholder, will be overridden by fragments

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.container, ListFragment.newInstance(),
                    FRAGMENT_TAG_LIST
                )
                .commitNow()
        }

        viewModel.toggleViewLiveData.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { toggleView(it) }
        })
    }

    private fun toggleView(navId: Int) {
        val fragmentList = supportFragmentManager.findFragmentByTag(FRAGMENT_TAG_LIST)
        val fragmentMap = supportFragmentManager.findFragmentByTag(FRAGMENT_TAG_MAP)

        val isMap = navId == MainActivityViewModel.NAV_LOCATION_MAP
        val outgoing = if (isMap) fragmentList else fragmentMap
        val incoming = if (isMap) fragmentMap else fragmentList

        val transaction = supportFragmentManager.beginTransaction()
        transaction.setReorderingAllowed(true)
        transaction.setCustomAnimations(
            R.anim.fade_in,
            R.anim.fade_out
        )

        outgoing?.let { transaction.detach(it) }
        if (incoming == null) {
            val newInstance =
                if (isMap) MapFragment.Companion::newInstance
                else ListFragment.Companion::newInstance
            transaction.replace(
                R.id.container, newInstance.invoke(),
                FRAGMENT_TAG_MAP
            )
        } else {
            transaction.attach(incoming)
        }

        transaction.commit()
    }

    private fun updateMenuIcons() {
        val navId = viewModel.navLocation
        menu?.findItem(R.id.action_toggle_map)
            ?.setIcon(if (navId == MainActivityViewModel.NAV_LOCATION_LIST) R.drawable.ic_map_24dp else R.drawable.ic_list_24dp)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.menu_main_activity, menu)
        updateMenuIcons()
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_toggle_map) {
            viewModel.toggleView()
            updateMenuIcons()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val BUNDLE_KEY_SORT_BY = "sort_by"
        const val BUNDLE_KEY_NAV_LOCATION = "nav_location"

        const val FRAGMENT_TAG_MAP = "map"
        const val FRAGMENT_TAG_LIST = "list"
    }
}
