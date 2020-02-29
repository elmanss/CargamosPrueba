package me.elmanss.cargamos.presentation.list.main

import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import me.elmanss.cargamos.data.local.ConfigRepository
import me.elmanss.cargamos.data.network.MovieAPI
import me.elmanss.cargamos.di.scope.PerActivity
import me.elmanss.cargamos.domain.interactor.NetworkMovieInteractor
import me.elmanss.cargamos.domain.mapper.ConfigDataMapper
import me.elmanss.cargamos.domain.mapper.MovieDataMapper
import me.elmanss.cargamos.domain.mapper.MovieDataMapperImpl

/**
 * cgs on 25/02/20.
 */
@Module
class MainModule(val activity: MainActivity) {

    @PerActivity
    @Provides
    fun provideMapper(): MovieDataMapper {
        return MovieDataMapperImpl()
    }

    @PerActivity
    @Provides
    fun provideInteractor(
        api: MovieAPI, mapper: MovieDataMapper,
        configRepository: ConfigRepository, configDataMapper: ConfigDataMapper
    ): NetworkMovieInteractor {
        return NetworkMovieInteractor(
            api,
            mapper,
            configRepository,
            configDataMapper
        )
    }

    @PerActivity
    @Provides
    fun providePresenter(
        interactor: NetworkMovieInteractor
    ): MainPresenter {
        return MainPresenterImpl(
            interactor,
            activity
        )
    }
}

@PerActivity
@Subcomponent(modules = [MainModule::class])
interface MainSubcomponent {
    fun inject(activity: MainActivity)
}