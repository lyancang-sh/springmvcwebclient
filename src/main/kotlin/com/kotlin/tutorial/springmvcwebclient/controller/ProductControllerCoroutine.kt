package com.kotlin.tutorial.springmvcwebclient.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.kotlin.tutorial.springmvcwebclient.utils.logger
import kotlinx.coroutines.*
import kotlinx.coroutines.reactive.awaitFirst
import org.springframework.http.HttpMethod
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.awaitExchange

@RestController
class ProductControllerCoroutine(var restTemplate: RestTemplate,
                                 var webClient: WebClient) {
    private var log = logger()

    @GetMapping("/get/mvc")
    fun getProduct(): Product {
        return Product(name = "mvc-apple")
    }

//    @GetMapping("/get/coroutine")
//    fun getProductCoroutine(): Product = runBlocking {
//
//        var result = CoroutineScope(Dispatchers.IO).async {
//            var resp = async {
//                log.info("resttemplate run")
//                restTemplate.exchange<Int>("http://localhost:8080/get", HttpMethod.GET, null, Int.javaClass).body
//            }
//
//            var resp2 = async {
//                log.info("webclient run")
//                webClient.get().uri("http://localhost:8080/get").awaitExchange { it.awaitBody() }
//            }
//            Product(name = "coroutine-apple ${resp.await()}, ${resp2.await()}")
//        }
//        result.await()
//    }

    @GetMapping("/get/coroutine2")
    fun getProductCoroutine2(): Product = runBlocking {
        log.info("line 46")
        withContext(Dispatchers.IO) {
            var resp = async {
                log.info("resttemplate run")
                restTemplate.exchange<Int>("http://localhost:8080/get", HttpMethod.GET, null, Int.javaClass).body
            }
//            takeTime(2000)
            var resp2 = async {
                log.info("webclient run")
                webClient.get().uri("http://localhost:8080/get").awaitExchange { it.awaitBody() }
            }
            delay(1000)
            log.info("line 57")
//            Product(name = "coroutine-apple ${resp.await()}, ${resp2.await()}")
        }
        Product(name = "coroutine-apple")
    }

    @GetMapping("/get")
    fun get(): Int {
        return 5
    }
}

suspend fun takeTime(time: Int) {
    for (i in 0..1000) {
        //
    }
}

data class Product(
        var id: Int = 0,
        var name: String = "apple",
        var price: Double = 0.0
)

