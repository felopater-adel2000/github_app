package com.felo.github_app.data.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchResponse(
    @SerializedName("total_count")
    @Expose
    val totalCount: Int? = null,

    @SerializedName("incomplete_results")
    @Expose
    val incompleteResults: Boolean? = null,

    @SerializedName("items")
    @Expose
    val items: List<RepositoryModel>? = null
) : Parcelable
//2536316