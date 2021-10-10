package gh.cloneconf.invidiousa.api

import gh.cloneconf.invidiousa.api.Const.USER_AGENT
import gh.cloneconf.invidiousa.api.Const.gson
import gh.cloneconf.invidiousa.api.Const.okhttp
import gh.cloneconf.invidiousa.api.model.Searches
import gh.cloneconf.invidiousa.api.model.Suggestions
import gh.cloneconf.invidiousa.model.Video
import gh.cloneconf.invidiousa.model.VideoInfo
import okhttp3.Request

object Invidious {


    var instance = "https://invidious.snopyta.org"

    fun suggestions(q : String) : List<String> {
        return ArrayList<String>().apply {
            okhttp.newCall(
                Request.Builder()
                    .header("User-Agent", USER_AGENT)
                    .url("$instance/api/v1/search/suggestions?q=$q")
                    .build()
            ).execute().apply {
                gson.fromJson(body()!!.string(), Suggestions::class.java).apply {
                    close()
                }.suggestions.forEach { add(it) }
            }
        }
    }




    fun search(q : String) : List<Video> {
        return ArrayList<Video>().apply {
            okhttp.newCall(Request.Builder()
                .header("User-Agent", USER_AGENT)
                .url("$instance/api/v1/search?q=$q")
                .build()).execute().apply {
                gson.fromJson(body()!!.string(), Searches::class.java).apply {
                    close()
                    println(this)
                    forEach {
                        add(Video(
                            id = it.videoId,
                            title = it.title,
                            thumbnail = Video.Thumbnail(
                                it.videoThumbnails[3].url,
                                it.videoThumbnails[3].width,
                                it.videoThumbnails[3].height
                            ),
                            author = it.author
                        ))
                    }
                }
            }
        }
    }



    fun video(id : String): VideoInfo {
        okhttp.newCall(Request.Builder()
            .header("User-Agent", USER_AGENT)
            .url("https://invidious.snopyta.org/api/v1/videos/$id")
            .build()).execute().apply {
            gson.fromJson(body()!!.string(), gh.cloneconf.invidiousa.api.model.Video::class.java).apply {
                close()


                var url : String
                adaptiveFormats.apply {

                    url = get(0).url
                    for (i in 0 until size) {
                        if (get(i).qualityLabel == "720p") {
                            url = get(i).url
                            break;
                        }
                    }
                }
                return VideoInfo(
                    title = title,
                    description = description,
                    url = url
                )
            }
        }
    }

}