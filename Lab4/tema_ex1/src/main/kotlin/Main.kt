


fun main() {
    val crawler = Crawler("https://jsonplaceholder.typicode.com/todos/1")
    val result = crawler.processContent("json")
    println(result)


    val crawler1 = Crawler("https://www.w3schools.com/xml/simple.xml")
    val result1 = crawler1.processContent("xml")
    println(result1)

    //if this is not available anymore search for another, but it should be.
    val crawler2 = Crawler("https://raw.githubusercontent.com/stackrox/kube-linter/refs/heads/main/.goreleaser.yaml")
    val result2 = crawler2.processContent("yaml")
    println(result2)

}