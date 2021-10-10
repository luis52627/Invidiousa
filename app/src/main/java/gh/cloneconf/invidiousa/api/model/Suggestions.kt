package gh.cloneconf.invidiousa.api.model

data class Suggestions(
    val query: String,
    val suggestions: List<String>
)