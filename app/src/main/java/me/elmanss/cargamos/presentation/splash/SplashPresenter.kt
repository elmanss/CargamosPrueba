package me.elmanss.cargamos.presentation.splash

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import me.elmanss.cargamos.data.network.ConfigResult
import me.elmanss.cargamos.data.network.MovieAPI
import me.elmanss.cargamos.util.Constants
import timber.log.Timber

/**
 * cgs on 25/02/20.
 */
interface SplashPresenter {
    fun loadConfig(): Disposable
    fun storeConfig(result: ConfigResult)
}

class SplashPresenterImpl(
    val api: MovieAPI,
    val gson: Gson,
    val prefs: SharedPreferences,
    val view: SplashView
) :
    SplashPresenter {
    override fun loadConfig(): Disposable {
        return api.getConfig().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                storeConfig(it)
                view.goToMain()
            }, {
                Timber.e(it)
                view.goToMain()
            })
    }

    override fun storeConfig(result: ConfigResult) {

        val sizeString = gson.toJson(result.images.posterSizes)

        prefs.edit {
            putString(Constants.KEY_SECURE_URL, result.images.secureBaseUrl)
            putString(Constants.KEY_POSTER_SIZES, sizeString)
        }
    }
}