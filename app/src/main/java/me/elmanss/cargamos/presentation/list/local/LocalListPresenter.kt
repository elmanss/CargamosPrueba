package me.elmanss.cargamos.presentation.list.local

import android.content.SharedPreferences
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import me.elmanss.cargamos.domain.interactor.LocalMovieInteractor
import me.elmanss.cargamos.util.Constants

/**
 * cgs on 26/02/20.
 */
interface LocalListPresenter {

    fun getSizeList(): List<String>

    fun getPosterBaseUrl(): String

    fun findAllMovies(): Disposable

    fun findMoviesWithPattern(str: String): Disposable
}

class LocalListPresenterImpl(
    val interactor: LocalMovieInteractor,
    val preferences: SharedPreferences,
    val gson: Gson,
    val view: LocalListView
) : LocalListPresenter {
    override fun getSizeList(): List<String> {
        val str = preferences.getString(Constants.KEY_POSTER_SIZES, "")!!
        return gson.fromJson(str, Array<String>::class.java).asList()
    }

    override fun getPosterBaseUrl(): String {
        return preferences.getString(Constants.KEY_SECURE_URL, "")!!
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