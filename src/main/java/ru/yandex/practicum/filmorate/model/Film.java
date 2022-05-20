package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.yaml.snakeyaml.events.Event;
import ru.yandex.practicum.filmorate.valid.FilmDateRelease;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class Film {

    int id;
    @NotBlank
    String name;
    @Size(min = 1, max = 200)
    String description;
    @FilmDateRelease
    LocalDate releaseDate;
    @Positive
    int duration;
}
