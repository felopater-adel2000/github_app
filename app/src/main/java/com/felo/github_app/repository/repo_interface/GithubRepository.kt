package com.felo.github_app.repository.repo_interface

import com.felo.github_app.data.model.RepositoryModel
import com.felo.github_app.data.model.SearchResponse
import com.felo.github_app.network.DataState
import kotlinx.coroutines.flow.StateFlow

interface GithubRepository {

    fun getRepos(
        since: Int
    ): StateFlow<DataState<List<RepositoryModel>>>

    fun searchRepos(
        query: String,
        page: Int,
        limit: Int
    ): StateFlow<DataState<SearchResponse>>
}