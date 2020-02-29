package me.elmanss.cargamos.domain.interactor

import io.reactivex.Single
import me.elmanss.cargamos.data.local.ConfigRepository
import me.elmanss.cargamos.data.network.ConfigAPI
import me.elmanss.cargamos.domain.mapper.ConfigDataMapper
import me.elmanss.cargamos.domain.models.ConfigModel

/**
 * ADDCEL on 28/02/20.
 */
interface SplashInteractor {

    fun loadConfig(): Single<ConfigModel>
    fun saveConfig(configModel: ConfigModel): Single<Any>
    fun hasConfig(): Boolean
}


class SplashInteractorImpl(
    val configAPI: ConfigAPI,
    val mapper: ConfigDataMapper,
    val configRepository: ConfigRepository
) :
    SplashInteractor {

    override fun loadConfig(): Single<ConfigModel> {
        return configAPI.getConfig().flatMap {
            Single.fromCallable { mapper.configModelFromNetwork(it) }
        }
    }

    override fun saveConfig(configModel: ConfigModel): Single<Any> {
        return Single.fromCallable {
            val local = mapper.localConfigFromModel(configModel)
            configRepository.saveConfig(local)
        }
    }

    override fun hasConfig(): Boolean {
        return !configRepository.isEmpty()
    }

}