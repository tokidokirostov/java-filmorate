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
    @NotBlank
    @Pattern(regexp = "\\S*$", message = "Login должен быть без пробелов!")
    private String login;
    private String name;
    @Past
    @NotNull
    private LocalDate birthday;
    private Set<Long> friends = new TreeSet<>();

    public User(Long id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public void addFriends(Long fid) {
        friends.add(fid);
    }
}
