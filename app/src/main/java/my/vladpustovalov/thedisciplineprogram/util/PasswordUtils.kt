package my.vladpustovalov.thedisciplineprogram.util

val String.isValidPassword: Boolean
    get() = passwordValidationMessage == null

val String.passwordValidationMessage: String?
    get() {
        if (isEmpty()) return null

        if (length !in 6..32) {
            return "Password must be 6–32 characters long."
        }

        val disallowedRegex = Regex("[\"'`\\\\/<>{}\\[\\]()\\n\\r\\t ]")
        if (disallowedRegex.containsMatchIn(this)) {
            return "Password contains disallowed characters (e.g. \", ', \\, <, >, {, }, [, ], (, ), `, or space)."
        }

        if (!contains(Regex("[A-Z]"))) {
            return "Password must contain at least one uppercase letter."
        }

        if (!contains(Regex("[a-z]"))) {
            return "Password must contain at least one lowercase letter."
        }

        if (!contains(Regex("\\d"))) {
            return "Password must contain at least one digit."
        }

        if (!contains(Regex("[!@#$%^&*()_+=|~?:\\[\\]{}.\\-]"))) {
            return "Password must contain at least one special character (e.g. !@#$%^&*)."
        }

        return null
    }
