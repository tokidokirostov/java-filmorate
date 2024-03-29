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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FilmControllerTest {

    String filmOk = "{\n" +
            "    \"id\": 1,\n" +
            "\t\t\"name\": \"Cat\",\n" +
            "    \"description\": \"Мой любимый котик!!\",\n" +
            "\t\t\"releaseDate\": \"1946-08-20\",\n" +
            "\t\t\"duration\": 20\n" +
            "} ";

    String filmNoName = "{\n" +
            "    \"id\": 1,\n" +
            "\t\t\"name\": \"\",\n" +
            "    \"description\": \"Мой любимый котик!!\",\n" +
            "\t\t\"releaseDate\": \"1946-08-20\",\n" +
            "\t\t\"duration\": 20\n" +
            "} ";

    String filmLongDescription = "{\n" +
            "    \"id\": 1,\n" +
            "\t\t\"name\": \"Caty\",\n" +
            "    \"description\": \"Мой любимый котикМой любимый котик!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" +
            "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" +
            "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\",\n" +
            "\t\t\"releaseDate\": \"1946-08-20\",\n" +
            "\t\t\"duration\": 20\n" +
            "\t\t\"mpa\": {\"id\": 1}" +
            "} ";
    String filmLongDescription200 = "{\n" +
            "    \"id\": 1,\n" +
            "\t\t\"name\": \"Cats!\",\n" +
            "    \"description\": \"Мой любимый котикМой любимый котик!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" +
            "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" +
            "!!!!!!!!!!!!!\",\n" +
            "\t\t\"releaseDate\": \"1946-08-20\",\n" +
            "\t\t\"duration\": 20\n" +
            "\t\t\"mpa\": {\"id\": 1}" +
            "} ";

    String filmLongDescription201 = "{\n" +
            "    \"id\": 1,\n" +
            "\t\t\"name\": \"Cat\",\n" +
            "    \"description\": \"Мой любимый котикМой любимый котик!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" +
            "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" +
            "!!!!!!!!!!!!!!\",\n" +
            "\t\t\"releaseDate\": \"1946-08-20\",\n" +
            "\t\t\"duration\": 20\n" +
            "} ";

    String filmDateNormal = "{\n" +
            "    \"id\": 1,\n" +
            "\t\t\"name\": \"Cats\",\n" +
            "    \"description\": \"Мой любимый котикМой любимый котик!\",\n" +
            "\t\t\"releaseDate\": \"1895-12-29\",\n" +
            "\t\t\"duration\": 20\n" +
            "} ";

    String filmDateNotNormal = "{\n" +
            "    \"id\": 1,\n" +
            "\t\t\"name\": \"Cat\",\n" +
            "    \"description\": \"Мой любимый котик!!\",\n" +
            "\t\t\"releaseDate\": \"1895-12-27\",\n" +
            "\t\t\"duration\": 20\n" +
            "} ";

    String filmDurationZero = "{\n" +
            "    \"id\": 1,\n" +
            "\t\t\"name\": \"Cat\",\n" +
            "    \"description\": \"Мой любимый котик!!\",\n" +
            "\t\t\"releaseDate\": \"1995-12-27\",\n" +
            "\t\t\"duration\": 0\n" +
            "} ";
    String filmDurationNotNormal = "{\n" +
            "    \"id\": 1,\n" +
            "\t\t\"name\": \"Cat\",\n" +
            "    \"description\": \"Мой любимый котик!!\",\n" +
            "\t\t\"releaseDate\": \"1995-12-27\",\n" +
            "\t\t\"duration\": -20\n" +
            "} ";

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

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void test() throws Exception {
        this.mockMvc.perform(get("/films"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void test2_createFilmWhithLongDescription() throws Exception {
        this.mockMvc.perform(post("/films").content(filmLongDescription).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }


    @Test
    public void test3_createFilmWhithDescription201() throws Exception {
        this.mockMvc.perform(post("/films").content(filmLongDescription201).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }


    @Test
    public void test4_createFilmWhithDateOverNorma() throws Exception {
        this.mockMvc.perform(post("/films").content(filmDateNotNormal).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test5_createFilmWhithDurationZero() throws Exception {
        this.mockMvc.perform(post("/films").content(filmDurationZero).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test6_createFilmWhithDurationNotNormal() throws Exception {
        this.mockMvc.perform(post("/films").content(filmDurationNotNormal).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test7_createFilmWhithNoName() throws Exception {
        this.mockMvc.perform(post("/films").content(filmNoName).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }


    @Test
    public void test8_updateFilmWhithLongDescription() throws Exception {
        this.mockMvc.perform(put("/films").content(filmLongDescription).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }


    @Test
    public void test9_updateFilmWhithDescription201() throws Exception {
        this.mockMvc.perform(put("/films").content(filmLongDescription201).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }


    @Test
    public void test10_updateFilmWhithDateOverNorma() throws Exception {
        this.mockMvc.perform(put("/films").content(filmDateNotNormal).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test11_updateFilmWhithDurationZero() throws Exception {
        this.mockMvc.perform(put("/films").content(filmDurationZero).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test12_updateFilmWhithDurationNotNormal() throws Exception {
        this.mockMvc.perform(put("/films").content(filmDurationNotNormal).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test13_updateFilmWhithNoName() throws Exception {
        this.mockMvc.perform(put("/films").content(filmNoName).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test14_deleteFilmWhithNoFilms() throws Exception {
        this.mockMvc.perform(delete("/films/{id}", 1))
                .andExpect(status().is4xxClientError());
    }


    @Test
    public void test15_getFilmWhithoutFilm() throws Exception {
        this.mockMvc.perform(get("/films/{id}", 1))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test16_deleteLikeFilmWhithoutFilms() throws Exception {
        this.mockMvc.perform(delete("/films/{id}/like/{userId}", 1,1))
                .andExpect(status().is4xxClientError());
    }


}
