package gh.cloneconf.invidiousa.api

import com.google.gson.Gson
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object Const {

    const val USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Brave Chrome/94.0.4606.61 Safari/537.36"

    val okhttp : OkHttpClient by lazy {
        OkHttpClient.Builder()
            .followRedirects(false)
            .followSslRedirects(false)
            .retryOnConnectionFailure(false)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    val gson by lazy { Gson() }

}