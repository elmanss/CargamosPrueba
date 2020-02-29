package me.elmanss.cargamos.domain.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * cgs on 27/02/20.
 */
@Parcelize
data class MovieModel(
    val id: Long,
    val remoteId: Long,
    val title: String,
    val overview: String,
    val posterPath: String,
    val score: Double,
    val remote: Boolean
) : Parcelable