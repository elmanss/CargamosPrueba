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
    fun checkIfMovieInfavs(model: MovieModel): Disposable
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

    /*
       Metodo que invoca evento de INSERT en base de datos en caso de que el insert sea exitoso,
       se encadena una operacion SELECT que regresa un Observable que contiene el ultimo id dado de
       alta en base de datos, si la operacion select es exitosa actualizamos el UI para manejar el
       modelo como favorito.
    */
    override fun saveMovie(model: MovieModel): Disposable {
        view.showProgress()
        return interactor.insertMovie(model)
            .flatMap { interactor.selectLatestMovie() }
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

    /*
        Metodo que invoca evento de DELETE en base de datos, si la operacion es exitosa actualizamos
        el UI para manejar el modelo como remoto.
     */
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

    /*
        Metodo para evaluar si la pelicula remota ya se encuentra en base de datos, en caso de
        regresar el id local, actualizamos el UI para manejar el modelo como favorito. Se manda a
        llamar unicamente si la vista de detalle se invocó desde la pantalla principal
     */
    override fun checkIfMovieInfavs(model: MovieModel): Disposable {
        return interactor.findRemoteId(model.remoteId).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                view.hideProgress()
                view.updateToLocal(it, true)
                view.showAlreadyInFavs()
            }, {
                view.hideProgress()
                view.updateToRemote(isChecking = true)
            })
    }

}