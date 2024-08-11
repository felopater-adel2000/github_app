package com.felo.github_app.network

import com.felo.github_app.data.model.RepositoryModel
import com.felo.github_app.data.model.SearchResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {
    @GET("repositories")
    fun getRepos(
        @Query("since") since: Int,
    ): Flow<GenericApiResponse<List<RepositoryModel>>>

    @GET("search/repositories")
    fun searchRepos(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") limit: Int
    ): Flow<GenericApiResponse<SearchResponse>>
}