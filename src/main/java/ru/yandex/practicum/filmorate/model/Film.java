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
import java.util.Set;
import java.util.TreeSet;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
//@AllArgsConstructor
//@RequiredArgsConstructor
@ToString

public class Film {

    Long id;
    @NotBlank
    String name;
    @Size(min = 1, max = 200)
    String description;
    @FilmDateRelease
    LocalDate releaseDate;
    @Positive
    int duration;
    Set<Long> likes = new TreeSet<>();

    public Film(Long id, String name, String description, LocalDate releaseDate, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
    public int getLikesSize(){
        return likes.size();
    }
}
