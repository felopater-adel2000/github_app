package com.felo.github_app.data.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RepositoryModel(
    @SerializedName("message")
    @Expose
    val message: String? = null,

    @SerializedName("documentation_url")
    @Expose
    val documentationUrl: String? = null,

    @SerializedName("status")
    @Expose
    val status: Int? = null,

    @SerializedName("id")
    @Expose
    val id: Int? = null,

    @SerializedName("node_id")
    @Expose
    val nodeId: String? = null,

    @SerializedName("name")
    @Expose
    val name: String? = null,

    @SerializedName("full_name")
    @Expose
    val fullName: String? = null,

    @SerializedName("private")
    @Expose
    val private: Boolean? = null,

    @SerializedName("owner")
    @Expose
    val owner: OwnerModel? = null,

    @SerializedName("html_url")
    @Expose
    val htmlUrl: String? = null,

    @SerializedName("description")
    @Expose
    val description: String? = null,

    @SerializedName("fork")
    @Expose
    val fork: Boolean? = null,

    @SerializedName("url")
    @Expose
    val url: String? = null,

    @SerializedName("forks_url")
    @Expose
    val forksUrl: String? = null,

    @SerializedName("keys_url")
    @Expose
    val keysUrl: String? = null,

    @SerializedName("collaborators_url")
    @Expose
    val collaboratorsUrl: String? = null,

    @SerializedName("teams_url")
    @Expose
    val teamsUrl: String? = null,

    @SerializedName("hooks_url")
    @Expose
    val hooksUrl: String? = null,

    @SerializedName("issue_events_url")
    @Expose
    val issueEventsUrl: String? = null,

    @SerializedName("events_url")
    @Expose
    val eventsUrl: String? = null,

    @SerializedName("assignees_url")
    @Expose
    val assigneesUrl: String? = null,

    @SerializedName("branches_url")
    @Expose
    val branchesUrl: String? = null,

    @SerializedName("tags_url")
    @Expose
    val tagsUrl: String? = null,

    @SerializedName("blobs_url")
    @Expose
    val blobsUrl: String? = null,

    @SerializedName("git_tags_url")
    @Expose
    val gitTagsUrl: String? = null,

    @SerializedName("git_refs_url")
    @Expose
    val gitRefsUrl: String? = null,

    @SerializedName("trees_url")
    @Expose
    val treesUrl: String? = null,

    @SerializedName("statuses_url")
    @Expose
    val statusesUrl: String? = null,

    @SerializedName("languages_url")
    @Expose
    val languagesUrl: String? = null,

    @SerializedName("stargazers_url")
    @Expose
    val stargazersUrl: String? = null,

    @SerializedName("contributors_url")
    @Expose
    val contributorsUrl: String? = null,

    @SerializedName("subscribers_url")
    @Expose
    val subscribersUrl: String? = null,

    @SerializedName("subscription_url")
    @Expose
    val subscriptionUrl: String? = null,

    @SerializedName("commits_url")
    @Expose
    val commitsUrl: String? = null,

    @SerializedName("git_commits_url")
    @Expose
    val gitCommitsUrl: String? = null,

    @SerializedName("comments_url")
    @Expose
    val commentsUrl: String? = null,

    @SerializedName("issue_comment_url")
    @Expose
    val issueCommentUrl: String? = null,

    @SerializedName("contents_url")
    @Expose
    val contentsUrl: String? = null,

    @SerializedName("compare_url")
    @Expose
    val compareUrl: String? = null,

    @SerializedName("merges_url")
    @Expose
    val mergesUrl: String? = null,

    @SerializedName("archive_url")
    @Expose
    val archiveUrl: String? = null,

    @SerializedName("downloads_url")
    @Expose
    val downloadsUrl: String? = null,

    @SerializedName("issues_url")
    @Expose
    val issuesUrl: String? = null,

    @SerializedName("pulls_url")
    @Expose
    val pullsUrl: String? = null,

    @SerializedName("milestones_url")
    @Expose
    val milestonesUrl: String? = null,

    @SerializedName("notifications_url")
    @Expose
    val notificationsUrl: String? = null,

    @SerializedName("labels_url")
    @Expose
    val labelsUrl: String? = null,

    @SerializedName("releases_url")
    @Expose
    val releasesUrl: String? = null,

    @SerializedName("deployments_url")
    @Expose
    val deploymentsUrl: String? = null,

    @SerializedName("created_at")
    @Expose
    val createdAt: String? = null,

    @SerializedName("updated_at")
    @Expose
    val updatedAt: String? = null,

    @SerializedName("pushed_at")
    @Expose
    val pushedAt: String? = null,

    @SerializedName("git_url")
    @Expose
    val gitUrl: String? = null,

    @SerializedName("ssh_url")
    @Expose
    val sshUrl: String? = null,

    @SerializedName("clone_url")
    @Expose
    val cloneUrl: String? = null,

    @SerializedName("svn_url")
    @Expose
    val svnUrl: String? = null,

    @SerializedName("homepage")
    @Expose
    val homepage: String? = null,

    @SerializedName("size")
    @Expose
    val size: Int? = null,

    @SerializedName("stargazers_count")
    @Expose
    val stargazersCount: Int? = null,

    @SerializedName("watchers_count")
    @Expose
    val watchersCount: Int? = null,

    @SerializedName("language")
    @Expose
    val language: String? = null,

    @SerializedName("has_issues")
    @Expose
    val hasIssues: Boolean? = null,

    @SerializedName("has_projects")
    @Expose
    val hasProjects: Boolean? = null,

    @SerializedName("has_downloads")
    @Expose
    val hasDownloads: Boolean? = null,

    @SerializedName("has_wiki")
    @Expose
    val hasWiki: Boolean? = null,

    @SerializedName("has_pages")
    @Expose
    val hasPages: Boolean? = null,

    @SerializedName("has_discussions")
    @Expose
    val hasDiscussions: Boolean? = null,

    @SerializedName("forks_count")
    @Expose
    val forksCount: Int? = null,

    @SerializedName("mirror_url")
    @Expose
    val mirrorUrl: String? = null,

    @SerializedName("archived")
    @Expose
    val archived: Boolean? = null,

    @SerializedName("disabled")
    @Expose
    val disabled: Boolean? = null,

    @SerializedName("open_issues_count")
    @Expose
    val openIssuesCount: Int? = null,

    @SerializedName("allow_forking")
    @Expose
    val allowForking: Boolean? = null,

    @SerializedName("is_template")
    @Expose
    val isTemplate: Boolean? = null,

    @SerializedName("web_commit_signoff_required")
    @Expose
    val webCommitSignoffRequired: Boolean? = null,

    @SerializedName("topics")
    @Expose
    val topics: List<String>? = null,

    @SerializedName("visibility")
    @Expose
    val visibility: String? = null,

    @SerializedName("forks")
    @Expose
    val forks: Int? = null,

    @SerializedName("open_issues")
    @Expose
    val openIssues: Int? = null,

    @SerializedName("watchers")
    @Expose
    val watchers: Int? = null,

    @SerializedName("default_branch")
    @Expose
    val defaultBranch: String? = null,

    var isLoading: Boolean = false,
    var isError: Boolean = false,
    var errorMessage: String? = null
) : Parcelable