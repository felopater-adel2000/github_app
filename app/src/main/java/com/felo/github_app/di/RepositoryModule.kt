package com.felo.github_app.di

import com.felo.github_app.repository.repo_impl.AuthRepositoryImpl
import com.felo.github_app.repository.repo_impl.GithubRepositoryImpl
import com.felo.github_app.repository.repo_interface.AuthRepository
import com.felo.github_app.repository.repo_interface.GithubRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<GithubRepository> { GithubRepositoryImpl(get(), get()) }

    single<AuthRepository> { AuthRepositoryImpl(get()) }
}