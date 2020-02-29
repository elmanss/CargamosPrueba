package me.elmanss.cargamos.di.component

import dagger.Component
import me.elmanss.cargamos.di.module.AppModule
import me.elmanss.cargamos.di.module.NetModule
import me.elmanss.cargamos.presentation.detail.DetailModule
import me.elmanss.cargamos.presentation.detail.DetailSubcomponent
import me.elmanss.cargamos.presentation.list.local.LocalListModule
import me.elmanss.cargamos.presentation.list.local.LocalListSubcomponent
import me.elmanss.cargamos.presentation.list.main.MainModule
import me.elmanss.cargamos.presentation.list.main.MainSubcomponent
import me.elmanss.cargamos.presentation.splash.SplashModule
import me.elmanss.cargamos.presentation.splash.SplashSubcomponent
import javax.inject.Singleton

/**
 * cgs on 25/02/20.
 */

@Singleton
@Component(modules = [AppModule::class, NetModule::class])
interface NetComponent {
    fun splashSubcomponent(module: SplashModule): SplashSubcomponent
    fun mainSubcomponent(module: MainModule): MainSubcomponent
    fun detailSubcomponent(module: DetailModule): DetailSubcomponent
    fun localListSubcomponent(module: LocalListModule): LocalListSubcomponent
}