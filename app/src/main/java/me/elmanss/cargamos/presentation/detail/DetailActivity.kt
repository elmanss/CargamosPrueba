package me.elmanss.cargamos.presentation.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import io.reactivex.disposables.CompositeDisposable
import me.elmanss.cargamos.CargamosApplication
import me.elmanss.cargamos.R
import me.elmanss.cargamos.databinding.ActivityDetailBinding
import me.elmanss.cargamos.domain.models.MovieModel
import me.elmanss.cargamos.presentation.BaseView
import timber.log.Timber
import javax.inject.Inject


interface DetailView : BaseView {
    fun setData()
    fun configFloatingButton()
    fun loadImg(path: String)
    fun clickFloatingButton()
    fun updateToLocal(id: Long, isChecking: Boolean = false)
    fun showAlreadyInFavs()
    fun showMsg(msg: String)
    fun updateToRemote(isChecking: Boolean = false)
}

class DetailActivity : AppCompatActivity(),
    DetailView {

    companion object {
        fun start(context: Context, model: MovieModel) {
            context.startActivity(
                Intent(context, DetailActivity::class.java).putExtra(
                    "model",
                    model
                )
            )
        }
    }

    lateinit var binding: ActivityDetailBinding

    @Inject
    lateinit var model: MovieModel
    @Inject
    lateinit var presenter: DetailPresenter

    private val compDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)

        CargamosApplication.get().getNetComponent().detailSubcomponent(
            DetailModule(this)
        )
            .inject(this)

        setData()
        configFloatingButton()

        if (model.remote) presenter.checkIfMovieInfavs(model)
    }

    override fun onDestroy() {
        compDisposable.clear()
        super.onDestroy()
    }

    override fun showProgress() {
        binding.detailProgress.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.detailProgress.visibility = View.GONE
    }

    override fun onError(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun setData() {
        binding.detailTitle.text = model.title
        binding.detailBody.text = model.overview
        binding.detailScore.text = getString(R.string.txt_detail_grade, model.score.toString())
        loadImg(model.posterPath)
    }

    override fun configFloatingButton() {
        if (model.remote) {
            binding.detailActionButton.setImageResource(R.drawable.heart)
        } else {
            binding.detailActionButton.setImageResource(R.drawable.delete)
        }

        binding.detailActionButton.setOnClickListener {
            clickFloatingButton()
        }
    }

    override fun loadImg(path: String) {
        val fullPath = presenter.getPosterUrl(path)
        Timber.d("PATH POSTER: %s", fullPath)
        Picasso.get().load(fullPath).error(R.drawable.heart_broken_outline)
            .resizeDimen(R.dimen.poster_detail_width, R.dimen.poster_detail_height)
            .into(binding.detailPoster)
    }

    override fun clickFloatingButton() {
        if (model.remote) {
            compDisposable.add(presenter.saveMovie(model))
        } else {
            compDisposable.add(presenter.deleteMovie(model))
        }
    }

    override fun updateToLocal(id: Long, isChecking: Boolean) {
        model = model.copy(id = id, remote = false)
        binding.detailActionButton.setImageResource(R.drawable.delete)
        if (!isChecking) showMsg(getString(R.string.txt_msg_detail_save))
    }

    override fun showAlreadyInFavs() {
        showMsg(getString(R.string.txt_detail_fav_msg, model.title))
    }

    override fun showMsg(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun updateToRemote(isChecking: Boolean) {
        model = model.copy(remote = true)
        binding.detailActionButton.setImageResource(R.drawable.heart)
        if (!isChecking) showMsg(getString(R.string.txt_msg_detail_delete))
    }
}
