package com.bogdan.codeforceswatcher

import android.app.Application
import com.bogdan.codeforceswatcher.redux.AppState
import com.bogdan.codeforceswatcher.redux.appMiddleware
import com.bogdan.codeforceswatcher.redux.appReducer
import com.bogdan.codeforceswatcher.room.RoomController
import com.google.firebase.analytics.FirebaseAnalytics
import org.rekotlin.Store

val store = Store(
    reducer = ::appReducer,
    state = RoomController.fetchAppState(),
    middleware = listOf(appMiddleware)
)

class CwApp : Application() {

    override fun onCreate() {
        super.onCreate()

        app = this

        RoomController.onAppCreated()
        FirebaseAnalytics.getInstance(this)
    }

    companion object {

        lateinit var app: CwApp
            private set
    }
}
