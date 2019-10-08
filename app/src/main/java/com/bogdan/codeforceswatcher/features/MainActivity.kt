package com.bogdan.codeforceswatcher.features

import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.features.add_user.AddUserActivity
import com.bogdan.codeforceswatcher.features.contests.ContestsFragment
import com.bogdan.codeforceswatcher.features.users.UsersFragment
import com.bogdan.codeforceswatcher.redux.actions.UIActions
import com.bogdan.codeforceswatcher.redux.states.UIState
import com.bogdan.codeforceswatcher.store
import com.bogdan.codeforceswatcher.ui.AppRateDialog
import com.bogdan.codeforceswatcher.util.Prefs
import kotlinx.android.synthetic.main.activity_main.*
import org.rekotlin.StoreSubscriber

class MainActivity : AppCompatActivity(), StoreSubscriber<UIState> {

    private val currentTabFragment: Fragment?
        get() = supportFragmentManager.fragments.lastOrNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Prefs.get().checkRateDialog()) showAppRateDialog()
        initViews()
    }

    override fun onStart() {
        super.onStart()
        store.subscribe(this) { state -> state.select { it.ui } }
    }

    override fun onStop() {
        super.onStop()
        store.unsubscribe(this)
    }

    override fun newState(state: UIState) {
        val fragment: Fragment = when (state.selectedHomeTab) {
            UIState.HomeTab.USERS -> {
                currentTabFragment as? UsersFragment ?: UsersFragment()
            }
            UIState.HomeTab.CONTESTS -> {
                currentTabFragment as? ContestsFragment ?: ContestsFragment()
            }
        }

        if (fragment != currentTabFragment) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }

        val bottomNavSelectedItemId = when (state.selectedHomeTab) {
            UIState.HomeTab.USERS -> R.id.navUsers
            UIState.HomeTab.CONTESTS -> R.id.navContests
        }

        if (bottomNavigation.selectedItemId != bottomNavSelectedItemId) {
            bottomNavigation.selectedItemId = bottomNavSelectedItemId
        }

        when (state.selectedHomeTab) {
            UIState.HomeTab.USERS -> {
                onUsersTabSelected()
            }
            UIState.HomeTab.CONTESTS -> {
                onContestsTabSelected()
            }
        }
    }

    private fun onUsersTabSelected() {
        llToolbar.visibility = View.VISIBLE
        fab.setOnClickListener {
            val intent =
                Intent(this@MainActivity, AddUserActivity::class.java)
            startActivity(intent)
        }
        fab.setImageDrawable(getDrawable(R.drawable.ic_plus))
    }

    private fun onContestsTabSelected() {
        llToolbar.visibility = View.GONE
        fab.setOnClickListener {
            val intent =
                Intent(Intent.ACTION_VIEW).setData(Uri.parse(CODEFORCES_LINK))
            startActivity(intent)
        }
        fab.setImageDrawable(getDrawable(R.drawable.ic_eye))
    }

    private fun initViews() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        spSort.background.setColorFilter(
            ContextCompat.getColor(this, R.color.white),
            PorterDuff.Mode.SRC_ATOP
        )

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            val homeTab = UIState.HomeTab.fromMenuItemId(item.itemId)
            if (homeTab != store.state.ui.selectedHomeTab) {
                store.dispatch(UIActions.SelectHomeTab(homeTab))
            }
            true
        }
    }

    private fun showAppRateDialog() {
        val rateDialog = AppRateDialog()
        rateDialog.isCancelable = false
        rateDialog.show(supportFragmentManager, "progressDialog")
    }

    companion object {
        private const val CODEFORCES_LINK = "http://codeforces.com/contests"
    }
}