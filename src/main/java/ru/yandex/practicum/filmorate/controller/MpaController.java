package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/mpa")
public class MpaController {
    MpaService mpaService;

    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping
    public List<Optional<Mpa>> findAll() {
        log.info("Получен запрос GET на список жанров");
        return mpaService.findAllMpa();
    }

    @GetMapping("/{id}")
    public Optional<Mpa> findById(@PathVariable Long id) {
        log.info("Получен запрос GET /genres/{}", id);
        return mpaService.findById(id);
    }
}
