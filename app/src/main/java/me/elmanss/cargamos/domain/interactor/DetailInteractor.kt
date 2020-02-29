package me.elmanss.cargamos.domain.interactor

import io.reactivex.Observable
import me.elmanss.cargamos.data.MovieQueries
import me.elmanss.cargamos.data.local.ConfigRepository
import me.elmanss.cargamos.domain.mapper.ConfigDataMapper
import me.elmanss.cargamos.domain.models.ConfigModel
import me.elmanss.cargamos.domain.models.MovieModel

/**
 * ADDCEL on 28/02/20.
 */
class DetailInteractor(
    val queries: MovieQueries,
    val configRepository: ConfigRepository,
    val configDataMapper: ConfigDataMapper
) {

    fun getConfig(): ConfigModel {
        return configDataMapper.configModelFromRepository(configRepository.getConfig())
    }

    fun insertMovie(movieModel: MovieModel): Observable<Any> {
        return Observable.fromCallable {
            queries.insertMovie(
                movieModel.title,
                movieModel.overview,
                movieModel.posterPath,
                movieModel.score
            )
        }
    }

    fun selectLatestMovie(): Long {
        return queries.lastInsertRowId().executeAsOne()
    }

    fun deleteMovie(movieModel: MovieModel): Observable<Any> {
        return Observable.fromCallable { queries.deleteMovie(movieModel.id) }
    }
}