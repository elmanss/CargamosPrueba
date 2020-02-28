package me.elmanss.cargamos.presentation.list.local

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
    ): LocalListPresenter {
        return LocalListPresenterImpl(
            interactor,
            preferences,
            gson,
            activity
        )
    }

}

@PerActivity
@Subcomponent(modules = [LocalListModule::class])
interface LocalListSubcomponent {
    fun inject(activity: LocalListActivity)
}


