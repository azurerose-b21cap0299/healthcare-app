package com.ilham.azurerosehealthmanagerapps

import android.app.Application
import com.ilham.azurerosehealthmanagerapps.core.di.databaseModule
import com.ilham.azurerosehealthmanagerapps.core.di.networkModule
import com.ilham.azurerosehealthmanagerapps.core.di.repositoryModule
import com.ilham.azurerosehealthmanagerapps.di.useCaseModule
import com.ilham.azurerosehealthmanagerapps.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyApplication)
            modules(
                databaseModule,
                networkModule,
                repositoryModule,
                useCaseModule,
                viewModelModule
            )
        }
    }
}