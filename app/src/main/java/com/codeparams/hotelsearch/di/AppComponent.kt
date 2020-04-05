package com.codeparams.hotelsearch.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.codeparams.hotelsearch.MyApp
import com.codeparams.hotelsearch.data.ApiInterface
import com.codeparams.hotelsearch.data.AppDatabase
import com.codeparams.hotelsearch.data.DbTypeConverters
import com.codeparams.hotelsearch.ui.MainActivity
import com.codeparams.hotelsearch.ui.MainSubModule
import com.codeparams.hotelsearch.ui.list.ListFragmentSubModule
import com.codeparams.hotelsearch.ui.map.MapFragmentSubModule
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        ActivityBindingModule::class
    ]
)
interface AppComponent : AndroidInjector<MyApp> {
    @Component.Factory
    abstract class Factory : AndroidInjector.Factory<MyApp>
}

@Module
class AppModule {
    @Provides
    internal fun provideContext(application: MyApp): Context {
        return application.applicationContext
    }

    @Singleton
    @Provides
    fun provideDb(app: MyApp): AppDatabase {
        return Room.databaseBuilder(app, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideApiInterface(): ApiInterface {
        return Retrofit.Builder()
            .baseUrl("http://localhost/") // required but overridden by string resource during api call
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(DbTypeConverters).build()
                )
            )
            .callFactory(OkHttpClient.Builder().build())
            .build()
            .create(ApiInterface::class.java)
    }
}

@Module
abstract class ActivityBindingModule {
    @ScopeActivity
    @ContributesAndroidInjector(modules = [ViewModelSubModule::class, MainSubModule::class, ListFragmentSubModule::class, MapFragmentSubModule::class])
    abstract fun contributeMainActivity(): MainActivity
}

@Module
abstract class ViewModelSubModule {
    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}