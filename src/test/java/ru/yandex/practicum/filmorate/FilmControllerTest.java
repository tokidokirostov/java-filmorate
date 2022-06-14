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
            "} ";

    String filmLongDescription200 = "{\n" +
            "    \"id\": 1,\n" +
            "\t\t\"name\": \"Cats!\",\n" +
            "    \"description\": \"Мой любимый котикМой любимый котик!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" +
            "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" +
            "!!!!!!!!!!!!!\",\n" +
            "\t\t\"releaseDate\": \"1946-08-20\",\n" +
            "\t\t\"duration\": 20\n" +
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
    public void test1_createFilmShouldBeOk() throws Exception {
        this.mockMvc.perform(post("/films").content(filmOk).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void test2_createFilmWhithLongDescription() throws Exception {
        this.mockMvc.perform(post("/films").content(filmLongDescription).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test3_createFilmWhithDescription200() throws Exception {
        this.mockMvc.perform(post("/films").content(filmLongDescription200).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void test4_createFilmWhithDescription201() throws Exception {
        this.mockMvc.perform(post("/films").content(filmLongDescription201).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test5_createFilmWhithDateNorma() throws Exception {
        this.mockMvc.perform(post("/films").content(filmDateNormal).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void test6_createFilmWhithDateOverNorma() throws Exception {
        this.mockMvc.perform(post("/films").content(filmDateNotNormal).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test7_createFilmWhithDurationZero() throws Exception {
        this.mockMvc.perform(post("/films").content(filmDurationZero).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test8_createFilmWhithDurationNotNormal() throws Exception {
        this.mockMvc.perform(post("/films").content(filmDurationNotNormal).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test9_createFilmWhithNoName() throws Exception {
        this.mockMvc.perform(post("/films").content(filmNoName).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test10_updateFilmShouldBeOk() throws Exception {
        this.mockMvc.perform(put("/films").content(filmOk).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void test11_updateFilmWhithLongDescription() throws Exception {
        this.mockMvc.perform(put("/films").content(filmLongDescription).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test12_updateFilmWhithDescription200() throws Exception {
        this.mockMvc.perform(post("/films").content(filmLongDescription200).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
        this.mockMvc.perform(put("/films").content(filmLongDescription200).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void test13_updateFilmWhithDescription201() throws Exception {
        this.mockMvc.perform(put("/films").content(filmLongDescription201).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test14_updateFilmWhithDateNorma() throws Exception {
        this.mockMvc.perform(put("/films").content(filmDateNormal).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void test15_updateFilmWhithDateOverNorma() throws Exception {
        this.mockMvc.perform(put("/films").content(filmDateNotNormal).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test16_updateFilmWhithDurationZero() throws Exception {
        this.mockMvc.perform(put("/films").content(filmDurationZero).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test17_updateFilmWhithDurationNotNormal() throws Exception {
        this.mockMvc.perform(put("/films").content(filmDurationNotNormal).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test18_updateFilmWhithNoName() throws Exception {
        this.mockMvc.perform(put("/films").content(filmNoName).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test19_deleteFilmWhithNoFilms() throws Exception {
        this.mockMvc.perform(delete("/films/{id}", 1))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test20_deleteFilmWhithFilm() throws Exception {
        this.mockMvc.perform(post("/films").content(filmOk).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        this.mockMvc.perform(delete("/films/{id}", 1))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void test21_getFilmWhithoutFilm() throws Exception {
        this.mockMvc.perform(get("/films/{id}", 1))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test22_getFilmWhithFilm() throws Exception {
        this.mockMvc.perform(post("/films").content(filmOk).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/films/{id}", 1))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void test23_likeFilmWhithoutFilm() throws Exception {
        this.mockMvc.perform(put("/films/{id}/like/{userId}", 1,2))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test24_getFilmWhithFilm() throws Exception {
        this.mockMvc.perform(post("/films").content(filmOk).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
        this.mockMvc.perform(post("/users").content(userOk).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        this.mockMvc.perform(put("/films/{id}/like/{userId}", 1,1))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void test25_deleteLikeFilmWhithoutFilms() throws Exception {
        this.mockMvc.perform(delete("/films/{id}/like/{userId}", 1,1))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test26_deleteLikeFilmWhithLike() throws Exception {
        this.mockMvc.perform(post("/films").content(filmOk).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
        this.mockMvc.perform(post("/users").content(userOk).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        this.mockMvc.perform(put("/films/{id}/like/{userId}", 1,1))
                .andExpect(status().is2xxSuccessful());
        this.mockMvc.perform(delete("/films/{id}/like/{userId}", 1,1))
                .andExpect(status().is2xxSuccessful());
    }

}
