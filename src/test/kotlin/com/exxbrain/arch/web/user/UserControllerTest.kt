package com.exxbrain.arch.web.user

import com.exxbrain.arch.entity.User
import com.exxbrain.arch.entity.UserRepository
import com.flextrade.jfixture.JFixture
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@WebMvcTest
internal class UserControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var repository: UserRepository

    @Test
    fun getAll() {
        val users = JFixture().collections().createCollection(User::class.java)
        Mockito.`when`(repository.findAll()).thenReturn(users)
        this.mockMvc
                .perform(get("/users"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk)
    }
}