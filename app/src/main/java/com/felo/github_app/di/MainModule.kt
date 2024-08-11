package com.felo.github_app.di

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.felo.github_app.preferences.PreferenceKeys.Companion.APP_PREFERENCES
import com.felo.github_app.preferences.SessionManager
import org.koin.dsl.module
import com.bumptech.glide.request.RequestOptions
import com.felo.github_app.R

val mainModule = module {

    single<SessionManager> {
        val context = get<Context>()
        SessionManager(context, context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE))
    }

    single<RequestManager> {
        val context = get<Context>()
        Glide.with(context).setDefaultRequestOptions(
            RequestOptions.placeholderOf(R.drawable.ic_error)
                .error(R.drawable.ic_error)
        )
    }

}