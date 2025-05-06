open class GenericRequest(
    urlInput: String,
    paramsInput: Map<String, String>?
) {
    val url: String
    val params: Map<String, String>? // Store as read-only

    init {
        require(urlInput.isNotBlank()) { "URL cannot be blank or empty" }
        this.url = urlInput
        // Create an immutable defensive copy
        this.params = paramsInput?.toMap() // Creates a new read-only map
    }

    open fun copy(
        url: String = this.url,
        params: Map<String, String>? = this.params // Pass the potentially new map
    ): GenericRequest {
        // Constructor handles validation and defensive copying
        return GenericRequest(url, params)
    }

}