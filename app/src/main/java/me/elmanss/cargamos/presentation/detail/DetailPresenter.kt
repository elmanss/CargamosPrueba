package me.elmanss.cargamos.presentation.detail

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import me.elmanss.cargamos.domain.interactor.DetailInteractor
import me.elmanss.cargamos.domain.models.MovieModel
import me.elmanss.cargamos.util.Constants

/**
 * cgs on 25/02/20.
 */
interface DetailPresenter {
    fun getPosterUrl(path: String): String
    fun saveMovie(model: MovieModel): Disposable
    fun deleteMovie(model: MovieModel): Disposable
}

class DetailPresenterImpl(
    val interactor: DetailInteractor,
    val view: DetailView
) :
    DetailPresenter {
    override fun getPosterUrl(path: String): String {
        val base = interactor.getConfig().baseUrl
        val imgIndex = interactor.getConfig().imgSizes.lastIndex - 2
        val size = interactor.getConfig().imgSizes[imgIndex]
        return base + size + path
    }

    override fun saveMovie(model: MovieModel): Disposable {
        view.showProgress()
        return interactor.insertMovie(model)
            .map { interactor.selectLatestMovie() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.hideProgress()
                view.updateToLocal(it)
            }, {
                it.printStackTrace()
                view.hideProgress()
                view.onError(
                    Constants.ERROR_DETAIL_LOCAL_SAVE
                )
            })

    }

    override fun deleteMovie(model: MovieModel): Disposable {
        return interactor.deleteMovie(model)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.hideProgress()
                view.updateToRemote()
            }, {
                view.hideProgress()
                view.onError(
                    Constants.ERROR_DETAIL_LOCAL_DELETE
                )
            })
    }

}