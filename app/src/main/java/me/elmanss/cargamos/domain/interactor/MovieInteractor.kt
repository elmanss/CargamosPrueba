package me.elmanss.cargamos.domain.interactor

import com.squareup.sqldelight.runtime.rx.asObservable
import io.reactivex.Observable
import me.elmanss.cargamos.data.MovieQueries
import me.elmanss.cargamos.data.network.MovieAPI
import me.elmanss.cargamos.domain.mapper.MovieDataMapper
import me.elmanss.cargamos.domain.models.MovieModel


/**
 * cgs on 27/02/20.
 */
class NetworkMovieInteractor(val api: MovieAPI, val mapper: MovieDataMapper) {

    fun getMoviesPaginated(page: Int): Observable<List<MovieModel>> {
        return api.getMovies(page).flatMap {
            Observable.fromCallable { mapper.movieModelListFromNetwork(it.results) }
        }
    }
}

class LocalMovieInteractor(val queries: MovieQueries, val mapper: MovieDataMapper) {
    fun getAllMovies(): Observable<List<MovieModel>> {
        return queries.selectAll().asObservable().flatMap {
            Observable.fromCallable { mapper.movieModelListFromDb(it.executeAsList()) }
        }
    }

    fun getMoviesFiltered(pattern: String): Observable<List<MovieModel>> {
        return queries.selectByTitle(pattern).asObservable().flatMap {
            Observable.fromCallable { mapper.movieModelListFromDb(it.executeAsList()) }
        }
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
