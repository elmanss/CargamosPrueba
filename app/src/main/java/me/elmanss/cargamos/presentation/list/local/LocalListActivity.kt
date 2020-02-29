package me.elmanss.cargamos.presentation.list.local

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.jakewharton.rxbinding2.widget.textChanges
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import me.elmanss.cargamos.CargamosApplication
import me.elmanss.cargamos.databinding.ActivityLocalListBinding
import me.elmanss.cargamos.domain.models.MovieModel
import me.elmanss.cargamos.presentation.BaseView
import me.elmanss.cargamos.presentation.detail.DetailActivity
import me.elmanss.cargamos.presentation.list.MovieAdapter
import me.elmanss.cargamos.util.ItemClickSupport
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject


data class LocalListModel(
    val originalMovies: MutableList<MovieModel> = mutableListOf(),
    val filteredMovies: MutableList<MovieModel> = mutableListOf()
)

interface LocalListView : BaseView {
    fun configList()

    fun configSearch()

    fun onAllMoviesFound(movies: List<MovieModel>)

    fun onFilteredMoviesFound(movies: List<MovieModel>)

    fun notifyAdapter(movies: List<MovieModel>)

    fun clickMovie(pos: Int)
}

class LocalListActivity : AppCompatActivity(),
    LocalListView {

    lateinit var binding: ActivityLocalListBinding
    lateinit var adapter: MovieAdapter

    @Inject
    lateinit var model: LocalListModel

    @Inject
    lateinit var presenter: LocalListPresenter

    private val compDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CargamosApplication.get().getNetComponent().localListSubcomponent(
            LocalListModule(this)
        )
            .inject(this)
        binding = ActivityLocalListBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)
        configList()
        configSearch()
        compDisposable.add(presenter.findAllMovies())
    }

    override fun onRestart() {
        super.onRestart()
        val search = binding.searchLocalList.text.toString()
        presenter.findMoviesWithPattern(search)
    }

    override fun showProgress() {
        binding.progressLocalList.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.progressLocalList.visibility = View.GONE
    }

    override fun onError(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun configList() {
        adapter = MovieAdapter(presenter.getConfig())
        binding.recyclerLocalList.adapter = adapter
        binding.recyclerLocalList.layoutManager = GridLayoutManager(this, 3)

        ItemClickSupport.addTo(binding.recyclerLocalList).setOnItemClickListener { _, pos, _ ->
            clickMovie(pos)
        }
    }


    override fun configSearch() {
        binding.searchLocalList
            .textChanges()
            .debounce(200, TimeUnit.MILLISECONDS)
            .subscribe({
                presenter.findMoviesWithPattern("" + it)
            }, { it.printStackTrace() }).addTo(compDisposable)
    }

    override fun onAllMoviesFound(movies: List<MovieModel>) {
        model.originalMovies.clear()
        model.originalMovies.addAll(movies)
        notifyAdapter(model.originalMovies)
    }

    override fun onFilteredMoviesFound(movies: List<MovieModel>) {
        Timber.d("Resultado Filtro: %s", movies)
        model.filteredMovies.clear()
        model.filteredMovies.addAll(movies)
        notifyAdapter(model.filteredMovies)
    }

    override fun notifyAdapter(movies: List<MovieModel>) {
        adapter.clearAndUpdate(movies)
    }

    override fun clickMovie(pos: Int) {
        val movie = adapter.getItem(pos)
        DetailActivity.start(
            this,
            MovieModel(
                movie.id,
                movie.title,
                movie.overview,
                movie.posterPath,
                movie.score,
                false
            )
        )
    }
}
