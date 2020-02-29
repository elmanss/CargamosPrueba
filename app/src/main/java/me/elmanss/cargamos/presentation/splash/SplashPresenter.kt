package me.elmanss.cargamos.presentation.splash

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import me.elmanss.cargamos.domain.interactor.SplashInteractor
import me.elmanss.cargamos.util.Constants

/**
 * cgs on 25/02/20.
 */
interface SplashPresenter {
    fun loadConfig(): Disposable
}

class SplashPresenterImpl(
    private val splashInteractor: SplashInteractor,
    val view: SplashView
) :
    SplashPresenter {
    override fun loadConfig(): Disposable {
        view.showProgress()

        return splashInteractor
            .loadConfig()
            .flatMap { splashInteractor.saveConfig(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.hideProgress()
                view.goToMain()
            }, {
                view.hideProgress()
                if (splashInteractor.hasConfig()) {
                    view.goToMain()
                } else {
                    view.onError(Constants.ERROR_SPLASH_CONFIG)
                }
            })

    }
}