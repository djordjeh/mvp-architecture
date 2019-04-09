package djordjeh.architecture.mvp.ui.tasks

import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity
import djordjeh.architecture.mvp.R

class HomeActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragment = supportFragmentManager.findFragmentById(R.id.container)
        if (fragment == null) {
            supportFragmentManager.beginTransaction().add(R.id.container, TasksFragment.newInstance(), TasksFragment.TAG).commit()
        }
    }
}
