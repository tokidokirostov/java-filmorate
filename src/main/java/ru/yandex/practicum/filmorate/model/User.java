package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Validated
public class User {
    private Long id;
    @Email
    @NotBlank
    private String email;
    private String name;
    @NotBlank
    @Pattern(regexp = "\\S*$", message = "Login должен быть без пробелов!")
    private String login;

    @Past
    @NotNull
    private LocalDate birthday;
    private Set<Long> friends = new TreeSet<>();

    public User(Long id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.login = login;
        this.birthday = birthday;
    }

    public void addFriends(Long fid) {
        friends.add(fid);
    }
}
