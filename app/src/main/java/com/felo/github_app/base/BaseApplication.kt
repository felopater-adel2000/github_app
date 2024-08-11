package com.felo.github_app.base

import android.app.Application
import android.content.Context
import com.felo.github_app.di.mainModule
import com.felo.github_app.di.networkModule
import com.felo.github_app.di.repositoryModule
import com.felo.github_app.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        injectWithKoin(this)
    }

    companion object {
        fun injectWithKoin(context: Context)
        {
            startKoin {
                androidContext(context)
                modules(
                    mainModule,
                    networkModule,
                    repositoryModule,
                    viewModelModule
                )
            }
        }
    }
}