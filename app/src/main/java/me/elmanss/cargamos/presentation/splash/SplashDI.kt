package me.elmanss.cargamos.presentation.splash

import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import me.elmanss.cargamos.data.local.ConfigRepository
import me.elmanss.cargamos.data.network.ConfigAPI
import me.elmanss.cargamos.di.scope.PerActivity
import me.elmanss.cargamos.domain.interactor.SplashInteractor
import me.elmanss.cargamos.domain.interactor.SplashInteractorImpl
import me.elmanss.cargamos.domain.mapper.ConfigDataMapper
import retrofit2.Retrofit

/**
 * cgs on 25/02/20.
 */
@Module
class SplashModule(val activity: SplashActivity) {

    @PerActivity
    @Provides
    fun provideConfigApi(r: Retrofit): ConfigAPI {
        return ConfigAPI.get(r)
    }

    @PerActivity
    @Provides
    fun provideInteractor(
        api: ConfigAPI,
        mapper: ConfigDataMapper,
        repository: ConfigRepository
    ): SplashInteractor {
        return SplashInteractorImpl(api, mapper, repository)
    }

    @PerActivity
    @Provides
    fun providePresenter(
        interactor: SplashInteractor
    ): SplashPresenter {
        return SplashPresenterImpl(
            interactor,
            activity
        )
    }
}

@Subcomponent(modules = [SplashModule::class])
@PerActivity
interface SplashSubcomponent {
    fun inject(activity: SplashActivity)
}