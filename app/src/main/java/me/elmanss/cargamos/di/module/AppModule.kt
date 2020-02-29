package me.elmanss.cargamos.di.module

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import me.elmanss.cargamos.Database
import me.elmanss.cargamos.data.local.ConfigRepository
import me.elmanss.cargamos.data.local.ConfigRepositoryImpl
import me.elmanss.cargamos.domain.mapper.ConfigDataMapper
import me.elmanss.cargamos.domain.mapper.ConfigDataMapperImpl
import javax.inject.Singleton


/**
 * cgs on 25/02/20.
 */
@Module
class AppModule(private val mApplication: Application) {

    @Provides
    @Singleton
    fun providesApplication(): Application {
        return mApplication
    }

    @Provides
    @Singleton
    fun providesSharedPreferences(application: Application?): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(application)
    }


    @Singleton
    @Provides
    fun provideConfigRepo(sharedPreferences: SharedPreferences): ConfigRepository {
        return ConfigRepositoryImpl(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideSqlDriver(application: Application): SqlDriver {
        return AndroidSqliteDriver(Database.Schema, application, "movies.db")
    }

    @Provides
    @Singleton
    fun provideDatabase(sqlDriver: SqlDriver): Database {
        Database.Schema.create(sqlDriver)
        return Database(sqlDriver)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        return gsonBuilder.create()
    }


    @Singleton
    @Provides
    fun provideConfigDataMapper(gson: Gson): ConfigDataMapper {
        return ConfigDataMapperImpl(gson)
    }
}