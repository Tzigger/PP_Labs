import khttp.responses.Response
import khttp.post
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


open class PostRequest(
    urlInput: String,
    paramsInput: Map<String, String>?
    // Can add properties like timeout, headers etc. if needed
) : GenericRequest(urlInput, paramsInput) {


    override fun copy(
        url: String,
        params: Map<String, String>?
    ): PostRequest {
        return PostRequest(url, params)
    }


    open fun postData(): Response? { // Changed to nullable Response?
        println("  Executing POST: URL=$url, Data=$params")
        return try {
            post(
                url = this.url,
                data = this.params ?: emptyMap<String, String>()
            )
        } catch (e: ConnectException) {
            println("  ERROR (PostRequest): Connection error for $url - ${e.message}")
            null // Return null on error
        } catch (e: SocketTimeoutException) {
            println("  ERROR (PostRequest): Socket timeout for $url - ${e.message}")
            null // Return null on error
        } catch (e: UnknownHostException) {
            println("  ERROR (PostRequest): Unknown host for $url - ${e.message}")
            null // Return null on error
        } catch (e: Exception) {
            println("  ERROR (PostRequest): Generic error for $url - ${e.message}")
            //e.printStackTrace()
            null // Return null on error
        }
    }

}