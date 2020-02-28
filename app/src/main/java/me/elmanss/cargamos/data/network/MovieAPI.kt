package me.elmanss.cargamos.data.network

import io.reactivex.Observable
import io.reactivex.Single
import me.elmanss.cargamos.util.Constants
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * cgs on 25/02/20.
 */
interface MovieAPI {

    companion object {
        fun get(r: Retrofit): MovieAPI {
            return r.create(MovieAPI::class.java)
        }
    }

    /**
     * Por medio de este metodo obtenemos los metadatos requeridos para formar las urls correspondientes a los posters de las peliculas.
     * Idealmente se llamará al cargar la APP y el resultado se conservara en almacenamiento local (prefs/db)
     */
    @GET("configuration?api_key=${Constants.API_KEY}")
    fun getConfig(): Single<ConfigResult>


    /**
     * Metodo utilizado para descargar lista paginada de peliculas, recibe como parametro la page (pagina) a obtener
     */
    @GET("discover/movie?api_key=${Constants.API_KEY}&language=es-MX")
    fun getMovies(@Query("page") page: Int): Observable<RemoteMovieResult>
}