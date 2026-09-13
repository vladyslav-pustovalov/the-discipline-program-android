package my.vladpustovalov.thedisciplineprogram.util

import my.vladpustovalov.thedisciplineprogram.BuildConfig

object Constants {
    object API {
        const val BASE_URL = "https://thedisciplineprogram.com"
        val BASE_PATH: String
            get() = if (BuildConfig.DEBUG) "/dev-api" else "/api"
        const val API_VERSION = "/v1"

        val FULL_BASE_URL: String
            get() = "$BASE_URL$BASE_PATH$API_VERSION/"
    }
}
