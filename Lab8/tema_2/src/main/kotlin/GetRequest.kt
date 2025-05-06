import HTTPGet
import khttp.responses.Response
import khttp.get // Import the khttp function
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class GetRequest( // Open for potential subclassing if needed, though composition is used for proxy
    urlInput: String,
    paramsInput: Map<String, String>?,
    val timeout: Int = 30 // Default timeout in seconds
) : GenericRequest(urlInput, paramsInput), HTTPGet {

    /**
     * Overridden copy method to return the correct type (GetRequest).
     * Preserves existing timeout unless specified.
     */
    open fun copy( // Marked open to allow CleanGetRequest to easily copy its underlying request
        url: String = this.url,
        params: Map<String, String>? = this.params,
        timeout: Int = this.timeout
    ): GetRequest {
        return GetRequest(url, params, timeout)
    }

    override fun getResponse(): Response? { // Changed to nullable Response?
        println("  Executing GET: URL=$url, Params=$params, Timeout=$timeout")
        return try {
            get(
                url = this.url,
                params = this.params ?: emptyMap<String, String>(),
                timeout = this.timeout.toDouble()
            )
        } catch (e: ConnectException) {
            println("  ERROR (GetRequest): Connection error for $url - ${e.message}")
            null // Return null on error
        } catch (e: SocketTimeoutException) {
            println("  ERROR (GetRequest): Socket timeout for $url - ${e.message}")
            null // Return null on error
        } catch (e: UnknownHostException) {
            println("  ERROR (GetRequest): Unknown host for $url - ${e.message}")
            null // Return null on error
        } catch (e: Exception) {
            println("  ERROR (GetRequest): Generic error for $url - ${e.message}")
            //e.printStackTrace() // Uncomment for debugging
            null // Return null on error
        }
    }

    override fun toString(): String {
        return "GetRequest(url='$url', params=$params, timeout=$timeout)"
    }

    // equals/hashCode considering timeout
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false // Check base class properties
        other as GetRequest
        if (timeout != other.timeout) return false
        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + timeout
        return result
    }
}