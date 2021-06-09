package com.ilham.azurerosehealthmanagerapps.core.di

import androidx.room.Room
import com.ilham.azurerosehealthmanagerapps.core.data.FoodRepository
import com.ilham.azurerosehealthmanagerapps.core.data.source.local.LocalDataSource
import com.ilham.azurerosehealthmanagerapps.core.data.source.local.room.FoodDatabase
import com.ilham.azurerosehealthmanagerapps.core.data.source.remote.RemoteDataSource
import com.ilham.azurerosehealthmanagerapps.core.data.source.remote.network.ApiService
import com.ilham.azurerosehealthmanagerapps.core.domain.repository.IFoodRepository
import com.ilham.azurerosehealthmanagerapps.core.utils.AppExecutors
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val databaseModule = module {
    factory { get<FoodDatabase>().foodDao() }
    single {
        Room.databaseBuilder(
            androidContext(),
            FoodDatabase::class.java,
            "Food.db"
        ).build()
    }
}

val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.edamam.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}
val repositoryModule = module {
    single { LocalDataSource(get()) }
    single { RemoteDataSource(get()) }
    factory { AppExecutors() }
    single<IFoodRepository> { FoodRepository(get(), get(), get()) }
}