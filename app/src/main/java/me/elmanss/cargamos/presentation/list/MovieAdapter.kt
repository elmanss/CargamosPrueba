package me.elmanss.cargamos.presentation.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import me.elmanss.cargamos.R
import me.elmanss.cargamos.domain.models.MovieModel
import timber.log.Timber

/**
 * Recibe como parametros la url base obtenida en el consumo del Configuration API y la lista de clave de
 * tamaños posibles de la imagen
 */
class MovieAdapter(private val baseUrl: String, private val sizeSet: List<String>) :
    RecyclerView.Adapter<ViewHolder>() {

    private val movies = mutableListOf<MovieModel>()

    fun clearAndUpdate(movies: List<MovieModel>) {
        if (this.movies.isNotEmpty()) this.movies.clear()
        this.movies.addAll(movies)
        notifyDataSetChanged()
    }

    fun update(movies: List<MovieModel>) {
        this.movies.addAll(movies)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_movie,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val url = baseUrl + sizeSet.elementAt(2) + movies[position].posterPath
        Timber.d("Url: %s", url)

        Picasso.get().load(url)
            .resizeDimen(R.dimen.poster_width, R.dimen.poster_height_placeholder)
            .into(holder.poster)
    }

    fun getItem(pos: Int): MovieModel {
        return movies[pos]
    }
}

class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    val poster: ImageView = v.findViewById(R.id.img_poster)
}

