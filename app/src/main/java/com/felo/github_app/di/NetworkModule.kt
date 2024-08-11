package com.felo.github_app.di

import com.felo.github_app.network.ApiInterface
import com.felo.github_app.network.FlowCallAdapterFactory
import com.felo.github_app.preferences.SessionManager
import com.felo.github_app.utils.Constants.Companion.BASE_URL
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {

    single<Gson> { GsonBuilder().excludeFieldsWithoutExposeAnnotation().create() }

    single<HttpLoggingInterceptor> {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        logging
    }


    single<OkHttpClient> {
        val httpLoggingInterceptor: HttpLoggingInterceptor = get()
        val sessionManager: SessionManager = get()
        OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    single<Retrofit.Builder> {
        Retrofit.Builder().baseUrl(BASE_URL)
            .addCallAdapterFactory(FlowCallAdapterFactory())
            .client(get())
            .addConverterFactory(GsonConverterFactory.create(get()))
    }

    single<ApiInterface> {
        get<Retrofit.Builder>().build().create(ApiInterface::class.java)
    }

}