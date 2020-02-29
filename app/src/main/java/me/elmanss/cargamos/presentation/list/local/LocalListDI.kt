package me.elmanss.cargamos.presentation.list.local

import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import me.elmanss.cargamos.Database
import me.elmanss.cargamos.data.MovieQueries
import me.elmanss.cargamos.data.local.ConfigRepository
import me.elmanss.cargamos.di.scope.PerActivity
import me.elmanss.cargamos.domain.interactor.LocalMovieInteractor
import me.elmanss.cargamos.domain.mapper.ConfigDataMapper
import me.elmanss.cargamos.domain.mapper.MovieDataMapper
import me.elmanss.cargamos.domain.mapper.MovieDataMapperImpl

/**
 * cgs on 26/02/20.
 */

@Module
class LocalListModule(val activity: LocalListActivity) {

    @PerActivity
    @Provides
    fun provideQueries(database: Database): MovieQueries {
        return database.movieQueries
    }

    @PerActivity
    @Provides
    fun provideModel(): LocalListModel {
        return LocalListModel()
    }

    @PerActivity
    @Provides
    fun provideMapper(): MovieDataMapper {
        return MovieDataMapperImpl()
    }

    @PerActivity
    @Provides
    fun provideInteractor(
        queries: MovieQueries,
        mapper: MovieDataMapper,
        configRepository: ConfigRepository,
        configDataMapper: ConfigDataMapper
    ): LocalMovieInteractor {
        return LocalMovieInteractor(
            queries,
            mapper,
            configRepository,
            configDataMapper
        )
    }

    @PerActivity
    @Provides
    fun providePresenter(
        interactor: LocalMovieInteractor
    ): LocalListPresenter {
        return LocalListPresenterImpl(
            interactor,
            activity
        )
    }

}

@PerActivity
@Subcomponent(modules = [LocalListModule::class])
interface LocalListSubcomponent {
    fun inject(activity: LocalListActivity)
}


