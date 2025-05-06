package org.example.kidsbrowser

import KidsBrowser
import GetRequest
import PostRequest
import CleanGetRequest

fun main() {
    println("--- Setting up Kids Browser ---")

    // --- 1. Create Prototypes ---

    // Base prototype
    val baseGetPrototype = GetRequest(
        urlInput = "http://example.com", // Default, will be overridden
        paramsInput = null,
        timeout = 20 // Default timeout for GETs in seconds
    )

    // Proxy Prototype
    val cleanGetPrototype = CleanGetRequest(
        actualGetRequest = baseGetPrototype,
        parentalControlDisallow = listOf(
            "gambling",
            "adultcontent", // More specific keyword
            "dangeroussite"  // Example site name for blocking
        )
    )

    // POST Prototype
    val postPrototype: PostRequest? = PostRequest(
        urlInput = "http://default-post-target.com", // Default, will be overridden
        paramsInput = null
    )

    // val postPrototype: PostRequest? = null

    // --- 2. Facade ---
    val browser = KidsBrowser(cleanGetPrototype, postPrototype)

    // --- 3. Use the Browser Facade ---
    browser.start()
    println("\n===================================")
    println("--- Starting Navigation Tests ---")
    println("===================================")

    // Test 1: Allowed Site (Wikipedia)
    println("\n>>> Test 1: Allowed Site (Wikipedia)")
    browser.navigateTo("https://www.wikipedia.org/")
    println("-----------------------------------")

    // Test 2: Allowed Site (Google Search for something harmless)
    println("\n>>> Test 2: Allowed Site (Google Search)")
    browser.navigateTo("https://www.google.com/search?q=kotlin+programming+language")
    println("-----------------------------------")

    // Test 3: Blocked Site (Keyword in URL Path)
    println("\n>>> Test 3: Blocked Site (Keyword 'gambling' in URL)")
    browser.navigateTo("https://www.example-news.com/articles/about-gambling-laws")
    println("-----------------------------------")

    // Test 4: Blocked Site (Keyword 'adultcontent' in domain-like name
    println("\n>>> Test 4: Blocked Site (Keyword 'adultcontent' in URL)")
    browser.navigateTo("https://example.com/path-to-some-adultcontent-info")
    println("-----------------------------------")

    // Test 5: Blocked Site (Domain name directly in blocklist)
    println("\n>>> Test 5: Blocked Site (Domain name in blocklist)")
    browser.navigateTo("http://www.dangeroussite.com/index.html")
    println("-----------------------------------")


    // Test 6: Network Error (Non-existent domain)
    println("\n>>> Test 6: Network Error (Bad Host)")
    browser.navigateTo("http://nonexistent-domain-for-sure-sdlkfjsldkjfs.invalid")
    println("-----------------------------------")

    // --- Data Submission Test ---
    println("\n===================================")
    println("--- Starting Submission Tests ---")
    println("===================================")

    if (postPrototype != null) {
        val formData = mapOf(
            "userName" to "KidsBrowserTestUser",
            "feedback" to "This is a test submission!",
            "rating" to "5"
        )


        println("\n>>> Test 7: Data Submission (httpbin.org/post)")
        browser.submitData("https://httpbin.org/post", formData)
        println("-----------------------------------")
    } else {
        println("\n>>> Test 7: Data Submission (Skipped - POST Disabled)")
        println("-----------------------------------")
    }

    println("\n===================================")
    println("--- Kids Browser Session End ---")
    println("===================================")
}