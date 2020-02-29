package me.elmanss.cargamos.presentation.list.main

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.disposables.CompositeDisposable
import me.elmanss.cargamos.CargamosApplication
import me.elmanss.cargamos.R
import me.elmanss.cargamos.databinding.ActivityMainBinding
import me.elmanss.cargamos.domain.models.MovieModel
import me.elmanss.cargamos.presentation.detail.DetailActivity
import me.elmanss.cargamos.presentation.list.MovieAdapter
import me.elmanss.cargamos.presentation.list.local.LocalListActivity
import me.elmanss.cargamos.util.InfiniteScrollListener
import me.elmanss.cargamos.util.ItemClickSupport
import javax.inject.Inject

interface MainView {
    fun showProgress()
    fun hideProgress()
    fun configList()
    fun setMovies(movies: List<MovieModel>)
    fun clickMovie(pos: Int)
}

class MainActivity : AppCompatActivity(),
    MainView {

    lateinit var binding: ActivityMainBinding
    lateinit var scrollListener: InfiniteScrollListener
    lateinit var adapter: MovieAdapter

    @Inject
    lateinit var prefs: SharedPreferences
    @Inject
    lateinit var presenter: MainPresenter

    private val compDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)

        setSupportActionBar(binding.mainToolbar)

        CargamosApplication.get().getNetComponent()
            .mainSubcomponent(
                MainModule(
                    this
                )
            )
            .inject(this)

        configList()

        compDisposable.add(presenter.loadMoviesPaginated(1))
    }

    override fun onDestroy() {
        compDisposable.clear()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.action_favs) {
            startActivity(Intent(this@MainActivity, LocalListActivity::class.java))
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun showProgress() {
        binding.mainProgress.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.mainProgress.visibility = View.GONE
    }

    override fun configList() {
        adapter = MovieAdapter(presenter.getConfig())

        binding.mainRecycler.adapter = adapter
        binding.mainRecycler.hasFixedSize()
        binding.mainRecycler.layoutManager = GridLayoutManager(this, 3)

        scrollListener = object :
            InfiniteScrollListener(binding.mainRecycler.layoutManager as GridLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                compDisposable.add(presenter.loadMoviesPaginated(page))
            }

        }

        binding.mainRecycler.addOnScrollListener(scrollListener)

        ItemClickSupport.addTo(binding.mainRecycler).setOnItemClickListener { _, pos, _ ->
            clickMovie(pos)
        }
    }

    override fun setMovies(movies: List<MovieModel>) {
        adapter.update(movies)
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
                remote = true
            )
        )
    }
}
