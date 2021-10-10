package gh.cloneconf.invidiousa.api.model

class Searches : ArrayList<Searches.SearchesItem>(){
    data class SearchesItem(
        val author: String,
        val authorId: String,
        val authorUrl: String,
        val description: String,
        val descriptionHtml: String,
        val isUpcoming: Boolean,
        val lengthSeconds: Int,
        val liveNow: Boolean,
        val premium: Boolean,
        val published: Int,
        val publishedText: String,
        val title: String,
        val type: String,
        val videoId: String,
        val videoThumbnails: List<VideoThumbnail>,
        val viewCount: Int
    ) {
        data class VideoThumbnail(
            val height: Int,
            val quality: String,
            val url: String,
            val width: Int
        )
    }
}