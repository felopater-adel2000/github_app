package com.felo.github_app.utils

import android.widget.EditText
import java.util.regex.Pattern

class Validation {
    companion object {

        const val PASSWORD_MIN_CAHRS = 6
        fun isValidEmail(email: String?): Boolean {
            return email != null && Pattern.compile(
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
            ).matcher(email).matches()
        }

        fun isValidPassword(password: String?): Boolean {
            return password != null && password.trim().length >= PASSWORD_MIN_CAHRS
        }

        fun isValidStringInput(input: String?): Boolean {
            return input != null && !input.trim().isEmpty()
        }

    }
}