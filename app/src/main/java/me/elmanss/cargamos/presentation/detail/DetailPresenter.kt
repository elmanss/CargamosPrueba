package me.elmanss.cargamos.presentation.detail

import android.content.SharedPreferences
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import me.elmanss.cargamos.domain.interactor.LocalMovieInteractor
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
    val interactor: LocalMovieInteractor,
    val preferences: SharedPreferences,
    val gson: Gson,
    val view: DetailView
) :
    DetailPresenter {
    override fun getPosterUrl(path: String): String {
        val base = preferences.getString(Constants.KEY_SECURE_URL, "")
        val str = preferences.getString(Constants.KEY_POSTER_SIZES, "")!!
        val sizes = gson.fromJson(str, Array<String>::class.java).asList()
        return base + sizes[sizes.lastIndex - 2] + path
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