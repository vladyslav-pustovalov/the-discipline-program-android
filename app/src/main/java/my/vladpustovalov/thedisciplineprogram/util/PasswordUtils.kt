package my.vladpustovalov.thedisciplineprogram.util

import androidx.annotation.StringRes
import my.vladpustovalov.thedisciplineprogram.R

val String.isValidPassword: Boolean
    get() = passwordValidationResId == null

val String.passwordValidationResId: Int?
    @StringRes
    get() {
        if (isEmpty()) return null

        if (length !in 6..32) {
            return R.string.password_validation_length
        }

        val disallowedRegex = Regex("[\"'`\\\\/<>{}\\[\\]()\\n\\r\\t ]")
        if (disallowedRegex.containsMatchIn(this)) {
            return R.string.password_validation_disallowed_chars
        }

        if (!contains(Regex("[A-Z]"))) {
            return R.string.password_validation_uppercase
        }

        if (!contains(Regex("[a-z]"))) {
            return R.string.password_validation_lowercase
        }

        if (!contains(Regex("\\d"))) {
            return R.string.password_validation_digit
        }

        if (!contains(Regex("[!@#$%^&*()_+=|~?:\\[\\]{}.\\-]"))) {
            return R.string.password_validation_special_char
        }

        return null
    }
