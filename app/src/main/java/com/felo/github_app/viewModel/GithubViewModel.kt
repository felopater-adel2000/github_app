package com.felo.github_app.viewModel

import androidx.lifecycle.ViewModel
import com.felo.github_app.repository.repo_interface.GithubRepository

class GithubViewModel(private val repo: GithubRepository) : ViewModel() {

    fun getRepos(since: Int) = repo.getRepos(since)

    fun searchRepos(query: String, page: Int, limit: Int) = repo.searchRepos(query, page, limit)
}