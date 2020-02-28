package me.elmanss.cargamos.presentation.detail

import android.content.SharedPreferences
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import me.elmanss.cargamos.Database
import me.elmanss.cargamos.data.MovieQueries
import me.elmanss.cargamos.di.scope.PerActivity
import me.elmanss.cargamos.domain.interactor.LocalMovieInteractor
import me.elmanss.cargamos.domain.mapper.MovieDataMapper
import me.elmanss.cargamos.domain.mapper.MovieDataMapperImpl
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
    fun provideMapper(): MovieDataMapper {
        return MovieDataMapperImpl()
    }

    @PerActivity
    @Provides
    fun provideInteractor(queries: MovieQueries, mapper: MovieDataMapper): LocalMovieInteractor {
        return LocalMovieInteractor(
            queries,
            mapper
        )
    }

    @PerActivity
    @Provides
    fun providePresenter(
        interactor: LocalMovieInteractor,
        preferences: SharedPreferences,
        gson: Gson
    ): DetailPresenter {
        return DetailPresenterImpl(
            interactor,
            preferences,
            gson,
            activity
        )
    }
}

@PerActivity
@Subcomponent(modules = [DetailModule::class])
interface DetailSubcomponent {
    fun inject(activity: DetailActivity)
}