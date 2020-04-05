package com.codeparams.hotelsearch.ui.list

import com.codeparams.hotelsearch.di.ScopeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ListFragmentSubModule {
    @ScopeFragment
    @ContributesAndroidInjector
    internal abstract fun contributeListFragment(): ListFragment
}
