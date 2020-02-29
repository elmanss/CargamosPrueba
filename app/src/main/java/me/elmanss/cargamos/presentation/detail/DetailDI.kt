package me.elmanss.cargamos.presentation.detail

import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import me.elmanss.cargamos.Database
import me.elmanss.cargamos.data.MovieQueries
import me.elmanss.cargamos.data.local.ConfigRepository
import me.elmanss.cargamos.di.scope.PerActivity
import me.elmanss.cargamos.domain.interactor.DetailInteractor
import me.elmanss.cargamos.domain.mapper.ConfigDataMapper
import me.elmanss.cargamos.domain.models.MovieModel

/**
 * cgs on 25/02/20.
 */

@Module
class DetailModule(val activity: DetailActivity) {

    @PerActivity
    @Provides
    fun provideModel(): MovieModel {
        return activity.intent?.getParcelableExtra("model")!!
    }

    @PerActivity
    @Provides
    fun provideQueries(database: Database): MovieQueries {
        return database.movieQueries
    }

    @PerActivity
    @Provides
    fun provideInteractor(
        queries: MovieQueries,
        repository: ConfigRepository,
        configDataMapper: ConfigDataMapper
    ): DetailInteractor {
        return DetailInteractor(
            queries,
            repository,
            configDataMapper
        )
    }

    @PerActivity
    @Provides
    fun providePresenter(
        interactor: DetailInteractor
    ): DetailPresenter {
        return DetailPresenterImpl(
            interactor,
            activity
        )
    }
}

@PerActivity
@Subcomponent(modules = [DetailModule::class])
interface DetailSubcomponent {
    fun inject(activity: DetailActivity)
}