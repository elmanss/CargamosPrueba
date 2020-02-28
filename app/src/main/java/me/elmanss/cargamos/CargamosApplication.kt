package me.elmanss.cargamos

import android.app.Application
import me.elmanss.cargamos.di.component.DaggerNetComponent
import me.elmanss.cargamos.di.component.NetComponent
import me.elmanss.cargamos.di.module.AppModule
import me.elmanss.cargamos.di.module.NetModule
import timber.log.Timber
import timber.log.Timber.DebugTree


/**
 * cgs on 25/02/20.
 */
class CargamosApplication : Application() {

    companion object {
        private lateinit var instance: CargamosApplication

        fun get(): CargamosApplication {
            return instance
        }
    }

    private lateinit var appModule: AppModule
    private lateinit var netComponent: NetComponent

    override fun onCreate() {
        super.onCreate()
        Timber.plant(DebugTree())

        appModule =
            AppModule(this@CargamosApplication)

        netComponent = DaggerNetComponent.builder()
            .appModule(appModule)
            .netModule(NetModule("https://api.themoviedb.org/3/"))
            .build()

        instance = this@CargamosApplication
    }

    fun getNetComponent(): NetComponent {
        return netComponent
    }
}