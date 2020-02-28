package me.elmanss.cargamos.presentation

/**
 * cgs on 27/02/20.
 */
interface BaseView {
    fun showProgress()
    fun hideProgress()
    fun onError(msg: String)
}