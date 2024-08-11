package com.felo.github_app.preferences

import com.felo.github_app.BuildConfig

class PreferenceKeys {
    companion object {
        const val APP_PREFERENCES = "${BuildConfig.APPLICATION_ID}.APP_PREFERENCES"
        const val USER_STATUS = "${BuildConfig.APPLICATION_ID}.USER_STATUS"
    }
}