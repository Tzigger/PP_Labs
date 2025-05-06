import khttp.responses.Response
import PostRequest
import CleanGetRequest


class KidsBrowser(
    private val cleanGetPrototype: CleanGetRequest,
    private val postReqPrototype: PostRequest? // Nullable for optional POST
) {

    fun start() {
        println("--- Kids Browser Initialized (Facade) ---")
        println("Parental controls active using filter: ${cleanGetPrototype.parentalControlDisallow}")
        if (postReqPrototype == null) {
            println("Note: Data submission (POST) is disabled.")
        }

    }

    fun navigateTo(url: String): Response? { // Return type remains Response?
        println("Browser Facade: Attempting navigation to: $url")
        val response: Response? = try {
            // Use the CleanGetRequest copy
            val specificCleanRequest = cleanGetPrototype.copy(url = url, params = null)
            // Execute via the proxy interface (returns Response?)
            specificCleanRequest.getResponse()
        } catch (e: Exception) {
            println("Browser Facade: UNEXPECTED ERROR during navigation setup/copy for $url: ${e.message}")
            e.printStackTrace()
            null // Null on unexpected error too
        }

        // might be null
        if (response != null) {
            // We got a real response from khttp
            if (response.statusCode in 200..299) {
                println("Browser Facade: Navigation SUCCESS (Status ${response.statusCode})")
                println("--- Page Content Snippet (Max 150 chars) ---")
                println(response.text.take(150) + if (response.text.length > 150) "..." else "")
                println("------------------------------------------")
            } else {
                // Got a response, but it indicates an HTTP error (4xx, 5xx)
                println("Browser Facade: Navigation FAILED (HTTP Status ${response.statusCode})")
                println("Content/Reason: ${response.text}")
            }
        } else {
            // response is null - could be blocked or network error during request execution
            println("Browser Facade: Navigation FAILED or BLOCKED (No response received)")
            // Could add logic here to differentiate block vs network error if needed,
            // but the proxy/request already printed specific messages.
        }
        return response // Return the actual response or null
    }


    fun submitData(url: String, data: Map<String, String>): Response? { // Return type remains Response?
        val currentPostPrototype = postReqPrototype ?: run {
            println("Browser Facade: Cannot submit data - POST requests are disabled.")
            return null
        }

        println("Browser Facade: Attempting to submit data to: $url")
        val response: Response? = try {
            // Use the PostRequest prototype's copy method
            val specificPostRequest = currentPostPrototype.copy(url = url, params = data)
            // Execute (returns Response?)
            specificPostRequest.postData()
        } catch (e: Exception) {
            println("Browser Facade: UNEXPECTED ERROR during data submission setup/copy for $url: ${e.message}")
            e.printStackTrace()
            null
        }

        // Handle the result
        if (response != null) {
            if (response.statusCode in 200..399) {
                println("Browser Facade: Submission SUCCESS (Status ${response.statusCode})")
                println("Response Text: ${response.text.take(150)}...")
            } else {
                println("Browser Facade: Submission FAILED (HTTP Status ${response.statusCode})")
                println("Response Text: ${response.text}")
            }
        } else {
            println("Browser Facade: Submission FAILED (No response received - likely network error)")
        }
        return response // Return the actual response or null
    }
}