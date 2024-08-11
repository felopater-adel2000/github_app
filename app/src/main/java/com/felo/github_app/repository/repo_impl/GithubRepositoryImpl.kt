package com.felo.github_app.repository.repo_impl

import com.felo.github_app.data.model.RepositoryModel
import com.felo.github_app.data.model.SearchResponse
import com.felo.github_app.network.AbsentStateFlow
import com.felo.github_app.network.ApiInterface
import com.felo.github_app.network.ApiSuccessResponse
import com.felo.github_app.network.DataState
import com.felo.github_app.network.GenericApiResponse
import com.felo.github_app.network.JobManager
import com.felo.github_app.network.NetworkBoundResource
import com.felo.github_app.network.Response
import com.felo.github_app.network.ResponseType
import com.felo.github_app.preferences.SessionManager
import com.felo.github_app.repository.repo_interface.GithubRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.AbstractFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

class GithubRepositoryImpl(
    val apiService: ApiInterface,
    val sessionManager: SessionManager
) : JobManager("GithubRepository"), GithubRepository {
    override fun getRepos(
        since: Int
    ): StateFlow<DataState<List<RepositoryModel>>> {
        return object: NetworkBoundResource<List<RepositoryModel>, List<RepositoryModel>>(
            isNetworkAvailable = sessionManager.isInternetAvailable(),
            isNetworkRequest = true,
            shouldLoadFromCache = false,
            shouldCancelIfNoInternet = true
        ){
            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<List<RepositoryModel>>) {
                if(response.statusCode == 200)
                {
                    onCompleteJob(
                        DataState.Success(
                            data = response.body,
                            response = Response(
                                message = "Success",
                                responseType = ResponseType.None()
                            )
                        )
                    )
                }
                else
                {
                    onCompleteJob(
                        DataState.Error(
                            response = Response(
                                message = "Some thing went wrong",
                                responseType = ResponseType.SnakeBar()
                            )
                        )
                    )
                }
            }

            override suspend fun createCacheRequest() {

            }

            override fun createCall(): Flow<GenericApiResponse<List<RepositoryModel>>> {
                return apiService.getRepos(since)
            }

            override fun setJob(job: Job) {
                addJop("getRepos", job)
            }

            override fun loadFromCache(): StateFlow<List<RepositoryModel>?> {
                return AbsentStateFlow.create()
            }

            override suspend fun updateLocalDb(cacheObject: List<RepositoryModel>?) {
            }


        }.asStateFlow()
    }

    override fun searchRepos(
        query: String,
        page: Int,
        limit: Int
    ): StateFlow<DataState<SearchResponse>> {
        return object: NetworkBoundResource<SearchResponse, SearchResponse>(
            isNetworkAvailable = sessionManager.isInternetAvailable(),
            isNetworkRequest = true,
            shouldLoadFromCache = false,
            shouldCancelIfNoInternet = true
        ){
            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<SearchResponse>) {
                if(response.statusCode == 200)
                {
                    onCompleteJob(
                        DataState.Success(
                            data = response.body,
                            response = Response(
                                message = "Success",
                                responseType = ResponseType.None()
                            )
                        )
                    )
                }
                else
                {
                    onCompleteJob(
                        DataState.Error(
                            response = Response(
                                message = "Some thing went wrong",
                                responseType = ResponseType.Dialog()
                            )
                        )
                    )
                }
            }

            override suspend fun createCacheRequest() {

            }

            override fun createCall(): Flow<GenericApiResponse<SearchResponse>> {
                return apiService.searchRepos(
                    query =query,
                    page = page,
                    limit = limit
                )
            }

            override fun setJob(job: Job) {
                addJop("searchRepos", job)
            }

            override fun loadFromCache(): StateFlow<SearchResponse?> {
                return AbsentStateFlow.create()
            }

            override suspend fun updateLocalDb(cacheObject: SearchResponse?) {
            }
        }.asStateFlow()
    }


}