package ru.yandex.practicum.filmorate.controller;

import java.util.Collection;
import java.util.Optional;

public abstract class Controller<T> {

    public abstract Collection<Optional<T>> findAll();

    abstract public T create(T t);

    abstract public T update(T t);

}
