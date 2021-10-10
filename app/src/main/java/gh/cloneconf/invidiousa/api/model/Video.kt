package gh.cloneconf.invidiousa.api.model

data class Video(
    val adaptiveFormats: List<AdaptiveFormat>,
    val allowRatings: Boolean,
    val allowedRegions: List<String>,
    val author: String,
    val authorId: String,
    val authorThumbnails: List<AuthorThumbnail>,
    val authorUrl: String,
    val captions: List<Any>,
    val dashUrl: String,
    val description: String,
    val descriptionHtml: String,
    val dislikeCount: Int,
    val formatStreams: List<FormatStream>,
    val genre: String,
    val genreUrl: Any,
    val isFamilyFriendly: Boolean,
    val isListed: Boolean,
    val isUpcoming: Boolean,
    val keywords: List<String>,
    val lengthSeconds: Int,
    val likeCount: Int,
    val liveNow: Boolean,
    val paid: Boolean,
    val premium: Boolean,
    val published: Int,
    val publishedText: String,
    val rating: Double,
    val recommendedVideos: List<RecommendedVideo>,
    val storyboards: List<Storyboard>,
    val subCountText: String,
    val title: String,
    val type: String,
    val videoId: String,
    val videoThumbnails: List<VideoThumbnail>,
    val viewCount: Int
) {
    data class AdaptiveFormat(
        val bitrate: String,
        val clen: String,
        val container: String,
        val encoding: String,
        val fps: Int,
        val index: String,
        val `init`: String,
        val itag: String,
        val lmt: String,
        val projectionType: String,
        val qualityLabel: String,
        val resolution: String,
        val type: String,
        val url: String
    )

    data class AuthorThumbnail(
        val height: Int,
        val url: String,
        val width: Int
    )

    data class FormatStream(
        val container: String,
        val encoding: String,
        val fps: Int,
        val itag: String,
        val quality: String,
        val qualityLabel: String,
        val resolution: String,
        val size: String,
        val type: String,
        val url: String
    )

    data class RecommendedVideo(
        val author: String,
        val authorId: String,
        val authorUrl: String,
        val lengthSeconds: Int,
        val title: String,
        val videoId: String,
        val videoThumbnails: List<VideoThumbnail>,
        val viewCount: Int,
        val viewCountText: String
    ) {
        data class VideoThumbnail(
            val height: Int,
            val quality: String,
            val url: String,
            val width: Int
        )
    }

    data class Storyboard(
        val count: Int,
        val height: Int,
        val interval: Int,
        val storyboardCount: Int,
        val storyboardHeight: Int,
        val storyboardWidth: Int,
        val templateUrl: String,
        val url: String,
        val width: Int
    )

    data class VideoThumbnail(
        val height: Int,
        val quality: String,
        val url: String,
        val width: Int
    )
}