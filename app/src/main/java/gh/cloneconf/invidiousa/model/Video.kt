package gh.cloneconf.invidiousa.model

data class Video (
    val id : String,
    val title : String,
    val thumbnail : Thumbnail,
    val author : String
) {
    data class Thumbnail(
        val src : String,
        val width : Int,
        val height : Int
    )
}