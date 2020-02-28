package me.elmanss.cargamos.presentation.splash

import android.content.SharedPreferences
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import me.elmanss.cargamos.data.network.MovieAPI
import me.elmanss.cargamos.di.scope.PerActivity

/**
 * cgs on 25/02/20.
 */
@Module
class SplashModule(val activity: SplashActivity) {

    @PerActivity
    @Provides
    fun providePresenter(
        api: MovieAPI,
        gson: Gson,
        preferences: SharedPreferences
    ): SplashPresenter {
        return SplashPresenterImpl(
            api,
            gson,
            preferences,
            activity
        )
    }
}

@Subcomponent(modules = [SplashModule::class])
@PerActivity
interface SplashSubcomponent {
    fun inject(activity: SplashActivity)
}