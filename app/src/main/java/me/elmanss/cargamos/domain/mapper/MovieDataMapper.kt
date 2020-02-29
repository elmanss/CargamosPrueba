package me.elmanss.cargamos.domain.mapper

import me.elmanss.cargamos.data.Movie
import me.elmanss.cargamos.data.network.RemoteMovie
import me.elmanss.cargamos.domain.models.MovieModel

/**
 * Interfaz de Mapper para objetos de capa de datos a modelos de logica de negocio
 */
interface MovieDataMapper {
    fun movieModelFromNetwork(remoteMovie: RemoteMovie): MovieModel
    fun movieModelFromDb(movie: Movie): MovieModel
    fun movieModelListFromNetwork(remoteMovies: List<RemoteMovie>): List<MovieModel>
    fun movieModelListFromDb(movies: List<Movie>): List<MovieModel>
}

/**
 * Implementacion MovieDataMapper que migra a modelos desde capa de datos tanto para objetos obtenidos de red como
 * de base de datos
 */
class MovieDataMapperImpl : MovieDataMapper {
    override fun movieModelFromNetwork(remoteMovie: RemoteMovie): MovieModel {
        return MovieModel(
            0,
            remoteMovie.id.toLong(),
            remoteMovie.title ?: "MISSING",
            remoteMovie.overview ?: "MISSING",
            remoteMovie.posterPath ?: "MISSING",
            remoteMovie.voteAverage,
            true
        )
    }

    override fun movieModelFromDb(movie: Movie): MovieModel {
        return MovieModel(
            movie.id,
            movie.remote_id,
            movie.title,
            movie.overview,
            movie.poster_path,
            movie.vote_avg,
            false
        )
    }

    override fun movieModelListFromNetwork(remoteMovies: List<RemoteMovie>): List<MovieModel> {
        return remoteMovies.map {
            movieModelFromNetwork(it)
        }
    }

    override fun movieModelListFromDb(movies: List<Movie>): List<MovieModel> {
        return movies.map {
            movieModelFromDb(it)
        }
    }
}