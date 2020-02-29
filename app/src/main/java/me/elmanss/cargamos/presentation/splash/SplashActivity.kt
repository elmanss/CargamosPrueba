package me.elmanss.cargamos.presentation.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import me.elmanss.cargamos.CargamosApplication
import me.elmanss.cargamos.databinding.ActivitySplashBinding
import me.elmanss.cargamos.presentation.BaseView
import me.elmanss.cargamos.presentation.list.main.MainActivity
import javax.inject.Inject

interface SplashView : BaseView {
    fun onLaunch()
    fun goToMain()
}

class SplashActivity : AppCompatActivity(), SplashView {

    private val cDisposable = CompositeDisposable()
    lateinit var binding: ActivitySplashBinding

    @Inject
    lateinit var presenter: SplashPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)

        CargamosApplication.get()
            .getNetComponent()
            .splashSubcomponent(
                SplashModule(
                    this
                )
            )
            .inject(this)

        binding.bSplashRetry.setOnClickListener {
            onLaunch()
        }

        onLaunch()

    }

    override fun onDestroy() {
        cDisposable.clear()
        super.onDestroy()
    }

    override fun onLaunch() {
        Handler().postDelayed({
            cDisposable.add(presenter.loadConfig())
        }, 2000)
    }

    override fun goToMain() {
        binding.bSplashRetry.visibility = View.GONE
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        finish()
    }

    override fun showProgress() {
        binding.splashProgressText.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.splashProgressText.visibility = View.GONE
    }

    override fun onError(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        binding.bSplashRetry.visibility = View.VISIBLE
    }
}
