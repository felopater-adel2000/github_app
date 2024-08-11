package com.felo.github_app.ui.search

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.felo.github_app.R
import com.felo.github_app.adapter.RepositoryAdapter
import com.felo.github_app.base.BaseActivity
import com.felo.github_app.data.model.RepositoryModel
import com.felo.github_app.data.model.SearchResponse
import com.felo.github_app.databinding.ActivityMainBinding
import com.felo.github_app.databinding.ActivitySearchBinding
import com.felo.github_app.network.DataState
import com.felo.github_app.network.StateError
import com.felo.github_app.utils.Constants
import com.felo.github_app.utils.displayToast
import com.felo.github_app.utils.hide
import com.felo.github_app.utils.hideSoftKeyboard
import com.felo.github_app.utils.orDefault
import com.felo.github_app.utils.setUpToolBar
import com.felo.github_app.utils.show
import com.felo.github_app.viewModel.GithubViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : BaseActivity(), View.OnClickListener, RepositoryAdapter.RepositoryItemCallback {
    private val TAG = "SearchActivity"
    private lateinit var binding: ActivitySearchBinding
    private val viewModel: GithubViewModel by viewModel()

    private lateinit var adapter: RepositoryAdapter
    private val repos: ArrayList<RepositoryModel> = ArrayList()
    private var page = 1
    private var isLoading = false
    private var isQueryExhausted = false
    private var query = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews()
    {
        binding.apply {
            setUpToolBar(
                toolbarBinding = toolbar,
                title = getString(R.string.search),
                isHome = false
            )

            adapter = RepositoryAdapter(requestManager = requestManager, this@SearchActivity)
            rvRepository.adapter = adapter

            rvRepository.addOnScrollListener(object: RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = rvRepository.layoutManager
                    val totalItemCount = (layoutManager as LinearLayoutManager).itemCount
                    val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                    if(lastVisibleItem == totalItemCount - 1)
                    {
                        if(!isLoading && !isQueryExhausted)
                        {
                            isLoading = true
                            page++
                            searchRepos()
                        }
                    }
                }
            })

            //set Search Icon in Keyboard
            ivSearch.setOnClickListener(this@SearchActivity)

            etSearch.setOnEditorActionListener { v, actionId, event ->
                if(actionId == EditorInfo.IME_ACTION_SEARCH)
                {
                    onSearchClick()
                }
                true
            }


        }
    }

    private fun searchRepos() = lifecycleScope.launch {
        viewModel.searchRepos(
            query = query,
            page = page,
            limit = Constants.PAGINATION_PAGE_SIZE
        ).collect {response ->
            showProgressBar(response.loading.isLoading)

            if(response is DataState.Error)
            {
                response.error?.let {stateError -> handelErrorResponse(stateError) }
            }
            else if(response is DataState.Success)
            {
                showErrorUI(false)
                response.data?.data?.let {response ->
                    populateData(response)
                }
            }
        }
    }

    private fun populateData(response: SearchResponse)
    {
        isQueryExhausted = repos.size != response.totalCount.orDefault()
        if(page == 1)
        {
            if(response.items?.isEmpty() == true)
            {
                showErrorUI(
                    show = true,
                    image = R.drawable.ic_error,
                    title = getString(R.string.no_data),
                    showButton = false
                )
            }
            else
            {
                repos.clear()
                repos.addAll(response.items.orEmpty())
                adapter.submitList(repos)
                adapter.notifyDataSetChanged()
            }
        }
        else
        {
            setPaginatedItem(RepositoryAdapter.ITEM_TYPE)
            repos.addAll(response.items.orEmpty())
            adapter.submitList(repos)
            adapter.notifyDataSetChanged()
            isLoading = false
        }
    }

    private fun resetPaginationData()
    {
        page = 1
        isLoading = false
        isQueryExhausted = false
        repos.clear()
    }

    private fun onSearchClick()
    {
        if(query == binding.etSearch.text.toString()) return
        query = binding.etSearch.text.toString()
        if(query.isEmpty())
        {
            displayToast(getString(R.string.enter_search_query))
            return
        }
        hideSoftKeyboard()
        resetPaginationData()
        searchRepos()
    }

    private fun setPaginatedItem(type: Int, errorMessage: String = "")
    {
        if(repos.isNotEmpty())
        {
            val lastItem = repos.last()
            if(lastItem.isLoading || lastItem.isError) repos.removeLast()

            when(type)
            {
                RepositoryAdapter.LOADING_TYPE -> repos.add(RepositoryModel(isLoading = true))
                RepositoryAdapter.ERROR_TYPE -> repos.add(RepositoryModel(isError = true, errorMessage = errorMessage))
            }
        }
        adapter.submitList(repos)
        adapter.notifyDataSetChanged()
    }

    private fun handelErrorResponse(stateError: StateError)
    {
        if(isLoading) setPaginatedItem(RepositoryAdapter.ERROR_TYPE, stateError.response?.message.orDefault())
        else
        {
            if(stateError.response?.message in Constants.EXPECTED_INTERNET_ERROR_MESSAGES)
            {
                showErrorUI(
                    show = true,
                    image = R.drawable.ic_error,
                    title = getString(R.string.no_internet),
                    desc = stateError.response?.message,
                    showButton = true
                )
            }
            else
            {
                showErrorUI(
                    show = true,
                    image = R.drawable.ic_error,
                    title = getString(R.string.something_went_wrong),
                    desc = stateError.response?.message,
                    showButton = true
                )
            }
        }
    }

    override fun showProgressBar(show: Boolean) {
        binding.apply {
            if(show)
            {
                if(isLoading)
                {
                    setPaginatedItem(RepositoryAdapter.LOADING_TYPE)
                }
                else
                {
                    loadingLayout.root.show()
                    rvRepository.hide()
                }
            }
            else
            {
                loadingLayout.root.hide()
                rvRepository.show()
            }
        }
    }

    override fun showErrorUI(
        show: Boolean,
        image: Int?,
        title: String?,
        desc: String?,
        isAuthenticated: Boolean?,
        showButton: Boolean?
    ) {
        binding.apply {
            if (show) {
                rvRepository.hide()
                errorLayout.root.show()
                errorLayout.ivError.setImageResource(image ?: R.drawable.ic_error)
                binding.errorLayout.tvTitle.text = title
                if (desc == null) binding.errorLayout.tvMsg.visibility = View.GONE
                else {
                    binding.errorLayout.tvMsg.visibility = View.VISIBLE
                    binding.errorLayout.tvMsg.text = desc
                }
                binding.errorLayout.btnReload.visibility =
                    if (showButton == true) View.VISIBLE else View.GONE
                binding.errorLayout.btnReload.setOnClickListener {
                    searchRepos()
                }
            } else {
                rvRepository.show()
                errorLayout.root.hide()
            }
        }
    }

    override fun onClick(v: View?) {
        binding.apply {
            when(v) {
                ivSearch -> onSearchClick()
            }
        }
    }

    override fun onRetryClick() {
        searchRepos()
    }
}