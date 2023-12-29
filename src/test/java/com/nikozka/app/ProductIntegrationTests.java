package com.nikozka.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikozka.app.dto.AuthenticationResponseToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = SpringSecurityApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class ProductIntegrationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String authenticateUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/user/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createUsersRequest()))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/user/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createUsersRequest()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                AuthenticationResponseToken.class
        ).getToken();
    }

    @Test
    void testAddValidProductThenReturn201() throws Exception {

        mvc.perform(MockMvcRequestBuilders.post("/products/add")
                        .header("Authorization", "Bearer " + authenticateUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createTableRequestJson()))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void testAddInvalidProductThenReturn400() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/products/add")
                        .header("Authorization", "Bearer " + authenticateUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createInvalidTableRequestJson()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void testUnauthorizedAddValidProductThenReturn403() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/products/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createInvalidTableRequestJson()))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testUnauthorizedUserGetProductThenReturn403() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/products/all"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
    @Test
    void testGetProductThenReturn200() throws Exception {
        String token = authenticateUser();
        mvc.perform(MockMvcRequestBuilders.post("/products/add")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createTableRequestJson()))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        mvc.perform(MockMvcRequestBuilders.get("/products/all")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].entryDate").value("2023-01-03"))
                .andExpect(jsonPath("$[0].itemCode").value("11111"))
                .andExpect(jsonPath("$[0].itemName").value("Test Inventory 2"))
                .andExpect(jsonPath("$[0].itemQuantity").value("20"))
                .andExpect(jsonPath("$[0].status").value("Paid"));
    }


    private byte[] createTableRequestJson() {
        return getTableRequestJson().getBytes();
    }

    private byte[] createInvalidTableRequestJson() {
        return getInvalidTableRequestJson().getBytes();
    }

    private String getTableRequestJson() {
        return "{\n" +
                "\"table\": \"products\",\n" +
                "\"records\": [\n" +
                getProductJson() +
                "]\n" +
                "}";
    }

    private String getInvalidTableRequestJson() {
        return "{\n" +
                "\"table\": \"products\",\n" +
                "\"records\": [\n" +
                getInvalidProductJson() +
                "]\n" +
                "}";
    }

    private String getProductJson() {
        return "{\n" +
                "\"entryDate\": \"03-01-2023\",\n" +
                "\"itemCode\": \"11111\",\n" +
                "\"itemName\": \"Test Inventory 2\",\n" +
                "\"itemQuantity\": \"20\",\n" +
                "\"status\": \"Paid\"\n" +
                "}";
    }

    private String getInvalidProductJson() {
        return "{\n" +
                "\"entryDate\": \"03-01-202\",\n" +
                "\"itemCode\": \"11111A\",\n" +
                "\"itemName\": \"T\",\n" +
                "\"itemQuantity\": \"20A\",\n" +
                "\"status\": \"PaidA\"\n" +
                "}";
    }

    private byte[] createUsersRequest() {
        return getUserJson().getBytes();
    }

    private String getUserJson() {
        return "{\"username\":\"testuser\",\"password\":\"testpassword\"}";
    }

}