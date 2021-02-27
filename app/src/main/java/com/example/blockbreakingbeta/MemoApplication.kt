package com.example.blockbreakingbeta
import android.app.Application
import io.realm.Realm

class MemoApplication {


    class MemoApplication: Application() {
        override fun onCreate() {
            super.onCreate()
            Realm.init(this)
        }
    }

}