package me.elmanss.cargamos.presentation.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import io.reactivex.disposables.CompositeDisposable
import me.elmanss.cargamos.CargamosApplication
import me.elmanss.cargamos.databinding.ActivitySplashBinding
import me.elmanss.cargamos.presentation.list.main.MainActivity
import javax.inject.Inject

interface SplashView {
    fun goToMain()
}

class SplashActivity : AppCompatActivity(),
    SplashView {

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

        Handler().postDelayed({
            cDisposable.add(presenter.loadConfig())
        }, 2000)
    }

    override fun onDestroy() {
        cDisposable.clear()
        super.onDestroy()
    }

    override fun goToMain() {
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        finish()
    }
}
