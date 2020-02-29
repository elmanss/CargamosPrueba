package me.elmanss.cargamos.domain.interactor

import com.squareup.sqldelight.runtime.rx.asObservable
import io.reactivex.Observable
import me.elmanss.cargamos.data.MovieQueries
import me.elmanss.cargamos.data.local.ConfigRepository
import me.elmanss.cargamos.domain.mapper.ConfigDataMapper
import me.elmanss.cargamos.domain.models.ConfigModel
import me.elmanss.cargamos.domain.models.MovieModel

/**
 * cgs on 28/02/20.
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
                movieModel.remoteId,
                movieModel.title,
                movieModel.overview,
                movieModel.posterPath,
                movieModel.score
            )
        }
    }

    fun selectLatestMovie(): Observable<Long> {
        return queries.lastInsertRowId().asObservable().map { it.executeAsOne() }
    }

    fun deleteMovie(movieModel: MovieModel): Observable<Any> {
        return Observable.fromCallable { queries.deleteMovie(movieModel.id) }
    }

    fun findRemoteId(remoteId: Long): Observable<Long> {
        return queries.selectIdByRemoteId(remoteId).asObservable().map { it.executeAsOne() }
    }
}