package djordjeh.architecture.mvp

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dagger.android.AndroidInjector
import dagger.android.support.HasSupportFragmentInjector

class ActivityTest : AppCompatActivity(), HasSupportFragmentInjector {

    private var supportFragmentInjector: AndroidInjector<Fragment>? = null

    fun startFragment(fragment: Fragment, supportFragmentInjector: AndroidInjector<Fragment>) {
        this.supportFragmentInjector = supportFragmentInjector
        supportFragmentManager
                .beginTransaction()
                .add(android.R.id.content, fragment, "Test")
                .commit()
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return supportFragmentInjector
    }
}
