package com.exxbrain.arch

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.web.cors.CorsConfiguration

@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
class AppProperties {
    val cors: CorsConfiguration = CorsConfiguration()
    var version: String = ""
    var hello: String = ""
    var proxyBasePath: String = ""

}