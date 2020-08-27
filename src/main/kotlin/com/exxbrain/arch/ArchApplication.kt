package com.exxbrain.arch

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.get
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.net.InetAddress
import java.util.*


@SpringBootApplication
@EnableJpaRepositories
@EnableTransactionManagement
@EnableConfigurationProperties(AppProperties::class)
class ArchApplication(val properties: AppProperties) : WebMvcConfigurer {

    @Bean
    fun corsFilter(): CorsFilter {
        val source = UrlBasedCorsConfigurationSource()
        val config: CorsConfiguration = properties.cors
        if (config.allowedOrigins != null && !config.allowedOrigins!!.isEmpty()) {
            source.registerCorsConfiguration("/**", config)
        }
        return CorsFilter(source)
    }
}

val log: Logger = LoggerFactory.getLogger(ArchApplication::class.java)

fun main(args: Array<String>) {
    val app = SpringApplication(ArchApplication::class.java)
    val defProperties: MutableMap<String, Any> = HashMap()
    /*
     * The default profile to use when no other profiles are defined
     * This cannot be set in the application.yml file.
     * See https://github.com/spring-projects/spring-boot/issues/1219
     */
    /*
     * The default profile to use when no other profiles are defined
     * This cannot be set in the application.yml file.
     * See https://github.com/spring-projects/spring-boot/issues/1219
     */
    defProperties["spring.profiles.default"] = "dev"
    app.setDefaultProperties(defProperties)
    val env = app.run(*args).environment
    logApplicationStartup(env)
}

fun logApplicationStartup(env: ConfigurableEnvironment) {
    var protocol = "http"
    if (env.getProperty("server.ssl.key-store") != null) {
        protocol = "https"
    }
    val serverPort = env["server.port"]
    var contextPath = env["server.servlet.context-path"]
    if (contextPath == null || contextPath.isEmpty()) {
        contextPath = "/"
    }
    val hostAddress = InetAddress.getLocalHost().hostAddress
    log.info("""
----------------------------------------------------------
	Application '{}' is running! Access URLs:
	Local: 		{}://localhost:{}{}
	External: 	{}://{}:{}{}
	Profile(s): 	{}
----------------------------------------------------------""",
            env["spring.application.name"],
            protocol,
            serverPort,
            contextPath,
            protocol,
            hostAddress,
            serverPort,
            contextPath,
            env.activeProfiles)
}
