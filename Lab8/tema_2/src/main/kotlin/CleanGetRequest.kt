import khttp.responses.Response
import HTTPGet
import GetRequest

class CleanGetRequest(
    // Hold the actual request object (composition preferred for Proxy)
    private val actualGetRequest: GetRequest,
    val parentalControlDisallow: List<String> = emptyList()
) : HTTPGet { // Implement the common interface

    // Expose properties from the underlying request if needed by the facade or others
    val url: String get() = actualGetRequest.url
    val params: Map<String, String>? get() = actualGetRequest.params
    val timeout: Int get() = actualGetRequest.timeout

    /**
     * Creates a new CleanGetRequest instance wrapping a *copied* underlying GetRequest
     * with potentially modified URL or parameters. Preserves the parental control list.
     * Essential for the Prototype pattern usage within the Facade.
     */
    fun copy(
        url: String = this.actualGetRequest.url,
        params: Map<String, String>? = this.actualGetRequest.params,
        // Allow copying timeout as well if needed, delegate to GetRequest's copy
        timeout: Int = this.actualGetRequest.timeout
    ): CleanGetRequest {
        // 1. Create a *new* specific underlying GetRequest using its copy method
        val specificGetRequest = actualGetRequest.copy(url = url, params = params, timeout = timeout)
        // 2. Return a new Proxy instance wrapping the new specific GetRequest
        return CleanGetRequest(specificGetRequest, this.parentalControlDisallow)
    }

    /**
     * Intercepts the getResponse call. Checks against the disallowed list before
     * delegating to the actual GetRequest.
     */
    override fun getResponse(): Response? { // Changed to nullable Response?
        val urlToCheck = actualGetRequest.url
        val isBlocked = parentalControlDisallow.any { disallowed ->
            urlToCheck.contains(disallowed, ignoreCase = true)
        }

        return if (isBlocked) {
            println(">>> BLOCKED (Parental Control): $urlToCheck (matched disallowed keyword)")
            null // Return null when blocked
        } else {
            println("<<< ALLOWED (Parental Control): $urlToCheck")
            // Delegate to the actual GetRequest's getResponse method (which now also returns Response?)
            actualGetRequest.getResponse()
        }
    }

}