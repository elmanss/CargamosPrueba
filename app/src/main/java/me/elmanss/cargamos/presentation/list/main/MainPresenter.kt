package me.elmanss.cargamos.presentation.list.main

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import me.elmanss.cargamos.domain.interactor.NetworkMovieInteractor
import me.elmanss.cargamos.domain.models.ConfigModel
import timber.log.Timber


/**
 * cgs on 25/02/20.
 */
interface MainPresenter {

    fun getConfig(): ConfigModel

    fun loadMoviesPaginated(page: Int): Disposable
}

class MainPresenterImpl(
    val interactor: NetworkMovieInteractor,
    val view: MainView
) : MainPresenter {

    override fun getConfig(): ConfigModel {
        return interactor.getConfig()
    }

    override fun loadMoviesPaginated(page: Int): Disposable {
        view.showProgress()

        return interactor.getMoviesPaginated(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.d("Models: %s", it)
                view.hideProgress()
                view.setMovies(it)
            }, {
                view.hideProgress()
                Timber.e(it)
            })
    }

}