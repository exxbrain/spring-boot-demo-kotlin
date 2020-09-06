package com.exxbrain.arch.web.main

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
    fun health() : HealthDTO {
        return HealthDTO(ServerStatusDTO.OK)
    }

    @GetMapping("/version")
    fun version() : VersionDTO {
        return VersionDTO(props.version)
    }
}