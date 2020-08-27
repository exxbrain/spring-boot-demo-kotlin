package com.exxbrain.arch.controller

import com.exxbrain.arch.AppProperties
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MainController(val props: AppProperties) {

    @GetMapping("/")
    fun hello() : String {
        return "${props.hello}, World!"
    }

    @GetMapping("/health")
    fun health() : Health {
        return Health(ServerStatus.OK)
    }

    @GetMapping("/version")
    fun version() : Version {
        return Version(props.version)
    }
}