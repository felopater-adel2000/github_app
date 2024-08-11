package com.felo.github_app.base

import androidx.fragment.app.Fragment
import com.bumptech.glide.RequestManager
import com.felo.github_app.preferences.SessionManager
import org.koin.android.ext.android.inject

abstract class BaseFragment : Fragment()
{

    val sessionManager: SessionManager by inject()

    val requestManager: RequestManager by inject()

    abstract fun showProgressBar(show: Boolean)

    abstract fun showErrorUI(
        show: Boolean,
        image: Int? = 0,
        title: String? = "",
        desc: String? = null,
        isAuthenticated: Boolean? = true,
        showButton: Boolean? = false
    )

}