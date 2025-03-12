import org.jsoup.Connection
import org.jsoup.Jsoup



//remember that it must be an absolute url
class Crawler(private var url:String) {
    fun getResource(): Connection.Response {
        val doc = Jsoup.connect(url).ignoreContentType(true).execute() //This should create the request and return the response.
        return doc
    }

    fun processContent(contentType: String): Map<String, String> {
        val response = getResource();
        val content = response.body();

        val parser: Parser = when (contentType.lowercase()) {
            "json" -> JsonParser()
            "xml" -> XmlParser()
            "yaml" -> YamlParser()
            else -> throw IllegalArgumentException("Unsupported content type")
        }
        return parser.parse(content)
    }
}




