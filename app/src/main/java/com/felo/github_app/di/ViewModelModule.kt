package com.felo.github_app.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import com.felo.github_app.viewModel.GithubViewModel
import com.felo.github_app.viewModel.AuthViewModel

val viewModelModule = module {
    viewModelOf(::GithubViewModel)
    viewModelOf(::AuthViewModel)
}