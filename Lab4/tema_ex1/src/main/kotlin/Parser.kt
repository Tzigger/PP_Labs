
import org.jsoup.Jsoup
import org.yaml.snakeyaml.Yaml
import org.json.JSONObject

interface Parser {
    fun parse(text: String): Map<String, String>
}


class JsonParser : Parser {
    override fun parse(text: String): Map<String, String> {
        val jsonObject = JSONObject(text)
        return jsonObject.keys().asSequence().associateWith { key ->
            jsonObject.get(key).toString()
        }
    }
}

class XmlParser : Parser {
    override fun parse(text: String): Map<String, String> {
        val doc = Jsoup.parse(text, "", org.jsoup.parser.Parser.xmlParser())
        return doc.children().associate { it.tagName() to it.text() }
    }
}


class YamlParser : Parser {

    override fun parse(text: String): Map<String, String> {
        val yaml = Yaml()
        val parsedData = yaml.load(text) as Map<String, Any>
        return parsedData.mapValues { it.value.toString() }
    }
}
