package me.elmanss.cargamos.domain.mapper

import com.google.gson.Gson
import me.elmanss.cargamos.data.local.LocalConfig
import me.elmanss.cargamos.data.network.ConfigResult
import me.elmanss.cargamos.domain.models.ConfigModel

/**
 * cgs on 28/02/20.
 */
interface ConfigDataMapper {
    fun configModelFromNetwork(configResult: ConfigResult): ConfigModel

    fun configModelFromRepository(localConfig: LocalConfig): ConfigModel

    fun localConfigFromModel(configModel: ConfigModel): LocalConfig
}

class ConfigDataMapperImpl(val gson: Gson) : ConfigDataMapper {

    override fun configModelFromNetwork(configResult: ConfigResult): ConfigModel {
        return ConfigModel(configResult.images.secureBaseUrl, configResult.images.posterSizes)
    }

    override fun configModelFromRepository(localConfig: LocalConfig): ConfigModel {
        val imgSizes = gson.fromJson(localConfig.imgSizes, Array<String>::class.java).asList()
        return ConfigModel(localConfig.baseUrl ?: "", imgSizes)
    }

    override fun localConfigFromModel(configModel: ConfigModel): LocalConfig {
        return LocalConfig(configModel.baseUrl, gson.toJson(configModel.imgSizes))
    }


}