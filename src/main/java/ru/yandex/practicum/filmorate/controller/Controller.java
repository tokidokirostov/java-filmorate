package ru.yandex.practicum.filmorate.controller;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class Controller<T> {

    @Getter
    private Map<Integer, T> storage = new HashMap<>();

    private Integer identifier = 0;

    public int generationId() {
        identifier += 1;
        return identifier;
    }


    @GetMapping()
    public Collection<T> findAll() {
        log.info("Получен запрос GET");
        return storage.values();
    }

    abstract public T create(T t);

    abstract public T update(T t);

}
