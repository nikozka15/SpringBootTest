package com.nikozka.app;

import com.nikozka.app.entity.UserEntity;
import com.nikozka.app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = SpringSecurityApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:test.properties")
class UserIntegrationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void whenUserAddedThanReturn201() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/user/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createUsersRequest()))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }
    @Test
    void whenInvalidUserAddedThanReturn400() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/user/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createInvalidUsersRequest()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    @Test
    void whenExistingUserAddedThanReturn409() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/user/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createUsersRequest()))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        mvc.perform(MockMvcRequestBuilders.post("/user/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createUsersRequest()))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    //    @Test
//    void whenUserNotSavedThenReturn500() throws Exception {
//        when(userRepository.saveAndFlush(any())).thenReturn(null);
//
//        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/user/add")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(createUsersRequest()))
//                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
//                .andReturn();
//        assertEquals("{\"errorMessage\":\"Error while saving user\"}", mvcResult.getResponse().getContentAsString());
//
//    }
    @Test
    void testAuthenticateValidExistedUserThanReturn200() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/user/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createUsersRequest()))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        mvc.perform(MockMvcRequestBuilders.post("/user/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createUsersRequest()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testAuthenticateInvalidUserThanReturn400() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/user/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createInvalidUsersRequest()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        // todo maybe Unproccesible Entity, also change in error handling
    }

    @Test
    void testAuthenticateValidNotExistedUserUserThanReturn401() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/user/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createUsersRequest()))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    private byte[] createUsersRequest() {
        return getUserJson().getBytes();
    }

    private static String getUserJson() {
        return "{\"username\":\"testuser\",\"password\":\"testpassword\"}";
    }

    private byte[] createInvalidUsersRequest() {
        return getInvalidUserJson().getBytes();
    }

    private static String getInvalidUserJson() {
        return "{\"username\":\"i\",\"password\":\"invalid\"}";
    }

    private UserEntity getEntity() {
        return new UserEntity("testuser", "testpassword");
    }
}