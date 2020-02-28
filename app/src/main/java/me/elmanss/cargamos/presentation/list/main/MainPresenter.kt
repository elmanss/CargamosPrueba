package me.elmanss.cargamos.presentation.list.main

import android.content.SharedPreferences
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import me.elmanss.cargamos.domain.interactor.NetworkMovieInteractor
import me.elmanss.cargamos.util.Constants
import timber.log.Timber


/**
 * cgs on 25/02/20.
 */
interface MainPresenter {

    fun getSizeList(): List<String>

    fun loadMoviesPaginated(page: Int): Disposable
}

class MainPresenterImpl(
    val interactor: NetworkMovieInteractor,
    val gson: Gson,
    val preferences: SharedPreferences,
    val view: MainView
) : MainPresenter {
    override fun getSizeList(): List<String> {
        val str = preferences.getString(Constants.KEY_POSTER_SIZES, "")!!
        val sizes = gson.fromJson(str, Array<String>::class.java).asList()
        return sizes
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