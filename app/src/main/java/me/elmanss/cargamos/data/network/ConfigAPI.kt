package me.elmanss.cargamos.data.network

import io.reactivex.Single
import me.elmanss.cargamos.util.Constants
import retrofit2.Retrofit
import retrofit2.http.GET

/**
 * cgs on 28/02/20.
 */
interface ConfigAPI {

    companion object {
        fun get(r: Retrofit): ConfigAPI {
            return r.create(ConfigAPI::class.java)
        }
    }


    /**
     * Por medio de este metodo obtenemos los metadatos requeridos para formar las urls correspondientes a los posters de las peliculas.
     * Idealmente se llamará al cargar la APP y el resultado se conservara en almacenamiento local (prefs/db)
     */
    @GET("configuration?api_key=${Constants.API_KEY}")
    fun getConfig(): Single<ConfigResult>

}