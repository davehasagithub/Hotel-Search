package com.codeparams.hotelsearch.ui.map

import com.codeparams.hotelsearch.di.ScopeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MapFragmentSubModule {
    @ScopeFragment
    @ContributesAndroidInjector
    internal abstract fun contributeMapFragment(): MapFragment
}
