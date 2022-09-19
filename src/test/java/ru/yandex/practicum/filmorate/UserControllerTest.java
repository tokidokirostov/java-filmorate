package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc

public class UserControllerTest {
    String userOk = "{\n" +
            "    \"id\": 1,\n" +
            "\t\t\"email\": \"m@ya.ru\",\n" +
            "    \"login\": \"Anna\",\n" +
            "\t  \"name\": \"Маша\",\n" +
            "\t\t\"birthday\": \"1999-08-20\"\n" +
            "}";
    String userOk1 = "{\n" +
            "    \"id\": 1,\n" +
            "\t\t\"email\": \"m1@ya.ru\",\n" +
            "    \"login\": \"Anna1\",\n" +
            "\t  \"name\": \"Маша\",\n" +
            "\t\t\"birthday\": \"1999-08-20\"\n" +
            "}";
    String userNoEmail = "{\n" +
            "    \"id\": 1,\n" +
            "\t\t\"email\": \"\",\n" +
            "    \"login\": \"Masha\",\n" +
            "\t  \"name\": \"Маша\",\n" +
            "\t\t\"birthday\": \"1999-08-20\"\n" +
            "}";
    String userErrorEmail = "{\n" +
            "    \"id\": 1,\n" +
            "\t\t\"email\": \"mya.ru\",\n" +
            "    \"login\": \"Masha\",\n" +
            "\t  \"name\": \"Маша\",\n" +
            "\t\t\"birthday\": \"1999-08-20\"\n" +
            "}";
    String userNoName = "{\n" +
            "    \"id\": 1,\n" +
            "\t\t\"email\": \"m@ya.ru\",\n" +
            "    \"login\": \"\",\n" +
            "\t  \"name\": \"Маша\",\n" +
            "\t\t\"birthday\": \"1999-08-20\"\n" +
            "}";
    String userNoName1 = "{\n" +
            "    \"id\": 1,\n" +
            "\t\t\"email\": \"m@ya.ru\",\n" +
            "    \"login\": \"  \",\n" +
            "\t  \"name\": \"Маша\",\n" +
            "\t\t\"birthday\": \"1999-08-20\"\n" +
            "}";
    String userNoName2 = "{\n" +
            "    \"id\": 1,\n" +
            "\t\t\"email\": \"m@ya.ru\",\n" +
            "    \"login\": \"Masha\",\n" +
            "\t  \"name\": \"\",\n" +
            "\t\t\"birthday\": \"1999-08-20\"\n" +
            "}";
    String userBirthdayInFuture = "{\n" +
            "    \"id\": 1,\n" +
            "\t\t\"email\": \"m@ya.ru\",\n" +
            "    \"login\": \"Masha\",\n" +
            "\t  \"name\": \"Маша\",\n" +
            "\t\t\"birthday\": \"2050-08-20\"\n" +
            "}";
    String userUpdated = "{\n" +
            "    \"id\": 1,\n" +
            "\t\t\"email\": \"m1@ya.ru\",\n" +
            "    \"login\": \"Kostya\",\n" +
            "\t  \"name\": \"\",\n" +
            "\t\t\"birthday\": \"1999-08-20\"\n" +
            "}";

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void test() throws Exception {
        this.mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void test1_createUserShouldBeOk() throws Exception {
        this.mockMvc.perform(post("/users").content(userOk).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void test2_createUserWhithoutEmil() throws Exception {
        this.mockMvc.perform(post("/users").content(userNoEmail).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test3_createUserWhithoutDog() throws Exception {
        this.mockMvc.perform(post("/users").content(userErrorEmail).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test4_createUserWhithoutLogin() throws Exception {
        this.mockMvc.perform(post("/users").content(userNoName).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test5_createUserWhithSpaceInLogin() throws Exception {
        this.mockMvc.perform(post("/users").content(userNoName1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test6_createUserWhithoutName() throws Exception {
        this.mockMvc.perform(post("/users").content(userNoName2).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Masha"));
    }

    @Test
    public void test7_createUserBirthdayInFuture() throws Exception {
        this.mockMvc.perform(post("/users").content(userBirthdayInFuture).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test8_createUserDuble() throws Exception {
        this.mockMvc.perform(post("/users").content(userOk).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        this.mockMvc.perform(post("/users").content(userOk).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void test9_updateUserShouldBeOk() throws Exception {
        //String body = mapper.writeValueAsString(user1);
        this.mockMvc.perform(put("/users").content(userOk).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void test10_updateUserWhithoutEmil() throws Exception {
        this.mockMvc.perform(put("/users").content(userNoEmail).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test11_updateUserWhithoutDog() throws Exception {
        this.mockMvc.perform(put("/users").content(userErrorEmail).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test12_updateUserWhithoutLogin() throws Exception {
        this.mockMvc.perform(put("/users").content(userNoName).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test13_updateUserWhithSpaceInLogin() throws Exception {
        this.mockMvc.perform(put("/users").content(userNoName1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test14_updateUserWhithoutName() throws Exception {
        this.mockMvc.perform(put("/users").content(userUpdated).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Kostya"));
    }

    @Test
    public void test15_updateUserBirthdayInFuture() throws Exception {
        this.mockMvc.perform(put("/users").content(userBirthdayInFuture).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test16_deleteUserWhithoutUser() throws Exception {
        this.mockMvc.perform(delete("/users/{id}",1))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test17_deleteUserWhithUser() throws Exception {
        this.mockMvc.perform(post("/users").content(userOk).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        this.mockMvc.perform(delete("/users/{id}",1))
                .andExpect(status().is2xxSuccessful());
    }
    @Test
    public void test18_getUserWhithoutUser() throws Exception {
        this.mockMvc.perform(get("/users/{id}",1))
                .andExpect(status().is4xxClientError());
    }
    @Test
    public void test19_getUserWhithUser() throws Exception {
        this.mockMvc.perform(post("/users").content(userOk).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/users/{id}",1))
                .andExpect(status().is2xxSuccessful());
    }
    @Test
    public void test20_addFriendUserWhithoutUser() throws Exception {
        this.mockMvc.perform(put("/users/{id}/friends/{friendId}",1,2))
                .andExpect(status().is4xxClientError());
    }
    @Test
    public void test21_addFriendUserWhithUser() throws Exception {
        this.mockMvc.perform(post("/users").content(userOk).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        this.mockMvc.perform(post("/users").content(userOk1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        this.mockMvc.perform(put("/users/{id}/friends/{friendId}",1,2))
                .andExpect(status().is2xxSuccessful());
    }
    @Test
    public void test22_getFriendUserWhithUser() throws Exception {
        this.mockMvc.perform(post("/users").content(userOk).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        this.mockMvc.perform(post("/users").content(userOk1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        this.mockMvc.perform(put("/users/{id}/friends/{friendId}",1,2))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/users/{id}/friends",1))
                .andExpect(status().is2xxSuccessful());
    }
    @Test
    public void test23_getFriendUserWhithoutUser() throws Exception {
        this.mockMvc.perform(get("/users/{id}/friends",1))
                .andExpect(status().is4xxClientError());
    }









}


