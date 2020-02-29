package me.elmanss.cargamos.presentation.list.local

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import me.elmanss.cargamos.domain.interactor.LocalMovieInteractor
import me.elmanss.cargamos.domain.models.ConfigModel
import me.elmanss.cargamos.util.Constants

/**
 * cgs on 26/02/20.
 */
interface LocalListPresenter {

    fun getConfig(): ConfigModel

    fun findAllMovies(): Disposable

    fun findMoviesWithPattern(str: String): Disposable
}

class LocalListPresenterImpl(
    val interactor: LocalMovieInteractor,
    val view: LocalListView
) : LocalListPresenter {

    override fun getConfig(): ConfigModel {
        return interactor.getConfig()
    }

    override fun findAllMovies(): Disposable {
        return interactor.getAllMovies().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.hideProgress()
                view.onAllMoviesFound(it)
            }, {
                it.printStackTrace()
                view.hideProgress()
                view.onAllMoviesFound(listOf())
                view.onError(
                    Constants.ERROR_LIST_LOCAL_LOAD
                )
            })
    }

    override fun findMoviesWithPattern(str: String): Disposable {
        return if (str.isEmpty()) {
            findAllMovies()
        } else {
            interactor.getMoviesFiltered(str).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view.hideProgress()
                    view.onFilteredMoviesFound(it)
                }, {
                    view.hideProgress()
                    view.onFilteredMoviesFound(listOf())
                    view.onError(
                        Constants.ERROR_LIST_LOCAL_FILTER
                    )
                })
        }
    }

}