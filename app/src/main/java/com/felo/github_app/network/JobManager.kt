package com.felo.github_app.network

import android.util.Log
import kotlinx.coroutines.Job

open class JobManager(private val className: String) {
    private val TAG = "JobManager"
    private val jobs: HashMap<String, Job> = HashMap()

    fun addJop(methodName: String, job: Job) {
        cancelJob(methodName)
        jobs[methodName] = job
    }

    private fun cancelJob(methodName: String) {
        getJob(methodName)?.cancel()
    }

    private fun getJob(methodName: String): Job? {
        if (jobs.containsKey(methodName)) {
            jobs[methodName]?.let {
                return it
            }
        }
        return null
    }


    fun cancelActiveJobs() {
        for ((methodName, job) in jobs) {
            if (job.isActive) {
                Log.d(TAG, "cancellAllJobs: $className: cancelling job in method: $methodName ")
                job.cancel()
            }
        }
    }
}