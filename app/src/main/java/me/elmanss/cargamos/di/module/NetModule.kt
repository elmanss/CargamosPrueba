package me.elmanss.cargamos.di.module

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import me.elmanss.cargamos.data.network.MovieAPI
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


/**
 * cgs on 25/02/20.
 */

@Module
class NetModule(private val baseUrl: String) {

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Timber.d(message)
            }

        })
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        return interceptor
    }


    @Singleton
    @Provides
    fun provideOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(40, TimeUnit.SECONDS)
            .writeTimeout(80, TimeUnit.SECONDS)
            .addInterceptor(interceptor)

        return builder.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Singleton
    @Provides
    fun provideApi(retrofit: Retrofit): MovieAPI {
        return MovieAPI.get(retrofit)
    }
}