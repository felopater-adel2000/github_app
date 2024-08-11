package com.felo.github_app.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.felo.github_app.R
import com.felo.github_app.adapter.RepositoryAdapter
import com.felo.github_app.base.BaseActivity
import com.felo.github_app.data.model.RepositoryModel
import com.felo.github_app.databinding.ActivityMainBinding
import com.felo.github_app.network.DataState
import com.felo.github_app.network.StateError
import com.felo.github_app.ui.auth.AuthActivity
import com.felo.github_app.ui.search.SearchActivity
import com.felo.github_app.utils.Constants
import com.felo.github_app.utils.Constants.Companion.PAGINATION_PAGE_SIZE
import com.felo.github_app.utils.hide
import com.felo.github_app.utils.orDefault
import com.felo.github_app.utils.setUpToolBar
import com.felo.github_app.utils.show
import com.felo.github_app.viewModel.GithubViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity(), RepositoryAdapter.RepositoryItemCallback {
    private val TAG = "MainActivity"
    lateinit var binding: ActivityMainBinding
    private val viewModel: GithubViewModel by viewModel()

    private lateinit var adapter: RepositoryAdapter
    private val repos: ArrayList<RepositoryModel> = ArrayList()
    private var since = 0
    private var isLoading = false
    private var isQueryExhausted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(!sessionManager.isUserLoggedIn()) navigateToAuthActivity()
        initViews()
        getReps()

    }

    private fun initViews()
    {
        binding.apply {
            setUpToolBar(
                toolbarBinding = toolbar,
                title = getString(R.string.app_name),
                isHome = true
            )

            adapter = RepositoryAdapter(requestManager = requestManager, this@MainActivity)
            rvRepository.adapter = adapter

            rvRepository.addOnScrollListener(object: RecyclerView.OnScrollListener(){
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
                            getReps()
                        }
                    }
                }
            })

        }
    }

    private fun getReps() = lifecycleScope.launch {
        viewModel.getRepos(
            since = since
        ).collect { response ->
            showProgressBar(response.loading.isLoading)

            if(response is DataState.Error) {
                response.error?.let {stateError -> handelErrorResponse(stateError) }
            }
            else if(response is DataState.Success)
            {
                showErrorUI(false)
                response.data?.data?.let {list ->
                    populateData(list)
                }

            }
        }
    }

    private fun populateData(list: List<RepositoryModel>)
    {
        isQueryExhausted = list.isEmpty()
        since = list.lastOrNull()?.id.orDefault()
        if(since == 0)
        {
            repos.clear()
            repos.addAll(list)
            adapter.submitList(repos)
            adapter.notifyDataSetChanged()
        }
        else
        {
            setPaginatedItem(RepositoryAdapter.ITEM_TYPE)
            repos.addAll(list)
            adapter.submitList(repos)
            adapter.notifyDataSetChanged()
            isLoading = false
        }
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

    private fun onItemSearchClick()
    {
        startActivity(
            Intent(this, SearchActivity::class.java)
        )
    }

    private fun onItemLogoutClick()
    {
        Firebase.auth.signOut()
        navigateToAuthActivity()
    }

    private fun navigateToAuthActivity()
    {
        startActivity(
            Intent(this, AuthActivity::class.java)
        )
        finish()
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
                   getReps()
                }
            } else {
                rvRepository.show()
                errorLayout.root.hide()
            }
        }
    }

    override fun onRetryClick() {
        getReps()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.item_search -> onItemSearchClick()

            R.id.item_logout -> onItemLogoutClick()
        }
        return super.onOptionsItemSelected(item)
    }

}