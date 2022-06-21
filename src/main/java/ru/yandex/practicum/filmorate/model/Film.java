package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.valid.FilmDateRelease;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class Film {

    Long id;
    @NotBlank
    String name;
    @Size(min = 1, max = 200)
    String description;
    @FilmDateRelease
    LocalDate releaseDate;
    @Positive
    Long duration;
    @NotNull
    Mpa mpa;

    List<Genre> genres;
    List<Long> likes;

    /*public int getLikesSize(){
        return likes.size();
    }*/
}
